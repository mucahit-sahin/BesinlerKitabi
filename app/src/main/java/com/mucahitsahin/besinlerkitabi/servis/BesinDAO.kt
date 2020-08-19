package com.mucahitsahin.besinlerkitabi.servis

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mucahitsahin.besinlerkitabi.model.Besin

@Dao
interface BesinDAO {
    //Data Access Objects

    @Insert
    suspend fun insertAll(vararg besin:Besin):List<Long>

    //Insert -> Room, insert into
    //suspend -> coroutine scope
    //vararg -> birden fazla istedigimiz sayıda besin
    //List<long> -> long, id'ler

    @Query("SELECT * FROM besin")
    suspend fun getAllBesin():List<Besin>

    @Query("SELECT * FROM besin WHERE uuid= :besinId")
    suspend fun getBesin(besinId:Int):Besin

    @Query("DELETE FROM besin")
    suspend fun deleteAllBesin()
}