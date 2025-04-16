package lucaslimb.com.github.cryptomonitor.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EconomiaAwesomeAPIFactory {
    fun create(): EconomiaAwesomeAPIService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://economia.awesomeapi.com.br/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(EconomiaAwesomeAPIService::class.java)
    }
}