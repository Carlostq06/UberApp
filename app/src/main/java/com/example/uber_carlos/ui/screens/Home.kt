package com.example.uber_carlos.ui.screens
import kotlinx.coroutines.launch

import com.google.android.gms.maps.CameraUpdateFactory
import androidx.compose.material3.Icon
import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber_carlos.R
import com.example.uber_carlos.ui.components.ScheduleRideBottomSheet
import com.example.uber_carlos.viewmodel.AuthViewModel
import com.example.uber_carlos.viewmodel.RideViewModel
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

    @Composable
    fun Home(
        authViewModel: AuthViewModel,
        rideViewModel: RideViewModel,
        onLogout: () -> Unit,
        onNavigateToMapSelection: () -> Unit,
        onNavigateToProfile: () -> Unit
    ) {
        var showSheet by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateToProfile,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFEDEDED), CircleShape)
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.Black)
                }

                Text(
                    text = "Logout",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    ),
                    modifier = Modifier.clickable {
                        authViewModel.logout()
                        onLogout()
                    }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
            RectangleHome()
            Spacer(modifier = Modifier.height(19.dp))

            WhereSection(
                onNowClick = { showSheet = true },
                onWhereToClick = onNavigateToMapSelection
            )

            Spacer(modifier = Modifier.height(30.dp))

            DestinationList(onDestinationClick = onNavigateToMapSelection)

            Spacer(modifier = Modifier.height(30.dp))

            AroundYouSection()

        }

        if (showSheet) {
            ScheduleRideBottomSheet(
                onDismiss = { showSheet = false },
                onSetPickupTime = { date, time ->
                    rideViewModel.setSchedule(date, time) // Guardamos la fecha/hora en el ViewModel
                    showSheet = false
                    onNavigateToMapSelection()
                }
            )
        }
    }

@Composable
fun RectangleHome() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(135.dp)
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(12.dp))
            .background(color = Color(0xFF10462E), shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Satisfy any craving",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.uber_move_text)),
                        fontWeight = FontWeight(500),
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Order on Eats",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.uber_move_text)),
                            fontWeight = FontWeight(500),
                            color = Color.White
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(start = 4.dp)
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.fondo_home),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(130.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun WhereSection(onNowClick: () -> Unit, onWhereToClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(54.dp)
            .background(color = Color(0xFFEDEDED))
            .clickable { onWhereToClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Where To?",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.uber_move_text)),
                    fontWeight = FontWeight(800),
                    color = Color(0xFF000000),
                )
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.linear),
                    contentDescription = "línea divisoria",
                    modifier = Modifier
                        .width(3.dp)
                        .height(30.dp),
                    contentScale = ContentScale.FillBounds
                )

                Spacer(modifier = Modifier.width(12.dp))

                NowButton(onClick = onNowClick)
            }
        }
    }
}

@Composable
fun NowButton(onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .width(105.dp)
            .height(34.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.svgexport_1_1),
            contentDescription = "image description",
            modifier = Modifier.padding(3.dp),
            contentScale = ContentScale.None
        )
        Text(
            text = "Now",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.uber_move_text)),
                fontWeight = FontWeight(500),
                color = Color(0xFF000000),
            )
        )
        Image(
            painter = painterResource(id = R.drawable.flechaabajo),
            contentDescription = "image description",
            modifier = Modifier.padding(15.dp),
            contentScale = ContentScale.None
        )

    }
}

@Composable
fun DestinationList(onDestinationClick: () -> Unit) {
    val items = listOf(
        Pair(R.drawable.star_black_24dp_1, "Choose a saved place"),
        Pair(R.drawable.vector__1_, "Set destination on map")
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        items.forEach { item ->
            val (vectorRes, title) = item

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDestinationClick() }
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp)
                        .height(63.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFEDEDED), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = vectorRes),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = title,
                        modifier = Modifier.weight(1f),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.uber_move_text)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFF000000),
                        )
                    )

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.LightGray
                    )
                }

                HorizontalDivider(
                    thickness = 1.5.dp,
                    color = Color(0xFFEEEEEE),
                    modifier = Modifier.padding(start = 56.dp)
                )
            }
        }
    }
}

@OptIn(com.google.accompanist.permissions.ExperimentalPermissionsApi::class)
@Composable
fun AroundYouSection() {
    val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(locationPermission.status.isGranted) {
        if (locationPermission.status.isGranted) {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedClient.lastLocation.addOnSuccessListener { loc ->
                    loc?.let { userLocation = LatLng(it.latitude, it.longitude) }
                }
            } catch (e: SecurityException) {
            }
        }
    }

    val mapProperties by remember(locationPermission.status.isGranted) {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = locationPermission.status.isGranted, // Habilita la geolocalización en el mapa punto azul
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            )
        )
    }

    val defaultLocation = LatLng(-12.0463, -77.0427)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation ?: defaultLocation, 15f)
    }
    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 15f))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Around you",
            modifier = Modifier
                .width(343.dp)
                .padding(vertical = 12.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.uber_move_text)),
                fontWeight = FontWeight(600),
                color = Color.Black,
            )
        )

        Box(
            modifier = Modifier
                .width(343.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFEDEDED))
        ) {
            if (locationPermission.status.isGranted) {
                // MAPA DE FONDO
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = mapProperties,
                    uiSettings = MapUiSettings(
                        myLocationButtonEnabled = false,
                        zoomControlsEnabled = false,
                        mapToolbarEnabled = false
                    )
                )

                FloatingActionButton(
                    onClick = {
                        userLocation?.let { loc ->
                            coroutineScope.launch {
                                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(loc, 15f)) //.move mas brusco instantaneo
                            }
                        }
                    },/*¿Por qué la cámara del mapa necesita este "ayudante"?
                        Mover la cámara no es un cambio de color instantáneo; es un proceso físico que lleva tiempo:
                        Cálculo: Calcular la ruta desde donde está el mapa ahora hasta tu ubicación.
                        Interpolación: Dibujar los fotogramas intermedios para que se vea fluido (60 imágenes por segundo).
                        Carga: Ir descargando los trozos de mapa (tiles) nuevos que van apareciendo por el camino.*/
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .size(30.dp),
                    containerColor = Color.White,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.current_location_icon),
                        contentDescription = "Centrar en mi ubicación",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }

            } else {
                Button(
                    onClick = { locationPermission.launchPermissionRequest() },
                    modifier = Modifier.align(Alignment.Center),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Enable Map Location")
                }
            }
        }
    }
}
