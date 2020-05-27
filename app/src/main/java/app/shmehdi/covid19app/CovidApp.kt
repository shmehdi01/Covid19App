package app.shmehdi.covid19app

import android.app.Application

class CovidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: CovidApp? = null
            private set
    }
}