package com.example.loginkmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.loginkmp.models.getDatabaseBuilder
import com.example.loginkmp.data.ProductRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val db = getDatabaseBuilder(applicationContext).build()
        val repo = ProductRepository(db, AuthClient)

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