package com.example.uni_kviz

import android.content.Context
import com.example.uni_kviz.data.Baza
import com.example.uni_kviz.data.FakultetDao
import com.example.uni_kviz.data.ImplementacijaBaze
import com.example.uni_kviz.data.PitanjeFakultetDao

object Graph {
    lateinit var baza:Baza
        private set
    val implementacijaBaze by lazy{
        ImplementacijaBaze(
            pitanjeDao = baza.pitanjeDao(),
            fakultetDao =  baza.fakultetDao(),
            pitanjeFakultetDao= baza.pitanjeFakultetDao()
        )
    }

    fun staviContext(context: Context){
        baza = Baza.uzmiBazu(context)
    }

}