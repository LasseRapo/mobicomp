package com.example.mobicomp.ui.home.reminderCard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobicomp.data.entity.Reminder
import com.example.mobicomp.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReminderCardList(
    modifier: Modifier = Modifier
) {
    val viewModel: ReminderCardListViewModel = viewModel()
    val viewState by viewModel.state.collectAsState()

    Column(modifier = modifier) {
        ReminderList(
            list = viewState.reminders
        )
    }
}

@Composable
private fun ReminderList(
    list: List<Reminder>
) {
    LazyColumn(
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(list) { reminder ->
            ReminderListItem(
                reminder = reminder,
                onClick = {},
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.clickable { onClick() }) {
        val (divider, reminderTitle, reminderCategory, icon, date) = createRefs()
        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        //Title
        Text(
            text = reminder.reminderTitle,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(reminderTitle) {
                linkTo(
                    start = parent.start,
                    end = icon.start,
                    startMargin = 24.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
                top.linkTo(parent.top, margin = 10.dp)
                width = Dimension.preferredWrapContent
            }
        )

        //Category
        Text(
            text = reminder.reminderCategory,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(reminderCategory) {
                linkTo(
                    start = parent.start,
                    end = icon.start,
                    startMargin = 24.dp,
                    endMargin = 8.dp,
                    bias = 0f
                )
                top.linkTo(reminderTitle.bottom, margin = 6.dp)
                bottom.linkTo(parent.bottom, margin = 10.dp)
                width = Dimension.preferredWrapContent
            }
        )

        //Date
        Text(
            text = when {
                reminder.reminderDate != null -> { reminder.reminderDate.formatToString() }
                else -> Date().formatToString()
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.constrainAs(date) {
                linkTo(
                    start = reminderCategory.end,
                    end = icon.start,
                    startMargin = 8.dp,
                    endMargin = 16.dp,
                    bias = 0f
                )
                centerVerticallyTo(reminderCategory)
                top.linkTo(reminderTitle.bottom, 6.dp)
                bottom.linkTo(parent.bottom, 10.dp)
            }
        )

        //Icon
        IconButton(
            onClick = {/*TODO*/},
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = stringResource(R.string.edit_icon)
            )
        }

    }
}

private fun Date.formatToString(): String {
    return SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(this)
}