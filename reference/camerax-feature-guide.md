# CameraX Feature Guide for Android Apps

This document explains how to build camera-driven features in an Android app using CameraX.
It is written for a Compose-based app like `MyCoffeeApp`, but the architecture works for any Android project.

Camera features are not one thing. A camera screen can behave differently depending on the task:

- Profile photo capture
- Barcode or QR scanning
- Search by scanning a code
- ML analysis on live frames
- Video recording

The key idea is not "build a camera screen". The key idea is "build a reusable camera engine and plug different tasks into it".

---

## 1. What CameraX Gives You

CameraX is the Jetpack camera library that sits on top of the camera framework and gives you lifecycle-aware camera use cases.

The main CameraX use cases are:

- `Preview`
  - Shows the live camera feed on screen.
- `ImageCapture`
  - Takes still photos.
- `ImageAnalysis`
  - Streams frames to your code for barcode scanning or ML inference.
- `VideoCapture`
  - Records video.

Why CameraX is useful:

- It is lifecycle-aware.
- It works well with Compose through `AndroidView` and `PreviewView`.
- It lets you attach only the use cases you need.
- You can reuse the same camera screen for multiple tasks.

Official references:

- CameraX overview: https://developer.android.com/media/camera/camerax
- CameraX video capture: https://developer.android.com/media/camera/camerax/video-capture
- ML Kit barcode scanning: https://developers.google.com/ml-kit/vision/barcode-scanning/android

---

## 2. There Is No Fixed Template, But There Is a Fixed Pattern

There is no single fixed UI template for all camera features.

What is fixed is the architecture:

1. Decide the task.
2. Request the right permissions.
3. Show a preview.
4. Bind the correct CameraX use cases.
5. Process the result.
6. Unbind and clean up.

The UI can look very different depending on the feature.

For example:

- A profile photo screen needs preview, capture, and a confirm button.
- A barcode scanner needs preview, analysis, and a scan overlay.
- A video recorder needs preview, record/stop controls, and audio permission.
- A search-by-scan screen needs preview and an analyzer that updates search state.
- An ML analysis screen needs preview and an analyzer that sends frames to an inference pipeline.

So the reusable part is the camera pipeline, not the buttons.

---

## 3. What You Need To Define Before Writing Code

Before coding, define these things clearly:

| Decision           | Why it matters                                                    |
| ------------------ | ----------------------------------------------------------------- |
| Camera task        | Determines which CameraX use cases to bind                        |
| Output type        | Photo URI, barcode text, analysis result, or video URI            |
| Permission needs   | CAMERA only, or CAMERA + RECORD_AUDIO for video                   |
| Storage strategy   | Save to MediaStore, app cache, or a backend upload flow           |
| Camera lens        | Back camera, front camera, or toggle support                      |
| Flash support      | Useful for capture and some scan flows                            |
| Result callback    | How the feature returns data to the screen or ViewModel           |
| Error handling     | Permission denied, no camera, analysis failure, file save failure |
| Lifecycle behavior | What happens on pause, resume, and screen exit                    |

If these are not defined first, the implementation becomes messy fast.

---

## 4. Recommended Project Structure

For a reusable camera feature, keep the code split by responsibility:

```text
camera/
  CameraTask.kt
  CameraScreen.kt
  CameraPermissionGate.kt
  CameraController.kt
  CameraResult.kt
  analyzers/
    BarcodeAnalyzer.kt
    MlFrameAnalyzer.kt
```

If you want a smaller structure, you can keep it in fewer files at first, but do not mix everything into one composable.

### Suggested responsibilities

- `CameraTask`
  - Describes what the camera should do.
- `CameraScreen`
  - Renders the preview and controls.
- `CameraPermissionGate`
  - Requests runtime permissions.
- `CameraController`
  - Binds and unbinds CameraX use cases.
- `BarcodeAnalyzer`
  - Reads QR codes and barcodes from frames.
- `MlFrameAnalyzer`
  - Processes frames for custom ML logic.

---

## 5. Important Note About Your Current Project

Your project already has:

- `app/src/main/java/com/example/mycoffeeapp/hardware/Camera.kt`
- `app/src/main/java/com/example/mycoffeeapp/hardware/CameraPermission.kt`

Those files are a good start, but they are currently too small and incomplete for a full camera feature.

I recommend:

- Rename `Camera()` to `CameraScreen()` or `CameraHost()`
- Keep permission logic in a separate composable
- Move the actual binding logic into a helper or controller
- Avoid naming a composable `Camera` because it can be confusing with `android.graphics.Camera`

Also, your permission composable should check the actual permission result map instead of assuming success.

---

## 6. Gradle Dependencies

Use the same CameraX version across all CameraX artifacts.

```kotlin
dependencies {
    val cameraxVersion = "1.4.1"

    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")
    implementation("androidx.camera:camera-video:$cameraxVersion")

    // Optional, useful for barcode and scanning flows
    implementation("androidx.camera:camera-mlkit-vision:$cameraxVersion")

    // ML Kit barcode scanning
    implementation("com.google.mlkit:barcode-scanning:17.3.0")
}
```

If you are using Compose, you also need:

```kotlin
implementation("androidx.activity:activity-compose:1.10.1")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.0")
```

If your project already includes equivalent versions through a version catalog, keep using that approach.

---

## 7. Manifest Permissions

Add only the permissions the feature really needs.

```xml
<manifest ...>
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />

    <uses-feature
        android:name="android.hardware.camera.any"
        android:required="false" />

    <application ...>
        ...
    </application>
</manifest>
```

Notes:

- `CAMERA` is required for preview, photo, scan, and ML analysis.
- `RECORD_AUDIO` is required only if you record video with sound.
- If the app should still install on devices without a camera, keep the hardware feature as `required="false"`.

---

## 8. Permission Gate Composable

Do not open the camera until permission is granted.

```kotlin
package com.example.mycoffeeapp.hardware

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CameraPermissionGate(
    permissions: Array<String>,
    onGranted: @Composable () -> Unit
) {
    var isGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        isGranted = permissions.all { permission -> result[permission] == true }
    }

    if (isGranted) {
        onGranted()
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { launcher.launch(permissions) }) {
                Text(text = "Grant camera permission")
            }
        }
    }
}
```

Use this:

- `arrayOf(Manifest.permission.CAMERA)` for photo, scan, and ML analysis
- `arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)` for video recording

---

## 9. Core Data Types

Define the camera task and the result types first.

```kotlin
package com.example.mycoffeeapp.hardware

import android.net.Uri

sealed interface CameraTask {
    data object ProfilePhoto : CameraTask
    data object BarcodeScan : CameraTask
    data object SearchByScan : CameraTask
    data object MlAnalysis : CameraTask
    data object VideoRecording : CameraTask
}

sealed interface CameraResult {
    data class Photo(val uri: Uri) : CameraResult
    data class Barcode(val value: String) : CameraResult
    data class Analysis(val label: String, val confidence: Float) : CameraResult
    data class Video(val uri: Uri) : CameraResult
}
```

This lets the camera feature return structured data instead of random callbacks everywhere.

---

## 10. Compose Preview Host

In Compose, CameraX preview usually lives inside an `AndroidView` that hosts `PreviewView`.

The basic preview host looks like this:

```kotlin
package com.example.mycoffeeapp.hardware

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executors

@Composable
fun CameraScreen(
    task: CameraTask,
    modifier: Modifier = Modifier,
    onPhotoCaptured: (android.net.Uri) -> Unit = {},
    onBarcodeFound: (String) -> Unit = {},
    onAnalysisResult: (String) -> Unit = {},
    onVideoSaved: (android.net.Uri) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    var camera by remember { mutableStateOf<Camera?>(null) }
    var lensFacing by rememberSaveable { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    )

    LaunchedEffect(task, lensFacing) {
        camera = bindCameraUseCases(
            context = context,
            lifecycleOwner = lifecycleOwner,
            previewView = previewView,
            lensFacing = lensFacing,
            task = task,
            imageCapture = imageCapture,
            analyzerExecutor = cameraExecutor,
            onBarcodeFound = onBarcodeFound,
            onAnalysisResult = onAnalysisResult
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }
}
```

The composable above is only the host.

The real work happens in the binding function.

---

## 11. Binding CameraX Use Cases

This is the important part.

```kotlin
package com.example.mycoffeeapp.hardware

import android.content.Context
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.video.QualitySelector
import androidx.camera.video.Quality
import androidx.camera.video.VideoCapture.OutputFileResults
import androidx.camera.video.VideoRecordEvent
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.PendingRecording
import androidx.camera.video.VideoCapture.withOutput
import androidx.camera.video.Recorder.Builder
import androidx.camera.video.FileDescriptorOutputOptions
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.camera.view.PreviewView
import java.util.concurrent.ExecutorService
import kotlin.coroutines.resume

suspend fun bindCameraUseCases(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    lensFacing: Int,
    task: CameraTask,
    imageCapture: ImageCapture,
    analyzerExecutor: ExecutorService,
    onBarcodeFound: (String) -> Unit,
    onAnalysisResult: (String) -> Unit
): Camera {
    val provider = context.getCameraProvider()
    val preview = Preview.Builder().build().also {
        it.setSurfaceProvider(previewView.surfaceProvider)
    }

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    val useCases = mutableListOf<UseCase>(preview)

    when (task) {
        CameraTask.ProfilePhoto -> {
            useCases += imageCapture
        }

        CameraTask.BarcodeScan, CameraTask.SearchByScan -> {
            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            analysis.setAnalyzer(
                analyzerExecutor,
                BarcodeAnalyzer(
                    onBarcodeFound = onBarcodeFound
                )
            )

            useCases += analysis
        }

        CameraTask.MlAnalysis -> {
            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            analysis.setAnalyzer(
                analyzerExecutor,
                MlFrameAnalyzer(
                    onResult = onAnalysisResult
                )
            )

            useCases += analysis
        }

        CameraTask.VideoRecording -> {
            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HD))
                .build()

            val videoCapture = VideoCapture.withOutput(recorder)
            useCases += videoCapture
        }
    }

    provider.unbindAll()
    return provider.bindToLifecycle(
        lifecycleOwner,
        cameraSelector,
        *useCases.toTypedArray()
    )
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
        val future = ProcessCameraProvider.getInstance(this)
        future.addListener(
            {
                continuation.resume(future.get())
            },
            ContextCompat.getMainExecutor(this)
        )
    }
```

### Why this pattern works

- `Preview` is always present.
- `ImageCapture` is added only when you need a still image.
- `ImageAnalysis` is added only when you need scanning or ML processing.
- `VideoCapture` is added only for video.
- `bindToLifecycle` makes the camera stop automatically when the screen is paused or destroyed.

### Important practical note

Some device combinations cannot support every use case at the same time.
If binding fails:

- Reduce resolution
- Remove a non-essential use case
- Split the camera into separate screens

That is normal. Camera hardware limits are real.

---

## 12. Barcode and QR Scanning

For scanning, the correct pattern is:

1. `Preview`
2. `ImageAnalysis`
3. Convert each frame to `InputImage`
4. Run ML Kit barcode scanning
5. Close the `ImageProxy`

### Barcode analyzer

```kotlin
package com.example.mycoffeeapp.hardware

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(
    private val onBarcodeFound: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner: BarcodeScanner by lazy {
        val options = com.google.mlkit.vision.barcode.BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC,
                Barcode.FORMAT_CODE_128,
                Barcode.FORMAT_EAN_13
            )
            .build()

        com.google.mlkit.vision.barcode.BarcodeScanning.getClient(options)
    }

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                val value = barcodes.firstOrNull()?.rawValue
                if (!value.isNullOrBlank()) {
                    onBarcodeFound(value)
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}
```

### Search by scanning

If the goal is "search by scanning", do not make the analyzer own the search logic.
Instead, forward the barcode value to a ViewModel:

```kotlin
fun onBarcodeFound(value: String) {
    _searchQuery.value = value
    searchCoffee(value)
}
```

This keeps the camera layer reusable.

### Barcode scanning tips

- Restrict the formats you support.
- Use `KEEP_ONLY_LATEST` backpressure strategy.
- Avoid heavy work in the analyzer thread.
- Close every `ImageProxy`.
- Prevent duplicate scans with a debounce or a "scan locked" flag.

The ML Kit barcode docs recommend limiting the expected barcode formats for faster detection.

---

## 13. ML Analysis

ML analysis uses the same `ImageAnalysis` pipeline as barcode scanning.
The difference is what you do with the frame.

Typical flow:

1. Receive the frame
2. Convert it to `InputImage`
3. Run your classifier or detector
4. Return the result to the UI or ViewModel
5. Close the image

### Example analyzer

```kotlin
package com.example.mycoffeeapp.hardware

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage

class MlFrameAnalyzer(
    private val onResult: (String) -> Unit
) : ImageAnalysis.Analyzer {

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        runCustomInference(inputImage,
            onSuccess = { label ->
                onResult(label)
            },
            onComplete = {
                imageProxy.close()
            }
        )
    }

    private fun runCustomInference(
        inputImage: InputImage,
        onSuccess: (String) -> Unit,
        onComplete: () -> Unit
    ) {
        try {
            // Replace this with TensorFlow Lite, ML Kit custom model,
            // or your own inference pipeline.
            onSuccess("Sample result")
        } finally {
            onComplete()
        }
    }
}
```

### ML analysis tips

- Do not run inference directly on the main thread.
- Throttle the analyzer if the model is expensive.
- If analysis is only needed after a shutter press, use `ImageCapture` instead of live analysis.

---

## 14. Profile Photo Capture

For profile photos, the simplest and most reliable pattern is:

1. Show preview.
2. Use `ImageCapture`.
3. Save to `MediaStore` or app cache.
4. Return the image URI.
5. Let the UI decide whether to crop, upload, or preview it.

### Capture photo example

```kotlin
package com.example.mycoffeeapp.hardware

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Locale

fun capturePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onPhotoCaptured: (android.net.Uri) -> Unit,
    onError: (String) -> Unit
) {
    val fileName = SimpleDateFormat(
        "yyyyMMdd_HHmmss",
        Locale.US
    ).format(System.currentTimeMillis())

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
    }

    val outputOptions = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val uri = outputFileResults.savedUri
                if (uri != null) {
                    onPhotoCaptured(uri)
                } else {
                    onError("Photo saved, but no URI was returned")
                }
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception.message ?: "Unable to save photo")
            }
        }
    )
}
```

### Profile photo tips

- Use the front camera by default if the feature is "take my photo".
- Add a square frame overlay if the image is meant for avatars.
- Let the user confirm before uploading.
- If you need cropping, crop after capture, not inside the camera pipeline.

---

## 15. Video Recording

Video recording uses a different mental model:

- Preview is still present.
- Recording is started and stopped.
- Audio permission is required if you want sound.

### Basic setup

```kotlin
val recorder = Recorder.Builder()
    .setQualitySelector(QualitySelector.from(Quality.HD))
    .build()

val videoCapture = VideoCapture.withOutput(recorder)
```

### Start and stop recording

```kotlin
import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.video.Recorder
import androidx.core.content.ContextCompat

fun startRecording(
    context: Context,
    videoCapture: VideoCapture<Recorder>,
    withAudio: Boolean,
    onEvent: (VideoRecordEvent) -> Unit
): Recording {
    val fileName = "video_${System.currentTimeMillis()}"

    val outputOptions = MediaStoreOutputOptions.Builder(
        context.contentResolver,
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    ).setContentValues(
        ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
        }
    ).build()

    val pendingRecording = videoCapture.output.prepareRecording(
        context,
        outputOptions
    )

    val finalRecording = if (withAudio) {
        pendingRecording.withAudioEnabled()
    } else {
        pendingRecording
    }

    return finalRecording.start(
        ContextCompat.getMainExecutor(context),
        onEvent
    )
}
```

### Video tips

- Ask for `RECORD_AUDIO` only if the user actually starts recording.
- Stop recording in `onDispose` or when the screen closes.
- Use a smaller quality if the device cannot support full resolution.
- Save to `MediaStore` if you want the video to appear in the gallery.

---

## 16. A Clean ViewModel Pattern

Camera screens often become easier to maintain when the ViewModel owns feature state and the camera composable only renders and dispatches events.

### Example ViewModel

```kotlin
package com.example.mycoffeeapp.hardware

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CameraUiState(
    val isFlashOn: Boolean = false,
    val lensFacing: Int = androidx.camera.core.CameraSelector.LENS_FACING_BACK,
    val lastPhotoUri: Uri? = null,
    val lastScanValue: String? = null,
    val isRecording: Boolean = false
)

class CameraViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    fun toggleFlash() {
        _uiState.value = _uiState.value.copy(isFlashOn = !_uiState.value.isFlashOn)
    }

    fun switchCamera() {
        val newLens = if (_uiState.value.lensFacing == androidx.camera.core.CameraSelector.LENS_FACING_BACK) {
            androidx.camera.core.CameraSelector.LENS_FACING_FRONT
        } else {
            androidx.camera.core.CameraSelector.LENS_FACING_BACK
        }
        _uiState.value = _uiState.value.copy(lensFacing = newLens)
    }

    fun onPhotoCaptured(uri: Uri) {
        _uiState.value = _uiState.value.copy(lastPhotoUri = uri)
    }

    fun onBarcodeScanned(value: String) {
        _uiState.value = _uiState.value.copy(lastScanValue = value)
    }

    fun setRecordingState(isRecording: Boolean) {
        _uiState.value = _uiState.value.copy(isRecording = isRecording)
    }
}
```

This is a better long-term pattern than storing everything inside the composable.

---

## 17. UI Controls You Usually Need

Depending on the feature, you may need some or all of these:

- Switch camera front/back
- Flash on/off
- Shutter button
- Scan frame overlay
- Record button
- Stop button
- Zoom slider
- Gallery thumbnail
- Confirm or cancel action

Do not add controls blindly.

For example:

- A barcode screen probably does not need a gallery thumbnail.
- A profile photo screen probably does not need a live scan overlay.
- A video screen needs a timer or recording indicator.

---

## 18. Common Mistakes

These are the mistakes that usually cause camera features to fail or become hard to maintain:

- Putting all camera logic in one composable
- Forgetting to close `ImageProxy`
- Assuming permission is granted without checking the result
- Using the wrong permission set for the feature
- Binding too many use cases on low-end devices
- Doing heavy ML work on the main thread
- Not cleaning up the analyzer executor
- Not handling the lifecycle when the screen leaves composition
- Using fixed UI for every camera task

If you avoid these, the implementation becomes much more stable.

---

## 19. How To Use CameraX For Specific Feature Types

### A. Profile photo

Use:

- `Preview`
- `ImageCapture`

Flow:

1. Open camera.
2. Let the user frame the shot.
3. Capture photo.
4. Save URI.
5. Show preview or upload.

### B. QR or barcode scanner

Use:

- `Preview`
- `ImageAnalysis`

Flow:

1. Open camera.
2. Analyze each frame.
3. Detect barcode.
4. Return scanned text.
5. Close or keep scanning.

### C. Search by scan

Use:

- `Preview`
- `ImageAnalysis`

Flow:

1. Scan code.
2. Convert value to search query.
3. Filter local data or call backend search.

### D. ML analysis

Use:

- `Preview`
- `ImageAnalysis`

Flow:

1. Feed live frames into a model.
2. Parse labels, objects, or pose data.
3. Update UI state.

### E. Video recording

Use:

- `Preview`
- `VideoCapture`

Flow:

1. Open preview.
2. Start recording.
3. Stop recording.
4. Save or upload the video URI.

---

## 20. How This Fits a Compose App

In a Compose app, the flow is usually:

1. A screen decides the task.
2. A permission gate requests access.
3. A camera host composable shows the preview.
4. A controller or analyzer produces the result.
5. The ViewModel stores the result or forwards it to business logic.

That means your app can reuse the same camera feature in many places:

- Profile screen
- Search screen
- Product scanning screen
- Inventory screen
- ML utility screen
- Content creation screen

The difference is only the task and the callback, not the whole implementation.

---

## 21. Recommended Approach For This Project

For `MyCoffeeApp`, a practical implementation would be:

1. Create a reusable `hardware` camera module.
2. Replace `Camera.kt` with `CameraScreen.kt`.
3. Replace `CameraPermission.kt` with `CameraPermissionGate.kt`.
4. Add `CameraTask` and `CameraResult`.
5. Use one screen for photo capture and scanning first.
6. Add video recording later if needed.

If you are integrating scanning into coffee search, the best first feature is:

- `Preview`
- `ImageAnalysis`
- QR or barcode scan
- Send scanned value to the search UI

That gives the highest value with the least code.

---

## 22. Minimal Starter Checklist

If you want the shortest possible path to a working feature, implement these in order:

1. Add dependencies.
2. Add manifest permissions.
3. Build a permission gate.
4. Add a `PreviewView` inside Compose using `AndroidView`.
5. Bind `Preview` plus one use case.
6. Test with front and back camera.
7. Add one analyzer or capture flow.
8. Move task-specific logic into the ViewModel.
9. Add cleanup in `DisposableEffect`.

---

## 23. Practical Recommendation

Do not start by trying to make one screen that does everything.

Start with a reusable camera backbone, then specialize by task:

- Capture task = `ImageCapture`
- Scan task = `ImageAnalysis`
- Record task = `VideoCapture`
- Live AI task = `ImageAnalysis`

That is the actual reusable "template" for camera features.

---

## 24. Quick Summary

- CameraX is use-case based.
- There is no single fixed UI template.
- The fixed part is the pipeline: permission, preview, bind, process, clean up.
- Use `PreviewView` in Compose via `AndroidView`.
- Use `ImageCapture` for photos.
- Use `ImageAnalysis` for scanning and ML.
- Use `VideoCapture` for recording.
- Keep camera binding logic separate from UI.
- Keep task-specific logic in analyzers or ViewModels.

If you build the camera feature this way, the same camera engine can power profile photos, barcode scanning, search-by-scan, ML analysis, and video recording without rewriting the whole feature every time.
