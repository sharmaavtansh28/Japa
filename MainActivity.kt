
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var todayCount = 0
    private var totalCount = 0
    private lateinit var lastUpdatedDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val incrementButton = findViewById<Button>(R.id.button_increment)
        val decrementButton = findViewById<Button>(R.id.button_decrement)
        val resetButton = findViewById<Button>(R.id.button_reset)
        val todayCountText = findViewById<TextView>(R.id.textView_today_count)
        val totalCountText = findViewById<TextView>(R.id.textView_total_count)

        // Load saved state
        val sharedPref = getSharedPreferences("MyApp", MODE_PRIVATE)
        loadSavedState(sharedPref, todayCountText, totalCountText)

        incrementButton.setOnClickListener {
            handleIncrement(todayCountText, totalCountText, sharedPref)
        }

        decrementButton.setOnClickListener {
            if (todayCount > 0) {
                todayCount--
                updateCountTexts(todayCountText, totalCountText)
            }
        }

        resetButton.setOnClickListener {
            todayCount = 0
            updateCountTexts(todayCountText, totalCountText)
            saveCounts(sharedPref)
        }
    }

    private fun loadSavedState(sharedPref: SharedPreferences, todayCountText: TextView, totalCountText: TextView) {
        totalCount = sharedPref.getInt("totalCount", 0)
        lastUpdatedDate = sharedPref.getString("lastUpdatedDate", getCurrentDate()) ?: getCurrentDate()

        if (lastUpdatedDate != getCurrentDate()) {
            todayCount = 0 // Reset today's count if the date has changed
        } else {
            todayCount = sharedPref.getInt("todayCount", 0)
        }

        updateCountTexts(todayCountText, totalCountText)
    }

    private fun handleIncrement(todayCountText: TextView, totalCountText: TextView, sharedPref: SharedPreferences) {
        todayCount++
        if (todayCount >= 108) {
            totalCount++
            todayCount = 0
        }
        updateCountTexts(todayCountText, totalCountText)
        saveCounts(sharedPref)
    }

    private fun updateCountTexts(todayCountText: TextView, totalCountText: TextView) {
        todayCountText.text = "Today's Count: $todayCount"
        totalCountText.text = "Total Count: $totalCount"
    }

    private fun saveCounts(sharedPref: SharedPreferences) {
        with(sharedPref.edit()) {
            putInt("totalCount", totalCount)
            putInt("todayCount", todayCount)
            putString("lastUpdatedDate", getCurrentDate())
            apply()
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
