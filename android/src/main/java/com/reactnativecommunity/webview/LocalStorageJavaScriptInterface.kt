package com.reactnativecommunity.webview

import android.content.Context
import android.webkit.JavascriptInterface
import com.phonepe.business.depository.core.localstorage.LocalStorageEntity
import com.reactnativecommunity.webview.data.database.CoreDataBase

class LocalStorageJavaScriptInterface(val context: Context, private val coreDataBase: CoreDataBase) {

  /**
   * This method allows to get an item for the given key
   * @param key : the key to look for in the local storage
   * @return the item having the given key
   */
  @JavascriptInterface
  fun getItem(key: String): String? {

    val result = coreDataBase.getLocalStorageDao().getKey(key)
    println("----- $key $result")
    return result
  }

  /**
   * set the value for the given key, or create the set of datas if the key does not exist already.
   * @param key
   * @param value
   */
  @JavascriptInterface
  fun setItem(key: String,value: String) {

    try {
      val localStorageEntity = LocalStorageEntity(key, value)
      coreDataBase.getLocalStorageDao().insert(localStorageEntity)
      println("----- insert $key $value")
    } catch (e: Exception) {
      println("----- ${e.printStackTrace()}")
      //FirebaseCrashlytics.getInstance().recordException(e)
    }
  }

  /**
   * removes the item corresponding to the given key
   * @param key
   */
  @JavascriptInterface
  fun removeItem(key: String) {
    println("----- delete $key")
    coreDataBase.getLocalStorageDao().delete(key)
  }

}