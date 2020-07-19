package de.upjoin.android.repository

import android.content.Context
import de.upjoin.android.repository.model.LRUInventory
import java.io.File
import java.io.IOException

open class LRUFileRepository(context: Context, prefix: String? = null,
                                                        protected val cacheDirPath: String,
                                                        private val maxEntries: Int,
                                                        private val maxSize: Long): FileRepository(context, prefix) {

    private val inventoryRepository by lazy { LRUInventoryRepository(context, directory) }

    override val directory: File
        by lazy {
            val cachedDirectory = File(context.cacheDir, cacheDirPath)
            if (!cachedDirectory.exists() && !cachedDirectory.mkdirs()) {
                throw IOException("Directory could not be accessed: ${cachedDirectory.absolutePath}")
            }
            cachedDirectory
        }

    override fun get(key: FileKey): FileCacheEntry? {
        val fileEntry = inventoryRepository.get(key.name) ?: return null
        val entry = super.get(key)
        if (entry == null) {
            inventoryRepository.remove(fileEntry.name)
            return null
        }
        return entry
    }

    private fun trimToSize() {
        inventoryRepository.update {
            this.trimToSize(maxEntries, maxSize).forEach { super.remove(it.name) }
        }
    }

    override fun set(key: FileKey, repositoryObject: FileCacheValue): Boolean {
        val success = super.set(key, repositoryObject)
        if (!success) return false

        val file = getFile(key.name)
        if (!file.exists()) return false

        inventoryRepository.set(LRUInventory.FileEntry(key.name, file.length()))
        trimToSize()
        return true
    }

    override fun remove(key: FileKey): Boolean {
        super.remove(key)
        inventoryRepository.remove(key.name)
        return true
    }

    override fun removeAll(): Boolean {
        if (prefix == null) return false
        val fileList = directory.listFiles() ?: return false
        for (file in fileList) {
            if (file.name.startsWith(prefix)) file.delete()
        }
        inventoryRepository.removeAll()
        return true
    }

    companion object {
        const val MAX_SIZE_MB = 1024*1024L
    }
}