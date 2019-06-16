package net.zeriteclient.zerite.game.tools.font

import net.zeriteclient.zerite.injection.bootstrap.impl.annotations.Instance
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

}