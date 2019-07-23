package net.zeriteclient.zerite.game.external.api.endpoints

import net.zeriteclient.zerite.game.external.api.UserResponse
import net.zeriteclient.zerite.game.external.api.getRequest
import net.zeriteclient.zerite.game.external.api.userUrl
import java.util.*

object ApiUserEndpoints {

    fun getUser(uuid: UUID): UserResponse? {
        val strippedUuid = uuid.toString().replace("-", "")
        return getRequest<UserResponse>("$userUrl/$strippedUuid")
    }

}