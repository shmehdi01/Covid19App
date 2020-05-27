package app.shmehdi.covid19app.repository

import androidx.lifecycle.LiveData
import app.shmehdi.covid19app.network.ApiState
import app.shmehdi.covid19app.network.models.SummaryResponse

interface CovidRepository {

    fun loadGlobalStats(): LiveData<ApiState<SummaryResponse>>

}