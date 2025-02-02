package com.reyaz.swipeassignment.presentation.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBarDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.reyaz.swipeassignment.R
import com.reyaz.swipeassignment.presentation.product.components.AddProductBottomSheet
import com.reyaz.swipeassignment.presentation.product.components.ProductItem
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductViewModel = koinViewModel(), onNavigateToNotification: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            delay(1_000)
            viewModel.loadProducts()
            pullToRefreshState.endRefresh()
        }
    }
    var isSearchBarVisible by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    // snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error, duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            if (!isSearchBarVisible) TopAppBar(title = {
                Image(
                    imageVector = ImageVector.vectorResource(if (isSystemInDarkTheme()) R.drawable.logo_night else R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(80.dp)
                        .scale(1.5f)
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                )
            }, scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(), actions = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier
                        .padding(8.dp)
                        .clickable { onNavigateToNotification() }) {
                        BadgedBox(badge = {
                            if (uiState.unViewedCount > 0) {
                                Badge(
                                    containerColor = Color.Red, contentColor = Color.White
                                ) {
                                    Text("${uiState.unViewedCount}")
                                }
                            }
                        }) {
                            Icon(Icons.Default.Notifications, "Notification")
                        }
                    }
                    // search icon
                    IconButton(onClick = { isSearchBarVisible = !isSearchBarVisible }) {
                        Icon(Icons.Default.Search, "Search")
                    }
                }
            })
            else SearchBar(query = searchQuery,
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
                            Icons.Default.ArrowBack, "back"
                        )
                    }
                }) {

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
                containerColor = MaterialTheme.colorScheme.primary,
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
        Box(contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.padding(innerPadding)) {
                if (uiState.isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                else if (uiState.products.isEmpty()) Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.no_item),
                        contentDescription = "no data"
                    )
                }
                else {
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
            }
        }
        if (isBottomSheetVisible) AddProductBottomSheet(viewModel = viewModel, onDismiss = {
            isBottomSheetVisible = false
        })
    }
}
