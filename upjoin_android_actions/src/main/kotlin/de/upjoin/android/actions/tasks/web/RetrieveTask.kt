package de.upjoin.android.actions.tasks.web

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

abstract class RetrieveTask<R>(private val resultClass: Class<R>): AbstractHTTPTask<R>() {

    private val objectMapper: ObjectMapper by lazy { taskType.createObjectMapper() }

    @Throws(IOException::class)
    override fun transformResponseBody(stream: InputStream): R {
        if (resultClass == String::class.java) {
            return inputStreamToString(stream) as R
        }
        InputStreamReader(stream).use { r ->
            return objectMapper.readValue(r, resultClass)
        }
    }

    @Throws(IOException::class)
    override fun transformResponseBody(string: String): R {
        return objectMapper.readValue(string, resultClass)
    }

}
