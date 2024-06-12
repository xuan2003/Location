package tw.edu.pu.csim.s1102294.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var locationManager: LocationManager? = null
    lateinit var textViewLocation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        textViewLocation = findViewById(R.id.textViewLocation)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 如果沒有定位權限，請求權限
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // 如果有定位權限，開始取得位置
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // 當位置改變時的處理
                showLocation(location)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }

        // 設定定位參數
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val requestLocationUpdates = locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            1f,
            locationListener
        )
    }

    private fun showLocation(location: Location) {
        // 顯示位置信息
        val latitude = location.latitude
        val longitude = location.longitude

        data class CityRange(val name: String, val minLatitude: Double, val maxLatitude: Double, val minLongitude: Double, val maxLongitude: Double)

        val cityRanges = listOf(
            CityRange("臺北市", 25.0, 25.2, 121.5, 121.6),
            CityRange("新北市", 24.8, 25.3, 121.3, 122.0),
            CityRange("桃園市", 24.7, 25.1, 121.0, 121.4),
            CityRange("臺中市", 24.0, 24.4, 120.5, 121.0),
            CityRange("臺南市", 22.9, 23.1, 120.0, 120.3),
            CityRange("高雄市", 22.5, 23.0, 120.2, 120.5),
            CityRange("基隆市", 25.1, 25.2, 121.7, 121.8),
            CityRange("新竹市", 24.7, 24.9, 120.9, 121.1),
            CityRange("嘉義市", 23.4, 23.5, 120.4, 120.5)
            // 可以根據需要添加更多的縣市範圍
        )

//        textViewLocation.text = "緯度: $latitude, 經度: $longitude"
        val city = cityRanges.find {
            latitude in it.minLatitude..it.maxLatitude && longitude in it.minLongitude..it.maxLongitude
        }?.name ?: "未知地點"

        textViewLocation.text = if (city == "未知地點") {
            "緯度: $latitude, 經度: $longitude"
        } else {
            city
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 如果用戶授予了定位權限，開始取得位置
                startLocationUpdates()
            }
        }
    }
}