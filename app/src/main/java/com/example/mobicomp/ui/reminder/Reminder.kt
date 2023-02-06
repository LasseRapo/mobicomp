package com.example.mobicomp.ui.reminder

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun Reminder(
    onBackPress: () -> Unit
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
                IconButton(
                    onClick = onBackPress
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(text = "Reminder")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(text = "Reminder title")},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(corner = CornerSize(25.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(text = "Date")},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(corner = CornerSize(25.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(text = "Time")},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(corner = CornerSize(25.dp))
                )

                Spacer(modifier = Modifier.height(220.dp))

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth().size(55.dp),
                    shape = RoundedCornerShape(corner = CornerSize(25.dp))
                ) {
                    Text("Save reminder")
                }
            }
        }
    }
}