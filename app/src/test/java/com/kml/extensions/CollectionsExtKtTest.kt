package com.kml.extensions

import com.kml.resources.getTestWorkList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CollectionsExtKtTest {

    @Test
    fun getWorksTimeTotal() {
       assertEquals("183h 18min",getTestWorkList().getWorksTimeTotal())
    }
}