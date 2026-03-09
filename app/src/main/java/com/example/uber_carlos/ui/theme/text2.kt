package com.example.uber_carlos.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber_carlos.R

@Composable
fun text2(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .width(193.dp)
            .height(19.dp)
    ) {
        Text(
            text = "Enter your mobile number",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight(700),
                fontFamily = FontFamily(Font(R.font.uber_move_text)),
                color = Color(0xFF000000),
            )
        )
    }
}