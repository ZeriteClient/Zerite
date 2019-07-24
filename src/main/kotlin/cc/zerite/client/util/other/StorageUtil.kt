package cc.zerite.client.util.other

import net.minecraft.launchwrapper.Launch
import java.io.File

object StorageUtil {

    fun getDataFile(name: String): File {
        val dataDir = File(Launch.minecraftHome, "zerite")
        dataDir.mkdirs()
        return File(dataDir, name)
    }
}