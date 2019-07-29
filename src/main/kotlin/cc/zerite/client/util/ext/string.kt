package cc.zerite.client.util.ext

import java.net.URLEncoder

fun String.urlEncoded() = URLEncoder.encode(this, "UTF-8")