package app.shmehdi.covid19app.network

sealed class ApiState<out Data> {

    class Loading(var loading: Boolean): ApiState<Nothing>()
    class Result<Data>(var result: Data): ApiState<Data>()
    class Error(var errorMsg: String, var errorCode: Int): ApiState<Nothing>()

}

