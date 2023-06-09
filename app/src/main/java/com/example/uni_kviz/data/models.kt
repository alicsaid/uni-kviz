package com.example.uni_kviz.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="pitanje")
data class Pitanje(
    @PrimaryKey(autoGenerate = true)
    val pitanje_id:Int = 0,
    val pitanje: String
)

@Entity(tableName = "fakultet")
data class Fakultet(
    @PrimaryKey(autoGenerate = true)
    val fakultet_id:Int = 0,
    val ime: String
)

@Entity(tableName = "pitanje_fakultet")
data class PitanjeFakultet(
    @PrimaryKey(autoGenerate = true )
    val pitanje_fakultet_id:Int = 0,
    val id_pitanja :Int,
    val id_fakultet:Int,
    val bodovi:Int
)

