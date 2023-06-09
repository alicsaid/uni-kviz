package com.example.uni_kviz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities=[Pitanje::class, Fakultet::class, PitanjeFakultet::class],
    version = 4,
    exportSchema = false
)
abstract class Baza: RoomDatabase() {
    abstract fun pitanjeDao(): PitanjeDao
    abstract fun fakultetDao(): FakultetDao
    abstract fun pitanjeFakultetDao(): PitanjeFakultetDao

    companion object{
        @Volatile
        var INSTANCE: Baza? = null
        fun uzmiBazu(context: Context):Baza{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    Baza::class.java,
                    name="baza"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}