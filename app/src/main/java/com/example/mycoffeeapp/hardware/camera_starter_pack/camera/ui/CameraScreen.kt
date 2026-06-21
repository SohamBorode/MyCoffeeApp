package camera.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import camera.controller.CameraController
import camera.permission.CameraPermissionManager

@Composable
fun CameraScreen(
    controller: CameraController = remember { CameraController() },
    permissionManager: CameraPermissionManager = remember { CameraPermissionManager() }
) {
    val uiState by controller.uiState.collectAsState()

    // Starter layout only. Replace with your real UI later.
    CameraPreview()
    CameraTopBar(
        onBack = { /* TODO */ },
        onSwitchCamera = { controller.switchCamera() },
        onFlashClick = { controller.toggleFlash() }
    )
    CameraBottomBar(
        uiState = uiState,
        onCaptureClick = { controller.capturePhoto() },
        onVideoClick = { controller.toggleVideoRecording() },
        onAnalyzeClick = { controller.startAnalysis() }
    )

    // Permission handling can be connected here.
    permissionManager
}
