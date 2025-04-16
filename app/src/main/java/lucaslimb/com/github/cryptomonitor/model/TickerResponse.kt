package lucaslimb.com.github.cryptomonitor.model

class TickerResponse(
    val ticker: Ticker
)

class Ticker(
    val last: String,
    val open: String,
    val date: Long
)