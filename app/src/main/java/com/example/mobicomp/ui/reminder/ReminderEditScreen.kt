package com.example.mobicomp.ui.reminder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.core.domain.entity.Reminder
import com.google.accompanist.insets.systemBarsPadding
import java.time.LocalDateTime

@Composable
fun ReminderEditScreen(
    navController: NavController,
    reminderId: Long?,
    message: String,
    reminderTime: String,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val id = remember { mutableStateOf(reminderId) }
    val reminderMessage = remember { mutableStateOf(message) }
    val time = remember { mutableStateOf(reminderTime) }

    Surface {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(text = "Edit Reminder")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)

            ) {
                OutlinedTextField(
                    value = reminderMessage.value,
                    onValueChange = { reminderMessage.value = it },
                    label = { Text(text = "Reminder message") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(corner = CornerSize(25.dp))
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = time.value,
                    onValueChange = { time.value = it },
                    label = { Text(text = "Reminder time") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(corner = CornerSize(25.dp))
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.editReminder(
                            Reminder(
                                reminderId = id.value,
                                message = reminderMessage.value,
                                location_x = 0.0,
                                location_y = 0.0,
                                reminderTime = time.value,
                                creationTime = LocalDateTime.now(),
                                creatorId = 1,
                                reminderSeen = 0
                            )
                        )
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth().size(55.dp),
                    shape = RoundedCornerShape(corner = CornerSize(25.dp))
                ) {
                    Text(text = "Save changes")
                }
            }
        }
    }
}