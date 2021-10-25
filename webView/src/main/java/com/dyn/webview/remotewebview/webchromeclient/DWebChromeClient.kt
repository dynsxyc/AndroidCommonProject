package com.dyn.webview.remotewebview.webchromeclient

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.dyn.webview.WebCallback
import com.orhanobut.logger.Logger
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DWebChromeClient(private val mWebCallback: WebCallback) : WebChromeClient() {
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null
    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
            if (title.isNullOrEmpty().not()){
                mWebCallback.onReceivedTitle(title!!)
        }
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        Logger.i(consoleMessage?.message()?:"")
        return super.onConsoleMessage(consoleMessage)
    }



    override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
        AlertDialog.Builder(view.context)
                .setTitle(R.string.dialog_alert_title)
                .setMessage(message)
                .setPositiveButton(R.string.ok
                ) { dialoginterface, i -> //按钮事件
                    Toast.makeText(view.context, view.context.getString(R.string.ok) + " clicked.", Toast.LENGTH_LONG).show()
                }.show()
        result.confirm();// 不加这行代码，会造成Alert劫持：Alert只会弹出一次，并且WebView会卡死
        return true
    }
    //For Android5.0+
    override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: WebChromeClient.FileChooserParams?): Boolean {
        if (mFilePathCallback != null) {
            try {
                mFilePathCallback!!.onReceiveValue(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        mFilePathCallback = filePathCallback
        var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent!!.resolveActivity(webView?.context?.packageManager!!) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.absolutePath
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
            } else {
                takePictureIntent = null
            }
        }
        mWebCallback.onShowFileChooser(takePictureIntent, mFilePathCallback)
        return true
    }

    /**
     * More info this method can be found at
     * http://developer.android.com/training/camera/photobasics.html
     *
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }
}