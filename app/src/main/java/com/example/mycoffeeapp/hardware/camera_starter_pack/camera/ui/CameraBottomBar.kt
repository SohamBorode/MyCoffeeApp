package camera.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CameraBottomBar(
    uiState: CameraUiState,
    onCaptureClick: () -> Unit,
    onVideoClick: () -> Unit,
    onAnalyzeClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // TODO add capture/video/analyze actions
    }
}
