package com.example.mycoffeeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.ui.theme.Cinnamon
import com.example.mycoffeeapp.ui.theme.Onyx
import com.example.mycoffeeapp.ui.theme.Orange
import com.example.mycoffeeapp.ui.theme.PureWhite
import com.example.mycoffeeapp.ui.theme.SoftGray


@Preview(showBackground = true)
@Composable
fun SearchBar() {
    val text = remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text.value,
            onValueChange = { text.value = it },
            modifier = Modifier
                .weight(1f) // Allows the text field to take remaining space
                .background(
                    color = Onyx, shape = RoundedCornerShape(
                        bottomEnd = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 16.dp,
                        topStart = 16.dp
                    )
                ),
            shape = RoundedCornerShape(16.dp),
            placeholder = {
                Text(
                    text = "Search coffee...",
                    color = SoftGray
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.regular_outline_search),
                    contentDescription = null,
                    tint = SoftGray, // Fix search icon color
                    modifier = Modifier.size(20.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = PureWhite,
                unfocusedTextColor = PureWhite,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent, // Hide default border
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Orange
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.width(10.dp))

        IconButton(
            onClick = { /* Handle filter click */ },
            modifier = Modifier
                .size(56.dp) // Match the height of the OutlinedTextField
                .background(
                    color = Cinnamon, shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
        ) {
            Icon(
                painter = painterResource(R.drawable.regular_outline_filter),
                contentDescription = "Filter",
                modifier = Modifier.size(24.dp),
                tint = PureWhite // Fix filter icon color
            )
        }
    }


}























//@Composable
//fun SearchBar() {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            painter = painterResource(R.drawable.regular_outline_search),
//            contentDescription = null,
//            tint = Color.White,
//            modifier = Modifier
//                .padding(start = 16.dp)
//                .size(20.dp)
//        )
//        OutlinedTextField(
//            leadingIcon = painterResource(R.drawable.regular_outline_search),
//            placeholder = {Text(text = "Srearch Coffee", color = Color.Gray)},
//            onValueChange = {},
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 8.dp)
//                .height(55.dp)
//                .clip(RoundedCornerShape(15.dp))
//                .background(Color(0xFF202020))
//        )
//    }
//}