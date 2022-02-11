package jp.sabiz.flow_er.flowitem

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = FlowItem.TABLE_NAME)
data class FlowItem(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_NAME_ID)
    val id: String,
    @ColumnInfo(name = COLUMN_NAME_POSITION)
    val position: Int,
    @ColumnInfo(name = COLUMN_NAME_COUNT)
    val count: Long
    ) {

    companion object {
        const val TABLE_NAME = "flow_items"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_POSITION = "position"
        const val COLUMN_NAME_COUNT = "count"

        fun generateId() = UUID.randomUUID().toString()

        fun newItem(position: Int): FlowItem {
            return FlowItem(generateId(), position, 5)
        }
    }
}
