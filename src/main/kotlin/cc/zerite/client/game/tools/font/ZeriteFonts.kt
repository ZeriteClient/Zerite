package cc.zerite.client.game.tools.font

import cc.zerite.client.injection.bootstrap.impl.annotations.Instance
import java.awt.Font

@Instance
object ZeriteFonts {

    val regular = ZeriteFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/zerite/fonts/Roboto-Regular.ttf")
        ), 10f, 2
    )
    val medium = ZeriteFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/zerite/fonts/Roboto-Medium.ttf")
        ), 10f, 2
    )
    val mediumSmall = ZeriteFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/zerite/fonts/Roboto-Medium.ttf")
        ), 8f, 2
    )
    val bold = ZeriteFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/zerite/fonts/Roboto-Bold.ttf")
        ), 10f, 2
    )
    val title = ZeriteFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/zerite/fonts/Roboto-Bold.ttf")
        ), 24f, 2
    )
    val titleLarge = ZeriteFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/zerite/fonts/Roboto-Bold.ttf")
        ), 42f, 2
    )

}