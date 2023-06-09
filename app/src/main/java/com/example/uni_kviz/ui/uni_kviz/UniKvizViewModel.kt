package com.example.uni_kviz.ui.uni_kviz

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uni_kviz.Graph
import com.example.uni_kviz.data.Fakultet
import com.example.uni_kviz.data.ImplementacijaBaze
import com.example.uni_kviz.data.Pitanje
import com.example.uni_kviz.data.PitanjeFakultet
import com.example.uni_kviz.data.PitanjeFakultetSpojeno
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UniKvizViewModel(
    private val implementacijaBaze: ImplementacijaBaze = Graph.implementacijaBaze
) : ViewModel() {
    var stanje by mutableStateOf(KvizState())
        private set
    init{
        getPitanjeFakultet()
        getPitanja()
        getFakulteti()
    }
    private fun getPitanjeFakultet(){
        viewModelScope.launch(){
            implementacijaBaze.pitanjeFakultet.collectLatest {
                stanje = stanje.copy(
                    pitanjaFakulteti = it
                )
            }
        }
    }

    private fun getPitanja(){
        viewModelScope.launch() {
            implementacijaBaze.pitanja.collectLatest {
                stanje = stanje.copy(
                    pitanja = it
                )

            }
        }
    }

    private fun getFakulteti(){
        viewModelScope.launch(){
            implementacijaBaze.fakulteti.collectLatest {
                stanje = stanje.copy(
                    fakulteti = it
                )
            }
        }
    }

    suspend fun dodajPitanja(){
        implementacijaBaze.ubaciPitanje(Pitanje(pitanje="TEST"))
    }


}

data class KvizState(
    val pitanja: List<Pitanje> = emptyList(),
    val fakulteti: List<Fakultet> = emptyList(),
    val pitanjaFakulteti: List<PitanjeFakultetSpojeno> = emptyList()
)