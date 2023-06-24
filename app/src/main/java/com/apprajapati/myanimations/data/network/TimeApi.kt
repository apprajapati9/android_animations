package com.apprajapati.myanimations.data.network

import com.apprajapati.myanimations.util.Constants
import com.apprajapati.myanimations.models.Time
import retrofit2.http.GET

interface TimeApi {

    @GET(Constants.BASE_URL)
    suspend fun getTime() : Time
}