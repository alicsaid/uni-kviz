package com.example.uni_kviz.ui.uni_kviz

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.uni_kviz.R
import com.example.uni_kviz.ui.navigation.NavigationDestination
import kotlin.math.round
import kotlin.random.Random

object UniKvizDestination : NavigationDestination {
    override val route = "uni-kviz"
    override val titleRes = R.string.app_name
}

@Composable
fun UniKvizScreen(
    navigateController: NavHostController,
    navigateBack: () -> Boolean,
    route: String,
) {
    val uniKvizViewModel = viewModel(modelClass=UniKvizViewModel::class.java)
    val stanjeKviza = uniKvizViewModel.stanje

    suspend fun hello(){
        uniKvizViewModel.dodajPitanja()
        uniKvizViewModel.dodajFakultete()
        uniKvizViewModel.ubacivanjeBodova()
    }
    val jeOdgovorio = remember { mutableStateOf(1) }
    val trenutnoPitanje = remember{ mutableStateOf(Random.nextInt(1, 20) ) }
    val bilaPitanja = remember { mutableStateListOf<Int>()}
    val fakultetBodoviMap = remember { mutableMapOf<String, Int>() }
    val pobjednickiFakulteti = remember { mutableMapOf<String, Double>() }
    val fakultetiPokupljeni = remember{mutableStateOf(false)}
    val jeZavrsio = remember { mutableStateOf(false) }

    bilaPitanja.add(trenutnoPitanje.value)
    if(!jeZavrsio.value)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val bluish = Color(4, 53, 85, 255)

            ProgressBar(trenutnoPitanje = jeOdgovorio.value, brojPitanja = stanjeKviza.pitanja.size)

            Spacer(modifier = Modifier.height(64.dp))

            LazyColumn(){
                items(stanjeKviza.pitanja){
                    item -> if(trenutnoPitanje.value == item.pitanje_id){
                        Text(item.pitanje)
                        if(!fakultetiPokupljeni.value){
                            for(fakultet in stanjeKviza.fakulteti)
                                fakultetBodoviMap[fakultet.ime] = 0
                            fakultetiPokupljeni.value = true
                        }

                        bilaPitanja.add(trenutnoPitanje.value)
                    }
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            AnswerButtons(onFirstButtonClick = {
                /*runBlocking{
                 launch{ hello()}
                }*/
                for(pitanjeFakultet in stanjeKviza.pitanjaFakulteti){
                    if(pitanjeFakultet.pitanje.pitanje_id == trenutnoPitanje.value){
                    fakultetBodoviMap[pitanjeFakultet.fakultet.ime] = fakultetBodoviMap[pitanjeFakultet.fakultet.ime]!! + 10 - pitanjeFakultet.pitanjeFakultet.bodovi
                }
            }
                if(jeOdgovorio.value == stanjeKviza.pitanja.size){
                    val najboljiFakulteti = fakultetBodoviMap.entries
                        .sortedByDescending { it.value }
                        .take(3)
                        .associate { it.key to it.value }
                    najboljiFakulteti.forEach { (key, value) ->
                        pobjednickiFakulteti[key] = round(value.toDouble()/(najboljiFakulteti.values.sum()) * 100)
                    }
                    jeZavrsio.value = true


                }else{
                    jeOdgovorio.value = jeOdgovorio.value + 1
                    while(trenutnoPitanje.value in bilaPitanja){
                        trenutnoPitanje.value = Random.nextInt(1,stanjeKviza.pitanja.size + 1)
                    }
                }

                    // Logika za odabir prvog odgovora ("NE")
                },
                onSecondButtonClick = {
                    for(pitanjeFakultet in stanjeKviza.pitanjaFakulteti){
                        if(pitanjeFakultet.pitanje.pitanje_id == trenutnoPitanje.value){
                            fakultetBodoviMap[pitanjeFakultet.fakultet.ime] = fakultetBodoviMap[pitanjeFakultet.fakultet.ime]!! + pitanjeFakultet.pitanjeFakultet.bodovi
                        }
                    }
                    if(jeOdgovorio.value == stanjeKviza.pitanja.size){
                        val najboljiFakulteti = fakultetBodoviMap.entries
                            .sortedByDescending { it.value }
                            .take(3)
                            .associate { it.key to it.value }
                        najboljiFakulteti.forEach { (key, value) ->
                            pobjednickiFakulteti[key] = round(value.toDouble()/(najboljiFakulteti.values.sum()) * 100)
                        }
                        jeZavrsio.value = true

                    }else{
                        jeOdgovorio.value = jeOdgovorio.value + 1
                        while(trenutnoPitanje.value in bilaPitanja){
                            trenutnoPitanje.value = Random.nextInt(1,stanjeKviza.pitanja.size + 1)
                        }
                    }
                    // Logika za odabir drugog odgovora ("Da")
                }
            )

            ActionButton(
                navigateBack = { navigateBack() }
            )
        }
    else
        EndScreen(pobjednickiFakulteti, navigateBack)
}




@Composable
fun AnswerButtons(
    onFirstButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit
) {
    val bluish = Color(4, 53, 85, 255)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onFirstButtonClick,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .height(48.dp)
                .weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = bluish,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(12.dp)
        ) {
            Text(
                text = "NE",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onSecondButtonClick,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .height(48.dp)
                .weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = bluish,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(12.dp)
        ) {
            Text(
                text = "DA",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Composable
fun ActionButton(
    navigateBack: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    val bluish = Color(4, 53, 85, 255)

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Potvrda") },
            text = { Text("Da li ste sigurni da želite odustati?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                        navigateBack()
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = bluish,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Da")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = bluish,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "No")
                }
            }
        )
    }

    Row(modifier = Modifier.padding(top = 16.dp)) {

        val darkRed = Color(229, 57, 53, 255)

        Button(
            onClick = { showDialog.value = true },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = darkRed,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(12.dp)
        ) {
            Text(text = "Odustani",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}


@Composable
fun ProgressBar(
    trenutnoPitanje: Int,
    brojPitanja: Int

) {
    LinearProgressIndicator(
        progress = (trenutnoPitanje).toFloat() / brojPitanja,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndScreen(pobjednickiFakulteti: MutableMap<String, Double>, navigateBack: () -> Boolean) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Čestitamo") },
            )
        }
    ) {

        val bluish = Color(4, 53, 85, 255)

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

                Text(
                    text = "Čestitamo. Završili ste kviz.",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ovo su fakulteti za Vas na osnovu vaših odgovora!",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.padding(20.dp))

                Accordion(pobjednickiFakulteti)

                Spacer(modifier = Modifier.padding(20.dp))

                Button(
                    onClick = { navigateBack() },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = bluish,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Text(text = "Početna",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun Accordion(pobjednickiFakulteti: MutableMap<String, Double>) {
    LazyColumn {
        items(pobjednickiFakulteti.entries.toList()) { entry ->
            ExpandableCard(
                title = entry.key,
                titleFontWeight = FontWeight.Bold,
                description = "Opis fakulteta 1",
                descriptionFontWeight = FontWeight.Normal,
                descriptionMaxLines = 4,
                padding = 16.dp,
                percentage = entry.value.toInt()
            )
            Spacer(modifier = Modifier.padding(10.dp))
        }
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
