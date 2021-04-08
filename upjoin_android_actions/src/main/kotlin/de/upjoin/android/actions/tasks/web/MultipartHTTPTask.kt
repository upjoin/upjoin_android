package de.upjoin.android.actions.tasks.web

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.URLConnection

abstract class MultipartHTTPTask<T>(private val resultClass: Class<T>, private val progressCallback: UploadProgressInfoCallback? = null): AbstractHTTPTask<T>() {

    private val objectMapper: ObjectMapper by lazy { taskType.createObjectMapper() }

    protected var request: DataOutputStream? = null

    @Throws(IOException::class)
    override suspend fun openURLConnection() = withContext(Dispatchers.IO) {
        val urlConnection = super.openURLConnection()
        urlConnection.requestMethod = "POST"
        urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
        urlConnection.setChunkedStreamingMode(-1) // use default chunk size
        return@withContext urlConnection
    }

    @Throws(IOException::class)
    override fun transformResponseBody(stream: InputStream): T {
        if (resultClass == String::class.java) {
            return inputStreamToString(stream) as T
        }
        InputStreamReader(stream).use { r ->
            return objectMapper.readValue(r, resultClass)
        }
    }

    @Throws(IOException::class)
    override fun transformResponseBody(string: String): T {
        return objectMapper.readValue(string, resultClass)
    }

    @Throws(IOException::class)
    protected abstract suspend fun prepareRequestBody()

    override suspend fun prepareRequest(urlConnection: URLConnection) {
        DataOutputStream(urlConnection.outputStream).use { request_ ->
            request = request_
            prepareRequestBody()
            request_.apply {
                writeBytes(crlf)
                writeBytes(twoHyphens + boundary + twoHyphens + crlf)
                flush()
            }
        }
    }

    protected open fun sendProgressUpdate() {
        val r = request ?: return
        progressCallback?.progress(r.size().toLong())
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    @Throws(IOException::class)
    protected fun addFormField(name:String, value:String) {
        request!!.apply {
            writeBytes(twoHyphens + boundary + crlf)
            writeBytes("Content-Disposition: form-data; name=\"$name\"$crlf")
            writeBytes("Content-Type: text/plain; charset=UTF-8$crlf")
            writeBytes(crlf)
            writeBytes(value)
            writeBytes(crlf)
            flush()
            sendProgressUpdate()
        }
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    @Throws(IOException::class)
    protected fun addJSONField(name:String, value:Any) {
        request!!.apply {
            writeBytes(twoHyphens + boundary + crlf)
            writeBytes("Content-Disposition: form-data; name=\"$name\"$crlf")
            writeBytes("Content-Type: application/json; charset=UTF-8$crlf")
            writeBytes(crlf)
            objectMapper.writeValue(request as OutputStream, value)
            writeBytes(crlf)
            flush()
            sendProgressUpdate()
        }
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..."></input>
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    @Throws(IOException::class)
    protected fun addFilePart(fieldName:String, fileName:String, uploadFile:ByteArrayOutputStream) {
        request!!.apply {
            writeBytes(twoHyphens + boundary + crlf)
            writeBytes("Content-Disposition: form-data; name=\"$fieldName\";filename=\"$fileName\"$crlf")
            writeBytes(crlf)
            write(uploadFile.toByteArray())
            writeBytes(crlf)
            flush()
            sendProgressUpdate()
        }
    }

    @Throws(IOException::class)
    protected fun addFilePart(fieldName:String, fileName:String, uploadFile:File) {
        request!!.apply {
            writeBytes(twoHyphens + boundary + crlf)
            writeBytes("Content-Disposition: form-data; name=\"$fieldName\";filename=\"$fileName\"$crlf")
            writeBytes(crlf)

            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            FileInputStream(uploadFile).use { fi->
                var n = fi.read(buffer)
                while (EOF != n) {
                    write(buffer, 0, n)
                    sendProgressUpdate()
                    n = fi.read(buffer)
                }
            }

            writeBytes(crlf)
            flush()
            sendProgressUpdate()
        }
    }

    interface UploadProgressInfoCallback {

        fun progress(totalWrittenBytes: Long)

    }

    companion object {
        private const val boundary = "*****"
        private const val crlf = "\r\n"
        private const val twoHyphens = "--"

        private const val EOF = -1
    }
}
