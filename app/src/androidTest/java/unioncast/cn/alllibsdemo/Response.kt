package unioncast.cn.alllibsdemo

import org.junit.Test
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

sealed class Response

data class Success(val body: String): Response()

data class Error(val code: Int, val message: String): Response()

object Timeout: Response()



