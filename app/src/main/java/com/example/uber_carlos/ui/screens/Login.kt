package com.example.uber_carlos.ui.screens
import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber_carlos.ui.components.CountrySelectorScreen
import com.example.uber_carlos.ui.components.FilledButtonExample
import com.example.uber_carlos.R
import com.example.uber_carlos.ui.theme.text2
import com.example.uber_carlos.viewmodel.AuthState
import com.example.uber_carlos.viewmodel.AuthViewModel


@Composable
fun LoginScreen(
    onNavigateToCode: () -> Unit, 
    viewModel: AuthViewModel, 
    onNavigateToHome: () -> Unit,
    onNavigateToEmailLogin: () -> Unit
) {
    val activity = LocalActivity.current ?: return
    val state = viewModel.authState

    LaunchedEffect(state) {
        when (state) {
            is AuthState.CodeSent -> onNavigateToCode()
            is AuthState.Authenticated -> {
                onNavigateToHome()
            }
            else -> {}
        }
    }
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(58.dp))
        Box() {
            text2(modifier = Modifier.align(Alignment.CenterStart))
        }
        Spacer(modifier = Modifier.height(16.dp))
        CountrySelectorScreen(viewModel= viewModel)
        Spacer(modifier = Modifier.height(16.dp))

        BotonSiguiente(
            onNavigateToCode = {
                viewModel.sendCode(activity)
            }
        )

        Spacer(modifier = Modifier.height(38.dp))

        TextoInformativo()

        Spacer(modifier = Modifier.height(30.dp))


        SectionOr()
        Spacer(modifier = Modifier.height(30.dp))
        LoginBttnEmail(onClick = onNavigateToEmailLogin)
        Spacer(modifier = Modifier.height(16.dp))

        LoginBttnG(onClick = {
            viewModel.signInWithGoogle(activity)
        })
    }
}



@Composable
fun BotonSiguiente(onNavigateToCode: () -> Unit) {

    Row(
        modifier = Modifier.width(850.dp)
    ) {
        FilledButtonExample(text = "Next", onClick = { onNavigateToCode()})
    }
}

@Composable
fun TextoInformativo() {

    Text(
        text = "By continuing you may receive an SMS for verification. Message and data rates may apply",
        modifier = Modifier.fillMaxWidth(),
        style = TextStyle(
            fontSize = 14.sp,
            lineHeight = 21.46.sp,
            fontWeight = FontWeight(400),
            color = Color(0xFF757575)
        )
    )
}

@Composable
fun SectionOr() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.vector_2),
            contentDescription = null,
            modifier = Modifier.width(172.dp)

        )
        Text(
            text = "or",
            modifier = Modifier.padding(horizontal = 12.dp),
            style = TextStyle(color = Color(0xFF757575), fontSize = 16.sp)
        )

        Image(
            painter = painterResource(id = R.drawable.vector_2),
            contentDescription = null,
            modifier = Modifier.width(172.dp)

        )
    }
}


@Composable
fun LoginBttnEmail (onClick: () -> Unit) {
    Column(
    )
    {Button(onClick = { onClick() }, modifier = Modifier
        .width(393.dp)
        .height(49.dp)
        .border(width = 0.8.dp, color = Color.Black)
        .background( color = Color.White),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        shape = RectangleShape

    ) {
        Box(modifier = Modifier.fillMaxSize()){

            Text(
                text = "Continue with Email",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(fontSize = 17.sp,
                    fontWeight = FontWeight(700)),
                fontFamily = FontFamily(Font(R.font.uber_move_text))
            )

            Image(
                painter = painterResource(id = R.drawable.email), // Asegúrate de tener este icono
                modifier = Modifier
                    .width(23.dp)
                    .height(22.dp)
                    .align(Alignment.CenterStart),
                contentDescription = null
            )

        }
    }}
}
@Composable
fun LoginBttnG (onClick: () -> Unit) {
    Column(

        horizontalAlignment = Alignment.CenterHorizontally
    )
    {Button(onClick = { onClick() }, modifier = Modifier
        .width(393.dp)
        .height(49.dp)
        .border(width = 0.8.dp, color = Color.Black)
        .background( color = Color.White),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        shape = RectangleShape
    ) {
        Box(modifier = Modifier.fillMaxSize()){

            Text(
                text = "Continue with Google",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(fontSize = 17.sp,
                    fontWeight = FontWeight(700)),

                fontFamily = FontFamily(Font(R.font.uber_move_text))
            )

            Image(
                painter = painterResource(id = R.drawable.image_5),
                modifier = Modifier
                    .width(23.dp)
                    .height(22.dp)
                    .align(Alignment.CenterStart),
                contentDescription = null
            )

        }
    }}
}
