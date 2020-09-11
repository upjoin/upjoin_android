package de.upjoin.android.repository

import android.content.ContentResolver
import android.net.Uri
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object IOUtils {

    private const val EOF = -1

    private const val DEFAULT_BUFFER_SIZE = 1024 * 4

    @Throws(IOException::class)
    fun copy(input: InputStream, output: OutputStream, bufferSize: Int): Long {
        val buffer = ByteArray(bufferSize)
        var count: Long = 0
        var n = input.read(buffer)
        while (EOF != n) {
            output.write(buffer, 0, n)
            count += n.toLong()
            n = input.read(buffer)
        }
        return count
    }

    @Throws(IOException::class)
    fun copy(input: InputStream, output: OutputStream): Int {
        val count = copy(input, output, DEFAULT_BUFFER_SIZE)

        return if (count > Integer.MAX_VALUE) -1
        else count.toInt()
    }
}

typealias OutputStreamConsumer = (OutputStream) -> Unit

object OutputStreamConsumerUtils {

    fun forInputStream(inputStream: InputStream): OutputStreamConsumer
            = { fo -> inputStream.use { fi -> IOUtils.copy(fi, fo) } }

    fun forUri(contentResolver: ContentResolver, uri: Uri): OutputStreamConsumer
            = {fo -> contentResolver.openInputStream(uri)?.use { fi -> IOUtils.copy(fi, fo) } ?: throw IllegalArgumentException("Cannot open input stream for uri $uri") }
}

