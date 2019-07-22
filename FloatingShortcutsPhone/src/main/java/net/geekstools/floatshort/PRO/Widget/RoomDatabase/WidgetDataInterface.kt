package net.geekstools.floatshort.PRO.Widget.RoomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WidgetDataModel::class], version = 1, exportSchema = false)
abstract class WidgetDataInterface : RoomDatabase() {
    abstract fun initDataAccessObject(): WidgetDataDAO
}