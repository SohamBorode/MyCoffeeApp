package camera.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CameraTopBar(
    onBack: () -> Unit,
    onSwitchCamera: () -> Unit,
    onFlashClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // TODO add icons/buttons
    }
}
