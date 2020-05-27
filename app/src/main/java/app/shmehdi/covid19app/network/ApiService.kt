package app.shmehdi.covid19app.network

import app.shmehdi.covid19app.network.models.SummaryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("summary")
    fun summary(): Call<SummaryResponse>

}