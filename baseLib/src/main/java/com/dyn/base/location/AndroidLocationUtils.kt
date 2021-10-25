package com.dyn.base.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.os.CancellationSignal
import androidx.core.os.ExecutorCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ServiceUtils
import com.dyn.base.common.handlerThread
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.Executors

object AndroidLocationUtils {
    var locationManager: LocationManager? = null
    var geocoder: Geocoder? = null
    private var context: Context? = null
    fun init(context: Context) {
        this.context = context
        geocoder = Geocoder(context)
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    }

    private fun getFromLocation(location: Location, data: MutableLiveData<LocationAddress>) {
        Observable.just(geocoder?.getFromLocation(location.latitude, location.longitude, 1))
            .handlerThread().subscribe({
                data.value = LocationAddress(location, it[0])
            }, {
                data.value = null
                it.printStackTrace()
            })
    }

    fun getFromLocationName(addressName: String, data: MutableLiveData<List<Address>>) {
        Observable.fromAction<List<Address>> { geocoder?.getFromLocationName(addressName, 1) }
            .handlerThread().subscribe {
                data.value = it
            }
    }

    fun getLastLocationAddress(
        data: MutableLiveData<LocationAddress>,
        provider: String = LocationManager.GPS_PROVIDER
    ) {
        if (context == null) {
            return
        }

        locationManager?.let { manager ->
            if (ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                data.postValue(null)
                return
            }
            LocationManagerCompat.getCurrentLocation(
                manager, provider,
                CancellationSignal(), {
                    it.run()
                }
            ) {
                if (it != null) {
                    getFromLocation(it, data)
                } else if (provider != LocationManager.NETWORK_PROVIDER && provider != LocationManager.PASSIVE_PROVIDER) {
                    getLastLocationAddress(data, LocationManager.NETWORK_PROVIDER)
                } else if (provider != LocationManager.PASSIVE_PROVIDER) {
                    getLastLocationAddress(data, LocationManager.PASSIVE_PROVIDER)
                } else {
                    data.postValue(null)
                }
            }
        }
    }


}