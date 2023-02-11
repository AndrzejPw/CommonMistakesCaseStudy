package com.example.commonmistakescasestudy.mistake3

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlin.math.max

class UpdatingStateIssuesFixedActivity : AppCompatActivity() {
    private val viewModel: UpdatingStateIssuesFixedViewModel by viewModels()
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


class UpdatingStateIssuesFixedViewModel : ViewModel() {

    private val _state = MutableStateFlow("")
    val state = _state.map { pin ->
        State(
            maskedPin = pin.replace(".".toRegex(), "*"),
            okActive = pin.length == REQUIRED_PIN_LENGTH,
            deleteActive = pin.isNotEmpty()
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        State(maskedPin = "", okActive = false, deleteActive = false)
    )

    fun onPinButtonClicked(digit: Int) {
        _state.update { it + digit }
    }

    fun onDeleteButtonClicked() {
        _state.update { it.substring(0, max(it.length - 1, 0)) }
    }

    fun clearPinInput() {
        _state.value = ""
    }

    fun onOkButtonClicked() {
        println("logging in with ${_state.value}")
    }

    companion object {
        const val REQUIRED_PIN_LENGTH = 5
    }
}
