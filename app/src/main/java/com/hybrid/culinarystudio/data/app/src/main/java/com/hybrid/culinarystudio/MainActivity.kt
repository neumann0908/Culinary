package com.hybrid.culinarystudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hybrid.culinarystudio.data.CulinaryDatabase
import com.hybrid.culinarystudio.data.InsumoEntity
import kotlinx.coroutines.launch

// Paleta Pixel Art Retro
val PixelBackground = Color(0xFF101D28)
val PixelPanel = Color(0xFF1B2A38)
val PixelBorder = Color(0xFF385266)
val PixelYellow = Color(0xFFFFB703)
val PixelGreen = Color(0xFF06D6A0)
val PixelRed = Color(0xFFEF476F)
val PixelPurple = Color(0xFF8338EC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = CulinaryDatabase.getDatabase(this)

        setContent {
            val insumosState by database.insumoDao().getAllInsumos().collectAsState(initial = emptyList())
            val coroutineScope = rememberCoroutineScope()

            Scaffold(
                containerColor = PixelBackground,
                floatingActionButton = {
                    Box(
                        modifier = Modifier
                            .background(PixelPurple)
                            .border(2.dp, Color.White, RoundedCornerShape(0.dp))
                            .clickable {
                                coroutineScope.launch {
                                    database.insumoDao().upsertInsumo(
                                        InsumoEntity(
                                            nombre = "Harina de Trigo",
                                            unidadCompra = "Kg",
                                            costoCompra = 2.5,
                                            cantidadCompra = 1000.0,
                                            costoUnitarioBase = 0.0025,
                                            stockActualGramos = 3500.0,
                                            stockMinimoGramos = 1000.0
                                        )
                                    )
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text("[ 👾 CHEF IA ]", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .border(2.dp, PixelBorder)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("☁️ AUTO-SAVE: ACTIVO", color = PixelGreen, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        Text("TASA: 1.00 USD", color = PixelYellow, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("> ALMACÉN DE INSUMOS", color = PixelYellow, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(insumosState) { insumo ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(PixelPanel)
                                    .border(2.dp, PixelBorder)
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(insumo.nombre.uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                        Text("$${String.format("%.4f", insumo.costoRealPorGramoServido)}/g", color = PixelYellow, fontFamily = FontFamily.Monospace)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("STOCK: ${insumo.stockActualGramos}g", color = PixelGreen, fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
