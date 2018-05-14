package unioncast.cn.alllibsdemo

import io.reactivex.Flowable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    inline fun <T, R> T.lock(body: () -> R): R {
        // ...
        return body()
    }

    inline fun <T> max(collection: Collection<out T>, less: (x: T, y: T) -> Boolean): T? {
        var max: T? = null
        for (it in collection)
            if (max == null || less(max, it))
                max = it
        return max
    }

    //字面函数
    val sum = { x: Int, y: Int -> x + y }

    val sum2: (Int, Int) -> Int = { x, y -> x + y }

    fun sum3(x: Int, y: Int): Int = x + y

    val sum4 = fun(x: Int, y: Int): Int {
        return x + y
    }

    @Test
    fun main() {
        val maxV = max(arrayListOf(1, 3, 5, 6), { a, b ->
            a < b
        })

        println("maxV $maxV")
        println("sum ${sum(1, 2)}")
    }

    @Test
    fun test() {

        Flowable.just("Hello world").subscribe(System.out::println);
        Flowable.just("Hello world")
                .subscribe({ s -> println(s) },  { s -> println(s) })

        val list = listOf("Alpha", "Beta", "Gamma", "Delta", "Epsilon")

        list.toObservable() // extension function for Iterables
                .filter { it.length >= 5 }
                .subscribeBy(  // named arguments for lambda Subscribers
                        onNext = { println(it) },
                        onError = { it.printStackTrace() },
                        onComplete = { println("Done!") }
                )
    }
}
