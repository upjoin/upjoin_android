package de.upjoin.android.repository

import android.content.Context
import android.net.Uri
import de.upjoin.android.core.logging.Logger
import java.io.*

open class FileRepository(val context: Context, protected val prefix: String? = null): Repository<FileRepository.FileKey, FileRepository.FileCacheEntry, FileRepository.FileCacheValue>{

    interface FileKey {
        val name: String
    }

    class FileCacheEntry(val uri: Uri, val size: Long)
    class FileCacheValue(val worker: ((OutputStream) -> Unit))

    protected open val directory: File get() = context.filesDir

    protected fun getFileName(name: String): String {
        val prefix = if (prefix == null) "" else prefix + "_"
        return "${prefix}${name}"
    }

    protected fun getFile(name: String) = File(directory, getFileName(name))

    @Throws(IOException::class)
    protected fun getFileStream(name: String): FileOutputStream {
        val f = getFile(name)
        if (!f.exists()) f.createNewFile()
        return FileOutputStream(f)
    }

    override fun has(key: FileKey) = getFile(key.name).exists()

    override fun get(key: FileKey): FileCacheEntry? {
        val file = getFile(key.name)
        if (!file.exists()) return null

        return FileCacheEntry(Uri.fromFile(file), file.length())
    }

    override fun set(key: FileKey, repositoryObject: FileCacheValue): Boolean {
            try {
                getFileStream(key.name).use { fo -> repositoryObject.worker.invoke(fo) }
            } catch (e: IOException) {
                Logger.error(this, e.message, e)
                return false
            }

            return true
        }

    protected fun remove(name: String): Boolean {
        val outFile = getFile(name)
        if (outFile.exists()) outFile.delete()
        return true
    }

    override fun remove(key: FileKey) = remove(key.name)

    override fun removeAll(): Boolean {
        if (prefix == null) return false
        val fileList = directory.listFiles() ?: return false
        for (file in fileList) {
            if (file.name.startsWith(prefix)) file.delete()
        }
        return true
    }

    fun cacheValueForWorker(worker: ((OutputStream) -> Unit)) = FileCacheValue(worker = worker)
    fun cacheValueForInputStream(inputStream: InputStream) = FileCacheValue(worker = { fo -> inputStream.use { fi -> IOUtils.copy(fi, fo) } })
    fun cacheValueForUri(uri: Uri) = FileCacheValue(
        worker = {fo -> context.contentResolver.openInputStream(uri)?.use { fi -> IOUtils.copy(fi, fo) } ?: throw IllegalArgumentException("Cannot open input stream for uri $uri") }
    )
}