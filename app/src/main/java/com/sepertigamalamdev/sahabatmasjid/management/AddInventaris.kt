//package com.sepertigamalamdev.sahabatmasjid.management
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Button
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import kotlinx.coroutines.launch
//
//@Composable
//fun ItemManagementScreen(viewModel: InventoryItemViewModel) {
//    val items by viewModel.items.collectAsStateWithLifecycle(initialValue = emptyList())
//    val error by viewModel.error.collectAsStateWithLifecycle(initialValue = null)
//    var itemName by remember { mutableStateOf("") }
//    var itemPrice by remember { mutableStateOf("") }
//    var itemQuantity by remember { mutableStateOf("") }
//    var itemDescription by remember { mutableStateOf("") }
//    val snackbarHostState = remember { SnackbarHostState() }
//    val coroutineScope = rememberCoroutineScope()
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//    ) {
//        // Form to add a new item
//        TextField(
//            value = itemName,
//            onValueChange = { itemName = it },
//            label = { Text("Name") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            value = itemPrice,
//            onValueChange = { itemPrice = it },
//            label = { Text("Price") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            value = itemQuantity,
//            onValueChange = { itemQuantity = it },
//            label = { Text("Quantity") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        TextField(
//            value = itemDescription,
//            onValueChange = { itemDescription = it },
//            label = { Text("Description") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                if (itemName.isNotBlank() && itemPrice.isNotBlank() && itemQuantity.isNotBlank()) {
//                    val newItem = InventoryItem(
//                        name = itemName,
//                        price = itemPrice.toDoubleOrNull() ?: 0.0,
//                        quantity = itemQuantity.toIntOrNull() ?: 0,
//                        description = itemDescription
//                    )
//                    viewModel.addItem(newItem)
//                    // Clear fields after submission
//                    itemName = ""
//                    itemPrice = ""
//                    itemQuantity = ""
//                    itemDescription = ""
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = itemName.isNotBlank() && itemPrice.isNotBlank() && itemQuantity.isNotBlank()
//        ) {
//            Text("Add Item")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Button to add dummy data
//        Button(
//            onClick = {
//                coroutineScope.launch {
//                    try {
//                        DummyDataGenerator.addDummyItems()
//                        viewModel.fetchItems() // Refresh the list
//                        snackbarHostState.showSnackbar("Dummy items added successfully!")
//                    } catch (e: Exception) {
//                        snackbarHostState.showSnackbar("Failed to add dummy items: ${e.message}")
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Add Dummy Items")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // List of items
//        LazyColumn {
//            items(items) { item ->
//                Text(
//                    text = "${item.name} - ${item.price} (Qty: ${item.quantity})",
//                    modifier = Modifier.padding(vertical = 4.dp)
//                )
//            }
//        }
//
//        // Snackbar for errors
//        error?.let { errorMessage ->
//            LaunchedEffect(errorMessage) {
//                snackbarHostState.showSnackbar(errorMessage)
//            }
//        }
//    }
//
//    SnackbarHost(
//        hostState = snackbarHostState,
//        modifier = Modifier.padding(16.dp)
//    )
//
//    LaunchedEffect(Unit) {
//        viewModel.fetchItems()
//    }
//}
//
//class DummyDataGenerator {
//
//}
//
//private fun InventoryItemViewModel.addItem(unit: Any) {}
//
//@Composable
//fun InventoryItem(name: String, price: Double, quantity: Int, description: String) {
//    TODO("Not yet implemented")
//}
//
//annotation class InventoryItemViewModel
