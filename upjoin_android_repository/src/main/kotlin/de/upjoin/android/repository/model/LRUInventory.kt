package de.upjoin.android.repository.model

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
@JsonInclude(JsonInclude.Include.NON_NULL)
class LRUInventory {

    val files = LinkedList<FileEntry>()

    fun get(fileName: String): FileEntry? = files.find { it.name == fileName }

    fun remove(fileName: String) {
        get(fileName)?.apply { files.remove(this) }
    }

    fun clear() {
        files.clear()
    }

    private val totalSize = files.map { it.size }.sum()

    fun trimToSize(maxEntries: Int, maxSize: Long): Collection<FileEntry> {
        var remainingEntries = files.size
        var remainingSize = totalSize

        val trimmedEntries = mutableListOf<FileEntry>()

        while (files.size>remainingEntries || remainingSize>maxSize) {
            val lastEntry = files.removeLast()
            trimmedEntries.add(lastEntry)
            remainingEntries -= 1
            remainingSize -= lastEntry.size
        }

        return trimmedEntries
    }

    fun add(fileEntry: FileEntry) {
        remove(fileEntry.name)
        files.add(fileEntry)
    }

    @JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class FileEntry(
        val name: String,
        val size: Long,
        var lastTouched: Long = System.currentTimeMillis()
    )

}