package de.upjoin.android.actions.tasks.web

import java.net.URL

class GetStaticPageTask(val url: URL): RetrieveTask<String>(String::class.java) {

    override fun getURL() = url

}