package com.example.loginkmp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.loginkmp.data.ProductRepository
import com.example.loginkmp.models.Product
import com.example.loginkmp.theme.SuccessGreen
import com.example.loginkmp.theme.WarningYellow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int, 
    onBack: () -> Unit,
    repository: ProductRepository
) {
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var selectedImageIndex by remember { mutableStateOf(0) }

    LaunchedEffect(productId) {
        repository.getProduct(productId)
            .onSuccess {
                product = it
                isLoading = false
            }
            .onFailure {
                error = it.message
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.title ?: "Product Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Error: $error",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            else -> {
                product?.let { prod ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Image Carousel
                        if (!prod.images.isNullOrEmpty()) {
                            ImageCarousel(
                                images = prod.images,
                                selectedIndex = selectedImageIndex,
                                onImageSelected = { selectedImageIndex = it }
                            )
                        }

                        // Product Details
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Title and Brand
                            Text(
                                text = prod.title,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            if (!prod.brand.isNullOrEmpty()) {
                                Text(
                                    text = prod.brand,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Price and Rating Card
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "$${prod.price}",
                                            style = MaterialTheme.typography.headlineLarge,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (prod.discountPercentage != null && prod.discountPercentage > 0) {
                                            Text(
                                                text = "${prod.discountPercentage}% OFF",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.tertiary
                                            )
                                        }
                                    }
                                    
                                    RatingDisplay(rating = prod.rating ?: 0.0)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Stock Status
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (prod.stock > 0) 
                                        Icons.Default.CheckCircle
                                    else 
                                        Icons.Default.Cancel,
                                    contentDescription = null,
                                    tint = if (prod.stock > 0) 
                                        SuccessGreen 
                                    else 
                                        MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (prod.stock > 0) 
                                        "In Stock (${prod.stock} available)" 
                                    else 
                                        "Out of Stock",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (prod.stock > 0) 
                                        SuccessGreen 
                                    else 
                                        MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Description
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = prod.description,
                                style = MaterialTheme.typography.bodyLarge,
                                lineHeight = 24.sp
                            )

                            // Additional Information
                            if (!prod.category.isNullOrEmpty() || !prod.returnPolicy.isNullOrEmpty()) {
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                HorizontalDivider()
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Text(
                                    text = "Additional Information",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                if (!prod.category.isNullOrEmpty()) {
                                    InfoRow(
                                        label = "Category",
                                        value = prod.category
                                    )
                                }
                                
                                if (!prod.returnPolicy.isNullOrEmpty()) {
                                    InfoRow(
                                        label = "Return Policy",
                                        value = prod.returnPolicy
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageCarousel(
    images: List<String>,
    selectedIndex: Int,
    onImageSelected: (Int) -> Unit
) {
    Column {
        // Main Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = images[selectedIndex],
                contentDescription = "Product image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // Image Thumbnails
        if (images.size > 1) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(images) { index, imageUrl ->
                    Card(
                        modifier = Modifier
                            .size(80.dp)
                            .clickable { onImageSelected(index) },
                        border = if (index == selectedIndex) 
                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary) 
                        else 
                            null
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Thumbnail ${index + 1}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RatingDisplay(rating: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = WarningYellow,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = String.format("%.1f", rating),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = " / 5.0",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(120.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}