package net.zeriteclient.zerite.util.other

object StringUtil {

    fun splitWithMaxLength(input: String, maxLineLength: Int): Array<String> {
        if (input.length < maxLineLength) {
            return arrayOf(input)
        }

        var length = 0
        val line = StringBuilder()
        val lines = ArrayList<String>()

        for (split in input.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            line.append(split).append(" ")
            length += split.length
            if (maxLineLength <= length) {
                lines.add(line.toString())
                length = 0
            }
        }

        return lines.toTypedArray()
    }

}