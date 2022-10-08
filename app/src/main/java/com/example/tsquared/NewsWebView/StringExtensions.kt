package com.example.tsquared.NewsWebView

import okio.ByteString

const val FILE_SCHEME = "file://"
const val UTF8 = "UTF-8"

fun String.sha1(): String = ByteString.of(*this.toByteArray()).sha1().hex()