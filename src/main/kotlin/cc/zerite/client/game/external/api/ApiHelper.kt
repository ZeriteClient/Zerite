package cc.zerite.client.game.external.api

import com.google.gson.Gson
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils

val httpClient: CloseableHttpClient = HttpClientBuilder.create().build()
val gson: Gson = Gson()

inline fun <reified T> getRequest(url: String): T? {
    val request = HttpGet(url)
    request.addHeader("User-Agent", "Zerite Client | API v1.0 (Java 1.8)")

    val response = httpClient.execute(request)
    val json = EntityUtils.toString(response.entity)

    return gson.fromJson(json, T::class.java)
}