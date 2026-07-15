package com.vacaciones.app.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import java.io.File

@Composable
fun PhotoSection(
    photoUri: String,
    userName: String,
    day: Int,
    onPhotoSaved: (String) -> Unit
) {
    val context = LocalContext.current
    var tempUri by remember { mutableStateOf<Uri?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempUri?.let { uri -> onPhotoSaved(uri.toString()) }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onPhotoSaved(it.toString()) }
    }

    var showSourceDialog by remember { mutableStateOf(false) }

    if (showSourceDialog) {
        AlertDialog(
            onDismissRequest = { showSourceDialog = false },
            title = { Text("Afegir foto") },
            text = { Text("D'on vols agafar la foto?") },
            confirmButton = {
                TextButton(onClick = {
                    showSourceDialog = false
                    val file = createPhotoFile(context, userName, day)
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                    tempUri = uri
                    cameraLauncher.launch(uri)
                }) {
                    Text("📷 Càmera")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSourceDialog = false
                    galleryLauncher.launch("image/*")
                }) {
                    Text("🖼️ Galeria")
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar foto") },
            text = { Text("Segur que vols eliminar la foto d'avui?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onPhotoSaved("")
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel·lar")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "📷 Foto del dia",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (photoUri.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = Uri.parse(photoUri),
                    contentDescription = "Foto del dia $day",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { showSourceDialog = true }
                )
                // Delete button
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar foto",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                // Change photo button
                IconButton(
                    onClick = { showSourceDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(
                        Icons.Default.AddAPhoto,
                        contentDescription = "Canviar foto",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { showSourceDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.AddAPhoto,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Afegeix una foto del dia",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Toca per obrir la càmera o la galeria",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun createPhotoFile(context: Context, userName: String, day: Int): File {
    val dir = File(context.getExternalFilesDir("Pictures"), "vacances")
    dir.mkdirs()
    return File(dir, "foto_${userName}_dia${day}_${System.currentTimeMillis()}.jpg")
}
