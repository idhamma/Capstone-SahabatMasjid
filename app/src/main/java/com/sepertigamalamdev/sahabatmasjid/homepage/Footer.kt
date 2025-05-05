package com.sepertigamalamdev.sahabatmasjid.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun Footer(navController: NavController){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF34A853))
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home Icon (Selected)
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Home",
            tint = Color.White,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF1A3C34))
                .padding(4.dp)
                .clickable{
                    navController.navigate("homepage")
                }
        )

        // Apps Icon
        Icon(
            imageVector = Icons.Default.Menu, //Sementara
            contentDescription = "Apps",
            tint = Color.White,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF1A3C34))
//                .padding(4.dp)
                .clickable {navController.navigate("peminjaman")}

        )

        // Profile Icon
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            tint = Color.White,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF1A3C34))
                .clickable {navController.navigate("profile")}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FooterPreview(navController: NavHostController = rememberNavController()) {
    Footer(navController = navController)
}