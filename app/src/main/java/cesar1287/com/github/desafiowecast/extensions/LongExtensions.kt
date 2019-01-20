package cesar1287.com.github.desafiowecast.extensions

fun Long.getTimeString(): String {
    val buf = StringBuffer()

    val hours = this / (1000 * 60 * 60)
    val minutes = this % (1000 * 60 * 60) / (1000 * 60)
    val seconds = this % (1000 * 60 * 60) % (1000 * 60) / 1000

    buf.append(String.format("%02d", hours))
        .append(":")
        .append(String.format("%02d", minutes))
        .append(":")
        .append(String.format("%02d", seconds))

    return buf.toString()
}