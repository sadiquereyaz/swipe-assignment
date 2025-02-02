# Swipe Assignment - Product Listing and Add Product App

This repository contains the implementation of an Android app for the Swipe Assignment, which includes two main screens: a **Product Listing Screen**, an **Add Product Screen** and **Notification Screen**. The app is built using modern Android development practices, including **MVVM architecture**, **Jetpack Compose**, **Retrofit**, **Room Database**, and **Koin** for dependency injection.

**Android APK File**: [Download here](https://drive.google.com/file/d/1dA9fplc-rKs-VhppKA--fyoMWitjlgCq/view?usp=sharing)

## Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Technologies Used](#technologies-used)
4. [Project Structure](#project-structure)
5. [Setup and Installation](#setup-and-installation)
6. [Future Improvements](#future-improvements)
7. [Screenshots](#screenshots)



## Overview

The app allows users to:
- View a list of products fetched from an API.
- Search for products by name, type, or price.
- Add new products with details such as product name, type, price, and tax.
- Upload product images (optional).
- Work offline by saving products locally and syncing them when the internet is available even when app is closed.

## Features

### 1. **Product Listing Screen**
- Displays a list of products fetched from the API.
- Product Image can be `Zoomed`.

- When no internet connection, show products saved fetched at last connection.
- Supports searching for products by name, type, or price.
- Shows a progress bar while loading products.
- Displays a default image if the product image URL is empty.
- Allows navigation to the **Add Product Screen** via a floating action button.

### 2. **Add Product Screen**
- Allows users to add a new product with the following details:
  - Product type (selected from a **dropdown**).
  - Product name.
  - Selling price.
  - Tax rate.
  - Optional image upload (JPEG or PNG format).
- **Validates** input fields (e.g., non-empty product name, valid decimal numbers for price and tax).
- Submits the product data to the API using a POST request.
- Shows a progress indicator while uploading the product.
- Provides feedback to the user upon successful or failed product upload in **Snackbar**.

### 3. **Notification Screen**
- Shows notifications for **history** product upload along with their status as *progress*, *success*, and *failure*.
- Notification **new notification badge** and **status update** handled. 
- Notification feedback and status is handled even when app is closed.

### 4. **Offline Functionality**
- Products added offline are saved locally using **Room Database**.
- When the internet is available, the app automatically syncs the locally saved products with the server.
- The app uses **Room Database** to save products locally when the user is offline. When the internet is available, the app automatically syncs the locally saved products with the server using **WorkManager**.

    ### How It Works:
    1. When a product is added offline, it is saved in the local database.
    2. The app periodically checks for an internet connection.
    3. When the internet is available, the app uploads the locally saved products to the server.

## Technologies Used

### Libraries and Tools
- **MVVM Architecture**: For separating concerns and improving maintainability.
- **WorkManager**: For handling background tasks like syncing offline data.
- **Koin**: For dependency injection.
- **Retrofit**: For making network requests to the API.
- **Room Database**: For local storage of products when offline.
- **Coil**: For loading and displaying images from URLs.
- **Kotlin Coroutines**: For asynchronous programming.
- **Jetpack Compose**: For building the UI.

## Project Structure

The project is organized into the following packages:

- **`data`**: Contains the data layer, including API calls, database entities, and repositories.
- **`domain`**: Contains the business logic and use cases.
- **`presentation`**: Contains the UI components and ViewModels.
- **`di`**: Contains dependency injection modules using Koin.
- **`utils`**: Contains utility classes like `NotificationHelper`.
- **`worker`**: Contains background tasks like `ProductUploadWorker` for syncing offline data.

```
└── sadiquereyaz-swipe-assignment/app/src/main/
    ├── app/
    │   ├── build.gradle.kts
    │   └── src/
    │───────└── main/
    │           ├── AndroidManifest.xml
    │           ├── java/
    │           │   └── com/
    │           │       └── reyaz/
    │           │           └── swipeassignment/
    │           │               ├── MainActivity.kt
    │           │               ├── base/
    │           │               │   └── BaseApplication.kt
    │           │               ├── data/
    │           │               │   ├── api/
    │           │               │   │   ├── SwipeApi.kt
    │           │               │   │   └── dto/
    │           │               │   │       ├── ApiResponse.kt
    │           │               │   │       └── ProductResponse.kt
    │           │               │   ├── db/
    │           │               │   │   ├── AppDatabase.kt
    │           │               │   │   ├── dao/
    │           │               │   │   │   ├── NotificationDao.kt
    │           │               │   │   │   ├── PendingUploadDao.kt
    │           │               │   │   │   └── ProductDao.kt
    │           │               │   │   └── entity/
    │           │               │   │       ├── NotificationEntity.kt
    │           │               │   │       ├── PendingUploadEntity.kt
    │           │               │   │       └── ProductEntity.kt
    │           │               │   └── repository/
    │           │               │       ├── NotificationRepositoryImpl.kt
    │           │               │       └── ProductRepositoryImpl.kt
    │           │               ├── di/
    │           │               │   ├── AppModule.kt
    │           │               │   ├── DbModule.kt
    │           │               │   └── RemoteModule.kt
    │           │               ├── domain/
    │           │               │   ├── model/
    │           │               │   │   ├── Product.kt
    │           │               │   │   ├── Resource.kt
    │           │               │   │   └── Status.kt
    │           │               │   └── repository/
    │           │               │       ├── NotificationRepository.kt
    │           │               │       └── ProductRepository.kt
    │           │               ├── presentation/
    │           │               │   ├── navigation/
    │           │               │   │   ├── AppNavigation.kt
    │           │               │   │   └── Route.kt
    │           │               │   ├── notification/
    │           │               │   │   ├── NotificationScreen.kt
    │           │               │   │   ├── NotificationUiState.kt
    │           │               │   │   ├── NotificationViewModel.kt
    │           │               │   │   └── components/
    │           │               │   │       └── NotificationItem.kt
    │           │               │   ├── product/
    │           │               │   │   ├── ProductListScreen.kt
    │           │               │   │   ├── ProductUiState.kt
    │           │               │   │   ├── ProductViewModel.kt
    │           │               │   │   └── components/
    │           │               │   │       ├── AddProductBottomSheet.kt
    │           │               │   │       ├── ImageDialogContent.kt
    │           │               │   │       ├── ProductItem.kt
    │           │               │   │       └── ZoomableImageContainer.kt
    │           │               │   └── theme/
    │           │               │       ├── Color.kt
    │           │               │       ├── Theme.kt
    │           │               │       └── Type.kt
    │           │               ├── utils/
    │           │               │   ├── NetworkUtils.kt
    │           │               │   └── NotificationHelper.kt
    │           │               └── worker/
    │           │                   ├── CustomWorkerFactory.kt
    │           │                   └── ProductUploadWorker.kt
 
```

## Setup and Installation

### Steps to Run the Project
1. Clone the repository:
   ```bash
   git clone https://github.com/sadiquereyaz/swipe-assignment.git
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Build and run the app on an emulator or physical device.

### Dependencies
The project uses the following dependencies (already included in the `build.gradle` files):

- **Koin**: For dependency injection.
- **WorkManager**: For background tasks.
- **Retrofit**: For API calls.
- **Room**: For local database storage.
- **Coil**: For image loading.

## Future Improvements

1. **Pagination**: Implement pagination for the product list to handle large datasets.
2. **Image Resize**: Compress images and crop to 1:1 ratio before uploading to reduce network usage.
3. **Enhanced Error Handling**: Provide more detailed error messages for failed API requests.
4. **Multiple Image**: Show Multiple Image in product detail and multiple image can be uploaded.

## Screenshots

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/c87764b3-123d-4cd7-ac2d-a134c7ade694" alt="Image 7" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/02b4d2c5-12d3-47c6-8ab3-030f648f2f49" alt="Image 2" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/66a2ecda-81d1-473a-9f93-7bc328dec74f" alt="Image 1" width="200"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/b301c86e-7e60-41e3-be9e-6577ebd5249e" alt="Image 6" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/2f7f221e-d360-435d-9d05-d9c85bf54699" alt="Image 4" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/add5d69d-1de9-4cc5-b94d-a1bceca6151c" alt="Image 5" width="200"></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/c06b1e8d-0a38-47ae-a240-cfc0b9b7b9f6" alt="Image 3" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/7c9dc87d-7254-468a-bdbe-e68c2af926ce" alt="Image 1" width="200"/></td>
  </tr>
</table>

https://github.com/user-attachments/assets/00061f67-100d-4067-b7f5-5c04be33ac26

https://github.com/user-attachments/assets/78c1d0f1-d902-4126-a6ce-ee960fd0f677

https://github.com/user-attachments/assets/4a60c02e-b696-4870-a7ce-28b466df2852

## Contact

For any questions or feedback, feel free to reach out:
- **Name**: Md Sadique
- **Email**: mdsadique47@gmail.com
- **Linked In**: [Md Sadique](https://linkedin.com/in/sadiquereyaz)

---

Thank you for reviewing my project! I hope it meets your expectations. 😊

