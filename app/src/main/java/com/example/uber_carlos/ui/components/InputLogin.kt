package com.example.uber_carlos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber_carlos.viewmodel.AuthViewModel
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.Locale

data class CountryData(
    val isoCode: String,
    val countryCode: String,
    val countryName: String,
    val flagRes: Int
)

@Composable
fun getAllGetCountries(): List<CountryData> {
    val context = LocalContext.current
    val phoneUtil = PhoneNumberUtil.getInstance()

    return remember {
        phoneUtil.supportedRegions.map { isoCode ->
            val locale = Locale("", isoCode)
            val dialCode = "+${phoneUtil.getCountryCodeForRegion(isoCode)}"
            val resourceName = "${isoCode.lowercase()}_flag"

            val resId = context.resources.getIdentifier(
                resourceName,
                "drawable",
                context.packageName
            )

            CountryData(
                isoCode = isoCode,
                countryCode = dialCode,
                countryName = locale.getDisplayCountry(Locale.getDefault()),
                flagRes = if (resId != 0) resId else android.R.drawable.ic_menu_help
            )
        }.sortedBy { it.countryName }
    }
}

@Composable
fun SeparatedPhoneInput(
    selectedCountry: CountryData,
    phoneNumber: String,
    onNumberChange: (String) -> Unit,
    onCountryClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.3f)
                .height(56.dp)
                .background(Color(0xFFF1F1F1), RoundedCornerShape(4.dp))
                .clickable { onCountryClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = selectedCountry.flagRes),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp, 20.dp).clip(RoundedCornerShape(2.dp)),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.ArrowDropDown, null, tint = Color.Black)
            }
        }

        Row(
            modifier = Modifier
                .weight(0.7f)
                .height(56.dp)
                .background(Color(0xFFF1F1F1), RoundedCornerShape(4.dp))
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(selectedCountry.countryCode, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.width(12.dp))

            BasicTextField(
                value = phoneNumber,
                onValueChange = onNumberChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                cursorBrush = SolidColor(Color.Black), // Cursor Negro
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                decorationBox = { innerTextField ->
                    if (phoneNumber.isEmpty()) Text("Mobile number", color = Color.Gray)
                    innerTextField()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountrySelectorScreen(viewModel: AuthViewModel) {
    val countries = getAllGetCountries()
    var selectedCountry by remember { mutableStateOf(countries.find { it.isoCode == "ES" } ?: countries[0]) }
    var rawInputNumber by remember { mutableStateOf("") }
    var showSheet by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    fun updateViewModelNumber(country: CountryData, number: String) {
        viewModel.onPhoneChanged("${country.countryCode}$number")
    }

    SeparatedPhoneInput(
        selectedCountry = selectedCountry,
        phoneNumber = rawInputNumber,
        onNumberChange = { 
            rawInputNumber = it
            updateViewModelNumber(selectedCountry, it)           
        },
        onCountryClick = { showSheet = true }
    )

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(modifier = Modifier.fillMaxHeight(0.9f)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search country...") },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                LazyColumn {
                    val filtered = countries.filter {
                        it.countryName.contains(searchQuery, ignoreCase = true) ||
                        it.countryCode.contains(searchQuery)
                    }
                    items(filtered) { country ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCountry = country
                                    updateViewModelNumber(country, rawInputNumber)
                                    showSheet = false
                                    searchQuery = ""
                                }
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = country.flagRes),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp, 22.dp).clip(RoundedCornerShape(2.dp)),
                                contentScale = ContentScale.FillBounds
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(country.countryName, Modifier.weight(1f), fontSize = 16.sp, color = Color.Black)
                            Text(country.countryCode, color = Color.Gray, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
