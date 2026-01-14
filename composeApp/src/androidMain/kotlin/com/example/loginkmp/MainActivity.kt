package com.example.loginkmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.loginkmp.data.local.db.getDatabaseBuilder
import com.example.loginkmp.data.repository.CartRepositoryImpl
import com.example.loginkmp.data.repository.ProductRepositoryImpl

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
        val productRepo = ProductRepositoryImpl(db.productDao(), client)
        val cartRepo = CartRepositoryImpl()

        setContent {
            App(productRepo, cartRepo)
        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App(TODO())
//}