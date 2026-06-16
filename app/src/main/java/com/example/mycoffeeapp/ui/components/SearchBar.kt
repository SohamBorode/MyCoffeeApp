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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mycoffeeapp.R
import com.example.mycoffeeapp.ui.theme.Cinnamon
import com.example.mycoffeeapp.ui.theme.Onyx
import com.example.mycoffeeapp.ui.theme.Orange
import com.example.mycoffeeapp.ui.theme.PureWhite
import com.example.mycoffeeapp.ui.theme.SoftGray

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearchClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .background(
                    color = Onyx,
                    shape = RoundedCornerShape(
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
                    tint = SoftGray,
                    modifier = Modifier.size(20.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = PureWhite,
                unfocusedTextColor = PureWhite,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Orange
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.width(10.dp))

        IconButton(
            onClick = { onSearchClick(value) },
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = Cinnamon,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
        ) {
            Icon(
                painter = painterResource(R.drawable.regular_outline_search),
                contentDescription = "Search",
                modifier = Modifier.size(24.dp),
                tint = PureWhite
            )
        }
    }
}
