package com.example.loginkmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.loginkmp.models.getDatabaseBuilder
import com.example.loginkmp.data.ProductRepository

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val db = getDatabaseBuilder(applicationContext).build()
        val client = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
        val repo = ProductRepository(db.productDao(), client)

        setContent {
            App(repo)
        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App(TODO())
//}