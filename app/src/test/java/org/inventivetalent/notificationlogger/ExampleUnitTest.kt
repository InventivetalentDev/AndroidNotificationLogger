package org.inventivetalent.notificationlogger

import org.junit.Assert.assertEquals
import org.junit.Test

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

    @Test
    fun radixTest() {
        val x =268
        for(i in 2..36){
            println("radix " + i + " of " + x + ": " + x.toString(i))
            println("  unsigned: "+Integer.toUnsignedString(x, i))
        }

    }

}
