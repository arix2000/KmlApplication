package com.kml.resources

import com.kml.extensions.createWorkListFrom
import com.kml.models.Work
import org.junit.platform.commons.util.ClassLoaderUtils

fun getTestWorkList(): List<Work> {
    val json = ClassLoaderUtils.getDefaultClassLoader().getResource("WorksList.json").readText()
    return createWorkListFrom(json)
}

fun getTypesList(): List<String> {
    return listOf("Wszystkie", "Brak", "Spotkania ogólne", "Spotkania sekcji",
        "Szkolenia", "Integracje", "Spotkania do wydarzeń", "Wydarzenia")
}