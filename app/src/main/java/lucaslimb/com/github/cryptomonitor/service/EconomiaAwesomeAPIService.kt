package lucaslimb.com.github.cryptomonitor.service

import lucaslimb.com.github.cryptomonitor.model.USDBRLResponse
import retrofit2.Response
import retrofit2.http.GET

interface EconomiaAwesomeAPIService {
    @GET("json/last/USD-BRL")
    suspend fun getUSDBRL(): Response<USDBRLResponse>
}