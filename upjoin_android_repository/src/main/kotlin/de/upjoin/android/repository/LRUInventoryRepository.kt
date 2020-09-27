package de.upjoin.android.repository

import android.content.Context
import de.upjoin.android.repository.model.LRUInventory
import java.io.File

internal class LRUInventoryRepository constructor(context: Context, override val directory: File):
    JSONFileRepository<LRUInventoryRepository.LRUInventoryKey, LRUInventory>(context, LRUInventory::class.java) {

    class LRUInventoryKey() : JSONFileKey {

        override fun getFileName() = "inventory.lru"

    }

    val key = LRUInventoryKey()

    private fun getLRUInventory() = get(key) ?: LRUInventory()

    @Synchronized fun set(fileEntry: LRUInventory.FileEntry) {
        val lruInventory = getLRUInventory()
        lruInventory.add(fileEntry)
        set(key, lruInventory)
    }

    @Synchronized fun remove(fileName: String) {
        val lruInventory = getLRUInventory()
        lruInventory.remove(fileName)
        set(key, lruInventory)
    }

    @Synchronized
    override fun removeAll(): Boolean {
        val lruInventory = getLRUInventory()
        lruInventory.clear()
        set(key, lruInventory)
        return true
    }

    @Synchronized fun get(fileName: String): LRUInventory.FileEntry? = getLRUInventory().get(fileName)

    @Synchronized fun update(func: (LRUInventory).() -> Unit) {
        val lruInventory = getLRUInventory()
        func.invoke(lruInventory)
        set(key, lruInventory)
    }
}