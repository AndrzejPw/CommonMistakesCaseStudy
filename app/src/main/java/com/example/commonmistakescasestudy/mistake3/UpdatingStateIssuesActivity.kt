package com.example.commonmistakescasestudy.mistake3

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.max

class UpdatingStateIssuesActivity : AppCompatActivity() {
    private val viewModel: UpdatingStateIssuesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState = viewModel.state.collectAsStateWithLifecycle().value
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Enter your PIN", fontSize = 20.sp)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(uiState.maskedPin, fontSize = 40.sp)
                }
                Row {
                    Column {
                        PinKeyboard(
                            okButtonEnabled = uiState.okActive,
                            deleteButtonEnabled = uiState.deleteActive,
                            onPinButtonClicked = { viewModel.onPinButtonClicked(it) },
                            onDeleteButtonClicked = { viewModel.onDeleteButtonClicked() },
                            onOkButtonClicked = { viewModel.onOkButtonClicked() }
                        )
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.clearPinInput()
    }
}

@Composable
private fun PinKeyboard(
    okButtonEnabled: Boolean,
    deleteButtonEnabled: Boolean,
    onPinButtonClicked: (Int) -> Unit,
    onDeleteButtonClicked: () -> Unit,
    onOkButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        PinRow(rowNumber = 0, onPinButtonClicked = onPinButtonClicked)
        PinRow(rowNumber = 1, onPinButtonClicked = onPinButtonClicked)
        PinRow(rowNumber = 2, onPinButtonClicked = onPinButtonClicked)
        Row(modifier = Modifier.padding(16.dp)) {
            Button(
                modifier = Modifier
                    .weight(1f),
                enabled = deleteButtonEnabled,
                onClick = { onDeleteButtonClicked() }) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "Localized description")
            }
            PinButton(0, Modifier.weight(1f)) { number ->
                onPinButtonClicked(number)
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = { onOkButtonClicked() },
                enabled = okButtonEnabled
            ) {
                Icon(Icons.Rounded.Check, contentDescription = "Localized description")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewPinKeyboard() {
    PinKeyboard(
        okButtonEnabled = false,
        deleteButtonEnabled = true,
        onPinButtonClicked = {},
        onDeleteButtonClicked = {},
        onOkButtonClicked = {}
    )
}

@Composable
private fun PinRow(rowNumber: Int, onPinButtonClicked: (Int) -> Unit) {
    Row(modifier = Modifier.padding(16.dp)) {
        PinButton(1 + rowNumber * 3, Modifier.weight(1f)) { number ->
            onPinButtonClicked(number)
        }
        PinButton(2 + rowNumber * 3, Modifier.weight(1f)) { number ->
            onPinButtonClicked(number)
        }
        PinButton(3 + rowNumber * 3, Modifier.weight(1f)) { number ->
            onPinButtonClicked(number)
        }
    }
}

@Composable
private fun PinButton(number: Int, modifier: Modifier = Modifier, onClick: (Int) -> Unit) {
    Button(
        onClick = { onClick(number) },
        modifier = modifier,
    ) {
        Text(number.toString())
    }
}

class UpdatingStateIssuesViewModel : ViewModel() {

    private var pin = ""
    private val _state =
        MutableStateFlow(State(maskedPin = "", okActive = false, deleteActive = false))
    val state = _state.asStateFlow()
    fun onPinButtonClicked(digit: Int) {
        pin += digit
        updateUi()
    }

    fun onDeleteButtonClicked() {
        pin = pin.substring(0, max(pin.length - 1, 0))
        updateUi()
    }

    fun clearPinInput() {
        pin = ""
        updateUi()
    }

    private fun updateUi() {

        _state.value = State(
            maskedPin = pin.replace(".".toRegex(), "*"),
            okActive = pin.length == REQUIRED_PIN_LENGTH,
            deleteActive = pin.isNotEmpty()
        )
    }

    fun onOkButtonClicked() {
        println("logging in with $pin")
    }

    companion object {
        const val REQUIRED_PIN_LENGTH = 5
    }
}

data class State(val maskedPin: String, val okActive: Boolean, val deleteActive: Boolean)
