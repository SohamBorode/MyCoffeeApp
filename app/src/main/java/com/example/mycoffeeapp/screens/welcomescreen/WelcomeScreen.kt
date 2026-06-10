package com.example.mycoffeeapp.screens.welcomescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.navigation.NavRoutes

@Composable
fun WelcomeScreen(navControllerX: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        Image(
            painter = painterResource(R.drawable.coffee_6),
            contentDescription = "Welcome Image",
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .padding(20.dp, 40.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Fall in Love with Coffee in Blissful Delight!",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Welcome to our crazy coffee corner, where every cup is a delight for you.",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light,
                fontSize = 18.sp,
                color = Color.LightGray
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth().padding(20.dp, 40.dp).align(alignment = Alignment.BottomCenter)
        ) {
            Button(
                onClick = {
                    navControllerX.navigate(NavRoutes.NavBarGraph) {
                        popUpTo(NavRoutes.WelcomeScreen) {
                            inclusive = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(colorResource(R.color.orange)),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp)
                ) {
                Text(
                    text = "Get Started",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }
        }
    }
}


