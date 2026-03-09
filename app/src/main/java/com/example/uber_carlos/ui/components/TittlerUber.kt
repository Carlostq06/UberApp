package com.example.uber_carlos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.uber_carlos.R


@Composable
fun Tittle() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 97.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.uber),
            contentDescription = "Uber Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(79.dp)
                .height(26.dp)
        )
    }
}