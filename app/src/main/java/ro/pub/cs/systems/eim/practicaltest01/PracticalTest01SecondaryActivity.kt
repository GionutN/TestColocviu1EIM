package ro.pub.cs.systems.eim.practicaltest01

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01SecondaryActivity : AppCompatActivity() {
    private var pressCount: TextView? = null
    private var okButton: Button? = null
    private var cancelButton: Button? = null

    private val buttonClickListener = ButtonClickListener()

    private inner class ButtonClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            when (view.id) {
                R.id.ok_button -> {
                    setResult(RESULT_OK, null)
                }
                R.id.cancel_button -> {
                    setResult(RESULT_CANCELED, null)
                }
            }
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_secondary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.secondActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pressCount = findViewById<View>(R.id.number_of_clicks_text_view) as TextView
        okButton = findViewById<View>(R.id.ok_button) as Button
        cancelButton = findViewById<View>(R.id.cancel_button) as Button

        val intent = intent
        if (intent != null) {
            if (intent.extras!!.containsKey(Constants.NUMBER_OF_CLICKS)) {
                val numClicks: Int = intent.getIntExtra(Constants.NUMBER_OF_CLICKS, 0)
                pressCount!!.text = numClicks.toString()
            }
        }

        okButton!!.setOnClickListener(buttonClickListener)
        cancelButton!!.setOnClickListener(buttonClickListener)
    }
}