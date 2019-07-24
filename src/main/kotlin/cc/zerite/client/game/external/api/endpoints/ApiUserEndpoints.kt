package cc.zerite.client.game.external.api.endpoints

import cc.zerite.client.game.external.api.UserResponse
import cc.zerite.client.game.external.api.getRequest
import cc.zerite.client.game.external.api.userUrl
import java.util.*

object ApiUserEndpoints {

    fun getUser(uuid: UUID): UserResponse? {
        val strippedUuid = uuid.toString().replace("-", "")
        return getRequest<UserResponse>("$userUrl/$strippedUuid")
    }

}