package com.example.arlearner.ui.screens

import android.os.Build
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.codewithfk.arlearner.util.Utils
import com.example.arlearner.ui.util.Utils
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.TrackingFailureReason
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CameraNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun ARscreen(navController: NavController, model: String) {
    val engine = rememberEngine()
    val modelloader = rememberModelLoader(engine)
    val materialloader = rememberMaterialLoader(engine)
    val cameranode = rememberARCameraNode(engine)
    val childnodes = rememberNodes()
    val view = rememberView(engine)
    val collisionSystem = rememberCollisionSystem(view = view)
    val planeRenderer = remember {
        mutableStateOf(true)
    }
    val modelInstance = remember {
        mutableListOf<ModelInstance>()
    }

    val trackingfailiureReason = remember {
        mutableStateOf<TrackingFailureReason?>(null)
    }

    val frame = remember {
        mutableStateOf<Frame?>(null)
    }

    ARScene(
        modifier = Modifier.fillMaxSize(),
        engine = engine,
        modelLoader = modelloader,
        cameraNode = cameranode,
        childNodes = childnodes,
        view = view,
        planeRenderer = planeRenderer.value,
        materialLoader = materialloader,
        onTrackingFailureChanged = {
            trackingfailiureReason.value = it
        },
        onSessionUpdated = { _, updatedframe ->
            frame.value = updatedframe
        },
        sessionConfiguration = { session, config ->
            config.depthMode =
                when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                    true -> Config.DepthMode.AUTOMATIC
                    false -> Config.DepthMode.DISABLED
                }
            config.lightEstimationMode =
                Config.LightEstimationMode.ENVIRONMENTAL_HDR
        },
        onGestureListener = rememberOnGestureListener(
            onSingleTapConfirmed = {
                    e: MotionEvent, node: Node? ->
                if (node == null) {
                    val hitTestResult = frame.value?.hitTest(e.x, e.y)
                    hitTestResult?.firstOrNull{
                        it.isValid (
                            depthPoint = false,
                            point = false
                        )
                    }?.createAnchorOrNull()?.let{
                        val nodemodel = Utils.createAnchorNode(
                            engine = engine,
                            modelLoader = modelloader,
                            materialLoader = materialloader,
                            modelInstance = modelInstance,
                            anchor = it,
                            model = model
                        )
                        childnodes += nodemodel
                    }
                }
            }
        )
    )
}









