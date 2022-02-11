package jp.sabiz.flow_er.extension

// https://github.com/thomasnield/kotlin-collection-extras/blob/master/src/main/kotlin/MutableListMoveExt.kt
fun <T> MutableList<T>.moveAt(oldIndex: Int, newIndex: Int)  {
    val item = this[oldIndex]
    removeAt(oldIndex)
    if (oldIndex > newIndex)
        add(newIndex, item)
    else
        add(newIndex - 1, item)
}