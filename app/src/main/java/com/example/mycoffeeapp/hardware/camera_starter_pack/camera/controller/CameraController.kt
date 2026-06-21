package camera.controller

import camera.model.CameraMode
import camera.model.FlashMode
import camera.model.LensFacing
import camera.result.CameraResult
import camera.ui.CameraUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraController {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    private val _result = MutableStateFlow<CameraResult>(CameraResult.Idle)
    val result: StateFlow<CameraResult> = _result.asStateFlow()

    fun setPermissionGranted(granted: Boolean) {
        _uiState.value = _uiState.value.copy(isPermissionGranted = granted)
    }

    fun setCameraReady(ready: Boolean) {
        _uiState.value = _uiState.value.copy(isCameraReady = ready)
    }

    fun switchCamera() {
        val newLens = if (_uiState.value.lensFacing == LensFacing.BACK) {
            LensFacing.FRONT
        } else {
            LensFacing.BACK
        }
        _uiState.value = _uiState.value.copy(lensFacing = newLens)
    }

    fun toggleFlash() {
        val nextFlash = when (_uiState.value.flashMode) {
            FlashMode.OFF -> FlashMode.AUTO
            FlashMode.AUTO -> FlashMode.ON
            FlashMode.ON -> FlashMode.TORCH
            FlashMode.TORCH -> FlashMode.OFF
        }
        _uiState.value = _uiState.value.copy(flashMode = nextFlash)
    }

    fun setMode(mode: CameraMode) {
        _uiState.value = _uiState.value.copy(mode = mode)
    }

    fun capturePhoto() {
        _result.value = CameraResult.Success("Capture photo requested")
    }

    fun toggleVideoRecording() {
        _uiState.value = _uiState.value.copy(isRecording = !_uiState.value.isRecording)
    }

    fun startAnalysis() {
        _result.value = CameraResult.Success("Analysis started")
    }

    fun setZoomRatio(ratio: Float) {
        _uiState.value = _uiState.value.copy(zoomRatio = ratio)
    }

    fun setMessage(message: String?) {
        _uiState.value = _uiState.value.copy(lastMessage = message)
    }

    fun reportError(message: String, throwable: Throwable? = null) {
        _result.value = CameraResult.Error(message, throwable)
    }
}
