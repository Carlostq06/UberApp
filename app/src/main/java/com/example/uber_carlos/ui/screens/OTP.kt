package com.example.uber_carlos.ui.screens

import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber_carlos.R
import com.example.uber_carlos.viewmodel.AuthState
import com.example.uber_carlos.viewmodel.AuthViewModel

@Composable
fun TextOTP(phoneNumber: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 58.dp, start = 16.dp),
    ) {
        Text(
            text = "Enter the 4-digit code sent you at $phoneNumber",
            style = TextStyle(
                fontSize =20.sp,
                lineHeight = 23.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF000000),
            ),
            modifier = Modifier
                .width(274.dp)
                .height(46.dp)
        )
    }
}

@Composable
fun Squares_OTP(onCodeChange: (String) -> Unit) {
    var d1 by remember { mutableStateOf("") }
    var d2 by remember { mutableStateOf("") }
    var d3 by remember { mutableStateOf("") }
    var d4 by remember { mutableStateOf("") }
    var d5 by remember { mutableStateOf("") }
    var d6 by remember { mutableStateOf("") }

    val f1 = remember { FocusRequester() }
    val f2 = remember { FocusRequester() }
    val f3 = remember { FocusRequester() }
    val f4 = remember { FocusRequester() }
    val f5 = remember { FocusRequester() }
    val f6 = remember { FocusRequester() }

    LaunchedEffect(d1, d2, d3, d4, d5, d6) {
        onCodeChange(d1 + d2 + d3 + d4 + d5 + d6)
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        modifier = Modifier.padding(top = 120.dp, start = 16.dp, end = 16.dp)
    ) {
        Codebox(value = d1, modifier = Modifier.focusRequester(f1).weight(1f)) {
            if (it.length <= 1) {
                d1 = it
                if (it.isNotEmpty()) f2.requestFocus()
            }
        }
        Codebox(value = d2, modifier = Modifier.focusRequester(f2).weight(1f)) {
            if (it.length <= 1) {
                d2 = it
                if (it.isNotEmpty()) f3.requestFocus()
                else if (it.isEmpty()) f1.requestFocus()
            }
        }
        Codebox(value = d3, modifier = Modifier.focusRequester(f3).weight(1f)) {
            if (it.length <= 1) {
                d3 = it
                if (it.isNotEmpty()) f4.requestFocus()
                else if (it.isEmpty()) f2.requestFocus()
            }
        }
        Codebox(value = d4, modifier = Modifier.focusRequester(f4).weight(1f)) {
            if (it.length <= 1) {
                d4 = it
                if (it.isNotEmpty()) f5.requestFocus()
                else if (it.isEmpty()) f3.requestFocus()
            }
        }
        Codebox(value = d5, modifier = Modifier.focusRequester(f5).weight(1f)) {
            if (it.length <= 1) {
                d5 = it
                if (it.isNotEmpty()) f6.requestFocus()
                else if (it.isEmpty()) f4.requestFocus()
            }
        }
        Codebox(value = d6, modifier = Modifier.focusRequester(f6).weight(1f)) {
            if (it.length <= 1) {
                d6 = it
                if (it.isEmpty()) f5.requestFocus()
            }
        }
    }
}
@Composable
fun Codebox(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .width(54.dp)
            .height(54.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF5F5F5),
            unfocusedContainerColor = Color(0xFFEEEEEE),
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        )
    )
}

@Composable
fun Putbttns(onClick: () -> Unit){
    Column(
        modifier = Modifier.padding(top =203.dp, start = 16.dp)
    ) {
       Button(
           onClick = { onClick() },
           modifier= Modifier
               .width(185.dp)
               .height(50.dp)
               .background(color = Color(0xFFEEEEEE), shape = RoundedCornerShape(size = 25.dp)),
           colors = ButtonDefaults.buttonColors(
               containerColor = Color(0xFFEEEEEE),
               contentColor = Color.Black
           )
       ){
           Text(
               text = "I didn’t recive a code",
               maxLines = 1,
               textAlign = TextAlign.Center,
               modifier = Modifier.fillMaxWidth(),
               style = TextStyle(
                   fontSize = 15.sp,
                   fontWeight = FontWeight(500),
                   color = Color(0xFF000000),
                   )
           )
       }
    }
}
@Composable
fun Putbttns2(onClick: () -> Unit) {
    Column(
        modifier = Modifier.padding(top = 263.dp, start = 16.dp)
            .fillMaxWidth()
    ) {
        Button(
            onClick = { onClick() },
            modifier= Modifier
                .width(195.dp)
                .height(50.dp)
                .background(color = Color(0xFFEEEEEE), shape = RoundedCornerShape(size = 25.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEEEEEE),
                contentColor = Color.Black
            )
        ){
            Text(
                text = "Login with password",
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000),
                )
            )
        }
    }
}

@Composable
fun IconPbttn(onClick: () -> Unit) {
    Column(modifier = Modifier.padding(top = 770.dp, start = 15.dp)
        .fillMaxWidth()) {
    LargeFloatingActionButton(
        onClick = { onClick() },
        shape = CircleShape,
        modifier = Modifier
            .padding(1.dp)
            .width(60.dp)
            .height(60.dp)
            .background(color = Color.Transparent),
        containerColor = Color(0xFFEEEEEE),
        elevation = FloatingActionButtonDefaults.elevation(0.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.flechaizq),
            contentDescription = null,
            modifier = Modifier
                .size(16.dp)

        )
    }
    }
}

@Composable
fun IconPbttn2(onClick: () -> Unit) {
    Column(
        modifier = Modifier.padding(top = 770.dp, end = 15.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.End
        ) {

        LargeFloatingActionButton(
            onClick = { onClick() },
            shape = CircleShape,
            modifier = Modifier
                .padding(1.dp)
                .width(96.dp)
                .height(60.dp)
                .background(color = Color.Transparent),
            containerColor = Color(0xFFEEEEEE),
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()){

            Text(
                text = "Next",
                modifier = Modifier.align(Alignment.Center).offset(x = (-10).dp),

                style = TextStyle(fontSize = 17.sp, color = Color(0x96979797))
            )

            Image(
                painter = painterResource(id = R.drawable.derecha),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterEnd).offset(x = (-20).dp),


            )

        }
        }
    }
}

@Composable
fun ScreenCode(
    viewModel: AuthViewModel,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val state = viewModel.authState
    var currentCode by remember { mutableStateOf("") }

    // el estado  cambia para al Home
    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) {
            onSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        TextOTP(viewModel.phoneNumber)

        Squares_OTP(onCodeChange = { code ->
            currentCode = code
        })

        Putbttns(onClick = {  }) // reenviar
        Putbttns2(onClick = { }) // login con contraseña

        IconPbttn(onClick = {
            viewModel.resetAuthState() // Reseteamos el estado para evitar el bucle de navegación
            onBack()
        })

        IconPbttn2(onClick = {
            if (currentCode.length == 6) {
                viewModel.verifyOtp(currentCode)

            }
        })

        // Indicador de carga si Firebase está validando
        if (state is AuthState.Loading) {
            androidx.compose.material3.LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                color = Color.Black
            )
        }

        if (state is AuthState.Error) {
            androidx.compose.material3.Text(
                text = "Invalid code, try again",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center).padding(top = 180.dp)
            )
        }
    }
}
