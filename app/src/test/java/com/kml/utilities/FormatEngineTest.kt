package com.kml.utilities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class FormatEngineTest {

    private val engine = FormatEngine()
    var result: String =""

    @Test
    fun convertToReadable() {
        val number = Random.nextDouble(0.0,1000.0)

        result = engine.convertToReadable(number.toString())
        assertNotEquals("", result)

        result = engine.convertToReadable("1.59")
        assertEquals("1 godz 35 min", result)
    }

    @Test
    fun formatSections() {
        result = engine.formatSections("Lider w... Wolontariusz w...")
        assertEquals("Lider w...  \n" + "\n" + "Wolontariusz w...", result)

        result = engine.formatSections("Wolontariusz w...")
        assertEquals("Wolontariusz w...", result)
    }

    @Test
    fun formatSeconds() {
        result = engine.formatSeconds(9)
        resultEquals("09")

        result = engine.formatSeconds(60)
        resultEquals("00")

        val randomNumber = Random.nextInt(10,60)
        result = engine.formatSeconds(randomNumber)
        resultEquals(randomNumber.toString())
    }

    @Test
    fun formatMinutes() {
        singleTimeTestWithColon { engine.formatMinutes(it) }
    }

    @Test
    fun formatHours() {
        singleTimeTestWithColon { engine.formatHours(it) }
    }

    private fun singleTimeTestWithColon(formatter: (Int)-> String)
    {
        result = formatter(9)
        resultEquals("09:")

        result = formatter(60)
        resultEquals("00:")

        val randomNumber = Random.nextInt(10,60)
        result = formatter(randomNumber)
        resultEquals("$randomNumber:")
    }

    private fun resultEquals(text:String)
    {
        assertEquals(text, result)
    }
}