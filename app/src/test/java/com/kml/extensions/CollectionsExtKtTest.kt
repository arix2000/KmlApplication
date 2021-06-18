package com.kml.extensions

import com.kml.resources.getTestWorkList
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.koin.test.AutoCloseKoinTest

internal class CollectionsExtKtTest: AutoCloseKoinTest() {

    @Test
    fun getWorksTimeTotal() {
       assertEquals("183h 18min",getTestWorkList().getWorksTimeTotal())
    }
}