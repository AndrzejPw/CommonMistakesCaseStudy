package com.example.commonmistakescasestudy.mistake2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.example.commonmistakescasestudy.R
import com.example.commonmistakescasestudy.databinding.FragmentStateIssuesBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewStateIssuesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state)
    }
}

class StateIssuesFragment : Fragment(R.layout.fragment_lifecycle_issues) {

    private var _binding: FragmentStateIssuesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StateIssuesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStateIssuesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    renderState(it)
                }
            }
        }

    }

    private fun renderState(state: State) {
        when (state) {
            State.Idle -> {}
            is State.NameUpdated -> binding.nameTextView.text = state.name
            is State.SurnameUpdated -> binding.surnameTextView.text = state.surName
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class StateIssuesViewModel : ViewModel() {
    private val _state = MutableStateFlow<State>(State.Idle)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // here in real world you would call repository to get data from some data source
            _state.value = State.NameUpdated("John")
            // this delay simulates network (and network delay made it work in real app)
            delay(100)
            _state.value = State.SurnameUpdated("Doe")
        }

    }
}

sealed class State {
    object Idle : State()
    data class NameUpdated(val name: String) : State()
    data class SurnameUpdated(val surName: String) : State()
}
