package com.example.mobicomp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.core.domain.entity.Reminder
import com.example.mobicomp.R
import com.example.mobicomp.ui.reminder.ReminderViewModel
import com.example.mobicomp.ui.reminder.ReminderViewState
import com.google.accompanist.insets.systemBarsPadding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*

@Composable
fun Home(
    viewModel: ReminderViewModel = hiltViewModel(),
    navController: NavController
) {
    val appBarColor = MaterialTheme.colors.surface.copy(alpha = 0.87f)
    Scaffold(
        drawerContent = { DrawerContent(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(route = "reminder") },
                contentColor = Color.Black,
                backgroundColor = Color.Green,
                modifier = Modifier.padding(all = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) {
        Column (
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
        ) {

            HomeAppBar(
                backgroundColor = appBarColor,
                navController = navController
            )
            ReminderList(
                reminderViewModel = viewModel,
                NavController = navController,
            )

        }
    }
}

@Composable
private fun ReminderList(
    reminderViewModel: ReminderViewModel,
    NavController: NavController
) {
    // cache the reminders in a mutable list
    val reminderList = remember { mutableStateListOf<Reminder>() }
    reminderViewModel.loadReminders()

    val reminderViewState by reminderViewModel.reminderState.collectAsState()
    println(reminderViewState)

    when (reminderViewState) {
        is ReminderViewState.Loading -> {}
        is ReminderViewState.Success -> {
            reminderList.clear()
            reminderList.addAll((reminderViewState as ReminderViewState.Success).data)
        }
        else -> {}
    }

    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(reminderList) { item ->
            ReminderListItem(
                reminder = item,
                onClick = {},
                navController = NavController,
                reminderViewModel = reminderViewModel,
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    onClick: () -> Unit,
    navController: NavController,
    reminderViewModel: ReminderViewModel,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(modifier = modifier.clickable { onClick() }) {
        val (divider, message, reminderTime, editButton, deleteButton, date) = createRefs()
        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        // Message
        Text(
            text = reminder.message,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(message) {
                linkTo(
                    start = parent.start,
                    end = editButton.start,
                    startMargin = 24.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
                top.linkTo(parent.top, margin = 10.dp)
                width = Dimension.preferredWrapContent
            }
        )

        // Date
        Text(
            text = reminder.reminderTime,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.constrainAs(date) {
                linkTo(
                    start = reminderTime.end,
                    end = editButton.start,
                    startMargin = 8.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
                centerVerticallyTo(reminderTime)
                top.linkTo(message.bottom, 6.dp)
                bottom.linkTo(parent.bottom, 10.dp)
            }
        )

        // Edit button
        IconButton(
            onClick = { navController.navigate(route = "editReminder/${reminder.reminderId}/${reminder.message}/${reminder.reminderTime}") },
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(editButton) {
                    top.linkTo(parent.top, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(deleteButton.start, 1.dp)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = stringResource(R.string.edit_icon)
            )
        }

        // Delete button
        IconButton(
            onClick = { reminderViewModel.deleteReminder(reminder) },
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(deleteButton) {
                    top.linkTo(parent.top, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = ""
            )
        }
    }
}

@Composable
private fun HomeAppBar(
    navController: NavController,
    backgroundColor: Color
) {
    TopAppBar(
        title = {
            Text(
                text = "Reminder app",
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        backgroundColor = backgroundColor,
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(R.string.search))

            }
            IconButton(onClick = { navController.navigate(route = "profile") }) {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.account))

            }
        }
    )
}

@Composable
private fun DrawerContent(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Button(
            onClick = { navController.navigate(route = "login") },
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        ) {
            Text(text = "Log out")
        }
    }

}

private val emptyTabIndicator: @Composable (List<TabPosition>) -> Unit = {}