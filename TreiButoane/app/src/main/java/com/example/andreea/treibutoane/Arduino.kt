package com.example.andreea.treibutoane

import com.google.android.things.pio.PeripheralManager
import com.google.android.things.pio.UartDevice

class Arduino(uartDevice: String = "USB1-1.2:1.0") : AutoCloseable {
    private val uart: UartDevice by lazy {
        PeripheralManager.getInstance().openUartDevice(uartDevice).apply {
            setBaudrate(115200)
            setDataSize(8)
            setParity(UartDevice.PARITY_NONE)
            setStopBits(1)
        }
    }

    fun read2(): String {
        val maxCount = 8
        val buffer = ByteArray(maxCount)
        var output = ""
        do {
            val count = uart.read(buffer, buffer.size)
            output += buffer.toReadableString()
            if (count == 0) break
            logd("Read ${buffer.toReadableString()} $count bytes from peripheral")
        } while (true)
        output = output.replace("\\n".toRegex(), "")
        return output
    }

    fun read(): String {
        val maxCount = 4
        val buffer = ByteArray(maxCount)
        uart.read(buffer, buffer.size)
        return buffer.toReadableString()
    }

    private fun ByteArray.toReadableString() = filter { it > 0.toByte() }
        .joinToString(separator = "") { it.toChar().toString() }

    fun write(value: String) {
        val count = uart.write(value.toByteArray(), value.length)
        uart.flush(value.length)
        logd("Wrote $value $count bytes to peripheral")
    }

    override fun close() {
        uart.close()
    }
}