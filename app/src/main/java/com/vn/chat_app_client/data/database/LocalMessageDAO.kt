package com.vn.chat_app_client.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vn.chat_app_client.data.model.SampleModel

@Dao
interface LocalMessageDAO {
    @Query("SELECT * FROM sample ORDER BY id DESC")
    fun getAllData(): List<SampleModel>

    @Insert
    fun insertData(sampleModel: SampleModel)

}
