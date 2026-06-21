package camera.capture

import camera.result.CameraResult

class VideoCaptureManager {

    fun start(): CameraResult {
        // TODO integrate CameraX VideoCapture/Recorder
        return CameraResult.Success("Video recording started stub")
    }

    fun stop(): CameraResult {
        return CameraResult.Success("Video recording stopped stub")
    }
}
