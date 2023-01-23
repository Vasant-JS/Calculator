package com.hobby_projects.myjc_calculator

sealed class CalculatorAction{
    data class Number(val number: Int): CalculatorAction()
    object Clear: CalculatorAction()
    object Delete: CalculatorAction()
    object Decimal: CalculatorAction()
    object DoubleZero: CalculatorAction()
    object Calculate: CalculatorAction()
    data class Opertation(val opertation: CalculatorOperation) : CalculatorAction()
}
