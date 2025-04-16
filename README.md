
# 🪙 Android Crypto Monitor

APP nativo Android de monitoração de cryptos utilizando APIs 

## Screenshots

<img src="assets/Screenshot0.png" alt="Screenshot do APP" width="200"> <img src="assets/Screenshot1.png" alt="Screenshot do APP" width="200">

## Como Testar

- Ative fontes desconhecidas no seu dispositivo:

`Configurações > Segurança e privacidade > Instalar apps desconhecidos > Ative a permição no seu APP de preferência`

- Baixe o APK disponível em:

`releases/download/v1.0/crypto_monitor.apk`

## Stack Utilizada

- Kotlin
- Android Studio
- Gradle
- Destaque de dependências:
  - Retrofit2
  - Retrofit2 Gson Converter
  - Kotlinx Coroutines
- APIs:
  - [MercadoBitcoin](https://api.mercadobitcoin.net/api/v4/docs)
  - [EconomiaAwesomeAPI](https://economia.awesomeapi.com.br/)

## Estrutura do projeto (simplificada)

```
├───java
│   └───lucaslimb.com.github.cryptomonitor
│           ├───adapter
│           ├───model           
│           ├───service    
│           ├───ui        
│           └───MainActivity.kt                             
└───res
    ├───drawable
    ├───layout
    ├───values
    └───xml
```
