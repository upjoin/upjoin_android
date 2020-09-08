package de.upjoin.android.actions.tasks.web

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.upjoin.android.core.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.URLConnection

abstract class MultipartHTTPTask<T>(private val progressCallback: UploadProgressInfoCallback? = null): AbstractHTTPTask<T>() {

    protected var request: DataOutputStream? = null

    private val objectMapper: ObjectMapper
    init {
        val messagePackFactory = JsonFactory()
        messagePackFactory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
        objectMapper = ObjectMapper(messagePackFactory).registerKotlinModule()
    }

    @Throws(IOException::class)
    override suspend fun openURLConnection() = withContext(Dispatchers.IO) {
        val urlConnection = super.openURLConnection()
        urlConnection.requestMethod = "POST"
        urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
        urlConnection.setChunkedStreamingMode(-1) // use default chunk size
        return@withContext urlConnection
    }

    @Throws(IOException::class)
    protected abstract suspend fun prepareRequestBody()

    @Throws(IOException::class)
    protected abstract fun transformResponseBody(r: Reader): T

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

    override suspend fun readResponse(urlConnection: URLConnection): T? {
        if (isHttpOk)
        {
            InputStreamReader(BufferedInputStream(urlConnection.inputStream)).use { r->
                return transformResponseBody(r)
            }
        }
        else
        {
            val sb = StringBuilder()
            try
            {
                BufferedReader(InputStreamReader(BufferedInputStream((urlConnection as HttpURLConnection).errorStream))).use { r->
                    var s:String? = r.readLine()
                    while (s != null) {
                        sb.append(s).append("\n")
                        s = r.readLine()
                    }
                }
            }
            catch (f:IOException) {
                // do nothing when failing to read error stream
            }

            Logger.error(this, "Multipart Request on URL ${getURL()} returned error code ${httpCode}\n$sb")
        }
        return null
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