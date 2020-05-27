package app.shmehdi.covid19app.repository


object RepositoryInjector {

    enum class Flavour {
        DUMMY,
        API
    }

    fun getRepository(flavour: Flavour): CovidRepository {
        return when (flavour) {
            Flavour.DUMMY -> DummyRepository
            Flavour.API -> ApiRepository
        }
    }
}