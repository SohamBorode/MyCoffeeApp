package camera.result

sealed class CameraResult {
    data class Success(val message: String) : CameraResult()
    data class Error(val reason: String, val throwable: Throwable? = null) : CameraResult()
    data object Idle : CameraResult()
}
