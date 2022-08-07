package com.dyn.webview

import android.os.Parcel
import android.os.Parcelable

class WebViewArgs(
    val loadUrl: String?,
    val interfaceName: String?,
    val title: String?,
    val isSyncCookie: Boolean,
    val isShowActionBar: Boolean?,
    val header: HashMap<String, String>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readSerializable() as HashMap<String, String>
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(loadUrl)
        parcel.writeString(interfaceName)
        parcel.writeString(title)
        parcel.writeByte(if (isSyncCookie) 1 else 0)
        parcel.writeByte(if (isShowActionBar == true) 1 else 0)
        parcel.writeSerializable(header)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WebViewArgs> {
        override fun createFromParcel(parcel: Parcel): WebViewArgs {
            return WebViewArgs(parcel)
        }

        override fun newArray(size: Int): Array<WebViewArgs?> {
            return arrayOfNulls(size)
        }
    }
}