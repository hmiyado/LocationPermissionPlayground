package com.hmiyado.locationpermissionplayground

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import com.hmiyado.locationpermissionplayground.ui.theme.LocationPermissionPlaygroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocationPermissionPlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RequestPermissions()
                }
            }
        }
    }

    companion object {
        const val requestCode = 1
    }
}

@Composable
fun RequestPermissions() {
    val activity = LocalContext.current.getActivity()!!
    Column {
        Text(text = buildString {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val shouldRequestForBackgroundLocation =
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        ACCESS_BACKGROUND_LOCATION
                    )
                this.append("shouldRequestForBackgroundLocation: $shouldRequestForBackgroundLocation")
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                this.append("\n")
                this.append("backgroundPermissionOptionLabel: ${activity.packageManager.backgroundPermissionOptionLabel}")
            }
        })
        RequestPermissionButton(arrayOf(ACCESS_FINE_LOCATION))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            RequestPermissionButton(arrayOf(ACCESS_BACKGROUND_LOCATION))
            RequestPermissionButton(arrayOf(ACCESS_BACKGROUND_LOCATION, ACCESS_FINE_LOCATION))
        }
    }
}

@Composable
fun RequestPermissionButton(
    permissions: Array<String>,
) {
    val activity = LocalContext.current.getActivity()!!
    Button(onClick = {
        requestPermissions(activity, permissions, MainActivity.requestCode)
    }) {
        Text(text = permissions.joinToString { it.split(".").last() })
    }
    Spacer(modifier = Modifier.padding(8.dp))
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LocationPermissionPlaygroundTheme {
        RequestPermissions()
    }
}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper    -> baseContext.getActivity()
    else                 -> null
}