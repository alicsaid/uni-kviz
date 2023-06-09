package com.example.uni_kviz.ui.uni_kviz

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uni_kviz.R
import com.example.uni_kviz.ui.navigation.NavigationDestination
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object UniKvizDestination : NavigationDestination {
    override val route = "uni-kviz"
    override val titleRes = R.string.app_name
}

@Composable
fun UniKvizScreen(navigateBack: () -> Unit) {
    val uniKvizViewModel = viewModel(modelClass=UniKvizViewModel::class.java)

    val stanjeKviza = uniKvizViewModel.stanje
    suspend fun hello(){
        uniKvizViewModel.dodajPitanja()
    }
    val trenutnoPitanje = remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val bluish = Color(4, 53, 85, 255)

        ProgressBar(currentQuestionIndex = trenutnoPitanje.value)

        Spacer(modifier = Modifier.height(64.dp))

        LazyColumn(){
            items(stanjeKviza.pitanja){
                item -> if(trenutnoPitanje.value == item.pitanje_id)
                    Text(item.pitanje)
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        AnswerButtons(onFirstButtonClick = {
            runBlocking{
             launch{ hello()}
            }

                // Logika za odabir prvog odgovora ("NE")
            },
            onSecondButtonClick = {
                trenutnoPitanje.value = trenutnoPitanje.value + 1
                // Logika za odabir drugog odgovora ("Da")
            })

        ActionButton(
            navigateBack = { navigateBack() }
        )
    }
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

        val darkRed = Color(128, 0, 0)

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
    currentQuestionIndex: Int
) {

    val questions = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    LinearProgressIndicator(
        progress = (currentQuestionIndex + 1).toFloat() / questions.size,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}