package com.example.uber_carlos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber_carlos.R

@Composable
fun FilledButtonExample(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(383.dp)
            .height(49.dp),
        shape = RoundedCornerShape(2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White,
            disabledContainerColor = Color.DarkGray
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 4.dp
        ),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500)
                )
            )

            Image(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterEnd)
            )
        }
    }
}
