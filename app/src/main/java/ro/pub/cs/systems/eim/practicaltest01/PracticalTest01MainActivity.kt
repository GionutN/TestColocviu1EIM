package ro.pub.cs.systems.eim.practicaltest01

import android.app.ComponentCaller
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01MainActivity : AppCompatActivity() {
    private var leftText: EditText? = null
    private var rightText: EditText? = null
    private var leftButton: Button? = null
    private var rightButton: Button? = null
    private var secondActivityButton: Button? = null

    private val buttonClickListener = ButtonClickListener()
    private var serviceStarted: Boolean = false

    private inner class ButtonClickListener : View.OnClickListener {
        private var leftCount: Int = 0
        private var rightCount: Int = 0
        override fun onClick(view: View) {
            when (view.id) {
                R.id.button1 -> {
                    leftCount++
                    leftText!!.setText(leftCount.toString())
                    checkAndStartService(leftCount, rightCount)
                }
                R.id.button2 -> {
                    rightCount++
                    rightText!!.setText(rightCount.toString())
                    checkAndStartService(leftCount, rightCount)
                }
                R.id.secondary_activity_button -> {
                    val intent = Intent(this@PracticalTest01MainActivity, PracticalTest01SecondaryActivity::class.java)
                    //val intent = Intent("ro.pub.cs.systems.eim.practicaltest01.intent.action.PracticalTest01SecondaryActivity")

                    val numberOfClicks = leftText!!.text.toString().toInt() +
                            rightText!!.text.toString().toInt()

                    intent.putExtra(Constants.NUMBER_OF_CLICKS, numberOfClicks)

                    startActivityForResult(intent, Constants.SECONDARY_ACTIVITY_REQUEST_CODE)
                }
            }
        }
    }

    private val messageBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("PracticalTest01Broadcast", intent?.getStringExtra("message") ?: "Empty")
        }
    }

    private val intentFilter = IntentFilter().apply {
        addAction("ro.pub.cs.systems.eim.action1")
        addAction("ro.pub.cs.systems.eim.action2")
        addAction("ro.pub.cs.systems.eim.action3")
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageBroadcastReceiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(messageBroadcastReceiver, intentFilter)
        }
    }

    override fun onPause() {
        unregisterReceiver(messageBroadcastReceiver)
        super.onPause()
    }

    private fun checkAndStartService(leftCount: Int, rightCount: Int) {
        if (leftCount + rightCount > Constants.SERVICE_THRESHOLD && !serviceStarted) {
            val intent = Intent(this, PracticalTest01Service::class.java)
            intent.putExtra("firstNumber", leftCount)
            intent.putExtra("secondNumber", rightCount)
            startService(intent)
            serviceStarted = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        leftText = findViewById<View>(R.id.press_count1) as EditText
        rightText = findViewById<View>(R.id.press_count2) as EditText
        leftButton = findViewById<View>(R.id.button1) as Button
        rightButton = findViewById<View>(R.id.button2) as Button
        secondActivityButton = findViewById<View>(R.id.secondary_activity_button) as Button

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constants.LEFT_COUNT)) {
                leftText!!.setText(savedInstanceState.getString(Constants.LEFT_COUNT))
            } else{
                leftText!!.setText("0")
            }
            if (savedInstanceState.containsKey(Constants.RIGHT_COUNT)) {
                rightText!!.setText(savedInstanceState.getString(Constants.RIGHT_COUNT))
            } else {
                rightText!!.setText("0")
            }
        } else {
            leftText!!.setText("0")
            rightText!!.setText("0")
        }

        leftButton!!.setOnClickListener(buttonClickListener)
        rightButton!!.setOnClickListener(buttonClickListener)
        secondActivityButton!!.setOnClickListener(buttonClickListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.LEFT_COUNT, leftText!!.text.toString())
        outState.putString(Constants.RIGHT_COUNT, rightText!!.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey(Constants.LEFT_COUNT)) {
            leftText!!.setText(savedInstanceState.getString(Constants.LEFT_COUNT))
        } else{
            leftText!!.setText("0")
        }
        if (savedInstanceState.containsKey(Constants.RIGHT_COUNT)) {
            rightText!!.setText(savedInstanceState.getString(Constants.RIGHT_COUNT))
        } else {
            rightText!!.setText("0")
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        if (requestCode == Constants.SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with code $resultCode", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        val intent = Intent(this, PracticalTest01Service::class.java)
        stopService(intent)
        super.onDestroy()
    }
}