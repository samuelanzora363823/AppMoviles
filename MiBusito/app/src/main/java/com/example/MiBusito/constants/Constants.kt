package com.example.MiBusito

object Constants {
    //false = produccion
    const val IS_RELEASE = false

    val BANNER_AD_UNIT_ID: String
        get() = if (IS_RELEASE) {
            "ca-app-pub-1095473884479372/8122487816" // tu ID real
        } else {
            "ca-app-pub-3940256099942544/6300978111" // ID de prueba oficial de Google
        }
}
