package app.shmehdi.covid19app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.shmehdi.covid19app.*
import app.shmehdi.covid19app.network.models.Countries
import app.shmehdi.covid19app.ui.adapter.GlobalStatsAdapter
import app.shmehdi.covid19app.ui.dialog.FilterDialog
import app.shmehdi.covid19app.utils.SortBy
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var vm: MainViewModel
    private lateinit var globalStatsAdapter: GlobalStatsAdapter
    private val countries = mutableListOf<Countries>()
    private val mainList = mutableListOf<Countries>()
    private var filterDialog: FilterDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vm = ViewModelProvider(this).get(MainViewModel::class.java)
        vm.setLifeCycleOwner(this)

        setGlobalStats()
        startObserving()
        clickListeners()
    }

    override fun onResume() {
        vm.loadAtScheduleTime()
        super.onResume()
    }

    private fun setGlobalStats() {
        rcv_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GlobalStatsAdapter(context, countries).also {
                globalStatsAdapter = it
            }
        }
    }

    private fun startObserving() {
        vm.getLoading().observe(this, Observer {
            if(mainList.isEmpty()) {
                if (it) progress_circular.visible()
                else progress_circular.gone()
            }
            else {
                if (it) tv_filter_text.text = "Checking For New Data"
                else tv_filter_text.text = ""
            }
        })

        vm.getError().observe(this, Observer {
            Snackbar.make(coordinator,it,Snackbar.LENGTH_SHORT).show()
        })

        vm.getCountries().observe(this, Observer {
            if(mainList.isEmpty()) {
                mainList.addAll(it)
            }
            notifyAdapter(it)
        })


        vm.getGlobalData().observe(this, Observer {
            tv_confirm.setCount(it.totalConfirmed)
            tv_confirm_new.text = ("[+${it.newConfirmed}]")

            tv_recovered.setCount(it.totalRecovered)
            tv_recovered_new.text = ("[+${it.newRecovered}]")

            tv_deaths.setCount(it.totalDeaths)
            tv_deaths_new.text = ("[+${it.newDeaths}]")
        })

        vm.sortBy.observe(this, Observer {

            label_country.setDrawable()
            label_total_case.setDrawable()
            label_recovered.setDrawable()
            label_deaths.setDrawable()

            when (it) {
                SortBy.TOTAL_CASE_ASC -> label_total_case.setDrawable(right = R.drawable.ic_up)
                SortBy.TOTAL_CASE_DESC -> label_total_case.setDrawable(right = R.drawable.ic_down)
                SortBy.NAME_ASC -> label_country.setDrawable(right = R.drawable.ic_up)
                SortBy.NAME_DESC -> label_country.setDrawable(right = R.drawable.ic_down)
                SortBy.RECOVERED_ASC -> label_recovered.setDrawable(right = R.drawable.ic_up)
                SortBy.RECOVERED_DESC -> label_recovered.setDrawable(right = R.drawable.ic_down)
                SortBy.DEATH_ASC -> label_deaths.setDrawable(right = R.drawable.ic_up)
                SortBy.DEATH_DESC -> label_deaths.setDrawable(right = R.drawable.ic_down)

            }
        })

        vm.getFilter().observe(this, Observer {
            tv_filter_text.text = it
        })
    }

    private fun clickListeners() {
        label_total_case.setOnClickListener {
            vm.sortBy.value?.let {
                if (it == SortBy.TOTAL_CASE_ASC)
                    notifyAdapter(vm.sortCountries(countries, SortBy.TOTAL_CASE_DESC))
                else notifyAdapter(vm.sortCountries(countries, SortBy.TOTAL_CASE_ASC))
            }
        }
        label_country.setOnClickListener {
            vm.sortBy.value.let {
                if (it == SortBy.NAME_ASC)
                    notifyAdapter(vm.sortCountries(countries, SortBy.NAME_DESC))
                else notifyAdapter(vm.sortCountries(countries, SortBy.NAME_ASC))
            }
        }

        label_recovered.setOnClickListener {
            vm.sortBy.value.let {
                if (it == SortBy.RECOVERED_ASC)
                    notifyAdapter(vm.sortCountries(countries, SortBy.RECOVERED_DESC))
                else notifyAdapter(vm.sortCountries(countries, SortBy.RECOVERED_ASC))
            }
        }

        label_deaths.setOnClickListener {
            vm.sortBy.value.let {
                if (it == SortBy.DEATH_ASC)
                    notifyAdapter(vm.sortCountries(countries, SortBy.DEATH_DESC))
                else notifyAdapter(vm.sortCountries(countries, SortBy.DEATH_ASC))
            }
        }

        iv_filter.setOnClickListener {
            if(filterDialog == null) {
                filterDialog = FilterDialog(this){ cMin,cMax,rMin,rMax,dMin,dMax ->
                    vm.filterCountries(mainList, cMin,cMax,rMin,rMax,dMin,dMax)
                }
                filterDialog?.setOnReset {
                    tv_filter_text.text = ""
                    notifyAdapter(mainList)
                }
            }
            filterDialog?.show()
        }
    }

    private fun notifyAdapter(it: List<Countries>) {
        countries.clear()
        countries.addAll(vm.sortCountries(it, vm.sortBy.value!!))

        val locale: String = resources.configuration.locale.country

        val dummyCountry = Countries("", locale, "", 0, 0, 0, 0, 0, 0, "")
        if (countries.contains(dummyCountry)) {
            val index = countries.indexOf(dummyCountry)
            val myCountry = countries[index]
            countries.removeAt(index)
            countries.add(0, myCountry)
            globalStatsAdapter.isMyCountryAvailable = true
        } else globalStatsAdapter.isMyCountryAvailable = false

        globalStatsAdapter.notifyDataSetChanged()

        if (countries.isEmpty()) {
            tv_no_result.visible()
            rcv_list.gone()
        } else {
            tv_no_result.gone()
            rcv_list.visible()
        }
    }

}
