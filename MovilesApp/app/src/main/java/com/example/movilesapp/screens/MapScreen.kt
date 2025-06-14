package com.example.movilesapp.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun MapScreen(
    urlMapa: String,
    onBackClick: () -> Unit
) {
    Column {
        Text(
            text = "Volver",
            modifier = Modifier
                .clickable { onBackClick() }
                .padding(16.dp),
            color = Color.Blue,
            fontWeight = FontWeight.Bold
        )

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(urlMapa)
                }
            }
        )
    }
}
