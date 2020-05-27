package app.shmehdi.covid19app.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import app.shmehdi.covid19app.network.ApiState
import app.shmehdi.covid19app.network.models.Countries
import app.shmehdi.covid19app.network.models.Global
import app.shmehdi.covid19app.repository.ApiRepository
import app.shmehdi.covid19app.repository.CovidRepository
import app.shmehdi.covid19app.repository.RepositoryInjector
import app.shmehdi.covid19app.utils.SortBy
import java.util.*
import kotlin.concurrent.timerTask


class MainViewModel : ViewModel() {

    private val repository by lazy {
        RepositoryInjector.getRepository(RepositoryInjector.Flavour.API)
    }

    var sortBy = MutableLiveData<Int>(SortBy.TOTAL_CASE_DESC)
        private set

    private val timer = Timer()

    private var lifecycleOwner: LifecycleOwner? = null

    private val globalLiveData = MutableLiveData<Global>()
    private val countriesLiveData = MutableLiveData<List<Countries>>()

    private val loading = MutableLiveData<Boolean>()
    private val error = MutableLiveData<String>()
    private val liveDataFilter = MutableLiveData<String>()


    fun getGlobalData(): LiveData<Global> = globalLiveData

    fun getCountries(): MutableLiveData<List<Countries>> = countriesLiveData

    fun getLoading(): LiveData<Boolean> = loading

    fun getError(): LiveData<String> = error

    fun getFilter(): LiveData<String> = liveDataFilter

    fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
    }

    private fun loadGlobalStats(repository: CovidRepository = RepositoryInjector.getRepository(RepositoryInjector.Flavour.API)) {
        repository.loadGlobalStats().observe(lifecycleOwner!!, Observer {
            when (it) {
                is ApiState.Loading -> loading.value = it.loading
                is ApiState.Result -> {
                    globalLiveData.value = it.result.global
                    countriesLiveData.value = it.result.countries
                }
                is ApiState.Error -> {
                    if (it.errorCode == ApiRepository.MAX_LIMIT_REACHED) {
                        loadGlobalStats(repository = RepositoryInjector.getRepository(RepositoryInjector.Flavour.DUMMY))
                    }
                    else error.value = it.errorMsg
                }
            }
        })
    }

    fun loadAtScheduleTime(second: Long = 60*2) {
        timer.scheduleAtFixedRate(
            timerTask {
                Handler(Looper.getMainLooper()).post {
                    loadGlobalStats()
                }
            },
            0,second *1000
        )

    }

    fun sortCountries(countries: List<Countries>, sortBy: Int): List<Countries> {
        this.sortBy.value = sortBy
        return when (sortBy) {
            SortBy.TOTAL_CASE_ASC -> countries.sortedBy { it.totalConfirmed }
            SortBy.TOTAL_CASE_DESC -> countries.sortedByDescending { it.totalConfirmed }
            SortBy.NAME_ASC -> countries.sortedBy { it.country }
            SortBy.NAME_DESC -> countries.sortedByDescending { it.country }
            SortBy.RECOVERED_ASC -> countries.sortedBy { it.totalRecovered }
            SortBy.RECOVERED_DESC -> countries.sortedByDescending { it.totalRecovered }
            SortBy.DEATH_ASC -> countries.sortedBy { it.totalDeaths }
            SortBy.DEATH_DESC -> countries.sortedByDescending { it.totalRecovered }
            else -> countries
        }
    }


    fun filterCountries(
        countries: List<Countries>,
        cMin: Int,
        cMax: Int,
        rMin: Int,
        rMax: Int,
        dMin: Int,
        dMax: Int
    ) {
        if ((cMin > -1 || dMax > -1) && (rMin > -1 || rMax > -1) && (dMin > -1 || dMax > -1)) {
            countriesLiveData.value = countries.filter {
                it.totalConfirmed in cMin..cMax && it.totalRecovered in rMin..rMax && it.totalDeaths in dMin..dMax
            }
            liveDataFilter.value = "Filtered by Total Case, Recovered & Death"

        } else if ((cMin > -1 || cMax > -1) && (rMin > -1 || rMax > -1)) {
            countriesLiveData.value = countries.filter {
                it.totalConfirmed in cMin..cMax && it.totalRecovered in rMin..rMax
            }
            liveDataFilter.value = "Filtered by Total Case & Recovered"
        } else if ((cMin > -1 || cMax > -1) && (dMin > -1 || dMax > -1)) {
            countriesLiveData.value = countries.filter {
                it.totalConfirmed in cMin..cMax && it.totalDeaths in dMin..dMax
            }
            liveDataFilter.value = "Filtered by Total Case & Deaths"
        } else if ((rMin > -1 || rMax > -1)  && (dMin > -1 || dMax > -1)) {
            countriesLiveData.value = countries.filter {
                it.totalRecovered in rMin..rMax && it.totalDeaths in dMin..dMax
            }
            liveDataFilter.value = "Filtered by Recovered & Deaths"
        } else if (cMin > -1 || cMax > -1) {
            countriesLiveData.value = countries.filter {
                it.totalConfirmed in cMin..cMax
            }
            liveDataFilter.value = "Filtered by Total Case"
        } else if (rMin > -1 || rMax > -1) {
            countriesLiveData.value = countries.filter {
                it.totalRecovered in rMin..rMax
            }
            liveDataFilter.value = "Filtered by  Recovered "
        } else if (dMin > -1 || dMax > -1) {
            countriesLiveData.value = countries.filter {
                it.totalDeaths in dMin..dMax
            }
            liveDataFilter.value = "Filtered by Deaths"
        }
    }



}