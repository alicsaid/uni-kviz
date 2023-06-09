package com.example.uni_kviz.ui.end

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uni_kviz.R
import com.example.uni_kviz.ui.how_to_play.HowToPlayDescription
import com.example.uni_kviz.ui.navigation.NavigationDestination

object EndDestination : NavigationDestination {
    override val route = "end"
    override val titleRes = R.string.app_name
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndScreen(navigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Kako igrati?") },
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Accordion()
                Button(
                    onClick = { navigateBack() },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Početna")
                }
            }
        }
    }
}

@Composable
fun Accordion() {
    Column(Modifier.fillMaxWidth()) {
        ExpandableCard(
            title = "Fakultet 1",
            titleFontWeight = FontWeight.Bold,
            description = "Opis fakulteta 1",
            descriptionFontWeight = FontWeight.Normal,
            descriptionMaxLines = 4,
            padding = 16.dp,
            percentage = 40
        )

        ExpandableCard(
            title = "Fakultet 2",
            titleFontWeight = FontWeight.Bold,
            description = "Opis fakulteta 2",
            descriptionFontWeight = FontWeight.Normal,
            descriptionMaxLines = 4,
            padding = 16.dp,
            percentage = 30
        )

        ExpandableCard(
            title = "Fakultet 3",
            titleFontWeight = FontWeight.Bold,
            description = "Opis fakulteta 3",
            descriptionFontWeight = FontWeight.Normal,
            descriptionMaxLines = 4,
            padding = 16.dp,
            percentage = 30
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableCard(
    title: String,
    titleFontWeight: FontWeight = FontWeight.Bold,
    description: String,
    descriptionFontWeight: FontWeight = FontWeight.Normal,
    descriptionMaxLines: Int = 4,
    padding: Dp = 12.dp,
    percentage: Int
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(6f),
                    text = title,
                    fontWeight = titleFontWeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = "$percentage%",
                    textAlign = TextAlign.End,
                    fontWeight = descriptionFontWeight
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(1f)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expandedState) {
                Text(
                    text = description,
                    fontWeight = descriptionFontWeight,
                    maxLines = descriptionMaxLines,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
