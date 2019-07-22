package net.geekstools.floatshort.PRO.Widget.RoomDatabase

import androidx.room.*

@Dao
interface WidgetDataDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewWidgetData(vararg arrayOfWidgetDataModels: WidgetDataModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWidgetData(vararg arrayOfWidgetDataModels: WidgetDataModel)

    @Delete
    fun delete(widgetDataModel: WidgetDataModel)

    @Query("SELECT * FROM WidgetData ORDER BY AppName ASC")
    fun getAllWidgetData(): List<WidgetDataModel>

    @Query("SELECT * FROM WidgetData WHERE WidgetId IN (:WidgetId)")
    fun loadWidgetById(WidgetId: Int): WidgetDataModel

    @Query("UPDATE WidgetData SET WidgetLabel = :WidgetLabel WHERE WidgetId = :WidgetId")
    fun updateWidgetLabelByWidgetId(WidgetId: Int, WidgetLabel: String): Int

    @Query("UPDATE WidgetData SET Recovery = :AddedWidgetRecovery WHERE WidgetId = :WidgetId")
    fun updateRecoveryByWidgetId(WidgetId: Int, AddedWidgetRecovery: Boolean): Int

    @Query("DELETE FROM WidgetData WHERE WidgetId = :WidgetId")
    fun deleteByWidgetId(WidgetId: Int)
}