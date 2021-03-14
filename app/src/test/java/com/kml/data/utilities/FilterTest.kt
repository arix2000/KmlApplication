package com.kml.data.utilities

import com.kml.data.models.Game
import com.kml.data.models.GameFilterInfo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class FilterTest {

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