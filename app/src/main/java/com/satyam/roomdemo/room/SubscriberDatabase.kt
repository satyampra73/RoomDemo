package com.satyam.roomdemo.room

import android.content.Context
import android.content.IntentFilter
import android.provider.CalendarContract.Instances
import androidx.core.os.persistableBundleOf
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(entities = [Subscriber::class], version = 3, exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1,to = 2, spec = SubscriberDatabase.Migration1to2::class),
        AutoMigration(from = 2,to = 3, spec = SubscriberDatabase.Migration2to3::class),
    ])
abstract class SubscriberDatabase : RoomDatabase() {
    @RenameColumn(tableName = "subscriber_data_table" , fromColumnName = "subscriber_id", toColumnName = "subs_id")
    class Migration1to2 : AutoMigrationSpec
    @RenameColumn(tableName = "subscriber_data_table" , fromColumnName = "subscriber_name", toColumnName = "subs_name")
    class Migration2to3 : AutoMigrationSpec

    abstract val subscriberDAO: SubscriberDAO
    companion object{
        @Volatile
        private var INSTANCE: SubscriberDatabase?=null
        fun getInstance(context : Context): SubscriberDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                        SubscriberDatabase::class.java,
                        "subscriber_data_database"
                        ).build()
                    INSTANCE= instance
                }
                return instance
            }

        }
    }

}