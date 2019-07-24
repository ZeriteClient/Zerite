package cc.zerite.client.game.external.api

import net.zeriteclient.util.entities.Cosmetic
import net.zeriteclient.util.entities.CosmeticMeta

// Auth
data class PreAuthResponse(
    val success: Boolean,
    val serverId: String
)

data class PostAuthResponse(
    val success: Boolean,
    val accessToken: String,
    val admin: Boolean
)

data class InvalidateResponse(
    val success: Boolean
)

// User
data class UserResponse(
    val success: Boolean,
    val username: String,
    val uuid: String,
    val cosmetics: ArrayList<Cosmetic>,
    val admin: Boolean? = false,
    val cosmeticMeta: CosmeticMeta? = CosmeticMeta()
)