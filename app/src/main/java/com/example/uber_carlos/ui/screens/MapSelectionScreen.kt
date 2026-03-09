package com.example.uber_carlos.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uber_carlos.R
import com.example.uber_carlos.network.DirectionsClient
import com.example.uber_carlos.ui.components.ScheduleRideBottomSheet
import com.example.uber_carlos.viewmodel.PaymentViewModel
import com.example.uber_carlos.viewmodel.RideState
import com.example.uber_carlos.viewmodel.RideViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.util.Locale

@SuppressLint("MissingPermission") // "exención de responsabilidad" o un "salvoconducto" que le das al compilador de Android. Anotación de Supresión de Lint
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapSelectionScreen(
    onBack: () -> Unit,
    onDone: () -> Unit,
    rideViewModel: RideViewModel = viewModel(),
    paymentViewModel: PaymentViewModel = viewModel()
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val placesClient = remember { Places.createClient(context) } // Instancia de PlacesClient para tener las predicciones de lugares

    var showScheduleSheet by remember { mutableStateOf(false) }
    var selectedVehicle by remember { mutableStateOf("UberX") }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    var destinationText by remember { mutableStateOf("") }
    var destinationLatLng by remember { mutableStateOf<LatLng?>(null) }
    var polylinePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var predictions by remember { mutableStateOf<List<com.google.android.libraries.places.api.model.AutocompletePrediction>>(emptyList()) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0463, -77.0427), 15f)
    }

    val state = rideViewModel.rideState

    LaunchedEffect(destinationText) {
        if (destinationText.length > 1) {
            val requestBuilder = FindAutocompletePredictionsRequest.builder().setQuery(destinationText)
            userLocation?.let {
                val bounds = RectangularBounds.newInstance(
                    LatLng(it.latitude - 0.15, it.longitude - 0.15),
                    LatLng(it.latitude + 0.15, it.longitude + 0.15)
                )
                requestBuilder.setLocationRestriction(bounds)
            }
            try {
                val response = placesClient.findAutocompletePredictions(requestBuilder.build()).await()
                predictions = response.autocompletePredictions
            } catch (e: Exception) {
                predictions = emptyList()
            }
        } else predictions = emptyList()
    }

    LaunchedEffect(locationPermission.status.isGranted) {
        if (locationPermission.status.isGranted) {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            fusedClient.lastLocation.addOnSuccessListener { loc ->
                loc?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    userLocation = latLng
                    rideViewModel.updateUserLocation(latLng)
                    scope.launch { cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 15f)) }
                }
            }
        } else locationPermission.launchPermissionRequest()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = locationPermission.status.isGranted,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            ),
            uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
        ) {
            if (polylinePoints.isNotEmpty()) Polyline(points = polylinePoints, color = Color.Black, width = 12f)
            destinationLatLng?.let { Marker(state = MarkerState(position = it)) }
        }

        if (state is RideState.VehicleSelection) {
            IconButton(
                onClick = { rideViewModel.cancelVehicleSelection(); polylinePoints = emptyList(); destinationLatLng = null },
                modifier = Modifier.padding(top = 40.dp, start = 16.dp).background(Color.White, CircleShape).size(44.dp).border(1.dp, Color.LightGray, CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.Black)
            }
        }

        when (state) {
            is RideState.Idle -> {
                BackHandler { onBack() }
                ModalBottomSheet(sheetState = sheetState, onDismissRequest = { onBack() }, containerColor = Color.White) {
                    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.95f).padding(horizontal = 16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Black) }
                            Text("Planifica tu viaje", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                            Spacer(Modifier.width(48.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(modifier = Modifier.fillMaxWidth().border(2.dp, Color.Black, RoundedCornerShape(12.dp)).background(Color.White, RoundedCornerShape(12.dp)).padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(end = 12.dp)) {
                                    Box(modifier = Modifier.size(8.dp).border(2.dp, Color.Black, CircleShape))
                                    Box(modifier = Modifier.width(1.5.dp).height(24.dp).background(Color.Black))
                                    Box(modifier = Modifier.size(8.dp).background(Color.Black))
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Mi ubicación", fontSize = 15.sp, color = Color.Black, maxLines = 1)
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp, color = Color(0xFFEEEEEE))
                                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                        BasicTextField(
                                            value = destinationText,
                                            onValueChange = { destinationText = it },
                                            textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
                                            modifier = Modifier.weight(1f),
                                            decorationBox = { innerTextField -> if (destinationText.isEmpty()) Text("¿A dónde vas?", color = Color.Gray, fontSize = 15.sp); innerTextField() }
                                        )
                                        if (destinationText.isNotEmpty()) {
                                            Icon(imageVector = Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(20.dp).clickable { destinationText = "" }, tint = Color.Black)
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn(modifier = Modifier.fillMaxSize().background(Color.White)) {
                            items(predictions) { prediction ->
                                ListItem(
                                    colors = ListItemDefaults.colors(containerColor = Color.White),
                                    headlineContent = { HighlightedText(prediction.getPrimaryText(null).toString(), destinationText) },
                                    supportingContent = { Text(prediction.getSecondaryText(null).toString(), color = Color.Gray) },
                                    leadingContent = { Icon(Icons.Outlined.LocationOn, null, tint = Color.Black) },
                                    modifier = Modifier.clickable {
                                        val primary = prediction.getPrimaryText(null).toString()
                                        destinationText = primary
                                        val request = FetchPlaceRequest.newInstance(prediction.placeId, listOf(Place.Field.LAT_LNG))
                                        placesClient.fetchPlace(request).addOnSuccessListener { resp ->
                                            resp.place.latLng?.let { dest ->
                                                destinationLatLng = dest
                                                rideViewModel.setDestination(dest, primary)
                                                userLocation?.let { origin ->
                                                    scope.launch {
                                                        try {
                                                            val apiKey = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData.getString("com.google.android.geo.API_KEY") ?: ""

                                                            val response = DirectionsClient.api.getDirections(
                                                                origin = "${origin.latitude},${origin.longitude}",
                                                                destination = "${dest.latitude},${dest.longitude}",
                                                                key = apiKey
                                                            )

                                                            if (response.status == "OK" && response.routes.isNotEmpty()) {
                                                                val route = response.routes[0]
                                                                // Decodificamos los puntos con el cliente centralizado
                                                                polylinePoints = DirectionsClient.decodePolyline(route.overview_polyline.points)

                                                                // Zoom automático para ver origen y destino
                                                                val bounds = LatLngBounds.Builder().include(origin).include(dest).build()
                                                                cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(bounds, 200))
                                                            }
                                                        } catch (e: Exception) { e.printStackTrace() }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            is RideState.VehicleSelection -> {
                BackHandler { rideViewModel.cancelVehicleSelection() }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    Surface(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        color = Color.White,
                        shadowElevation = 16.dp
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                            Box(modifier = Modifier.padding(vertical = 12.dp).width(40.dp).height(4.dp).background(Color.LightGray, CircleShape).align(Alignment.CenterHorizontally))
                            Text("Viajes recomendados", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontWeight = FontWeight.Bold, color = Color.Black)
                            val basePrice = rideViewModel.estimatedPrice
                            VehicleItem(
                                name = "UberX",
                                description = "Viajes asequibles y rápidos para el día a día",
                                price = String.format(Locale.getDefault(), "%.2f €", basePrice),
                                oldPrice = String.format(Locale.getDefault(), "%.2f €", basePrice * 1.2),
                                time = rideViewModel.estimatedUberXTime,
                                iconPainter = painterResource(R.drawable.car),
                                isSelected = selectedVehicle == "UberX"
                            ) { selectedVehicle = "UberX" }
                            VehicleItem(
                                name = "Uber Black",
                                description = "Viajes con coches de alta gama",
                                price = String.format(Locale.getDefault(), "%.2f €", basePrice * 1.5),
                                oldPrice = String.format(Locale.getDefault(), "%.2f €", basePrice * 1.8),
                                time = rideViewModel.estimatedUberBlackTime,
                                iconPainter = painterResource(R.drawable.car_black),
                                isSelected = selectedVehicle == "Uber Black"
                            ) { selectedVehicle = "Uber Black" }
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Button(onClick = { rideViewModel.requestRide() }, modifier = Modifier.weight(1f).height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Black), shape = RoundedCornerShape(8.dp)) {
                                    Text("Viajar ahora", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(Modifier.width(12.dp))
                                Surface(
                                    onClick = { showScheduleSheet = true },
                                    modifier = Modifier.size(56.dp),
                                    color = Color.Black,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Programar", tint = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Los demás estados (Searching, InProgress, etc.) se mantienen igual
            is RideState.Searching -> {
                Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color.Black)
                        Spacer(Modifier.height(16.dp))
                        Text("Buscando conductor...", fontWeight = FontWeight.Bold)
                    }
                }
            }
            is RideState.DriverAssigned, is RideState.InProgress -> {
                TripInProgressScreen(rideViewModel = rideViewModel)
            }
            is RideState.Completed -> {
                val finalPrice = if (selectedVehicle == "UberX") rideViewModel.estimatedPrice else rideViewModel.estimatedPrice * 1.5
                PaymentScreen(
                    paymentVm = paymentViewModel,
                    ridePrice = finalPrice,
                    rideSummary = "Viaje en $selectedVehicle a ${rideViewModel.destinationName}",
                    onPaymentOk = {
                        rideViewModel.resetRide()
                        paymentViewModel.resetPayment()
                        onDone()
                    },
                    onBack = {
                        rideViewModel.cancelVehicleSelection()
                        rideViewModel.resetRide()
                    }
                )
            }
            else -> {}
        }

        if (showScheduleSheet) {
            ScheduleRideBottomSheet(
                onDismiss = { showScheduleSheet = false },
                onSetPickupTime = { date, time ->
                    rideViewModel.setSchedule(date, time)
                    showScheduleSheet = false
                }
            )
        }
    }
}

@Composable
fun VehicleItem(
    name: String,
    description: String,
    price: String,
    oldPrice: String,
    time: String,
    iconPainter: androidx.compose.ui.graphics.painter.Painter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .border(
                if (isSelected) 2.dp else 0.dp,
                if (isSelected) Color.Black else Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = iconPainter, contentDescription = name, modifier = Modifier.size(60.dp))
        Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Text(text = time, fontSize = 14.sp, color = Color.Gray)
            Text(text = description, fontSize = 12.sp, color = Color.Gray, lineHeight = 14.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = price, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Text(text = oldPrice, fontSize = 14.sp, color = Color.Gray, style = TextStyle(textDecoration = TextDecoration.LineThrough))
        }
    }
}

@Composable
fun HighlightedText(text: String, query: String) {
    val startIndex = text.indexOf(query, ignoreCase = true)
    val annotatedString = buildAnnotatedString {
        if (startIndex >= 0 && query.isNotEmpty()) {
            append(text.take(startIndex))
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(text.substring(startIndex, startIndex + query.length)) }
            append(text.substring(startIndex + query.length))
        } else append(text)
    }
    Text(text = annotatedString, fontSize = 15.sp, color = Color.Black)
}
