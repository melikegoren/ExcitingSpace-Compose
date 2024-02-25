package com.melikegoren.excitingspace.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.melikegoren.excitingspace.BuildConfig
import com.melikegoren.excitingspace.R
import com.melikegoren.excitingspace.ui.theme.ExcitingSpaceTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit){
        viewModel.apodUiState(BuildConfig.API_KEY)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = modifier,
                title = {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.logo) ,
                            contentDescription = "app_logo" )
                        Text(
                            text = "Exciting Space",
                            fontFamily = FontFamily.Default,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .fillMaxWidth(1f)
                                .padding(horizontal = 20.dp)
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /*TODO*/ },

                ) {
                Image(
                    painter = painterResource(id = R.drawable.download_icon),
                    contentDescription = "download_icon")
            }
        }
    ){ innerPadding->
        val scrollState = rememberScrollState()


        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        )

        {
            when( uiState){
                is ApodUiState.Success -> {
                    Text(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally),
                        text = (uiState as ApodUiState.Success).data.title,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Default,
                        color = MaterialTheme.colorScheme.primary
                    )
                    GlideImage(
                        model = (uiState as ApodUiState.Success).data.url,
                        contentDescription = "space",
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    ExpandableCard(text = (uiState as ApodUiState.Success).data.explanation)
                }
                is ApodUiState.Error -> {
                    Text(text = (uiState as ApodUiState.Error).message.toString())
                }
                is ApodUiState.Loading -> {
                    Text(text = "Loading")
                }
            }
        }
    }
}

@Composable
fun ExpandableCard(
    text: String
){
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    val cardShape = RoundedCornerShape(size = 10.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth(1f),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = cardShape
                )
                .fillMaxWidth(0.9f)
                .clip(cardShape),

            ) {
            Row(
                modifier = Modifier
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
                        .padding(12.dp)
                        .weight(1f)
                ) {

                    if (expanded) {
                        Text(
                            text = text,
                        )
                    }
                    else Text(
                        textAlign = TextAlign.Start ,
                        text = "Click to see the explanation...",
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 5.dp),
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
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen()
        }
    }
}













