package ie.koala.weather.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat
import com.fondesa.kpermissions.extension.listeners
import com.fondesa.kpermissions.extension.permissionsBuilder
import ie.koala.weather.MainActivity
import ie.koala.weather.R
import ie.koala.weather.ui.snackbar
import org.jetbrains.anko.alert
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LocationUtils {

    private val log: Logger = LoggerFactory.getLogger(LocationUtils::class.java)

    fun requestLocationUpdates(activity: MainActivity, frequency: Long, accuracy: Float) {
        if (ContextCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // permission is granted

            // get the location manager
            val locationManager: LocationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // select the default location provider
            val provider = locationManager.getBestProvider(Criteria(), false)

            val location = locationManager.getLastKnownLocation(provider)
            if (location != null) {
                // we already have our location
                activity.onLocationChanged(location)
            }

            // start the updates
            locationManager.requestLocationUpdates(provider, frequency, accuracy, activity)
        } else {
            log.error("requestLocationUpdates: don't have permission")
        }
    }

    fun removeLocationUpdates(activity: MainActivity) {
        if (ContextCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // permission is granted
            val locationManager: LocationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.removeUpdates(activity) // remove the location updates
        }
    }

    fun requestPermission(activity: Activity, coordinatorLayout: View) {
        val request = activity.permissionsBuilder(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION).build()
        request.listeners {
            onAccepted { _ ->
                // notified when the permissions are accepted
                // we now have permission to use the location
                coordinatorLayout.snackbar(R.string.message_location_permission_granted)
            }
            onDenied { _ ->
                // notified when the permissions are denied
                coordinatorLayout.snackbar(R.string.message_location_permission_denied)
            }
            onPermanentlyDenied { _ ->
                activity.alert(activity.getString(R.string.message_location_permission_requested)) {
                    titleResource = R.string.title_location_permission
                    positiveButton(R.string.action_settings) {
                        // Open the app's settings.
                        val intent = createAppSettingsIntent(activity)
                        activity.startActivity(intent)
                    }
                    negativeButton(android.R.string.cancel) {}
                }.show()
            }

            onShouldShowRationale { _, nonce ->
                activity.alert(activity.getString(R.string.message_location_permission_requested)) {
                    titleResource = R.string.title_location_permission
                    positiveButton(R.string.button_location_permission_requested_again) {
                        // Send the request again.
                        nonce.use()
                    }
                    negativeButton(android.R.string.cancel) {}
                }.show()
            }
        }
        request.send()
    }

    private fun createAppSettingsIntent(activity: Activity) = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", activity.packageName, null)
    }
}
