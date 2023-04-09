package com.vn.chat_app_client.data.api.common

sealed class DataResponse<T> constructor(var loadingStatus: LoadingStatus) {
    class DataLoading<T>(private val loadingType: LoadingStatus) : DataResponse<T>(loadingType)
    class DataIdle<T> : DataResponse<T>(LoadingStatus.Idle)
    class DataError<T> : DataResponse<T>(LoadingStatus.Error)
    data class DataSuccess<T>(val body: T) : DataResponse<T>(LoadingStatus.Success)
    class DataLoadingProgress<T>(val progress: Int) : DataResponse<T>(LoadingStatus.Loading)
}