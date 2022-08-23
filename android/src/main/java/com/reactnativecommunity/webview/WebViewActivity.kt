package com.reactnativecommunity.webview

import android.R
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.reactnativecommunity.webview.R.id
import com.reactnativecommunity.webview.R.layout
import com.reactnativecommunity.webview.data.database.CoreDataBase


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

    intent.getStringExtra(URL_NAME_PARAM)?.let {
      webView.loadUrl(it)
    }

    val settings = webView.settings
    settings.setSupportMultipleWindows(true)
    settings.javaScriptEnabled = true
    settings.javaScriptCanOpenWindowsAutomatically = true

    webView.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return false
      }
    }

    webView.addJavascriptInterface(
      LocalStorageJavaScriptInterface(
        this,
        CoreDataBase.getInstance(this)
      ), "LocalStorage"
    )

    webView.webChromeClient = object : WebChromeClient() {
      override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
      ): Boolean {
        val dialog = Dialog(this@WebViewActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(layout.layout_dialog_webview)
        var newWebView = dialog.findViewById<WebView>(id.wv_web_view_fragment_main_dialog)
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
            if (url.startsWith("https") || url.startsWith("http")) {
              dialog.show()
              view?.loadUrl(url)
            } else {
              try {
                val browseIntent = Intent(Intent.ACTION_VIEW)
                browseIntent.data = Uri.parse(url)
                this@WebViewActivity.startActivity(browseIntent)
              } catch (e: Exception) {
                // Do nothing
              }
            }
            return super.shouldOverrideUrlLoading(view, request)
          }
        }

        newWebView.webChromeClient = object : WebChromeClient() {
          override fun onCloseWindow(window: WebView?) {
            super.onCloseWindow(window)
            dialog.dismiss()
          }
        }
        resultMsg.sendToTarget()
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

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // handle arrow click here
    if (item.itemId == R.id.home) {
      onBackPressed()
    }
    return super.onOptionsItemSelected(item)
  }
}