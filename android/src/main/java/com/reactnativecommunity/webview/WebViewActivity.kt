package com.reactnativecommunity.webview

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Message
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.reactnativecommunity.webview.R.*


class WebViewActivity : AppCompatActivity() {

  private lateinit var webView: WebView

  companion object {
    const val URL_NAME_PARAM = "URL_NAME_PARAM"
    const val SCREEN_NAME = "SCREEN_NAME"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(layout.activity_web_view)

    webView = findViewById(id.wv_web_view_fragment_main)

    val toolbar = findViewById<Toolbar>(id.toolbar)

    toolbar.title = intent.getStringExtra(SCREEN_NAME) ?: "Lending"

    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowHomeEnabled(true)

    webView.loadUrl(intent.getStringExtra(URL_NAME_PARAM)!!)

    val settings = webView.settings
    settings.setSupportMultipleWindows(true)
    settings.javaScriptEnabled = true
    settings.javaScriptCanOpenWindowsAutomatically = true

    webView.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return false
      }


    }

    webView.webChromeClient = object : WebChromeClient() {
      override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
      ): Boolean {
        println("---------- in web view")
//                val newWebView = WebView(this@WebViewActivity)
//                view?.addView(newWebView)
        val dialog = Dialog(this@WebViewActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(layout.layout_dialog_webview)
        var newWebView = dialog.findViewById<WebView>(id.wv_web_view_fragment_main_dialog)
//                addView(this@WebViewActivity, newWebView)
        val transport = resultMsg?.obj as WebView.WebViewTransport
        transport.webView = newWebView

        val settingsNew = newWebView.settings
        settingsNew.javaScriptEnabled = true
        settingsNew.javaScriptCanOpenWindowsAutomatically = true
        settingsNew.setSupportMultipleWindows(true)
        settingsNew.useWideViewPort = false
        newWebView.webViewClient = object : WebViewClient() {

          override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
          ): Boolean {
            val url = request?.url.toString()
            view?.loadUrl(url)
            return super.shouldOverrideUrlLoading(view, request)
          }

          override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
          }

          override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            println("---------- in web view $url")
            Toast.makeText(this@WebViewActivity, url, Toast.LENGTH_LONG).show()
            super.onPageStarted(view, url, favicon)
          }
        }

        newWebView.webChromeClient = object : WebChromeClient() {
          override fun onCloseWindow(window: WebView?) {
            super.onCloseWindow(window)
            dialog.dismiss()
          }
        }
        resultMsg.sendToTarget()
        dialog.show()
        return true
      }

      override fun onCloseWindow(window: WebView?) {
        super.onCloseWindow(window)
        finish()
      }
    }
  }

  override fun onBackPressed() {
    if (webView.canGoBack()) {
      webView.goBack()
    } else {
      super.onBackPressed()
    }
  }

}