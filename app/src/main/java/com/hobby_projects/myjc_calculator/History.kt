package com.hobby_projects.myjc_calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun History(
    modifier: Modifier = Modifier,
    data: ArrayList<String> = arrayListOf()
) {
    Column(modifier) {
        for (index in data.size - 1 until 0) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = data[index]
            )
        }
    }
}