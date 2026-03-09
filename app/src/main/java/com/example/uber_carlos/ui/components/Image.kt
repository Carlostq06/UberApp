package com.example.uber_carlos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.uber_carlos.R

@Composable
fun Section() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 84.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.uber2),
            contentDescription = "image",
            modifier = Modifier.size(200.dp)
        )
    }
}
