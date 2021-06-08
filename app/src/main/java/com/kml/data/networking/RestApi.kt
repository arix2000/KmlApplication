package com.kml.data.networking

import com.kml.models.dto.Profile
import com.kml.models.dto.Volunteer
import com.kml.models.dto.Work
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RestApi {

    @FormUrlEncoded
    @POST("login.php")
    fun logIn(@Field("user") user: String, @Field("pass") pass: String): Single<String>

    @FormUrlEncoded
    @POST("getDataAboutUser.php")
    fun fetchUserInfo(@Field("loginId") loginId: Int): Single<Profile>

    @FormUrlEncoded
    @POST("updateCzasPracy.php")
    fun addWork(
        @Field("loginId") loginId: String,
        @Field("czasPracy") workTime: String,
        @Field("nazwaZadania") workName: String,
        @Field("opisZadania") workDescription: String,
        @Field("czasPracyDokladny") readableWorkTime: String,
        @Field("imie") firstname: String,
        @Field("nazwisko") lastname: String
    ): Single<Boolean>

    @FormUrlEncoded
    @POST("getWorkHistory.php")
    fun fetchWorkHistory(
        @Field("firstName") firstname: String,
        @Field("lastName") lastname: String,
    ): Single<List<Work>>

    @FormUrlEncoded
    @POST("getMeetingsHistory.php")
    fun fetchMeetingsHistory(
        @Field("firstName") firstname: String,
        @Field("lastName") lastname: String,
    ): Single<List<Work>>

    @GET("getAllDataAboutUser.php")
    fun fetchVolunteers(): Single<List<Volunteer>>

    @FormUrlEncoded
    @POST("addTimeOfWorkToChosen.php")
    fun addWorkToChosen(
        @Field("ids") ids: String,
        @Field("workTime") workTime: String,
        @Field("workName") workName: String,
        @Field("meetingDesc") workDescription: String,
        @Field("readAbleWorkTime") readableWorkTime: String,
        @Field("volunteersName") volunteersName: String,
        @Field("firstName") firstname: String,
        @Field("lastname") lastname: String,
        @Field("meetingType") meetingType: String
    ): Single<Boolean>

    @FormUrlEncoded
    @POST("changePass.php")
    fun changePass(
        @Field("newPassword") newPassword: String,
        @Field("oldPassword") oldPassword: String,
        @Field("loginId") loginId: String
    ): Single<String>
}