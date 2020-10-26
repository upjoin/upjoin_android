package de.upjoin.android.repository

import android.content.Context
import android.util.LruCache
import androidx.core.util.lruCache
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.upjoin.android.core.logging.Logger
import java.io.*

abstract class JSONFileRepository<K: JSONFileRepository.JSONFileKey, T: Any>(val context: Context, val clazz: Class<out T>, protected val prefix: String? = null, lruSize: Int = 0): Repository<K, T, T>{

    interface JSONFileKey {

        fun getFileName(): String

    }

    private val inMemoryCache: LruCache<K, T>? =
        if (lruSize==0) null
        else lruCache(lruSize, create = { key -> getUncached(key) })

    private val objectMapper = jacksonObjectMapper()

    protected open val directory: File? get() = null

    // TODO: These methods are temporary
    private fun openFileInput(fileName: String): FileInputStream? {
        if (directory==null) {
            return context.openFileInput(fileName)
        }
        else {
            val file = File(directory, fileName)
            if (!file.exists()) return null
            return FileInputStream(file)
        }
    }

    private fun openFileOutput(fileName: String): FileOutputStream {
        if (directory==null) {
            return context.openFileOutput(fileName, Context.MODE_PRIVATE)
        } else {
            val file = File(directory, fileName)
            if (file.exists()) file.delete()
            file.createNewFile()
            return FileOutputStream(file)
        }
    }

    private fun deleteFile(fileName: String): Boolean {
        if (directory==null) {
            return context.deleteFile(fileName)
        } else {
            val file = File(directory, fileName)
            if (file.exists()) return file.delete()
            return false
        }
    }

    private fun fileList(): Array<out String> {
        val directory = directory
        return if (directory==null) context.fileList() else  directory.list()
    }

    protected fun getUncached(key: K): T? {
        try {
            openFileInput(key.getFileName())?.use { fis ->
                BufferedReader(InputStreamReader(fis)).use { reader ->
                    return objectMapper.readValue(reader, clazz)
                }
            }
        } catch (e: FileNotFoundException) {
            // do nothing, cache page does not exist, that's ok
        } catch (e: IOException) {
            // object could not be read or transformed, whatever the reason, invalidate the cache
            Logger.error(this, e.message ?: "", e)
            remove(key)
        }

        return null
    }

    @Synchronized override fun get(key: K): T? {
        if (inMemoryCache==null) return getUncached(key)
        return inMemoryCache.get(key)
    }

    @Synchronized override fun set(key: K, repositoryObject: T): Boolean {
        try {
            openFileOutput(key.getFileName()).use { fos ->
                PrintWriter(fos).use { printWriter -> objectMapper.writeValue(printWriter, repositoryObject) }
            }

        } catch (e: IOException) {
            // object could not be written to cache
            Logger.error(this, e.message ?: "", e)
            return false
        } finally {
            inMemoryCache?.remove(key)
        }

        return true
    }

    @Synchronized override fun remove(key: K): Boolean {
        try {
            return deleteFile(key.getFileName())
        }
        finally {
            inMemoryCache?.remove(key)
        }
    }

    @Synchronized override fun removeAll(): Boolean {
        try {
            for (file in fileList()) {
                if (prefix == null || file.startsWith(prefix)) {
                    deleteFile(file)
                }
            }
        }
        finally {
            inMemoryCache?.evictAll()
        }
        return false
    }

}