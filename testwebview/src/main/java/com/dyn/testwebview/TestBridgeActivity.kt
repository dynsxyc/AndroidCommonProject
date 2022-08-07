package com.dyn.testwebview

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dyn.webview.BaseWebView
import com.dyn.webview.jsbridge.BridgeHandler
import com.dyn.webview.jsbridge.BridgeWebView
import com.dyn.webview.jsbridge.CallBackFunction

data class User(val name:String = "张麻子")

class TestBridgeActivity : AppCompatActivity() {
    private lateinit var webView: BaseWebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_bridge)
        webView = findViewById(R.id.webView)
        webView.loadUrl("file:///android_asset/demo.html")
        webView.callHandler("functionInJs","native 端发送的数据",object : CallBackFunction {
            override fun onCallBack(data: String?) {
                Log.i("dyn","onCallBack data->$data")
            }

        })
        webView.send("hello")
    }
    fun onClick(view :View){
        webView.callHandler("functionInJs", "data from Java", object : CallBackFunction {
            override fun onCallBack(data: String?) {
                // TODO Auto-generated method stub
                Log.i("dyn", "reponse data from js $data")
            }
        })
        webView.registerHandler("submitFromWeb",object : BridgeHandler {
            override fun handler(data: String?, function: CallBackFunction?) {
                Log.i("dyn", "submitFromWeb reponse data from js $data")
            }

        })
    }
}