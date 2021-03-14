package com.kml.repositories

import com.kml.data.externalDbOperations.DbGetAllUsersData

class VolunteerRepository {
    fun readArrayFromDatabase(): String {
        val dbGetAllUsersData = DbGetAllUsersData()
        dbGetAllUsersData.start()
        return dbGetAllUsersData.result
    }
}