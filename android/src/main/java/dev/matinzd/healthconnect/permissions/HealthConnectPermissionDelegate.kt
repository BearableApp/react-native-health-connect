package dev.matinzd.healthconnect.permissions

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.health.connect.client.PermissionController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

object HealthConnectPermissionDelegate {
  private val coroutineScope = CoroutineScope(Dispatchers.IO)
  private val permissionsChannel = Channel<Set<String>>()

  private lateinit var requestPermission: ActivityResultLauncher<Set<String>>

  fun setPermissionDelegate(
    activity: ComponentActivity,
    providerPackageName: String = "com.google.android.apps.healthdata"
  ) {
    val contract = PermissionController.createRequestPermissionResultContract(providerPackageName)

    requestPermission = activity.registerForActivityResult(contract) {
      coroutineScope.launch {
        permissionsChannel.send(it)
        coroutineContext.cancel()
      }
    }
  }

  suspend fun launchPermissionsDialog(permissions: Set<String>): Set<String> {
    requestPermission.launch(permissions)
    return permissionsChannel.receive()
  }
}
