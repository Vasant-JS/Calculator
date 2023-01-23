package com.hobby_projects.myjc_calculator

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import kotlin.math.pow

class CalculatorViewModel : ViewModel() {
    var state by mutableStateOf(CalculatorState())
        private set

    var textSize by mutableStateOf(30.sp)
    var log = mutableListOf<String>()
    private var logCalculatorState = mutableListOf<CalculatorState>()

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.DoubleZero -> enterDoubleZero()
            is CalculatorAction.Clear -> performClear()
            is CalculatorAction.Opertation -> enterOperation(action.opertation)
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.Delete -> performDeletion()
        }
        resize()
    }

    private fun performClear() {
        val CLEAR_TEXT = "------Cleared------"
        if(log.size>0){
            if(log[log.size-1] != CLEAR_TEXT) {
                log.add(CLEAR_TEXT)
                logCalculatorState.add(CalculatorState())
            }
        }else {
            log.add(CLEAR_TEXT)
            logCalculatorState.add(CalculatorState())
        }
        state = CalculatorState()
    }

    private fun resize() {
        Log.d("ResizeTag", "old Text Size: " + textSize)
        val length = (state.number1 + state.operation?.symbol + state.number2).length
        Log.d("ResizeTag", "length: " + length)
        textSize =
            if (length <= 10) 60.sp
            else if (length <= 13) 55.sp
            else if (length <= 17) 50.sp
            else if (length <= 25) 45.sp
            else 40.sp
        Log.d("ResizeTag", "new Text Size: " + textSize + "\n\n")
    }

    private fun enterDoubleZero() {
        if (state.operation == null) {

            if (state.number1.isNotBlank()) {
                state = state.copy(
                    number1 = state.number1 + "00"
                )
            }
            return
        }

        if (state.number2.isNotBlank()) {
            state = state.copy(
                number2 = state.number2 + "00"
            )
        }
        return
    }

    private fun performDeletion() {
        when {
            state.number2.isNotBlank() -> state = state.copy(
                number2 = state.number2.dropLast(1)
            )

            state.operation != null -> state = state.copy(
                operation = null
            )

            state.number1.isNotBlank() -> state = state.copy(
                number1 = state.number1.dropLast(1)
            )
        }
    }

    private fun performCalculation() {
        val number1 = state.number1.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()

        Log.d("Operation => ", "$number1 ${state.operation?.symbol} $number2")

        if (number1 != null && number2 != null) {
            val result = when (state.operation) {
                is CalculatorOperation.Add -> number1 + number2
                is CalculatorOperation.Subtract -> number1 - number2
                is CalculatorOperation.Multiply -> number1 * number2
                is CalculatorOperation.Divide -> number1 / number2
                is CalculatorOperation.Power -> number1.pow(number2)// toThePowerOf number2 //Math.pow(number1,number2)
                null -> return
            }

            /*Log.d(
                "Logged:::",
                state.number1 + " " + state.operation?.symbol + " " + state.number2 + " = " + updatedResult(
                    result.toString()
                )
            )*/

            logCalculatorState.add(state)

            log.add(
                state.number1 + " " + state.operation?.symbol + " " + state.number2 + " = " + updatedResult(
                    result.toString()
                )
            )

            state = state.copy(
                number1 = updatedResult(result.toString()),
                number2 = "",
                operation = null
            )
        }
    }

    private fun updatedResult(unformattedResult: String): String {
//        Log.d("UnFormattedResult: ", unformattedResult)

        if (unformattedResult.contains(".")) {
            val charsBeforeDecimal = unformattedResult.substringBefore(".")
            val charsAfterDecimal =
                if (unformattedResult.contains("E"))
                    unformattedResult.substringAfter(".")
                else
                    unformattedResult.substringAfter(".").take(2)
            return "$charsBeforeDecimal.$charsAfterDecimal"
        }
//        Log.d("FormattedResult: ", unformattedResult)
        return unformattedResult
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.number1.isNotBlank()) {
            if (state.number1.isNotBlank() && state.number2.isNotBlank() && state.operation != null) {
                performCalculation()
            }
            state = state.copy(operation = operation)
        }
    }


    private fun enterDecimal() {
        if (state.operation == null && !state.number1.contains(".")) {
            state = state.copy(
                number1 = when {
                    state.number1.isNotBlank() -> state.number1 + "."
                    else -> "0."
                }
            )
            return
        }

        if (!state.number2.contains(".")) {
            state = state.copy(
                number2 = when {
                    state.number2.isNotBlank() -> state.number2 + "."
                    else -> "0."
                }
            )
            return
        }
    }

    private fun enterNumber(number: Int) {
        if (state.operation == null) {
            if (state.number1.length >= MAX_NUM_LENGTH) {
                return
            }
            state = state.copy(
                number1 = state.number1 + number
            )
            return
        }
        if (state.number2.length >= MAX_NUM_LENGTH) {
            return
        }
        state = state.copy(
            number2 = state.number2 + number
        )
    }

    fun revisit(pointer: Int) {
        log = log.dropLast(log.size-1-pointer) as MutableList<String>

        logCalculatorState = logCalculatorState.dropLast(logCalculatorState.size-1-pointer) as MutableList<CalculatorState>

        val lastState = logCalculatorState[logCalculatorState.size-1]

        state = state.copy(
            number1 = lastState.number1,
            number2 = lastState.number2,
            operation = lastState.operation
        )
    }

    companion object {
        private const val MAX_NUM_LENGTH = 24
    }

}
