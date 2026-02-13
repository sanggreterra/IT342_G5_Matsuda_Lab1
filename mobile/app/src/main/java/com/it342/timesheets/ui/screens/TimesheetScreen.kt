package com.it342.timesheets.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.it342.timesheets.ui.theme.SummaryBg
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TimesheetRecord(
    val employee: String,
    val clockIn: String,
    val clockOut: String,
    val hoursWorked: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimesheetScreen() {
    val employees = remember { mutableStateListOf<String>() }
    val records = remember { mutableStateListOf<TimesheetRecord>() }
    val activeRecords = remember { mutableStateOf<Map<String, Pair<String, Date>>>(emptyMap()) }
    val summary = remember { mutableStateOf<Map<String, Double>>(emptyMap()) }
    var selectedEmployee by remember { mutableStateOf("") }
    var searchValue by remember { mutableStateOf("") }
    var newEmployeeName by remember { mutableStateOf("") }
    var employeeDropdownExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val isClockOutEnabled = selectedEmployee.isNotEmpty() && activeRecords.value.containsKey(selectedEmployee)

    val formatDateTime: (Date) -> String = { date ->
        SimpleDateFormat("M/d/yyyy h:mm:ss a", Locale.getDefault()).format(date)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Add Employee
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Add Employee",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newEmployeeName,
                        onValueChange = { newEmployeeName = it },
                        placeholder = { Text("Enter Employee Name") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            val name = newEmployeeName.trim()
                            if (name.isEmpty()) return@Button
                            if (employees.contains(name)) return@Button
                            employees.add(name)
                            newEmployeeName = ""
                        }
                    ) {
                        Text("Add")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Clock In / Clock Out
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Clock In / Clock Out",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Select Employee:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = employeeDropdownExpanded,
                    onExpandedChange = { employeeDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedEmployee,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Select Employee") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = employeeDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = employeeDropdownExpanded,
                        onDismissRequest = { employeeDropdownExpanded = false }
                    ) {
                        employees.forEach { emp ->
                            DropdownMenuItem(
                                text = { Text(emp) },
                                onClick = {
                                    selectedEmployee = emp
                                    employeeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (selectedEmployee.isEmpty()) return@Button
                            if (activeRecords.value.containsKey(selectedEmployee)) return@Button
                            activeRecords.value = activeRecords.value + (selectedEmployee to (formatDateTime(
                                Date()
                            ) to Date()))
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clock In")
                    }
                    Button(
                        onClick = {
                            if (selectedEmployee.isEmpty() || !activeRecords.value.containsKey(selectedEmployee)) return@Button
                            val (clockInStr, clockInDate) = activeRecords.value[selectedEmployee]!!
                            val clockOutTime = Date()
                            val hoursWorked = (clockOutTime.time - clockInDate.time) / (1000.0 * 60 * 60)
                            val clockOutStr = formatDateTime(clockOutTime)
                            records.add(
                                TimesheetRecord(
                                    employee = selectedEmployee,
                                    clockIn = clockInStr,
                                    clockOut = clockOutStr,
                                    hoursWorked = "%.2f".format(hoursWorked)
                                )
                            )
                            summary.value = summary.value + (selectedEmployee to (summary.value.getOrDefault(
                                selectedEmployee,
                                0.0
                            ) + hoursWorked))
                            activeRecords.value = activeRecords.value - selectedEmployee
                        },
                        modifier = Modifier.weight(1f),
                        enabled = isClockOutEnabled
                    ) {
                        Text("Clock Out")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Timesheet Records
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Timesheet Records",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchValue,
                    onValueChange = { searchValue = it },
                    placeholder = { Text("Search Employee") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                val filtered = records.filter {
                    it.employee.contains(searchValue, ignoreCase = true)
                }
                if (filtered.isEmpty()) {
                    Text(
                        "No records",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    filtered.forEach { rec ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(rec.employee, style = MaterialTheme.typography.bodySmall)
                            Text("${rec.hoursWorked} hrs", style = MaterialTheme.typography.bodySmall)
                        }
                        Text(
                            "In: ${rec.clockIn}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Out: ${rec.clockOut}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Employee Summary
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SummaryBg)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Employee Summary",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (summary.value.isEmpty()) {
                    Text(
                        "No summary yet",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    summary.value.forEach { (emp, hrs) ->
                        Text(
                            "$emp: %.2f hrs".format(hrs),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
