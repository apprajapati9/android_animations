package com.apprajapati.myanimations.data.network

import com.apprajapati.myanimations.util.Constants
import com.apprajapati.myanimations.models.Time
import com.apprajapati.myanimations.models.TimeApiTime
import retrofit2.Response
import retrofit2.http.GET

interface TimeApi {

    @GET(Constants.BASE_URL)
    suspend fun getTime() : Response<Time>

    @GET(Constants.BASE_URL2)
    suspend fun getTimeTimeApi() : Response<TimeApiTime>
}