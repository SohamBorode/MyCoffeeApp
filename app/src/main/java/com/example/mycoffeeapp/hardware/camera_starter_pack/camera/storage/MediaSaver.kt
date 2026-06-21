package camera.storage

class MediaSaver {

    fun saveImage(bytes: ByteArray): String {
        // TODO save to MediaStore or app storage
        return "content://stub/image"
    }

    fun saveVideo(path: String): String {
        // TODO save video reference
        return "content://stub/video"
    }
}
