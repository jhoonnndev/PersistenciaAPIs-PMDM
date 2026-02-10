package com.example.trabajonavegacionpmdm.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//Componente barra de búsqueda por marca o modelo de coche
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
){
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = {Text("Buscar por marca o modelo")},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar"
            )
        }, //Icono de lupa
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical =8.dp),
        singleLine = true //Busqueda en una sola linea
    )
}