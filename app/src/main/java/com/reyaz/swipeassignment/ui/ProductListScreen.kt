package com.reyaz.swipeassignment.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.reyaz.swipeassignment.R
import com.reyaz.swipeassignment.data.db.entity.ProductEntity
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductViewModel = koinViewModel(),
    onNavigateToNotification: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.loadProducts()
            pullToRefreshState.endRefresh()
        }
    }
    var isSearchBarVisible by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var itemCount by remember { mutableStateOf(3) }
    // Show snackbar when error occurs
    LaunchedEffect(uiState.productAdditionError) {
        uiState.productAdditionError?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            // Clear the error after showing snackbar
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            if (!isSearchBarVisible)
                TopAppBar(
                    title = {
                        Image(
                            imageVector = ImageVector.vectorResource(if (isSystemInDarkTheme()) R.drawable.logo_night else R.drawable.logo),
                            contentDescription = "logo",
                            modifier = Modifier
                                .size(80.dp)
                                .scale(1.5f)
                                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                        )
                    },
                    scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(modifier = Modifier.padding(8.dp)) {
                                BadgedBox(
                                    badge = {
                                        if (itemCount > 0) {
                                            Badge(
                                                containerColor = Color.Red,
                                                contentColor = Color.White
                                            ) {
                                                Text("$itemCount")
                                            }
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Notifications, "Notification")
                                }
                            }
                            IconButton(onClick = { isSearchBarVisible = !isSearchBarVisible }) {
                                Icon(Icons.Default.Search, "Search")
                            }
                        }
                    }
                )
            else
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { query ->
                        searchQuery = query
                        viewModel.updateSearchQuery(query)
                    },
                    onSearch = {
                        viewModel.updateSearchQuery(searchQuery)
                    },
                    placeholder = { Text("Search") },
                    active = isSearchBarVisible,
                    onActiveChange = { active ->
                        if (!active) {
                            isSearchBarVisible = false
                            searchQuery = ""
                            viewModel.updateSearchQuery("")
                        }
                    },
                    leadingIcon = {
                        IconButton(onClick = { isSearchBarVisible = false }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                "back"
                            )
                        }
                    }
                ) {

                    Column(modifier = Modifier.fillMaxHeight()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(uiState.searchList) {
                                ProductItem(it)
                            }
                        }
                    }
                }
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { isBottomSheetVisible = true },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Default.Add, "add")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection),
    ) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {
            if (uiState.isLoading)
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                columns = GridCells.Adaptive(180.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.products) { product ->
                    ProductItem(product = product)
                }
            }

            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
        if (isBottomSheetVisible)
            AddProductBottomSheet(onDismiss = { isBottomSheetVisible = false })

    }
}

@Composable
fun ProductItem(product: ProductEntity) {

    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .clickable { showDialog = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box {
            Column(
                modifier = Modifier
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    onLoading = {},
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)) {
                    Text(
                        text = product.productName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Price: ₹${product.price}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(text = "Tax: ${product.tax}%", style = MaterialTheme.typography.bodySmall)
                }
            }
            Text(
                text = product.productType,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .background(Color(0xFFCE7A00).copy(alpha = 0.9f))
                    .padding(end = 10.dp, start = 8.dp)
                    .align(alignment = Alignment.BottomEnd)
            )
        }
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(shape = RoundedCornerShape(12.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    product.image?.let {
                        Image(
                            painter = rememberImagePainter(
                                product.image
                            ),
                            contentDescription = null,
                            modifier = Modifier
                        )
                    } ?: Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.no_image),
                        contentDescription = "upload image",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier
                            .size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { showDialog = false }) {
                        Text("Close")
                    }
                }
            }
        }
    }
}
