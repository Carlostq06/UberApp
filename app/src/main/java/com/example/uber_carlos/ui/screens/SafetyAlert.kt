package com.example.uber_carlos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.uber_carlos.R
@Composable
fun bckgrndI (onClick: () -> Unit){
    Column(modifier = Modifier.padding(top = 45.dp)) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(208.dp)
        .background(color = Color(0xFFFFE1DF))
        .padding(top = 45.dp)){

        Image(painter = painterResource(id = R.drawable.imageotp),
            contentDescription = "hola",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(249.dp)
                .height(177.dp)
                .align(alignment = Alignment.Center)
            )
        Button(
            onClick = {onClick()},
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(8.dp),
            modifier =Modifier
                .background(color = Color.Transparent)
                .align(Alignment.TopStart)
                .offset(x = (-5).dp, y = (-45).dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent)
            ) {
            Image(
                painter = painterResource(id = R.drawable.vector_x),
                contentDescription = "image description",
                contentScale = ContentScale.None,
            )
        }

    }
    }
}

@Composable
fun Texts(){
    Column(modifier = Modifier.padding(top = 253.dp)){
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(572.dp)
            .padding(top = 45.dp))
        {
            Text(
                text = "Uber’s Comunity Guidlines",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 19.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000)),
                modifier = Modifier.padding(start = 16.dp).offset(y = (-25).dp)
            )
            Text(
                text = "Safety and respect for all",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000)),
                modifier = Modifier.padding(start = 16.dp).offset(y = (8).dp)
            )
            Text(
                text = "We’re commited, along with millions of riders and drivers, to:",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF000000)),
                modifier = Modifier.padding(start = 16.dp).offset(y = (60).dp).width(340.dp)
            )
            Text(
                text = "Treat everyone with kindness and respect",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000)),
                modifier = Modifier.padding(start = 48.dp).offset(y = 130.dp).width(320.dp))

            Text(
                text = "Help keep one another safe",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000)),
                modifier = Modifier.padding(start = 48.dp).offset(y = 200.dp).width(320.dp))

            Text(
                text = "Follow the law",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000)),
                modifier = Modifier.padding(start = 48.dp).offset(y = 260.dp).width(320.dp))

            Text(
                text = "Everyone who uses Uber apps is expected to follow these guidlines.",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 22.93.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF000000)),
                modifier = Modifier.padding(start = 16.dp).offset(y = (325).dp).width(380.dp)
            )

            Text(
                text = "You can read about our Community Guidlines here",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.93.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFF000000)),
                modifier = Modifier.padding(start = 16.dp).offset(y = (390).dp).width(380.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.vectorcheck),
                contentDescription = "image description",
                contentScale = ContentScale.None,
                modifier = Modifier.padding(18.dp).offset(y= 130.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.vectorcheck),
                contentDescription = "image description",
                contentScale = ContentScale.None,
                modifier = Modifier.padding(18.dp).offset(y= 190.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.vectorcheck),
                contentDescription = "image description",
                contentScale = ContentScale.None,
                modifier = Modifier.padding(18.dp).offset(y= 250.dp)
            )
        }
    }
}
@Composable
fun SABttn1 (onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = 825.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {Button(onClick = { onClick() },shape = RectangleShape, modifier = Modifier
        .width(343.dp)
        .height(49.dp)
        .background( color = Color.Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()){

            Text(
                text = "I understand",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(fontSize = 17.sp, color = Color.White)
            )


        }
    }}

}

@Composable
fun SafetyAndRespect(onNavigateToHome: () -> Unit){
    bckgrndI(onClick = onNavigateToHome)
    Texts()
    SABttn1(onClick = onNavigateToHome)
}