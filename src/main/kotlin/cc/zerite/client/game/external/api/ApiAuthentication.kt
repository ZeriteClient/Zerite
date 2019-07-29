package cc.zerite.client.game.external.api

import cc.zerite.client.event.ClientShutdownEvent
import cc.zerite.client.event.ClientStartEvent
import cc.zerite.client.event.Subscribe
import cc.zerite.client.injection.bootstrap.impl.annotations.Instance
import cc.zerite.client.util.ext.urlEncoded
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.util.EntityUtils
import org.apache.logging.log4j.LogManager

@Instance(registerEvents = true)
object ApiAuthentication {

    private val logger = LogManager.getLogger("Zerite-API-Authentication")

    private var successful = false
    private var accessToken = ""

    @Subscribe
    private fun onClientStart(e: ClientStartEvent) {
        GlobalScope.launch { authenticate() }
    }

    @Subscribe
    private fun onClientShutdown(e: ClientShutdownEvent) {
        GlobalScope.launch { invalidate() }
    }

    private fun authenticate() {
        try {
            logger.info("Beginning to authenticate with the Zerite API")

            val session = Minecraft.getMinecraft().session ?: return
            val username = session.username
            val uuid = session.playerID

            val encodedUsername = username.urlEncoded()
            val encodedUuid = uuid.urlEncoded()

            val gson = Gson()

            val httpClient = HttpClientBuilder.create().build()

            logger.info("Obtaining required values...")

            val preRequest = HttpGet("$preAuthUrl?username=$encodedUsername&uuid=$encodedUuid")
            val preResponse = httpClient.execute(preRequest)

            val preJson = gson.fromJson(
                EntityUtils.toString(preResponse.entity),
                PreAuthResponse::class.java
            )

            if (!preJson.success) {
                logger.error("Login failed: Pre-authentication.")
                return
            }

            logger.info("Authenticating with Mojang...")

            val serverId = preJson.serverId
            val jsonObject = JsonObject()

            jsonObject.let {
                it.addProperty("accessToken", session.token)
                it.addProperty("selectedProfile", uuid)
                it.addProperty("serverId", serverId)
            }

            val joinRequest = HttpPost(mojangJoinUrl)

            joinRequest.entity = StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON)

            val joinResponse = httpClient.execute(joinRequest)

            if (joinResponse.statusLine.statusCode != 204) {
                logger.error("Login failed: Mojang authentication")
                return
            }

            logger.info("Success, sending post-auth request...")

            val postRequest =
                HttpGet("$postAuthUrl?username=$encodedUsername&uuid=$encodedUuid&serverId=${serverId.urlEncoded()}")
            val postResponse = httpClient.execute(postRequest)

            val postJson = gson.fromJson(
                EntityUtils.toString(postResponse.entity),
                PostAuthResponse::class.java
            )

            if (!postJson.success) {
                logger.error("Login failed: Post-authentication.")
                return
            }

            accessToken = postJson.accessToken
            successful = true

            logger.info("Login success!")
        } catch (e: Exception) {
        }
    }

    private fun invalidate() {
        try {
            val session = Minecraft.getMinecraft().session ?: return
            val username = session.username
            val uuid = session.playerID

            val encodedUsername = username.urlEncoded()
            val encodedUuid = uuid.urlEncoded()
            val encodedAccessToken = accessToken.urlEncoded()

            val gson = Gson()

            val httpClient = HttpClientBuilder.create().build()

            val invalidateRequest =
                HttpGet("$invalidateAuthUrl?username=$encodedUsername&uuid=$encodedUuid&accessToken=$encodedAccessToken")
            val invalidateResponse = httpClient.execute(invalidateRequest)

            val invalidateJson = gson.fromJson(
                EntityUtils.toString(invalidateResponse.entity),
                InvalidateResponse::class.java
            )

            if (!invalidateJson.success) {
                logger.error("Invalidation failed.")
                return
            }

            logger.info("Invalidation succeeded.")
        } catch (e: Exception) {
        }
    }

}