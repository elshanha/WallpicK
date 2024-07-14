package com.elshan.wallpick.utils


// WALLPAPER DETAILS CODE

/*    val imagePainter = rememberAsyncImagePainter(
//        model = ImageRequest.Builder(LocalContext.current)
//            .data(wallpaper.imageUrl)
//            .size(Size.ORIGINAL)
//            .build()
//    )
//    val imageState = imagePainter.state
//
//    if (imageState is AsyncImagePainter.State.Success) {
//
//        val image = imageState.result.drawable.toBitmap()
//
//        dominantColor = getAverageColor(imageBitmap = image.asImageBitmap())
//        secondaryColor = getAverageColor(imageBitmap = image.asImageBitmap(), isSecondary = true)
//
//        Column(
//            modifier = modifier
//                .fillMaxSize()
//                .statusBarsPadding(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            var showBottomSheet by remember { mutableStateOf(false) }
//
//            if (showBottomSheet) {
//                CustomModalBottomSheet(
//                    wallpaper = wallpaper,
//                    onDismissRequest = { showBottomSheet = false }
//                )
//            }
//
//            Box(
//                modifier = modifier
//                    .padding(horizontal = 16.dp)
//            ) {
//                Image(
//                    painter = imagePainter,
//                    contentDescription = wallpaper.name,
//                    contentScale = ContentScale.Crop,
//                    modifier = modifier
//                        .clickable {
//                            navController.navigate(
//                                Screen.FullScreenImage(
//                                    imageUrl = wallpaper.imageUrl,
//                                    name = wallpaper.name,
//                                    resolution = wallpaper.resolution,
//                                    category = wallpaper.category
//                                )
//                            )
//                        }
//                        .border(
//                            width = 3.dp,
//                            color = dominantColor,
//                            shape = MaterialTheme.shapes.extraLarge
//                        )
//                        .fillMaxWidth()
//                        .clip(MaterialTheme.shapes.extraLarge)
//                        .aspectRatio(1f / 1f),
//                )
//            }
//
//            DetailBox(
//                modifier = modifier,
//                wallpaper = wallpaper,
//                color = dominantColor
//            )
//
//            Row(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//
//                Button(
//                    modifier = Modifier,
//                    onClick = {
//                        showBottomSheet = true
//                    }
//                ) {
//                    Text(text = "Apply")
//                }
//
//                Button(
//                    onClick = {
//                    }
//                ) {
//                    Text(text = "Favorite")
//                }
//
//                Button(
//                    onClick = {
//                    }
//                ) {
//                    Text(text = "Download")
//                }
//            }
//        }
//    } */


// WALLPAPER ITEM CODE

/*    Box(
//        modifier = Modifier
//            .padding(
//                bottom = 8.dp,
//                start = 4.dp,
//                end = 4.dp
//            )
//            .clip(RoundedCornerShape(12.dp))
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(12.dp))
//                .border(
//                    width = 1.dp,
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            dominantColor,
//                            MaterialTheme.colorScheme.secondaryContainer,
//                        ),
//                    ),
//                    shape = RoundedCornerShape(12.dp)
//                ),
//        ) {
//
//            when (imageState) {
//                AsyncImagePainter.State.Empty -> {
//
//
//                }
//
//                is AsyncImagePainter.State.Error -> {
//                    Image(
//                        painter = painterResource(id = R.drawable.image_not_found),
//                        contentDescription = wallpaper.name,
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier
//                            .aspectRatio(3f / 4f)
//                    )
//                }
//
//                is AsyncImagePainter.State.Loading -> {
//
//                    Image(
//                        painter = painterResource(id = R.drawable.photo),
//                        contentDescription = wallpaper.name,
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier
//                            .aspectRatio(3f / 4f)
//                    )
//                }
//
//                is AsyncImagePainter.State.Success -> {
//                    val image = imageState.result.drawable.toBitmap()
//                    dominantColor = getAverageColor(imageBitmap = image.asImageBitmap())
//                    Box {
//                        Image(
//                            bitmap = image.asImageBitmap(),
//                            contentDescription = wallpaper.name,
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier
//                                .aspectRatio(3f / 4f)
//                                .combinedClickable(
//                                    onClick = {
//                                        navController.navigate(
//                                            Screen.WallpaperDetails(
//                                                wallpaper.category,
//                                                wallpaper.imageUrl,
//                                                wallpaper.name,
//                                                wallpaper.resolution
//                                            )
//                                        )
//                                    },
//                                    onLongClick = {
//
//                                    }
//                                )
//                        )
//                        IconButton(
//                            modifier = Modifier
//
//                                .padding(8.dp)
//                                .align(Alignment.BottomEnd),
//                            onClick = {
//                                onEvent.apply {
//                                    val entity = wallpaper.toWallpaperEntity(isFavorite = true)
//                                    if (mainUiState.favoriteWallpapers.contains(entity)) {
//                                        this(
//                                            MainUiEvents.RemoveFromFavorite(
//                                                entity,
//                                            )
//                                        )
//                                        this(
//                                            MainUiEvents.ShowSnackBar(
//                                                message = "Removed from favorites",
//                                                isBottomAppBarVisible = isBottomBarVisible
//                                            )
//                                        )
//                                    } else {
//                                        this(
//                                            MainUiEvents.AddToFavorite(
//                                                wallpaper = wallpaper
//                                            )
//                                        )
//                                        this(
//                                            MainUiEvents.ShowSnackBar(
//                                                message = "Added to favorites",
//                                                isBottomAppBarVisible = isBottomBarVisible
//                                            )
//                                        )
//                                    }
//
//                                }
//                            }) {
//                            Icon(
//                                imageVector =
//                                if (wallpaper.toWallpaperEntity(isFavorite = true) in mainUiState.favoriteWallpapers)
//                                    Icons.Filled.Favorite
//                                else Icons.Outlined.FavoriteBorder,
//                                contentDescription = "heart",
//                                tint = if (mainUiState.favoriteWallpapers.contains(
//                                        wallpaper.toWallpaperEntity(
//                                            isFavorite = true
//                                        )
//                                    )
//                                )
//                                    animatedColor(
//                                        targetColor = MaterialTheme.colorScheme.error
//                                    )
//                                else
//                                    Color.White
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    } */


//fun convertHardwareBitmapToSoftware(hardwareBitmap: Bitmap): Bitmap {
//    return hardwareBitmap.copy(Bitmap.Config.ARGB_8888, true)
//}