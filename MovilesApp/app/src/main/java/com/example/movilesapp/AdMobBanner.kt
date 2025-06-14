
package com.example.movilesapp
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext


// true = prueba, flase = deploy
private const val USE_TEST_ADS = true

@Composable
fun AdMobBanner(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val adUnitId = if (USE_TEST_ADS) {
        "ca-app-pub-3940256099942544/6300978111" //ID de prueba
    } else {
        "ca-app-pub-1095473884479372/8122487816" //ID real
    }

    Log.d("AdMob", "Usando adUnitId: $adUnitId")

    val adView = remember {
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            this.adUnitId = adUnitId
            loadAd(AdRequest.Builder().build())
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { adView }
    )
}



