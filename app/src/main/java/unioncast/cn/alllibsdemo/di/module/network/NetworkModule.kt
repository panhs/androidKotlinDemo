package unioncast.cn.alllibsdemo.di.module

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import unioncast.cn.alllibsdemo.BuildConfig
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by JokAr on 2017/6/14.
 */
@Module
class NetworkModule {
    private val DEFAULT_TIMEOUT = 15
    private val BASE_URL = "http://news-at.zhihu.com/api/4/"

    @Singleton
    @Provides
    fun providerRetrofit(): Retrofit {
        val interceptor = LoggingInterceptor()
        if (BuildConfig.DEBUG)
            interceptor.level = LoggingInterceptor.Level.BODY
        else
            interceptor.level = LoggingInterceptor.Level.NONE

        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .build()

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}

class LoggingInterceptor() : Interceptor,AnkoLogger {
    @Volatile
    var level = Level.NONE
    private val UTF8 = Charset.forName("UTF-8")

    enum class Level {
        NONE,

        BASIC,

        HEADERS,

        BODY
    }

    /**
     *  Change the level at which this interceptor logs.
     */
    fun setLevel(level: Level): LoggingInterceptor {
        if (level == null) throw NullPointerException("level == null. Use Level.NONE instead.")
        this.level = level
        return this
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = this.level

        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }

        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        val requestBody = request.body()
        val hasRequestBody = requestBody != null

        val connection = chain.connection()
        val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
        var requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody!!.contentLength() + "-byte body)"
        }
        log(requestStartMessage)

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody!!.contentType() != null) {
                    log("Content-Type: " + requestBody.contentType()!!)
                }
                if (!requestBody.contentLength().equals(-1)) {
                    log("Content-Length: " + requestBody.contentLength())
                }
            }

            val headers = request.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                val name = headers.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(name, ignoreCase = true)
                        && !"Content-Length".equals(name, ignoreCase = true)) {
                    log(name + ": " + headers.value(i))
                }
                i++
            }

            if (!logBody || !hasRequestBody) {
                log("--> END " + request.method())
            } else if (bodyEncoded(request.headers())) {
                log("--> END " + request.method() + " (encoded body omitted)")
            } else {
                val buffer = Buffer()
                requestBody!!.writeTo(buffer)

                var charset = UTF8
                val contentType = requestBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                log("")
                if (isPlaintext(buffer)) {
                    log(buffer.readString(charset))
                    log("--> END " + request.method()
                            + " (" + requestBody.contentLength() + "-byte body)")
                } else {
                    log("--> END " + request.method() + " (binary "
                            + requestBody.contentLength() + "-byte body omitted)")
                }
            }
        }

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            log("<-- HTTP FAILED: " + e)
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body()
        val contentLength = responseBody!!.contentLength()
        val bodySize = if (!contentLength.equals(-1)) {
            contentLength.toString() + "-byte"
        } else "unknown-length"
        log("<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + (if (!logHeaders)
            ", "
                    + bodySize + " body"
        else
            "") + ')')

        if (logHeaders) {
            val headers = response.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                log(headers.name(i) + ": " + headers.value(i))
                i++
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                log("<-- END HTTP")
            } else if (bodyEncoded(response.headers())) {
                log("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()

                var charset = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                if (!isPlaintext(buffer)) {
                    log("")
                    log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)")
                    return response
                }

                if (!contentLength.equals(0)) {
                    warn(buffer.clone().readString(charset))
                }

                log("<-- END HTTP (" + buffer.size() + "-byte body)")
            }
        }

        return response
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size() < 64) buffer.size() else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    fun log(message: String) {
        warn(message)
    }


    override val loggerTag: String
        get() = "OkHttp"
}
