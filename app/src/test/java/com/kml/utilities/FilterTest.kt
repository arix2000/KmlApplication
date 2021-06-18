package com.kml.utilities

import com.kml.models.Game
import com.kml.models.GameFilterInfo
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.koin.test.AutoCloseKoinTest

internal class FilterTest: AutoCloseKoinTest() {

    @Test
    fun byAll() {

        val filter = Filter(GameFilterInfo("test", "5-15", "7-11","plener","w kole","Zabawy"))
        val games = listOf(
                Game("", "", "","5-15","7-11","plener","w kole","Zabawy"),
                Game("TEST", "", "","15-30","3-6","mała zamknięta przestrzeń","rozwijająca","Zabawy"),
                Game("", "", "","30+","12+","plener","pobudzająca","Zabawy"),
                Game("", "", "","2-5","7-11","plener","Wszystkie","Drużynowe"), )

        assertEquals(1, filter.byName(games).size)
        assertEquals(1, filter.byNumberOfKids(games).size)
        assertEquals(2, filter.byKidsAge(games).size)
        assertEquals(3, filter.byPlace(games).size)
        assertEquals(3, filter.byCategory(games).size)
    }
}