package lucaslimb.com.github.cryptomonitor

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lucaslimb.com.github.cryptoMonitor.R
import lucaslimb.com.github.cryptomonitor.adapter.InfoAdapter
import lucaslimb.com.github.cryptomonitor.model.InfoItem
import lucaslimb.com.github.cryptomonitor.service.EconomiaAwesomeAPIFactory
import lucaslimb.com.github.cryptomonitor.service.MercadoBitcoinServiceFactory
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var coins: List<Pair<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbarMain: Toolbar = findViewById(R.id.toolbar_main)
        configureToolbar(toolbarMain)

        configureSpinner()

        val recycler = findViewById<RecyclerView>(R.id.recycler_info)
        recycler.layoutManager = LinearLayoutManager(this@MainActivity)
        recycler.adapter = InfoAdapter(mutableListOf())

        val btnRefresh: Button = findViewById(R.id.btn_refresh)
        btnRefresh.setOnClickListener{
            makeRestCall()
        }
    }

    private fun configureToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(getColor(R.color.white))
        supportActionBar?.setTitle(getText(R.string.app_title))
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.teal_700))
    }

    private fun makeRestCall() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val service = MercadoBitcoinServiceFactory().create()
                val response = service.getTicker(selectedCoin())
                if(response.isSuccessful){
                    val coin = selectedCoin()
                    val tickerResponse = response.body()
                    val lblValue: TextView = findViewById(R.id.lbl_value)
                    val lblDate: TextView = findViewById(R.id.lbl_date)
                    val lblVariation: TextView = findViewById(R.id.lbl_variation)
                    var lastValue = tickerResponse?.ticker?.last?.toDoubleOrNull()
                    val openValue = tickerResponse?.ticker?.open?.toDoubleOrNull()

                    if(lastValue != null && openValue != null) {
                        val variation = ((lastValue - openValue) / openValue) * 100
                        val percentageFormat =
                            String.format(Locale.getDefault(), "%.2f%%", variation)
                        lblVariation.text = percentageFormat
                        lblVariation.setTextColor(
                            if (variation >= 0) getColor(R.color.green) else getColor(
                                R.color.red
                            )
                        )

                        val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()) as DecimalFormat
                        numberFormat.maximumFractionDigits = 2
                        numberFormat.minimumFractionDigits = 2
                        if (Locale.getDefault().language == "en"){
                            val bid = makeAwesomeAPICall()
                            if (bid != null) lastValue /= bid
                        }
                        lblValue.text = numberFormat.format(lastValue)

                        val date = tickerResponse?.ticker?.date?.let { Date(it * 1000L) }
                        val isEnglish = Locale.getDefault().language == "en"
                        val sdf = if (isEnglish)
                            SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())
                        else
                            SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                        lblDate.text = sdf.format(date)

                        val simpleSdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                        val simpleDate = simpleSdf.format(date)

                        val newItem = InfoItem(coin, simpleDate, numberFormat.format(lastValue))
                        val recycler = findViewById<RecyclerView>(R.id.recycler_info)
                        val adapter = recycler.adapter as InfoAdapter
                        adapter.addItemAtTop(newItem)
                        recycler.scrollToPosition(0)

                        recycler.visibility = View.VISIBLE
                    }
                } else {
                    val errorMessage = when (response.code()) {
                        400 -> "Bad Request"
                        401 -> "Unauthorized"
                        403 -> "Forbidden"
                        404 -> "Not Found"
                        else -> "Unknown error"
                    }
                    Toast.makeText(this@MainActivity, errorMessage,
                                    Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Falha na chamada: ${e.message}",
                                Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun configureSpinner() {
        coins = listOf(
            "BTC" to getString(R.string.spinner_btc_text),
            "XLM" to getString(R.string.spinner_xlm_text),
            "ETH" to getString(R.string.spinner_eth_text),
            "USDT" to getString(R.string.spinner_usdt_text),
            "XRP" to getString(R.string.spinner_xrp_text),
            "SOL" to getString(R.string.spinner_sol_text),
            "DOGE" to getString(R.string.spinner_doge_text),
            "TRX" to getString(R.string.spinner_trx_text),
            "USDC" to getString(R.string.spinner_usdc_text)
        )

        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, coins.map { it.second })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun selectedCoin(): String {
        val spinner = findViewById<Spinner>(R.id.spinner)
        val selectedIndex = spinner.selectedItemPosition
        return coins[selectedIndex].first
    }

    private suspend fun makeAwesomeAPICall(): Double? {
        return try {
            val service = EconomiaAwesomeAPIFactory().create()
            val response = service.getUSDBRL()
            if (response.isSuccessful) {
                response.body()?.USDBRL?.bid?.toDoubleOrNull()
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Bad Request"
                    401 -> "Unauthorized"
                    403 -> "Forbidden"
                    404 -> "Not Found"
                    else -> "Unknown error"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                null
            }
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Falha na chamada: ${e.message}", Toast.LENGTH_LONG).show()
            null
        }
    }
}