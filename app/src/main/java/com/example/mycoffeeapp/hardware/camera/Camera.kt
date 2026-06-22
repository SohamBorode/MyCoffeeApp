package com.example.mycoffeeapp.hardware.camera

import android.content.Context
import android.net.Uri
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.ui.screens.profile.ProfileViewModel
import com.example.mycoffeeapp.ui.theme.OffWhite
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraPreview(navBarController: NavHostController,viewModel: ProfileViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }

    val previewView = remember { PreviewView(context) }
    val preview = remember { Preview.Builder().build() }
    val imageCapture = remember { ImageCapture.Builder().build() }
    var camera by remember { mutableStateOf<Camera?>(null) }


    LaunchedEffect(cameraSelector) {
        val cameraProvider = context.getCameraProvider()
        try {
            cameraProvider.unbindAll() //It closes any previous camera sessions
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            preview.setSurfaceProvider(previewView.surfaceProvider)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = OffWhite)
    ) {
        Button(
            onClick = { navBarController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 40.dp, start = 16.dp)
        ) {
            Icon(
                painter = androidx.compose.ui.res.painterResource(R.drawable.regular_outline_arrow_left),
                contentDescription = "Back"
            )
            Text(text = "Back")
        }
        
        AndroidView(
            factory = { previewView },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 100.dp)
                .clip(shape = CircleShape)
                .align(Alignment.Center),
            update = { view ->

            },
            onRelease = { view -> view.removeAllViews() }

        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {

            Button(
                onClick = {
                    takePhoto(imageCapture, context) { uri ->
                        println("Saved Uri: $uri")
                        viewModel.onProfileImageSelected(uri.toString())
                        navBarController.popBackStack()
                    }
                },
                modifier = Modifier
                    .padding(bottom = 50.dp)
            ) {
                Text(text = "TakePhoto")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                    cameraSelector =
                        if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else {
                            CameraSelector.DEFAULT_BACK_CAMERA
                        }
                },
                modifier = Modifier
                    .padding(bottom = 50.dp)
            ) {
                Text(text = "Switch Camera")
            }
        }
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                continuation.resume(cameraProviderFuture.get())
            } catch (e: Exception) {
                // In a real app, you'd want to resume with an error or handle this better
            }
        }, ContextCompat.getMainExecutor(this))
    }

private fun takePhoto(
    imageCapture: ImageCapture,
    context: Context,
    onImageCaptured: (Uri) -> Unit
) {
    val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
        .format(System.currentTimeMillis())
    val file = File(context.externalCacheDir, "$name.jpg")

    // 2. Create Output Options (This tells the camera WHERE to put the pixels)
    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

    // 3. Take the picture
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                // SUCCESS: The file is now on the disk!
                val savedUri = Uri.fromFile(file)
                println("Photo saved successfully: ${file.absolutePath}")
                onImageCaptured(savedUri)
            }

            override fun onError(exception: ImageCaptureException) {
                // ERROR: Something went wrong (e.g. out of storage)
                exception.printStackTrace()
            }
        }
    )
}
