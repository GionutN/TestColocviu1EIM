package ro.pub.cs.systems.eim.practicaltest01

import android.content.Context
import android.content.Intent
import android.util.Log
import kotlin.math.sqrt

class ProcessingThread(private val context: Context, private val num1: Int, private val num2: Int) : Thread() {
    private var isRunning = true
    private val actions = listOf(
        "ro.pub.cs.systems.eim.action1",
        "ro.pub.cs.systems.eim.action2",
        "ro.pub.cs.systems.eim.action3"
    )

    override fun run() {
        while (isRunning) {
            val date = java.util.Date().toString()
            val arithmeticMean = (num1 + num2) / 2.0
            val geometricMean = sqrt((num1 * num2).toDouble())

            val intent = Intent()
            intent.action = actions.random()
            intent.putExtra("message", "Time: $date, ArithMean: $arithmeticMean, GeomMean: $geometricMean")
            context.sendBroadcast(intent)

            try {
                sleep(10000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun stopThread() {
        isRunning = false
    }
}
