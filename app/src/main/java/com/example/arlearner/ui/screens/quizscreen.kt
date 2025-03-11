package com.example.arlearner.ui.screens

import android.os.Build
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.arlearner.ui.navigation.ALPHABETSCREEN
import com.example.arlearner.ui.util.Utils
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.model.ModelInstance
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
fun quizscreen(navController: NavController) {
    val score = remember {
        mutableStateOf(0)
    }
    Box(modifier = Modifier.fillMaxSize()) {

        val model = remember {
            mutableStateOf(Utils.randomModel())
        }

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
                if (childnodes.isEmpty()) {
                    updatedframe.getUpdatedPlanes().firstOrNull() {
                        it.type == Plane.Type.HORIZONTAL_UPWARD_FACING
                    }?.let {
                        it.createAnchorOrNull(it.centerPose)?.let {
                            childnodes += Utils.createAnchorNode(
                                engine = engine,
                                anchor = it,
                                modelLoader = modelloader,
                                materialLoader = materialloader,
                                modelInstance = modelInstance,
                                model = model.value.second
                            )
                        }
                    }
                }
            },
            sessionConfiguration = { session, config ->
                config.depthMode =
                    when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                        true -> Config.DepthMode.AUTOMATIC
                        false -> Config.DepthMode.DISABLED
                    }
                config.lightEstimationMode =
                    Config.LightEstimationMode.ENVIRONMENTAL_HDR
            }
        )
        val listofanswers = remember {
            mutableStateOf(
                listOf(
                    Utils.alphabets.keys.random(),
                    Utils.alphabets.keys.random(),
                    model.value.first
                ).shuffled()
            )
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Quiz Screen", fontSize = 24.sp, modifier = Modifier.align(Alignment.Center))
            Text(text = "Score : ${score.value}", fontSize = 24.sp, modifier = Modifier.align(Alignment.CenterEnd))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listofanswers.value.forEach({
                AlphabetItem(alphabet = it) {
                    score.value += 1
                    if(model.value.first == it){
                        model.value = Utils.randomModel()
                        listofanswers.value = listOf(
                            Utils.alphabets.keys.random(),
                            Utils.alphabets.keys.random(),
                            model.value.first).shuffled()
                        childnodes.clear()
                        modelInstance.clear()
                        frame.value = null
                    }
                }
            })
        }
    }
}