package camera.ui

import camera.model.CameraMode
import camera.model.FlashMode
import camera.model.LensFacing

data class CameraUiState(
    val isPermissionGranted: Boolean = false,
    val isCameraReady: Boolean = false,
    val mode: CameraMode = CameraMode.PHOTO,
    val lensFacing: LensFacing = LensFacing.BACK,
    val flashMode: FlashMode = FlashMode.AUTO,
    val isRecording: Boolean = false,
    val zoomRatio: Float = 1f,
    val lastMessage: String? = null
)
