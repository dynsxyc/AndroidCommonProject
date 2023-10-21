package com.dyn.base.utils

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object ImgToBase64 {

    /**
     * 本地图片转base64
     */
    fun getImgFileToBase64(imgFile: String): String? {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        var buffer = try {
            val inputStream = FileInputStream(imgFile)//读取图片字节数组
            var count = 0
            while (count == 0) {
                count = inputStream.available();
            }
            val buffer = ByteArray(count)
            inputStream.read(buffer);
            buffer
        } catch (e: IOException) {
            e.printStackTrace();
            null
        }
        // 对字节数组Base64编码
        return if (buffer == null) {
            null
        } else {
            Base64.encodeToString(buffer, Base64.DEFAULT);
        }
    }

    /**
     * 网络图片转base64
     */
    fun getImgUrlToBase64(imgUrl: String): String? {
        var buffer: ByteArray? = null
        var inputStream: InputStream? = null
        try {
            val outputStream = ByteArrayOutputStream()
            // 创建URL
            val url = URL(imgUrl);
            // 创建链接
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET";
            conn.connectTimeout = 5000;
            inputStream = conn.getInputStream();
            // 将内容读取内存中
            buffer = ByteArray(1024);
            var len = -1;
            while (inputStream.read(buffer).also { len = it } != -1) {
                outputStream.write(buffer, 0, len);
            }
            buffer = outputStream.toByteArray();
        } catch (e: IOException) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    // 关闭inputStream流
                    inputStream.close();
                } catch (e: IOException) {
                    e.printStackTrace();
                }
            }
        }
        // 对字节数组Base64编码
        return if (buffer == null) {
            null
        } else {
            Base64.encodeToString(buffer, Base64.DEFAULT);
        }
    }

    /**
     * 本地或网络图片转base64
     */
    public fun getImgStrToBase64(imgStr: String): String? {
        var inputStream: InputStream? = null;
        var buffer: ByteArray? = null;
        try {
            val outputStream = ByteArrayOutputStream();
            //判断网络链接图片文件/本地目录图片文件
            if (imgStr.startsWith("http://") || imgStr.startsWith("https://")) {
                // 创建URL
                val url = URL(imgStr);
                // 创建链接
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET";
                conn.connectTimeout = 5000;
                inputStream = conn.inputStream;
                // 将内容读取内存中
                buffer = ByteArray(1024)
                var len = -1;
                while (inputStream.read(buffer).also {
                        len = it
                    } != -1) {
                    outputStream.write(buffer, 0, len);
                }
                buffer = outputStream.toByteArray();
            } else {
                inputStream = FileInputStream(imgStr);
                var count = 0;
                while (count == 0) {
                    count = inputStream.available();
                }
                buffer = ByteArray(count);
                inputStream.read(buffer);
            }
        } catch (e: Exception) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    // 关闭inputStream流
                    inputStream.close();
                } catch (e: IOException) {
                    e.printStackTrace();
                }
            }
        }

        // 对字节数组Base64编码
        return if (buffer == null) {
            null
        } else {
            Base64.encodeToString(buffer, Base64.DEFAULT);
        }
    }

    /**
     * base64转图片存储在本地
     * @param imgBase64 图片base64
     * @param imgPath 图片本地存储地址
     */
    fun getImgBase64ToImgFile(imgBase64: String, imgPath: String): Boolean {
        var flag = true;
        try {
            val outputStream = FileOutputStream(imgPath);
            // 解密处理数据
            var bytes = Base64.decode(imgBase64, Base64.DEFAULT)
            bytes.forEachIndexed { index, byte ->
                if (byte<0){
                    bytes[index] = bytes[index].plus(256).toByte()
                }
            }
            outputStream.write(bytes);
        } catch (e: Exception) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

}