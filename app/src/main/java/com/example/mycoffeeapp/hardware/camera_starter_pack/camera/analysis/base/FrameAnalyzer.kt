package camera.analysis.base

interface FrameAnalyzer<T> {
    fun analyze(image: T)
}
