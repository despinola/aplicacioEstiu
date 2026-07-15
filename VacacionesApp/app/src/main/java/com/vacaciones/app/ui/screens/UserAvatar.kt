package com.vacaciones.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vacaciones.app.R

fun avatarResId(name: String): Int? = when (name) {
    "Blai" -> R.drawable.cara_blai
    "Rita" -> R.drawable.cara_rita
    "Miriam" -> R.drawable.cara_miriam
    "David" -> R.drawable.cara_david
    else -> null
}

@Composable
fun UserAvatar(
    name: String,
    fallbackEmoji: String,
    size: Dp = 40.dp,
    fallbackFontSize: TextUnit = 24.sp
) {
    val resId = avatarResId(name)
    if (resId != null) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
        )
    } else {
        Text(text = fallbackEmoji, fontSize = fallbackFontSize)
    }
}
