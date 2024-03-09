package com.melikegoren.excitingspace.ui.screens.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.melikegoren.excitingspace.BuildConfig
import com.melikegoren.excitingspace.R
import com.melikegoren.excitingspace.data.remote.ApodApiService
import com.melikegoren.excitingspace.ui.theme.inconsalataFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    apiService: ApodApiService
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit){
        val serviceVersion =  apiService.getApod(BuildConfig.API_KEY).service_version
        if(serviceVersion == "video"){
            viewModel.apodUiStateVideo(BuildConfig.API_KEY)
        }
        else{
            viewModel.apodUiStatePhoto(BuildConfig.API_KEY)
        }
    }

    var permissionState by rememberSaveable { mutableStateOf(false) }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // İzin reddedildi
            permissionState = isGranted
        }

    Log.d("permissions", permissionState.toString())



    val snackbarHostState = remember{ SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },


    ){ innerPadding->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )

        {
            when( uiState){
                is ApodUiState.Success -> {

                    Row(modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxWidth(),
                        ) {
                        Title(
                            title = (uiState as ApodUiState.Success).data.title.toString(),
                            modifier = Modifier,
                            )
                    }

                    val url = (uiState as ApodUiState.Success).data.url

                    if((uiState as ApodUiState.Success).data.serviceVersion == "video"){
                        VideoCard(url,context)
                    }
                    else{
                        Image(
                            imageUrl = url,
                            modifier = Modifier,
                            context = context
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp))


                    ExpandableCard(
                        text = (uiState as ApodUiState.Success).data.explanation,
                        modifier = Modifier
                    )

                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(url)
                            .size(coil.size.Size.ORIGINAL) // Set the target size to load the image at.
                            .build()
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Log.d("statee", url)
                    Button(
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .padding(horizontal = 20.dp)
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(1f)
                            .wrapContentHeight(Alignment.CenterVertically)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(20.dp)
                            ),

                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                viewModel.saveApodImageToGallery(
                                    painter,
                                    context,
                                    url,
                                    (uiState as ApodUiState.Success).data.title
                                )
                            }
                            if (permissionState){
                                viewModel.saveApodImageToGallery(
                                    painter,
                                    context,
                                    url,
                                    (uiState as ApodUiState.Success).data.title
                                )
                            }
                            else {
                                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            }
                        },
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.download_icon),
                                contentDescription = "download_icon",
                                modifier = Modifier
                                    .padding(horizontal = 18.dp)
                                    .align(Alignment.CenterVertically)
                                )

                            Text(
                                text = "Download this image",
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                fontFamily = inconsalataFamily)
                        }

                    Spacer(modifier = Modifier.padding(10.dp))


                    Log.d("statee", "Painter: ${painter}, State: ${painter?.state}")
                }
                is ApodUiState.Error -> {
                    ShowSnackbar(
                        scope = scope,
                        snackbarHostState = snackbarHostState,
                        error = (uiState as ApodUiState.Error).message)
                }
                is ApodUiState.Loading -> {
                   ShimmerItem(modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun VideoCard(url:String, context: Context){
    Card(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.video_icon),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = "This time it's not a photo, it's a video!",
                fontFamily = inconsalataFamily,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.padding(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Row(modifier = Modifier) {
                    Text(
                        text = "Click to see the video",
                        fontSize = 20.sp,
                        fontFamily = inconsalataFamily,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(10.dp)
                    )
                    IconButton(
                        onClick = {
                            val intent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                                  },
                        modifier = Modifier.padding(10.dp)


                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(10.dp),
                            MaterialTheme.colorScheme.primary
                            )

                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun VideoCardPreview(){
    Surface(
        modifier = Modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        VideoCard("abc", LocalContext.current)
    }
}
@Composable
fun ShimmerItem(modifier: Modifier){
    Row(modifier = Modifier
        .background(MaterialTheme.colorScheme.primaryContainer)
        .fillMaxWidth(),
    ) {
        Title(
            title = "",
            modifier = modifier.shimmerEffect()
        )
    }
    Image(
        imageUrl = "",
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .shimmerEffect()
            .fillMaxWidth(0.9f)
            .height(300.dp)
            .padding(10.dp),
        context = LocalContext.current
    )

    ExpandableCard(
        text = "",
        modifier = Modifier.shimmerEffect())

}

@Composable
fun ExpandableCard(
    text: String,
    modifier: Modifier
){
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    val cardShape = RoundedCornerShape(size = 10.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(bottom = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(1f)
                .clip(cardShape)
                .padding(horizontal = 20.dp),

            ) {
            Row(
                modifier = modifier
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                    .padding(10.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {

                    if (expanded) {
                        Text(
                            text = text,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = inconsalataFamily,
                            fontSize = 16.sp
                        )
                    }
                    else Text(
                        textAlign = TextAlign.Start ,
                        text = text.take(100),
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = inconsalataFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = modifier.padding(vertical = 5.dp),
                    )
                }
                IconButton(
                    onClick = { expanded = !expanded },
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            "show less"
                        } else {
                            "show more"
                        }
                    )
                }

            }
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShowSnackbar(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    error: String,
    viewModel: HomeViewModel = hiltViewModel(),
){

    scope.launch{
        val result = snackbarHostState.showSnackbar(
            message =  error,
            actionLabel = "Try again.",
            duration = SnackbarDuration.Indefinite
        )
        
        when(result){
            SnackbarResult.ActionPerformed -> {
                viewModel.apodUiStatePhoto(BuildConfig.API_KEY)
                viewModel.apodUiStateVideo(BuildConfig.API_KEY)
            }

            SnackbarResult.Dismissed -> {
            }
        }
    }
}

@Composable
fun Title(title: String, modifier: Modifier){
    Text(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 15.dp)
            .fillMaxWidth(1f),
        text = title,
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        fontFamily = inconsalataFamily,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.primary
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Image(imageUrl: String, modifier: Modifier, context: Context) {


    var scale by remember {
        mutableFloatStateOf(1f)
    }

    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(10.dp))
    ) {

        val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)

            val extraWidth = (scale - 1) * constraints.maxWidth
            val extraHeight = (scale - 1) * constraints.maxHeight

            val maxX = extraWidth / 2
            val maxY = extraHeight / 2

            offset = Offset(
                x = (offset.x + panChange.x).coerceIn(-maxX, maxX),
                y = (offset.y + panChange.y).coerceIn(-maxY, maxY),
            )
        }

        AsyncImage(
            imageUrl,
            contentDescription = "space",
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(size = 10.dp))
                .wrapContentSize(Alignment.Center)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .transformable(state),

            )
       // Log.d("statee", "Painter: ${painter}, State: ${painter.state}")

    }
}
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}





















