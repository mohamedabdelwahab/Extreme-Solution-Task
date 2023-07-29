package  com.mohamed.sampleapp.core.utilities

import com.mohamed.sampleapp.core.utilities.networkUtils.Failure
import com.mohamed.sampleapp.core.utilities.networkUtils.Resource
import com.mohamed.sampleapp.core.baseView.UiStateWrapper

class UiHelper private constructor() {

    companion object {
        fun getErrorUiState(result: Resource.Error): UiStateWrapper.Error {
            var uiErrorState: UiStateWrapper.Error
            when (result.error) {
                is Failure.NetworkConnection -> {
                    uiErrorState = UiStateWrapper.Error(40, "No Internet Connection")
                }
                is Failure.UnAuthorize -> {
                    uiErrorState = UiStateWrapper.Error(401, "Authorization Error")
                }
                is Failure.ServerError -> {
                    uiErrorState = UiStateWrapper.Error(500, "Server Error")
                }
                is Failure.UnknownError -> {
                    uiErrorState = UiStateWrapper.Error(
                        400, result.error.message
                            ?: "Unknown Error"
                    )
                }
                else -> {
                    uiErrorState = UiStateWrapper.Error(300, "Something happen")
                }
            }
            return uiErrorState
        }
    }
}