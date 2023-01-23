package com.hobby_projects.myjc_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hobby_projects.myjc_calculator.ui.theme.MyJC_CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyJC_CalculatorTheme {
                MyCalculatorApp()
            }
        }
    }

}

@Composable
fun MyCalculatorApp() {
    val viewModel = viewModel<CalculatorViewModel>()
    var openDialog by remember { mutableStateOf(false) }
    val data = viewModel.log
    val state = viewModel.state
    val buttonSpacing = 8.dp
    Box(modifier = Modifier.fillMaxSize()) {
        Calculator(
            state = state,
            onAction = viewModel::onAction,
            buttonSpacing = buttonSpacing,
            fontSize = viewModel.textSize,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(197, 197, 197, 255)
                ),
                onClick = {
                    openDialog = true
                }) {
                Text(
                    color = Color(58, 58, 58, 255),
                    text = "History"
                )
            }
        }

        if (openDialog) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                backgroundColor = Color.Gray,
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.White,
                            fontSize = 30.sp,
                            text = "History"
                        )
                        Icon(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable(
                                    onClick = {
                                        openDialog = false
                                    }
                                ),
                            imageVector = Icons.Default.Close,
                            tint = Color.White,
                            contentDescription = null)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (data.size > 0) {
                        Column(Modifier.verticalScroll(rememberScrollState())) {
                            for (index in 0 until data.size) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Text(
                                        modifier = Modifier
                                            .padding(16.dp),
                                        text = data.reversed()[index]
                                    )
                                    Icon(
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                            .clickable(
                                                onClick = {
                                                    openDialog = false
                                                    viewModel.revisit(data.size-1-index)
                                                }
                                            ),
                                        imageVector = Icons.Filled.Refresh,
                                        tint = Color.Black,
                                        contentDescription = null)
                                }
                                Divider(color = Color(221, 221, 221, 255))
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    } else {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            text = "No History"
                        )
                    }

                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyJC_CalculatorTheme {
        MyCalculatorApp()
    }
}