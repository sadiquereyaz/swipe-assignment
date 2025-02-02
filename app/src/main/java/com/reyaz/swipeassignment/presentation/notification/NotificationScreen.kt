package com.reyaz.swipeassignment.presentation.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reyaz.swipeassignment.R
import com.reyaz.swipeassignment.data.db.entity.NotificationEntity
import com.reyaz.swipeassignment.data.db.entity.PendingUploadEntity
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = koinViewModel(),
    navigateToHome:()->Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.fetchAll()
            pullToRefreshState.endRefresh()
        }
    }
    val errorShown = remember {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = uiState.error, block = {
        if (uiState.error != null) {
            errorShown.value = true
        }
    })
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Notification")
                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                navigationIcon = { IconButton(onClick = {navigateToHome()}){
                    Icon(Icons.Default.ArrowBack, "back")
                } }

            )
        },

        modifier = Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection),
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxWidth().padding(innerPadding)){
            if(errorShown.value){
                uiState.error?.let { error ->
                    LaunchedEffect(key1 = Unit, block = {
                        kotlinx.coroutines.delay(5000)
                        errorShown.value = false
                    })
                    Text(
                        error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.error)
                            .padding(8.dp)
                    )

                }

            }
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            } else if(uiState.notificationList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                ) {
                    itemsIndexed(uiState.notificationList) { index, item ->
                        NotificationItem(modifier = Modifier, item = item)
                        if (index != uiState.notificationList.lastIndex) {
                            HorizontalDivider(modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }else{
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Image(painter = painterResource(id = R.drawable.no_item), contentDescription = "no data")
                }
            }
            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }

    }
}

@Composable
fun NotificationItem(modifier: Modifier = Modifier, item: NotificationEntity) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = item.productName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(item.productType, maxLines = 1)

            Text(item.status.name, fontStyle = FontStyle.Italic, color = item.status.color)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun NotificationPreview() {
   /* val list =
        PendingUploadEntity(
            id = 0L,
            productName = "Product Name",
            productType = "Clothing",
            price = 123.3,
            tax = 4.1,
            imageUri = null,
            timestamp = System.currentTimeMillis(),
        )

    NotificationItem(item = list)*/
}


