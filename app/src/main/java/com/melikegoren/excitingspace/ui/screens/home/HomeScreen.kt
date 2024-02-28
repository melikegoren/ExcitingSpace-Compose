package com.melikegoren.excitingspace.ui.screens.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.runtime.DisposableEffect
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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.compose.ExcitingSpaceTheme
import com.melikegoren.excitingspace.BuildConfig
import com.melikegoren.excitingspace.R
import com.melikegoren.excitingspace.common.ClearImageCacheWorker
import com.melikegoren.excitingspace.data.remote.ApodApiService
import com.melikegoren.excitingspace.ui.theme.inconsalataFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalGlideComposeApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    //apiService: ApodApiService
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

//    LaunchedEffect(Unit){
//        val serviceVersion =  apiService.getApod(BuildConfig.API_KEY).service_version
//        if(serviceVersion == "video"){
//            viewModel.apodUiStateVideo(BuildConfig.API_KEY)
//        }
//        else{
//            viewModel.apodUiStatePhoto(BuildConfig.API_KEY)
//        }
//    }

                //viewModel.apodUiStateVideo(BuildConfig.API_KEY)
                viewModel.apodUiStatePhoto(BuildConfig.API_KEY)



    val context = LocalContext.current


    val snackbarHostState = remember{ SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /*TODO*/ },

                ) {
                Image(
                    painter = painterResource(id = R.drawable.download_icon),
                    contentDescription = "download_icon",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary))
            }
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
                        Log.d("titleee",(uiState as ApodUiState.Success).data.url.toString())
                    }

                    val url = (uiState as ApodUiState.Success).data.url

                    if((uiState as ApodUiState.Success).data.serviceVersion == "video"){
                        VideoCard(url,context)
                    }
                    else{

                        Image(
                            imageUrl = (uiState as ApodUiState.Success).data.url,
                            modifier = Modifier,
                            context = context
                        )
//                        DisposableEffect(url) {
//                            val workRequest = OneTimeWorkRequestBuilder<ClearImageCacheWorker>()
//                                .setInitialDelay(1, TimeUnit.SECONDS) // Delay the cache clearing to ensure it's done after the image is loaded
//                                .build()
//
//                            WorkManager.getInstance(context)
//                                .enqueue(workRequest)
//
//                            onDispose {
//                                // Cleanup or additional actions when the effect is disposed
//                            }
//                        }
                    }



                    ExpandableCard(
                        text = (uiState as ApodUiState.Success).data.explanation,
                        modifier = Modifier
                    )

                }
                is ApodUiState.Error -> {
                    //Text(text = (uiState as ApodUiState.Error).message.toString())
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
//    val url = "dasdas"
//    val context = LocalContext.current

    Card(
        modifier = Modifier
            .height(280.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.video_icon),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
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
                                .padding(10.dp))

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
fun Image(imageUrl: String, modifier: Modifier, context: Context){

    val url by remember { mutableStateOf(imageUrl) }

//    DisposableEffect(url) {
//        val workRequest = OneTimeWorkRequestBuilder<ClearImageCacheWorker>()
//            .setInitialDelay(1, TimeUnit.SECONDS) // Delay the cache clearing to ensure it's done after the image is loaded
//            .build()
//
//        WorkManager.getInstance(context)
//            .enqueue(workRequest)
//
//        onDispose {
//            // Cleanup or additional actions when the effect is disposed
//        }
//    }


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

        val state = rememberTransformableState{ zoomChange, panChange, rotationChange ->
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
            model = imageUrl,
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

    }


//    GlideImage(
//        model = url,
//        contentDescription = "space",
//        modifier = modifier
//            .padding(horizontal = 20.dp)
//            .clip(RoundedCornerShape(size = 10.dp))
//            .wrapContentSize(Alignment.Center),
//
//    )
}

//@Composable
//@Preview
//fun ImagePreview(){
//    ExcitingSpaceTheme {
//        Image(url = "sadasd", modifier = Modifier)
//    }
//}


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
            .padding(bottom = 80.dp),
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    ExcitingSpaceTheme {

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


//topBar = {
//    TopAppBar(
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer,
//            titleContentColor = MaterialTheme.colorScheme.primary
//        ),
//        modifier = modifier,
//        title = {
//            Row {
//                Image(
//                    painter = painterResource(id = R.drawable.logo) ,
//                    contentDescription = "app_logo" )
//                Text(
//                    text = "Exciting Space",
//                    fontFamily = FontFamily.Default,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier
//                        .align(Alignment.CenterVertically)
//                        .fillMaxWidth(1f)
//                        .padding(horizontal = 20.dp)
//                )
//            }
//        }
//    )
//},













