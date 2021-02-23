package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.mycalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // variable reference to the views in the activity main binding
    private lateinit var binding: ActivityMainBinding

    // these are logic checks when a period is used
    // to check if the last button was numeric
    var lastNumeric: Boolean = false

    // check if a dot was used
    var lastDot: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        // call super
        super.onCreate(savedInstanceState)
        // create binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        // set the content view to the binding .root
        setContentView(binding.root)
    }

    // testing buttons methods
    fun onDigit(view: View) {
        // view in this case is the super class, button has the methods
        // append to the tv input (since we are creating a string of numbers)
        binding.tvInput.append((view as Button).text)
        // we pressed a digit
        lastNumeric = true
    }

    // the view here is a Button (view as Button) casting
    fun onClear(view: View) {
    // clear the text from the TextView
        binding.tvInput.text = ""

        // clearing the input reset the lastDot
        lastDot = false
        // also clear the last numeric
        lastNumeric = false

    }

    fun onDecimalPoint(view: View) {
        // check if the value before was a number and not a dot before that
        if (lastNumeric && !lastDot) {
            // add a period
            binding.tvInput.append(".")
            // last numeric is false, since the last number is a . not a number
            // this changes once we input a number
            lastNumeric = false
            // we used a dot, we should only have 1 per number lists
            lastDot = true
        }
    }

    fun onOperator(view: View) {
        // check if the last number is numeric and if the
        // an operator has been added (excluding the negative operator)
        if (lastNumeric && !isOperatorAdded(binding.tvInput.text.toString())) {
            binding.tvInput.append((view as Button).text)
        // the operators are neither numeric or a decimal
            lastDot = false
            lastNumeric = false
        }
    }

    private fun isOperatorAdded(value: String): Boolean {
        // check startsWith since the value of the number can be negative (-21)
        return if (value.startsWith("-")) {
            // if it is negative return false since we are not using that as an operator
            false
        }
        // add only 1 of these operators
        else {
            // check if the list contains any operators (if it does return true )
            // can do .contains("+") || .contains("-") etc...
            // since .contains can take a regex
            val contains = value.contains("[*-+/]".toRegex())
            // Log.i("MAIN", contains.toString())

            // return the contains bools
            contains
        }
    }

    // when the equal button is pressed
    fun onEqual(view: View) {
        // if the last value was numeric (to perform valid calc)
        if (lastNumeric) {
            // get the input as string
            var parseText = binding.tvInput.text.toString()
            // prefix holder (negation)
            var prefix = "";
            // check if there is an negation (minus)
            if (parseText.startsWith("-")) {
                prefix = "-"
                parseText = parseText.substring(1)
            }
            when {
                parseText.contains("-") -> {
                    // if there is an operator in front remove it
                    // 123 - 321, [0] = 123 , [1] = 321
                    val parseList: List<String> = parseText.split("-")
                    // grab the operands
                    var operandOne: String = parseList[0]
                    val operandTwo: String = parseList[1]
                    // if there was a prefix, add it back
                    if (prefix.isNotEmpty()) {
                        operandOne = prefix + operandOne
                    }
                    // calculate values then convert to string
                    val outputCalc : String = zeroFormat((operandOne.toDouble() - operandTwo.toDouble()).toString())
                    // set calculation to TextView
                    binding.tvInput.text = outputCalc
                }
                parseText.contains("+") -> {
                    val parseList: List<String> = parseText.split("+")
                    // grab the operands
                    var operandOne: String = parseList[0]
                    val operandTwo: String = parseList[1]
                    // if there was a prefix, add it back
                    if (prefix.isNotEmpty()) {
                        operandOne = prefix + operandOne
                    }
                    // calculate values then convert to string
                    val outputCalc : String = zeroFormat((operandOne.toDouble() + operandTwo.toDouble()).toString())
                    // set calculation to TextView
                    binding.tvInput.text = outputCalc
                }
                parseText.contains("*") -> {
                    val parseList: List<String> = parseText.split("*")
                    // grab the operands
                    var operandOne: String = parseList[0]
                    val operandTwo: String = parseList[1]
                    // if there was a prefix, add it back
                    if (prefix.isNotEmpty()) {
                        operandOne = prefix + operandOne
                    }
                    // calculate values then convert to string
                    val outputCalc : String = zeroFormat((operandOne.toDouble() * operandTwo.toDouble()).toString())
                    // set calculation to TextView
                    binding.tvInput.text = outputCalc
                }
                parseText.contains("/") -> {
                    val parseList: List<String> = parseText.split("/")
                    // grab the operands
                    var operandOne: String = parseList[0]
                    val operandTwo: String = parseList[1]
                    // if there was a prefix, add it back
                    if (prefix.isNotEmpty()) {
                        operandOne = prefix + operandOne
                    }
                    // calculate values then convert to string
                    val outputCalc : String = zeroFormat((operandOne.toDouble() / operandTwo.toDouble()).toString())
                    // set calculation to TextView
                    binding.tvInput.text = outputCalc
                }
            }
        }
    }

    private fun zeroFormat(result: String) : String {
        // check if the calculation ends with 0
        if(result.endsWith("0")) {
            // format 9*6 = 54.00 -> 54
            return "%.0f".format(result.toDouble())
        }
        return result
    }

}