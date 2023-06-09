package com.example.uni_kviz.data

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PitanjeDao{
    @Insert
    suspend fun ubaci(pitanje:Pitanje)

    @Query("SELECT * FROM pitanje")
    fun uzmiSvaPitanja(): Flow<List<Pitanje>>

}

@Dao
interface FakultetDao{
    @Insert
    suspend fun ubaci(fakultet: Fakultet)

    @Query("SELECT * FROM fakultet")
    fun uzmiSveFakultete(): Flow<List<Fakultet>>

}

@Dao
interface PitanjeFakultetDao{
    @Insert
    suspend fun ubaci(pitanjeFakultet:PitanjeFakultet)

    @Query("SELECT * FROM pitanje as p INNER JOIN " +
            "pitanje_fakultet AS pf on pf.id_pitanja = p.pitanje_id INNER JOIN " +
            "fakultet as f on pf.id_fakultet = f.fakultet_id")
    fun uzmiVezu(): Flow<List<PitanjeFakultetSpojeno>>

}

data class PitanjeFakultetSpojeno(
    @Embedded val pitanje:Pitanje,
    @Embedded val fakultet: Fakultet,
    @Embedded val pitanjeFakultet: PitanjeFakultet,
)