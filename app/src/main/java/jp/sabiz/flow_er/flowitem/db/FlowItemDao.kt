package jp.sabiz.flow_er.flowitem.db

import androidx.room.*
import jp.sabiz.flow_er.flowitem.FlowItem

@Dao
interface FlowItemDao {

    @Query("SELECT id, position, count FROM ${FlowItem.TABLE_NAME} ORDER BY position")
    fun loadAll(): List<FlowItem>

    @Query("SELECT COUNT(*) FROM ${FlowItem.TABLE_NAME}")
    fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(item: FlowItem)

    @Delete()
    fun delete(item: FlowItem)

    @Update()
    fun update(vararg item: FlowItem)
}