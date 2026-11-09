package ro.pub.cs.systems.eim.practicaltest01

import android.app.Service
import android.content.Intent
import android.os.IBinder

class PracticalTest01Service : Service() {
    private var startMode: Int = START_NOT_STICKY
    private var processingThread: ProcessingThread? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val first = intent?.getIntExtra("firstNumber", 0) ?: 0
        val second = intent?.getIntExtra("secondNumber", 0) ?: 0
        processingThread = ProcessingThread(this, first, second)
        processingThread?.start()
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        processingThread?.stopThread()
        super.onDestroy()
    }
}