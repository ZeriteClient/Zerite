package cc.zerite.client.game.tools.font

import cc.zerite.client.injection.bootstrap.impl.annotations.Instance
import java.awt.Font

@Instance
object ZeriteFonts {

    val regular = ZeriteFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/zerite/fonts/Roboto-Regular.ttf")
        ), 20f, 2
    )
    val medium = ZeriteFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/zerite/fonts/Roboto-Medium.ttf")
        ), 20f, 2
    )
    val mediumSmall = ZeriteFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/zerite/fonts/Roboto-Medium.ttf")
        ), 16f, 2
    )
    val bold = ZeriteFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/zerite/fonts/Roboto-Bold.ttf")
        ), 20f, 2
    )
    val title = ZeriteFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/zerite/fonts/Roboto-Bold.ttf")
        ), 48f, 2
    )
    val titleLarge = ZeriteFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/zerite/fonts/Roboto-Bold.ttf")
        ), 84f, 2
    )

}