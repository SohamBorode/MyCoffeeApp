package camera.task

sealed interface CameraTask {
    data object Preview : CameraTask
    data object CapturePhoto : CameraTask
    data object RecordVideo : CameraTask
    data object AnalyzeBarcode : CameraTask
    data object AnalyzeFace : CameraTask
    data object AnalyzeOCR : CameraTask
    data object AnalyzeObject : CameraTask
}
