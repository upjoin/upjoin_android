package de.upjoin.android.repository

import android.content.Context
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.upjoin.android.core.logging.Logger
import java.io.*

abstract class JSONFileRepository<K: JSONFileRepository.JSONFileKey, T>(val mContext: Context, val mClazz: Class<out T>, protected val mPrefix: String? = null): Repository<K, T, T>{

    interface JSONFileKey {

        fun getFileName(): String

    }

    private val objectMapper = jacksonObjectMapper()

    protected open val directory: File? get() = null

    // TODO: These methods are temporary
    private fun openFileInput(fileName: String): FileInputStream? {
        if (directory==null) {
            return mContext.openFileInput(fileName)
        }
        else {
            val file = File(directory, fileName)
            if (!file.exists()) return null
            return FileInputStream(file)
        }
    }

    private fun openFileOutput(fileName: String): FileOutputStream {
        if (directory==null) {
            return mContext.openFileOutput(fileName, Context.MODE_PRIVATE)
        } else {
            val file = File(directory, fileName)
            if (file.exists()) file.delete()
            file.createNewFile()
            return FileOutputStream(file)
        }
    }

    private fun deleteFile(fileName: String): Boolean {
        if (directory==null) {
            return mContext.deleteFile(fileName)
        } else {
            val file = File(directory, fileName)
            if (file.exists()) return file.delete()
            return false
        }
    }

    private fun fileList(): Array<out String> {
        val directory = directory
        return if (directory==null) mContext.fileList() else  directory.list()
    }

    @Synchronized override fun get(key: K): T? {
        try {
            openFileInput(key.getFileName())?.use { fis ->
                BufferedReader(InputStreamReader(fis)).use { reader ->
                    return objectMapper.readValue(reader, mClazz)
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

    @Synchronized override fun set(key: K, repositoryObject: T): Boolean {
        try {
            openFileOutput(key.getFileName()).use { fos ->
                PrintWriter(fos).use { printWriter -> objectMapper.writeValue(printWriter, repositoryObject) }
            }

        } catch (e: IOException) {
            // object could not be written to cache
            Logger.error(this, e.message ?: "", e)
            return false
        }

        return true
    }

    @Synchronized override fun remove(key: K): Boolean {
        val fileDeleted = deleteFile(key.getFileName())
        return fileDeleted
    }

    @Synchronized override fun removeAll(): Boolean {
        if (mPrefix != null) {
            for (file in fileList()) {
                if (file.startsWith(mPrefix)) {
                    val fileDeleted = deleteFile(file)
                    /*if (fileDeleted) {
                        val key = getKeyFromFileName(file)
                    }*/
                }
            }
        }
        return false
    }

    abstract fun getKeyFromFileName(fileName: String): K?

}