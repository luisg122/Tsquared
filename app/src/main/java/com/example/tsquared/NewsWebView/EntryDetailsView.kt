package com.example.tsquared.NewsWebView

/*
 * Copyright (c) 2012-2018 Frederic Julian
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider.getUriForFile
import com.example.tsquared.Constants.Constants
import com.example.tsquared.Fragments.TextFormatBottomSheet
import com.example.tsquared.R
import net.dankito.readability4j.Article
import okhttp3.Call
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.colorAttr
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import java.io.File
import java.io.IOException
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit
import net.dankito.readability4j.extended.Readability4JExtended



@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class EntryDetailsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                 defStyleAttr: Int = 0) : WebView(context, attrs, defStyleAttr), TextFormatBottomSheet.TextFormatChangeListener {

    private var value: Float = 0.0F;
    private val TEXT_HTML = "text/html"
    private val HTML_IMG_REGEX = "(?i)<[/]?[ ]?img(.|\n)*?>"
    private val BACKGROUND_COLOR = colorString(R.attr.cardBackgroundColor)
    private val QUOTE_BACKGROUND_COLOR = colorString(R.attr.bubbleItemColor)
    private val QUOTE_LEFT_COLOR = colorString(R.attr.commentsSecColor)
    private val TEXT_COLOR = colorString(R.attr.lightTextColor)
    private val SUBTITLE_COLOR = colorString(R.attr.lightTextColor)
    private val SUBTITLE_BORDER_COLOR = "solid " + colorString(R.attr.lightTextColor)
    private val CSS = "<head><style type='text/css'> " +
            "body {max-width: 100%; padding: 30px 15px; font-family: sans-serif-light; color: " + TEXT_COLOR + "; background-color:" + BACKGROUND_COLOR + "; line-height: 150%} " +
            "* {max-width: 100%; word-break: break-word}" +
            "table {display: table; border-collapse: separate; box-sizing: border-box; text-indent: initial; border-spacing: 0; border-color: grey; border: 1px solid #DFE1E3; font-size: 14px;}" +
            "tr {display: table-row; vertical-align: inherit; border-color: inherit;}" +
            "tbody, td {border: 1px solid #DFE1E3; font-size: 14px;}" +
            "h1, h2 {font-weight: normal; line-height: 130%} " +
            "h1 {font-size: 170%; margin-bottom: 0.1em} " +
            "h2 {font-size: 140%} " +
            "h1.titleHeading {text-align: center; font-weight: bold; padding: 60px 0px}" +
            "a {color: #0099CC}" +
            "h1 a {color: inherit; text-decoration: none}" +
            "img {height: auto; display: block; margin: 3px auto; border-radius: 5px;} " +
            "pre {white-space: pre-wrap; direction: ltr;} " +
            "blockquote {border-left: thick solid " + QUOTE_LEFT_COLOR + "; background-color:" + QUOTE_BACKGROUND_COLOR + "; margin: 0.5em 0 0.5em 0em; padding: 0.5em} " +
            "p {margin: 0.8em 0 0.8em 0} " +
            "p.subtitle {color: " + SUBTITLE_COLOR + "; border-top:1px " + SUBTITLE_BORDER_COLOR + "; border-bottom:1px " + SUBTITLE_BORDER_COLOR + "; padding-top:2px; padding-bottom:2px; font-weight:800 } " +
            "ul, ol {margin: 0 0 0.8em 0.6em; padding: 0 0 0 1em} " +
            "ul li, ol li {margin: 0 0 0.8em 0; padding: 0} " +
            "</style><meta name='viewport' content='width=device-width'/></head>"
    private val TITLE_START = "<h1 class='titleHeading'><a href='"
    private val TITLE_MIDDLE = "'>"
    private val TITLE_END = "</a></h1>"
    private val DIVIDER = "<div   ></div>"
    private val BODY_START = "<body dir=\"auto\">"
    private val BODY_END = "</body>"
    private val SUBTITLE_START = "<p class='subtitle'>"
    private val SUBTITLE_END = "</p>"

    init {

        // For scrolling
        isHorizontalScrollBarEnabled = false
        settings.useWideViewPort = false
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        settings.allowFileAccess = true

        if(value!!.toInt() != 0)
            settings.textZoom = 150 * value!!.toInt() + 20;

        @SuppressLint("SetJavaScriptEnabled")
        settings.javaScriptEnabled = true

        // For color
        setBackgroundColor(Color.parseColor(BACKGROUND_COLOR))

        webViewClient = object : WebViewClient() {

            @Suppress("OverridingDeprecatedMember")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                try {
                    if (url.startsWith(FILE_SCHEME)) {
                        val file = File(url.replace(FILE_SCHEME, ""))
                        val contentUri = getUriForFile(context, "com.example.tsquared.fileprovider", file)
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(contentUri, "image/jpeg")
                        context.startActivity(intent)
                        view.title
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                } catch (e: ActivityNotFoundException) {
                    //Toast.makeText(context, R.string.cant_open_link, Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return true
            }
        }
    }

    private val COOKIE_MANAGER = CookieManager().apply {
        setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }

    private val HTTP_CLIENT: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .cookieJar(JavaNetCookieJar(COOKIE_MANAGER))
        .build()

    fun createCall(url: String): Call = HTTP_CLIENT.newCall(
        Request.Builder()
        .url(url)
        .header("User-agent", "Mozilla/5.0 (compatible) AppleWebKit Chrome Safari") // some feeds need this to work properly
        .addHeader("accept", "*/*")
        .build())

    fun setEntry(url: String) {
        doAsync {
            var (htmlTitle, htmlArticleContent) = extractHTML_stuff(url)

            uiThread {
                val html = StringBuilder(CSS)
                    .append(TITLE_START).append(url).append(TITLE_MIDDLE).append(htmlTitle).append(TITLE_END)
                    .append(BODY_START)
                    .append(htmlArticleContent)
                    .append(BODY_END)
                    .toString()


                loadDataWithBaseURL(url, html, TEXT_HTML, UTF8, null)

                // display top of article
                ObjectAnimator.ofInt(this@EntryDetailsView, "scrollY", scrollY, 0).setDuration(500).start()
            }
        }
    }

    data class HTML_Values(var htmlTitle: String?, var htmlArticleContent: String?)

    private fun extractHTML_stuff(link: String): HTML_Values {
        var htmlTitle: String? = null
        var htmlArticleContent: String? = null
        var article: Article? = null
        try {
            createCall(link).execute().use { response ->
                response.body?.byteStream()?.let { input ->
                    article = Readability4JExtended(link, Jsoup.parse(input, null, link)).parse()
                }
            }
        } catch (t: Throwable) {
            error("Can't mobilize feedWithCount ${link}")
        }

        article?.title?.let {
            htmlTitle = HtmlUtils.improveHtmlContent(it, getBaseUrl(link))
        }

        article?.articleContent?.html().let {
            htmlArticleContent = HtmlUtils.improveHtmlContent(it, getBaseUrl(link))
        }

        return HTML_Values(htmlTitle, htmlArticleContent)
    }

    private fun getBaseUrl(link: String): String {
        var baseUrl = link
        val index = link.indexOf('/', 8) // this also covers https://
        if (index > -1) {
            baseUrl = link.substring(0, index)
        }

        return baseUrl
    }

    private fun colorString(resourceInt: Int): String {
        val color = context.colorAttr(resourceInt)
        return String.format("#%06X", 0xFFFFFF and color)
    }

    override fun changeTextSize(value: Float) {
        this.value = value;
    }

    override fun changeTextFont() {

    }
}