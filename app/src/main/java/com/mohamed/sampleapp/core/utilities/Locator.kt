package com.mohamed.sampleapp.core.utilities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes.RESOLUTION_REQUIRED
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.location.LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE

object Locator {

  private const val TAG = "Locator"
  const val REQUEST_CHECK_SETTINGS = 1000
  const val REQUEST_CANCEL_SETTINGS = 2000
  private const val UPDATE_INTERVAL = 3000L
  private const val FASTEST_INTERVAL = UPDATE_INTERVAL / 2
  private const val SMALLEST_DISPLACEMENT = 200F
  private lateinit var activity: AppCompatActivity
  private lateinit var locationProviderClient: FusedLocationProviderClient
  private val lastLocation = MutableLiveData<Location>()
  private var locationCallbackAttached = false

  private val locationRequest by lazy {
    com.google.android.gms.location.LocationRequest().apply {
      priority = PRIORITY_HIGH_ACCURACY
      interval = UPDATE_INTERVAL
      fastestInterval = FASTEST_INTERVAL
      smallestDisplacement = SMALLEST_DISPLACEMENT
    }
  }

  private val locationSettingsRequestBuilder by lazy {
    LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setAlwaysShow(true).build()
  }

  private val locationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
      lastLocation.value = locationResult.lastLocation
    }
  }

  /**
   * Determine as precise a location as possible from the available location providers, GPS as well as WiFi
   * and mobile cell data. if the caller has location permission, otherwise it will ask user for permission.
   *
   * @param activity The target activity.
   * @param requireLocationSettings Indicates whether location settings is required or not.
   * @param onSuccess The call back to receive the location.
   */
  @SuppressLint("MissingPermission")
  fun getCurrentLocation(@NonNull activity: AppCompatActivity,
                         requireLocationSettings: Boolean,
                         onSuccess: (location: Location) -> Unit) {
        Log.e("dialog_incoming", "it")
        if (requireLocationSettings) checkLocationSettings(activity) {
          Log.e("dialog_incoming", "if")
          getLastLocation(activity, onSuccess)

        }
        else {
          Log.e("dialog_incoming","else")
          getLastLocation(activity, onSuccess)
        }

      }


  /**
   * Listen to location updates.
   * Don't forget to call stopLocationUpdates() when no more required.
   *
   * @param activity The target activity.
   * @param requireLocationSettings Indicates whether location settings is required or not.
   * @param onLocationChanged The call back to receive the updated location.
   */


  /**
   * Checks if location is enabled, if not, opens a dialog asking to open it.
   * Don't forget to check the result in onActivityResult() with request code REQUEST_CHECK_SETTINGS.
   *
   * @param activity The target activity.
   * @param onCheckedCompleted The call back when check completed.
   */
  private fun checkLocationSettings(activity: AppCompatActivity, onCheckedCompleted: () -> Unit) {
    LocationServices.getSettingsClient(activity)
        .checkLocationSettings(locationSettingsRequestBuilder)
        .addOnSuccessListener { onCheckedCompleted.invoke() }
        .addOnFailureListener { exception ->
          when ((exception as ApiException).statusCode) {
            RESOLUTION_REQUIRED -> try {
              (exception as ResolvableApiException).startResolutionForResult(activity, REQUEST_CANCEL_SETTINGS)
            } catch (sendEx: IntentSender.SendIntentException) {
              // Ignore the error.
            }
            SETTINGS_CHANGE_UNAVAILABLE -> {
              Log.e(
                TAG, "Location settings are not satisfied. " +
                  "However, we have no way to fix the settings. (من الأخر الجهاز مفيش فيه GPS)")
              onCheckedCompleted.invoke()
            }
          }
        }
  }

  /**
   * Stops location updates when no more required.
   */
  fun stopLocationUpdates() {
    if (locationCallbackAttached) {
      locationProviderClient.removeLocationUpdates(locationCallback)
      locationCallbackAttached = false
      lastLocation.removeObservers(activity)
    }
  }

  /**
   * Initialize location provider client if it not before.
   *
   * @param activity The target activity.
   */
  private fun initLocationProviderClient(activity: AppCompatActivity) {
    if (Locator::locationProviderClient.isInitialized.not())
      locationProviderClient = getFusedLocationProviderClient(activity)
  }

  @SuppressLint("MissingPermission")
  /**
   * The fused Location Provider will only maintain background location if at least one client is
   * connected to it. But we don't want to launch the Maps app to get last location, and also we can't say
   * our users to launch Maps app to get last location.
   * What we need to do is request location updates once it get location and stop it.
   *
   * @param activity The target activity.
   * @param onLocationReturned The The call back when location returned.
   */
  private fun getLastLocation(activity: AppCompatActivity,
                              onLocationReturned: (location: Location) -> Unit) {
    initLocationProviderClient(activity)
    requestLocationUpdates(activity) {
      onLocationReturned(it)
      stopLocationUpdates()
    }
  }

  /**
   * Listen to location changes.
   *
   * @param activity The target activity.
   * @param onLocationChanged The call back which triggered every time location changed.
   */
  @SuppressLint("MissingPermission")
  private fun requestLocationUpdates(activity: AppCompatActivity,
                                     onLocationChanged: (lastLocation: Location) -> Unit) {
    Locator.activity = activity
    initLocationProviderClient(activity)
    locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    locationCallbackAttached = true
    lastLocation.observe(activity) {
      Log.d(TAG, "LocationUpdates: ${it.latitude}, ${it.longitude}")
      onLocationChanged(it)
    }
  }

  /**
   * Show settings alert dialog.
   *
   * @param activity The target activity.
   */
  private fun showSettingsAlert(@NonNull activity: AppCompatActivity) {
          openSettings(activity)
  }

  /**
   * Navigating user to app settings
   *
   * @param activity The target activity.
   */
  private fun openSettings(@NonNull activity: AppCompatActivity) {
    Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
      data = Uri.fromParts("package", activity.packageName, null)
      activity.startActivity(this)
    }
  }
}