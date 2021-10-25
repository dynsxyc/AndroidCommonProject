package com.dyn.webview.utils

import android.os.Parcel
import android.os.Parcelable

data class AidlError(val code:Int,val message:String?,val extra:String? = "") :Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(code)
        parcel.writeString(message)
        parcel.writeString(extra)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AidlError> {
        override fun createFromParcel(parcel: Parcel): AidlError {
            return AidlError(parcel)
        }

        override fun newArray(size: Int): Array<AidlError?> {
            return arrayOfNulls(size)
        }
    }
}