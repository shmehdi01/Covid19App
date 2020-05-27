package app.shmehdi.covid19app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.shmehdi.covid19app.CovidApp
import app.shmehdi.covid19app.network.ApiState
import app.shmehdi.covid19app.network.RetrofitClient
import app.shmehdi.covid19app.network.models.SummaryResponse
import app.shmehdi.covid19app.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ApiRepository : CovidRepository {

    const val SOMETHING_WENT_WRONG = -1
    const val UNKNOWN_ERROR = -2

    val apiService = RetrofitClient.apiService

    override fun loadGlobalStats(): LiveData<ApiState<SummaryResponse>> {
        val apiStateLiveData = MutableLiveData<ApiState<SummaryResponse>>()

        apiStateLiveData.value = ApiState.Loading(true)

        apiService.summary().enqueue(object : Callback<SummaryResponse> {
            override fun onFailure(call: Call<SummaryResponse>, t: Throwable) {
                apiStateLiveData.value = ApiState.Loading(false)
                apiStateLiveData.value = handleError(t)
            }

            override fun onResponse(
                call: Call<SummaryResponse>,
                response: Response<SummaryResponse>
            ) {
                apiStateLiveData.value = ApiState.Loading(false)

                if (response.isSuccessful && response.body() != null) {
                    apiStateLiveData.value = ApiState.Result(response.body()!!)
                } else {
                    apiStateLiveData.value = handleError(response)
                }
            }

        })

        return apiStateLiveData
    }

    fun handleError(o: Any): ApiState.Error {
        when (o) {
            is Throwable -> return ApiState.Error(
                o.message ?: "Something went wrong",
                SOMETHING_WENT_WRONG
            )
            is Response<*> -> return ApiState.Error(
                o.errorBody()?.string() ?: "Empty Message",
                o.code()
            )
        }

        return ApiState.Error("Unknown Error", UNKNOWN_ERROR)
    }
}