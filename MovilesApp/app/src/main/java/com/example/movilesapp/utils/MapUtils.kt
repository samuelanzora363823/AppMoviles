
package com.example.movilesapp.utils

fun extraerUrlMapa(xmlKml: String): String? {
    val regex = "<href>(.*?)</href>".toRegex(RegexOption.DOT_MATCHES_ALL)
    return regex.find(xmlKml)?.groups?.get(1)?.value
}

