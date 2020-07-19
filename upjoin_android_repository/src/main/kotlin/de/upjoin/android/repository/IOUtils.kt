package de.upjoin.android.repository

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