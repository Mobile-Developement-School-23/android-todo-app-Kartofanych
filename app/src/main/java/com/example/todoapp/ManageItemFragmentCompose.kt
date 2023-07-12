package com.example.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.todoapp.theme.YandexTodoTheme
import com.example.todoapp.theme.YandexTodoTheme.colors
import com.example.todoapp.theme.blue
import com.example.todoapp.theme.gray_light
import com.example.todoapp.theme.tertiary

class ManageItemFragmentCompose : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        view.apply {
            setContent {
                FragmentPage()
            }
        }
        return view
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun BottomShadow(alpha: Float = 0.1f, height: Dp = 8.dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = alpha),
                        Color.Transparent,
                    )
                )
            )
    )
}


@Preview(showBackground = true)
@Composable
fun FragmentPage() {
    YandexTodoTheme {

        val openDialog = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.fillMaxWidth(),

            verticalArrangement = Arrangement.Top
        ) {
            Toolbar()
            BottomShadow(alpha = 0.2f, height = 5.dp)
            EditField()
            ImportanceBlock()
            DoUntilBlock(openDialog)
            DeleteButton()

        }
        AlertDialogSample(openDialog)
    }
}

@Composable
fun Toolbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "Close page",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(40.dp)
                    .padding(10.dp)
            )
        }
        Text(
            text = "СОХРАНИТЬ",
            style = TextStyle(
                color = blue,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
            ),
            modifier = Modifier
                .clickable {
                    //save click
                }
                .align(Alignment.CenterVertically)
                .padding(horizontal = 10.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun EditField() {
    val state = remember { mutableStateOf("") }
    OutlinedTextField(
        value = state.value,
        onValueChange = { state.value = it },
        label = {
            Text(
                "Что надо сделать...",
                style = TextStyle(
                    color = tertiary
                )
            )
        },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = gray_light,
            unfocusedBorderColor = gray_light,
            cursorColor = blue
        ),
        modifier = Modifier
            .sizeIn(minHeight = 56.dp)
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
    )
}

@Composable
fun ImportanceBlock() {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable {
                //click
            }
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        Text(
            text = "Важность",
            style = TextStyle(
                fontSize = 16.sp,
                color = colors.labelPrimary
            )
        )
        Text(
            text = "Нет",
            style = TextStyle(
                fontSize = 13.sp,
                color = colors.labelTertiary
            )
        )
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = tertiary
                )
        )
    }
}

@Composable
fun DoUntilBlock(openDialog: MutableState<Boolean>) {
    val checkedState = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .height(110.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    checkedState.value = !checkedState.value
                }
                .padding(vertical = 20.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Column(
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Сделать до",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = colors.labelPrimary
                    )
                )

                if (checkedState.value) {
                    Text(
                        text = "дата здесь",
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = colors.colorBlue
                        ),
                    )
                }
            }
            Switch(
                checked = checkedState.value,
                onCheckedChange = {
                    checkedState.value = it
                    if (checkedState.value) {
                        openDialog.value = true
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colors.colorBlue,
                    uncheckedThumbColor = colors.colorGray,
                    uncheckedTrackColor = colors.colorGrayLight,
                    checkedTrackColor = colors.colorGrayLight

                ),
            )

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = tertiary
                )
        )
    }
}

@Composable
fun DeleteButton() {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, top = 10.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {}
            )
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_delete_24dp),
            contentDescription = "Delete Icon",
            tint = colors.labelTertiary
        )
        Text(
            text = "Удалить",
            color = colors.labelTertiary,
            modifier = Modifier
                .padding(start = 5.dp)
        )
    }
}

@Composable
fun AlertDialogSample(value: MutableState<Boolean>) {

    val openDialog = value
    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                openDialog.value = false
            },
            title = {
                Text(text = "Dialog Title")
            },
            text = {
                Text("Here is a text ")
            },
            confirmButton = {
                Button(

                    onClick = {
                        openDialog.value = false
                    }) {
                    Text("This is the Confirm Button")
                }
            },
            dismissButton = {
                Button(

                    onClick = {
                        openDialog.value = false
                    }) {
                    Text("This is the dismiss Button")
                }
            }
        )
    }

}

