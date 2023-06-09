package com.example.uni_kviz.data

class ImplementacijaBaze(
    private val pitanjeDao:PitanjeDao,
    private val fakultetDao: FakultetDao,
    private val pitanjeFakultetDao: PitanjeFakultetDao
) {
    val pitanja = pitanjeDao.uzmiSvaPitanja()
    val fakulteti = fakultetDao.uzmiSveFakultete()
    val pitanjeFakultet = pitanjeFakultetDao.uzmiVezu()

    suspend fun ubaciPitanje(pitanje:Pitanje){
        pitanjeDao.ubaci(pitanje)
    }

    suspend fun ubaciFakultet(fakultet:Fakultet){
        fakultetDao.ubaci(fakultet)
    }

    suspend fun ubaciPitanjeFakultet(pitanjeFakultet: PitanjeFakultet){
        pitanjeFakultetDao.ubaci(pitanjeFakultet)
    }



}