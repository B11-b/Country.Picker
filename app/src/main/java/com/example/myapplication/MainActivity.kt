package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.TextStyle
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.widget.Toast

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    CountryPickerScreen()
                }
            }
        }
    }
}

@Composable
fun CountryPickerScreen() {
    var phoneNumber by remember { mutableStateOf("") }
    var isAgreed by remember { mutableStateOf(false) }
    var countryMenuExpanded by remember { mutableStateOf(false) }
    data class Country(
        val name: String,
        val code: String,
        val flagRes: Int,
        val maxDigits: Int
    )
    val countries = listOf(
        Country("United Kingdom", "+44", R.drawable.united_kingdom, maxDigits = 11),
        Country("Egypt", "+20", R.drawable.flag_egypt, maxDigits = 11),
        Country("United States", "+1", R.drawable.usa, maxDigits = 10)
    )
    var selectedCountry by remember { mutableStateOf(countries.first()) }

    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val screenWidth = configuration.screenWidthDp.dp
    val horizontalPadding = (screenWidth * 0.06f).coerceAtLeast(16.dp)
    val topSpacing = 8.dp
    val inputCorner = 20.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(topSpacing))

        Text(
            text = "One last step",
            fontSize = 20.sp,
            color = Color(0xFF1B1132),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = buildAnnotatedString {
                append("Please login or signup for a\n")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("  free") }
                append(" account to continue")
            },
            fontSize = 14.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(inputCorner),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp),
            border = BorderStroke(1.dp, Color(0xFFDDDDDD))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPadding / 2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { countryMenuExpanded = true }
                ) {
                    Image(
                        painter = painterResource(id = selectedCountry.flagRes),
                        contentDescription = selectedCountry.name,
                        modifier = Modifier.size(28.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(start = 4.dp)
                    )
                    Text(
                        text = selectedCountry.code,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    DropdownMenu(
                        expanded = countryMenuExpanded,
                        onDismissRequest = { countryMenuExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        countries.forEach { c ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = c.flagRes),
                                            contentDescription = c.name,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = "${c.name}  ${c.code}", color = Color.Black)

                                    }
                                },
                                onClick = {
                                    selectedCountry = c
                                    if (phoneNumber.length > c.maxDigits) {
                                        phoneNumber = phoneNumber.take(c.maxDigits)
                                        Toast.makeText(
                                            context,
                                            "Limited to ${c.maxDigits} digits for ${c.name}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    countryMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .width(1.dp)
                        .background(Color(0xFFCCCCCC))
                )
                Spacer(modifier = Modifier.width(12.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { input ->
                        val digitsOnly = input.filter { it.isDigit() }
                        val max = selectedCountry.maxDigits
                        if (digitsOnly.length > max) {
                            Toast.makeText(
                                context,
                                "Maximum ${max} digits for ${selectedCountry.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        phoneNumber = digitsOnly.take(max)
                    },
                    placeholder = {
                        Text(
                            text = "7xxxxxxxx"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )


            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "We will use this to verify your account",
            fontSize = 10.sp,
            color = Color(0xFF888888),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isAgreed,
                onCheckedChange = { isAgreed = it },
                modifier = Modifier.padding(end = 2.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF673AB7),
                    uncheckedColor = Color(0xFF757575),
                    checkmarkColor = Color.White,
                    disabledCheckedColor = Color(0xFFB39DDB),
                    disabledUncheckedColor = Color(0xFFBDBDBD)
                )
            )

            Text(
                text = buildAnnotatedString {
                    append("I agree and comply to the ")
                    withStyle(SpanStyle(color = Color(0xFF03A9F4))) {
                        append("community guidelines")
                    }
                },
                color = Color.Black,
                fontSize = 10.sp,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { println("Continue") },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F41BD)),
            shape = RoundedCornerShape(28.dp),
            contentPadding = PaddingValues(horizontal = horizontalPadding)
        ) {
            Box(Modifier.fillMaxWidth()) {
                Text(
                    text = "Continue",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Continue",
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOnboardingPhone() {
    MyApplicationTheme {
        CountryPickerScreen()
    }
}
