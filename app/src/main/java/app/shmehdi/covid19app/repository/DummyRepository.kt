package app.shmehdi.covid19app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.shmehdi.covid19app.CovidApp
import app.shmehdi.covid19app.loadJSONFromAsset
import app.shmehdi.covid19app.network.ApiState
import app.shmehdi.covid19app.network.models.SummaryResponse
import com.google.gson.Gson

object DummyRepository : CovidRepository {

    override fun loadGlobalStats(): LiveData<ApiState<SummaryResponse>> {
        val apiStateLiveData = MutableLiveData<ApiState<SummaryResponse>>()
        apiStateLiveData.value = ApiState.Loading(true)

        val strJson = CovidApp.instance?.loadJSONFromAsset("dummay_summary.json")

        apiStateLiveData.value = ApiState.Loading(false)
        try {
            val response = Gson().fromJson<SummaryResponse>(strJson, SummaryResponse::class.java)
            apiStateLiveData.value = ApiState.Result(response)
        } catch (e: Exception) {
            apiStateLiveData.value = ApiState.Error(e.message ?: "Error", -1)
        }
        return apiStateLiveData
    }

}