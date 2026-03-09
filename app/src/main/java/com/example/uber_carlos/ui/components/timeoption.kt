package com.example.uber_carlos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber_carlos.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleRideBottomSheet(
    onDismiss: () -> Unit,
    onSetPickupTime: (String, String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val is24Hour = android.text.format.DateFormat.is24HourFormat(context)

    val formatterTime = remember(is24Hour) {
        SimpleDateFormat(if (is24Hour) "HH:mm" else "hh:mm a", Locale.getDefault()) //  meomizacion
    }
    val formatterDate = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // locale Locale.getDefault() pregunta por el idioma y region
    }

    val initialDate = formatterDate.format(calendar.time)
    val initialTime = formatterTime.format(calendar.time)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedDate by remember { mutableStateOf(initialDate) }
    var showDatePicker by remember { mutableStateOf(false) } //mostrar el dial de fecha
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis // cuando se abara muestre la fecha actual
    )

    var selectedTime by remember { mutableStateOf(initialTime) }
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = is24Hour
    )

    // --- TU DISEÑO ORIGINAL INTACTO ---
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RectangleShape,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Schedule a Ride",
                style = TextStyle(
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.uber_move_text)),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                ),
                modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)
            )

            HorizontalDivider(color = Color(0xFFEEEEEE))

            // Selector de Fecha
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedDate,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            HorizontalDivider(Modifier.width(300.dp), color = Color(0xFFEEEEEE))

            // Selector de Hora
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTimePicker = true }
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedTime,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            HorizontalDivider(color = Color(0xFFEEEEEE))

            Button(
                onClick = { onSetPickupTime(selectedDate, selectedTime) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(54.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Confirmar hora", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = formatterDate.format(Date(millis))
                    }
                    showDatePicker = false
                }) {
                    Text("ACEPTAR", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("CANCELAR", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            colors = DatePickerDefaults.colors(containerColor = Color.White)
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    headlineContentColor = Color.Black,
                    weekdayContentColor = Color.Gray,
                    subheadContentColor = Color.Black,
                    yearContentColor = Color.Black,
                    currentYearContentColor = Color.Black,
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = Color.Black,
                    dayContentColor = Color.Black,
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = Color.Black,
                    todayContentColor = Color.Black,
                    todayDateBorderColor = Color.Black,

                    navigationContentColor = Color.DarkGray,
                    dateTextFieldColors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,        // Borde al escribir: Negro
                        unfocusedBorderColor = Color.Gray,       // Borde sin tocar: Gris
                        focusedLabelColor = Color.Black,         // Texto flotante al escribir: Negro
                        unfocusedLabelColor = Color.Gray,        // Texto flotante sin tocar: Gris
                        cursorColor = Color.Black,               // El palito que parpadea: Negro
                        focusedTextColor = Color.Black,          // Texto ingresado: Negro
                        unfocusedTextColor = Color.Black         // Texto ingresado sin foco: Negro
                    )
                )
            )
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val cal = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        set(Calendar.MINUTE, timePickerState.minute)
                    }
                    selectedTime = formatterTime.format(cal.time)
                    showTimePicker = false
                }) {
                    Text("ACEPTAR", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("CANCELAR", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color.White,
            title = { Text("Elegir hora", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            text = {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        // 1. Fondo circular del reloj
                        clockDialColor = Color(0xFFF3F3F3),

                        // 2. La manecilla y el círculo de selección
                        selectorColor = Color.Black,

                        clockDialUnselectedContentColor = Color.Black,
                        clockDialSelectedContentColor = Color.White,

                        // 4. Las cajas cuadradas de arriba (Hora : Minutos)
                        timeSelectorSelectedContainerColor = Color.Black,
                        timeSelectorSelectedContentColor = Color.White,
                        timeSelectorUnselectedContainerColor = Color(0xFFF3F3F3),
                        timeSelectorUnselectedContentColor = Color.Black
                    )
                )
            }
        )
    }
}