package net.zeriteclient.zerite.util.other

import net.minecraft.launchwrapper.Launch
import java.io.File

object StorageUtil {

    fun getDataFile(name: String): File {
        val dataDir = File(Launch.minecraftHome, "Zerite")
        dataDir.mkdirs()
        return File(dataDir, name)
    }
}