package com.example.mobicomp.ui.reminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.example.core.domain.entity.Reminder
import com.google.accompanist.insets.systemBarsPadding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

@Composable
fun ReminderScreen(
    navController: NavController,
    viewModel: ReminderViewModel = hiltViewModel(),
) {

    val message = remember {mutableStateOf("")}
    val reminderTime = remember {mutableStateOf("")}

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            val date = remember { mutableStateOf(LocalDate.now()) }
            val time = remember { mutableStateOf(LocalTime.now()) }
            TopAppBar {
                IconButton(
                    onClick = { navController.popBackStack() }
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
                    value = message.value,
                    onValueChange = {message.value = it},
                    label = { Text(text = "Reminder message")},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(corner = CornerSize(50.dp))
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(modifier = Modifier.weight(0.7f)) {
                        DatePicker(context = LocalContext.current as FragmentActivity, date = date)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(0.3f)) {
                        TimePicker(context = LocalContext.current as FragmentActivity, time = time)
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = {
                        val id = Random.nextLong()
                        viewModel.saveReminder(
                            navController = navController,
                            reminder = Reminder(
                                reminderId = id,
                                message = message.value,
                                location_x = 0.0,
                                location_y = 0.0,
                                reminderTime = date.value.atTime(time.value),
                                creationTime = LocalDateTime.now(),
                                reminderSeen = false,
                                creatorId = 1
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(corner = CornerSize(50.dp))
                ) {
                    Text("Save reminder")
                }
            }
        }
    }
}

@Composable
fun DatePicker(context: Context, date: MutableState<LocalDate>) {
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = LocalDate.of(year, month + 1, dayOfMonth)
        }, year, month, day
    )

    OutlinedTextField(
        modifier = Modifier.clickable { datePickerDialog.show() },
        value = date.value.toString(),
        onValueChange = {},
        label = { Text(text = "Date") },
        shape = RoundedCornerShape(corner = CornerSize(50.dp)),
        enabled = false
    )
}

@Composable
fun TimePicker( context: Context, time: MutableState<LocalTime> ) {
    val hour: Int = time.value.hour
    val minute: Int = time.value.minute

    val dialog = TimePickerDialog(
        context,
        { _: TimePicker, hour: Int, minute: Int ->
            time.value = LocalTime.of(hour, minute)
        }, hour, minute, true
    )

    OutlinedTextField(
        modifier = Modifier.clickable { dialog.show() },
        label = { Text(text = "Time") },
        value = time.value.format(DateTimeFormatter.ofPattern("HH:mm")).toString(),
        onValueChange = {},
        enabled = false,
        shape = RoundedCornerShape(corner = CornerSize(50.dp))
    )
}