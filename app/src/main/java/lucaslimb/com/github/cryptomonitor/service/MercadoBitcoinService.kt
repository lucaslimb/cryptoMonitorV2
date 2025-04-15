package lucaslimb.com.github.cryptomonitor.service

import lucaslimb.com.github.cryptomonitor.model.TickerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MercadoBitcoinService {
    @GET("api/{coin}/ticker")
    suspend fun getTicker(@Path("coin") coin: String): Response<TickerResponse>
}