package pe.edu.upc.permissionscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import pe.edu.upc.permissionscompose.ui.theme.PermissionsComposeTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionsComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    FeatureThatRequiresCameraPermission()
                }
            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FeatureThatRequiresCameraPermission() {
    var doNotShowRationale by rememberSaveable {
        mutableStateOf(false)
    }

    val cameraPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    val context = LocalContext.current

    PermissionRequired(
        permissionState = cameraPermissionState,
        permissionNotGrantedContent = {
            if (doNotShowRationale) {
                Text("Feature not available")
            } else {
                PermissionNotGrantedUI(
                    onYesClick = {
                        cameraPermissionState.launchPermissionRequest()
                    }, onCancelClick = {
                        doNotShowRationale = true
                    })
            }
        },
        permissionNotAvailableContent = {
            PermissionNotAvailableContent(
                onOpenSettingsClick = { context.openSettings() })
        },
        content = {
            Text("Camera Permission Granted")
        }
    )


}

@Composable
fun PermissionNotAvailableContent(onOpenSettingsClick: () -> Unit) {

    Column {
        Text("Camera permission denied.")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onOpenSettingsClick() }) {
            Text("Open settings")
        }
    }
}


@Composable
fun PermissionNotGrantedUI(onYesClick: () -> Unit, onCancelClick: () -> Unit) {
    Column {
        Text("Camera is important for this app. Please grant ther permission.")
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = {
                onYesClick()
            }) {
                Text("Yes")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                onCancelClick()
            }) {
                Text("Cancel")
            }
        }
    }

}