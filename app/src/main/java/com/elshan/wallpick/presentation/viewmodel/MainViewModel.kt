package com.elshan.wallpick.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.elshan.wallpick.main.data.local.WallpaperEntity
import com.elshan.wallpick.main.data.mappers.toWallpaperEntity
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.elshan.wallpick.main.domain.repository.MainRepository
import com.elshan.wallpick.notifications.NotificationsService
import com.elshan.wallpick.presentation.screen.login.sign_in.GoogleAuthUiClient
import com.elshan.wallpick.presentation.screen.login.sign_in.SignInResult
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.utils.Resource
import com.elshan.wallpick.utils.custom.FilterType
import com.elshan.wallpick.utils.datastore.DataStoreManager
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mainRepository: MainRepository,
    private val notificationsService: NotificationsService,
    val googleAuthUiClient: GoogleAuthUiClient,
) : ViewModel() {

    private val _mainUiState = MutableStateFlow(MainUiState())
    val uiState = _mainUiState.asStateFlow()

    private var snackbarHostState = SnackbarHostState()

    private var searchJob: Job? = null


    init {
        loadWallpapers()
        loadOnboardingPreference()
        onEvent(MainUiEvents.LoadLayoutPreference)
        onEvent(MainUiEvents.LoadNotificationPreference)
    }


    fun setSnackbarHostState(state: SnackbarHostState) {
        snackbarHostState = state
    }


    private fun loadWallpapers(fetchFromRemote: Boolean = false) {
        if (_mainUiState.value.allWallpapers.isEmpty() && _mainUiState.value.filteredWallpapers.isEmpty()) {
            fetchWallpapers(fetchFromRemote = true)
        } else {
            fetchWallpapers(fetchFromRemote)
        }
        filterWallpapers(FilterType.LATEST, fetchFromRemote)
        fetchCategories()
        getFavorites()
    }

    fun onEvent(event: MainUiEvents) {
        when (event) {
            is MainUiEvents.GetFromFirebase ->
                fetchWallpapers(fetchFromRemote = true)

            is MainUiEvents.GetFavorites -> getFavorites()
            is MainUiEvents.DeleteAllFavorites -> deleteAllFavorites()

            is MainUiEvents.ShowToast -> {
                Toast.makeText(
                    context,
                    event.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is MainUiEvents.GetWallpapersByCategory ->
                fetchWallpapersByCategory(event.category)


            is MainUiEvents.ShowSnackBar -> {
                showCustomSnackbar(
                    message = event.message,
                    actionLabel = event.actionLabel,
                    duration = event.duration ?: SnackbarDuration.Short,
                    customDurationMillis = event.customDurationMillis,
                    isBottomAppBarVisible = event.isBottomAppBarVisible ?: false,
                )
            }

            is MainUiEvents.AddToFavorite ->
                addWallpaperToFavorites(event.wallpaper)

            is MainUiEvents.RemoveFromFavorite ->
                removeWallpaperFromFavorites(event.wallpaperEntity)

            is MainUiEvents.SearchWallpapers ->
                searchWallpapers(event.query)

            is MainUiEvents.DownloadWallpaper ->
                downloadWallpaper(event.wallpaper)

            is MainUiEvents.FilterWallpapers -> filterWallpapers(event.filterType)

            is MainUiEvents.ShareWallpaper ->
                shareWallpaper(event.wallpaper.imageUrl.toUri(), event.context)

            is MainUiEvents.ShowNotification ->
                showNotification(event.value)

            is MainUiEvents.SearchFavorites -> searchFavorites(event.query)
            is MainUiEvents.ColorSelected -> searchWallpapersByColor(event.color)

            is MainUiEvents.SetPaletteColors -> setPaletteColors(event.palette)

            is MainUiEvents.StoreUserInfo -> storeUserInfo(event.user)
            is MainUiEvents.GetUserInfo -> getUserInfo(event.uid)
            is MainUiEvents.SignOut -> signOut()
            is MainUiEvents.SignIn -> signIn()
            is MainUiEvents.ResetState -> resetState()

            is MainUiEvents.GetMostDownloadedWallpapers -> getMostDownloadedWallpapers(event.minDownloads)
            is MainUiEvents.GetPopularWallpapers -> getPopularWallpapers(event.minViews)
            is MainUiEvents.TrackEvent ->
                trackEvent(event.category, event.wallpaperId, event.interactionType)

            is MainUiEvents.LoadLayoutPreference -> loadLayoutPreference()
            is MainUiEvents.SaveLayoutPreference -> saveLayoutPreference(event.isGridLayout)
            is MainUiEvents.CheckWallpaperDownloaded -> checkWallpaperDownloaded(event.wallpaperId)
            is MainUiEvents.SetWallpaperDownloaded -> setWallpaperDownloaded(
                event.wallpaperId,
                event.downloaded
            )

            is MainUiEvents.LoadNotificationPreference -> loadNotificationPreference()
            is MainUiEvents.SaveNotificationPreference -> saveNotificationPreference(event.enabled)
            is MainUiEvents.ToggleNotification -> toggleNotification(event.enabled)
            is MainUiEvents.CompleteOnboarding -> setOnboardingCompleted()
        }
    }

    private fun loadOnboardingPreference() {
        viewModelScope.launch {
            DataStoreManager.isOnboardingCompleted(context).collect { completed ->
                _mainUiState.update { it.copy(onboardingCompleted = completed, isReady = true) }
            }
        }
    }

    private fun setOnboardingCompleted() {
        viewModelScope.launch {
            DataStoreManager.setOnboardingCompleted(context, true)
            _mainUiState.update { it.copy(onboardingCompleted = true) }
        }
    }

    private fun loadNotificationPreference() {
        viewModelScope.launch {
            DataStoreManager.isNotificationsEnabled(context).collect { enabled ->
                _mainUiState.update { it.copy(notificationsEnabled = enabled) }
            }
        }
    }

    private fun saveNotificationPreference(enabled: Boolean) {
        viewModelScope.launch {
            DataStoreManager.setNotificationsEnabled(context, enabled)
            _mainUiState.update { it.copy(notificationsEnabled = enabled) }
        }
    }

    private fun loadLayoutPreference() {
        viewModelScope.launch {
            DataStoreManager.isGridLayout(context).collect { isGridLayout ->
                _mainUiState.update { it.copy(isGridLayout = isGridLayout) }
            }
        }
    }

    private fun toggleNotification(enabled: Boolean) {
        viewModelScope.launch {
            if (enabled) {
                notificationsService.showNotification("Notification enabled")
            } else {
                notificationsService.hideNotification()
            }
        }
    }

    private fun saveLayoutPreference(isGridLayout: Boolean) {
        viewModelScope.launch {
            DataStoreManager.setGridLayout(context, isGridLayout)
            _mainUiState.update { it.copy(isGridLayout = isGridLayout) }
        }
    }

    private fun setWallpaperDownloaded(wallpaperId: String, downloaded: Boolean) {
        viewModelScope.launch {
            DataStoreManager.setWallpaperDownloaded(
                context, wallpaperId,
                downloaded
            )
            _mainUiState.update {
                val newDownloadedWallpapers = if (downloaded) {
                    it.downloadedWallpapers + wallpaperId
                } else {
                    it.downloadedWallpapers - wallpaperId
                }
                it.copy(downloadedWallpapers = newDownloadedWallpapers)
            }
        }
    }

    private fun checkWallpaperDownloaded(wallpaperId: String) {
        viewModelScope.launch {
            DataStoreManager.isWallpaperDownloaded(context, wallpaperId)
                .collect { isDownloaded ->
                    _mainUiState.update {
                        val newDownloadedWallpapers = if (isDownloaded) {
                            it.downloadedWallpapers + wallpaperId
                        } else {
                            it.downloadedWallpapers - wallpaperId
                        }
                        it.copy(downloadedWallpapers = newDownloadedWallpapers)
                    }
                }
        }
    }

    private fun filterWallpapers(filterType: FilterType, fetchFromRemote: Boolean = false) {
        viewModelScope.launch {
            val filteredWallpapers = when (filterType) {
                FilterType.LATEST -> mainRepository.fetchWallpapers(fetchFromRemote)
                    .firstOrNull()?.data ?: emptyList()

                FilterType.POPULAR -> mainRepository.getPopularWallpapers(10).firstOrNull()
                    ?: emptyList()

                FilterType.MOST_DOWNLOADED -> mainRepository.getMostDownloadedWallpapers(10)
                    .firstOrNull() ?: emptyList()
            }
            _mainUiState.update {
                it.copy(
                    filteredWallpapers = filteredWallpapers,
                    selectedFilter = filterType
                )
            }
        }
    }

    fun extractColorsForNewWallpapers() {
        viewModelScope.launch {
            mainRepository.extractColorsForNewWallpapers(context)
        }
    }


    // TODO : SIGN IN AND SIGN OUT

    fun onSignInResult(result: SignInResult) {
        _mainUiState.update {
            it.copy(
                fireBaseUser = result.data?.firebaseUser,
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
        result.data?.firebaseUser?.let { user ->
            storeUserInfo(user)
            getUserInfo(user.uid)
        }
    }

    private fun resetState() {
        _mainUiState.update {
            it.copy(

                isSignInSuccessful = false,
                signInError = null
            )
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            val intentSender = googleAuthUiClient.signIn()
            _mainUiState.update {
                it.copy(
                    intentSender = intentSender
                )
            }
        }
    }

    fun handleSignInResult(intent: Intent) {
        viewModelScope.launch {
            val signInResult = googleAuthUiClient.signInWithIntent(intent)
            onSignInResult(signInResult)
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            googleAuthUiClient.signOut()
            _mainUiState.update {
                it.copy(
                    fireBaseUser = null,
                    isSignInSuccessful = false,
                    user = null
                )
            }
        }
    }


    private fun setPaletteColors(palette: Palette) {
        viewModelScope.launch {
            val dominantColor = Color(palette.getDominantColor(Color.White.toArgb()))
            val vibrantColor = Color(palette.getVibrantColor(Color.White.toArgb()))
            val mutedColor = Color(palette.getMutedColor(Color.White.toArgb()))
            val darkMutedColor = Color(palette.getDarkMutedColor(Color.White.toArgb()))
            val lightMutedColor = Color(palette.getLightMutedColor(Color.White.toArgb()))
            val darkVibrantColor = Color(palette.getDarkVibrantColor(Color.White.toArgb()))
            val lightVibrantColor = Color(palette.getLightVibrantColor(Color.White.toArgb()))
            palette.swatches.forEach { swatch ->
                _mainUiState.update {
                    it.copy(
                        bodyTextColor = Color(swatch.bodyTextColor),
                        titleTextColor = Color(swatch.titleTextColor),

                        )
                }
            }

            _mainUiState.update {
                it.copy(
                    dominantColor = dominantColor,
                    vibrantColor = vibrantColor,
                    mutedColor = mutedColor,
                    darkMutedColor = darkMutedColor,
                    lightMutedColor = lightMutedColor,
                    darkVibrantColor = darkVibrantColor,
                    lightVibrantColor = lightVibrantColor,
                )
            }
        }
    }

    fun createNotificationChannel() {
        notificationsService.createNotificationChannel()
    }

    private fun showNotification(value: String) {
        viewModelScope.launch {
            notificationsService.showNotification(value)
        }
    }

    fun hideNotification() {
        notificationsService.hideNotification()
    }

    private fun fetchWallpapers(fetchFromRemote: Boolean) {
        viewModelScope.launch {
            mainRepository
                .fetchWallpapers(
                    fetchFromRemote = fetchFromRemote,
                )
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { wallpapers ->
                                _mainUiState.update {
                                    it.copy(
                                        allWallpapers = wallpapers
                                    )
                                }
                            }
                        }


                        is Resource.Error -> {
                        }

                        is Resource.Loading -> {
                        }
                    }
                }
        }

    }


    private fun fetchCategories() {
        viewModelScope.launch {
            mainRepository.fetchCategories()
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            result.message?.let { message ->
                                showCustomSnackbar(message)
                            }
                        }

                        is Resource.Loading -> {
                            result.message?.let { message ->
                                showCustomSnackbar(message)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { categories ->
                                _mainUiState.update {
                                    it.copy(
                                        categories = categories
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun fetchWallpapersByCategory(categoryName: String) {
        viewModelScope.launch {
            mainRepository.fetchWallpapersByCategory(categoryName.lowercase())
                .collect { result ->
                    result.let { wallpapers ->
                        _mainUiState.update {
                            it.copy(
                                wallpapersByCategory = wallpapers,
                            )
                        }
                    }
                }
        }
    }

    private fun addWallpaperToFavorites(wallpaper: Wallpaper) {
        viewModelScope.launch {
            val wallpaperEntity = wallpaper.toWallpaperEntity(isFavorite = true)
            mainRepository.insertWallpaper(wallpaperEntity)
            getFavorites()
        }
    }

    private fun removeWallpaperFromFavorites(wallpaper: WallpaperEntity) {
        viewModelScope.launch {
            mainRepository.updateWallpaper(wallpaper.copy(isFavorite = false))
            getFavorites()
        }
    }

    private fun deleteAllFavorites() {
        viewModelScope.launch {
            mainRepository.deleteAllFavorites()
                .collect { result ->
                    result.let {
                        _mainUiState.update {
                            it.copy(
                                favoriteWallpapers = emptyList(),
                            )
                        }
                    }
                }
            getFavorites()
        }
    }

    private fun getFavorites() {
        viewModelScope.launch {
            mainRepository.getFavorites()
                .collect { result ->
                    result.let { favorites ->
                        _mainUiState.update {
                            it.copy(
                                favoriteWallpapers = favorites,
                                filteredFavorites = favorites
                            )
                        }
                    }
                }
        }
    }

    private fun searchWallpapers(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isEmpty()) {
                _mainUiState.update {
                    it.copy(
                        query = query,
                        searchWallpapers = emptyList(),
                    )
                }
            } else {
                delay(200L)
                mainRepository.searchWallpapers(query)
                    .collect { result ->
                        result.let { wallpapers ->
                            _mainUiState.update {
                                it.copy(
                                    query = query,
                                    searchWallpapers = wallpapers,

                                    )
                            }
                        }
                    }
            }
        }
    }

    private fun searchWallpapersByColor(color: Color) {
        viewModelScope.launch {
            mainRepository.searchWallpapersByColor(color)
                .collect { result ->
                    result.let { wallpapers ->
                        _mainUiState.update {
                            it.copy(
                                selectedColor = color,
                                searchWallpapers = wallpapers,
                            )
                        }
                    }
                }
        }
    }

    private fun searchFavorites(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isEmpty()) {
                _mainUiState.update { state ->
                    state.copy(
                        favoritesQuery = query,
                        filteredFavorites = state.favoriteWallpapers,
                    )
                }
            } else {
                val filtered = _mainUiState.value.favoriteWallpapers.filter { wallpaper ->
                    wallpaper.name.contains(query, ignoreCase = true) ||
                            wallpaper.category.contains(query, ignoreCase = true)
                }
                _mainUiState.update {
                    it.copy(
                        favoritesQuery = query,
                        filteredFavorites = filtered
                    )
                }
            }
        }
    }

    private fun downloadWallpaper(wallpaper: Wallpaper) {
        val fileName = wallpaper.name
        viewModelScope.launch {
            val success = mainRepository.downloadWallpaper(
                context = context,
                imageUrl = wallpaper.imageUrl,
                imageName = fileName
            )

            if (success) {
                _mainUiState.update {
                    it.copy(
                        isDownloaded = true
                    )
                }
                showCustomSnackbar("Wallpaper downloaded successfully")
            } else {
                _mainUiState.update {
                    it.copy(
                        isDownloaded = false
                    )
                }
                showCustomSnackbar("Failed to download wallpaper")
            }
        }
    }

    private fun trackEvent(category: String, wallpaperId: String, interactionType: String) {
        viewModelScope.launch {
            mainRepository.trackInteraction(category, wallpaperId, interactionType)
        }
    }

    private fun getPopularWallpapers(minViews: Long) {
        viewModelScope.launch {
            mainRepository.getPopularWallpapers(minViews)
                .collect { popularWallpapers ->
                    _mainUiState.update {
                        it.copy(
                            popularWallpapers = popularWallpapers
                        )
                    }
                }
        }
    }

    private fun getMostDownloadedWallpapers(minDownloads: Long) {
        viewModelScope.launch {
            mainRepository.getMostDownloadedWallpapers(minDownloads)
                .collect { mostDownloadedWallpapers ->
                    _mainUiState.update {
                        it.copy(
                            mostDownloadedWallpapers = mostDownloadedWallpapers
                        )
                    }
                }
        }
    }


    private fun shareWallpaper(imageUri: Uri, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, imageUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    setDataAndType(imageUri, "image/*")
                    type = "image/jpeg"
                }
                withContext(Dispatchers.Main) {
                    context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
                    showCustomSnackbar("Wallpaper shared successfully")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showCustomSnackbar("Failed to share wallpaper: ${e.message}")
                }
            }
        }
    }

    private fun showCustomSnackbar(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        customDurationMillis: Long? = null,
        isBottomAppBarVisible: Boolean = false,
    ) {
        viewModelScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration,
            )
        }
    }

    private fun storeUserInfo(user: FirebaseUser) {
        viewModelScope.launch {
            mainRepository.storeUserInfo(user)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            result.message?.let { message ->
                                showCustomSnackbar("Failed to store user info: $message")
                            }
                        }

                        is Resource.Loading -> {
                            result.message?.let { message ->
                                showCustomSnackbar(message)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { message ->
                                showCustomSnackbar("Welcome back ${user.displayName.toString()}!")
                            }
                        }
                    }
                }
        }

    }

    private fun getUserInfo(uid: String) {
        viewModelScope.launch {
            mainRepository.getUserInfo(uid)
                .collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            result.message?.let { message ->
                                showCustomSnackbar("Failed to get user info: $message")
                            }
                        }

                        is Resource.Loading -> {
                            result.message?.let { message ->

                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { user ->
                                _mainUiState.update {
                                    it.copy(
                                        user = user
                                    )
                                }
                                //  showCustomSnackbar("Welcome back ${user.email.toString()}!")
                            }
                        }
                    }
                }
        }
    }

}

