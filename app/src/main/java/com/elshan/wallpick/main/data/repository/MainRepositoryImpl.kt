package com.elshan.wallpick.main.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import coil.request.ImageRequest
import com.elshan.wallpick.R
import com.elshan.wallpick.WallpicKApp
import com.elshan.wallpick.main.data.local.WallpaperDatabase
import com.elshan.wallpick.main.data.local.WallpaperEntity
import com.elshan.wallpick.main.data.mappers.toUser
import com.elshan.wallpick.main.data.mappers.toWallpaper
import com.elshan.wallpick.main.data.mappers.toWallpaperEntity
import com.elshan.wallpick.main.domain.model.Category
import com.elshan.wallpick.main.domain.model.User
import com.elshan.wallpick.main.domain.repository.MainRepository
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.elshan.wallpick.utils.Resource
import com.elshan.wallpick.utils.animation.extractColorsAndUploadToFirestore
import com.elshan.wallpick.utils.color.colorDistance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class MainRepositoryImpl(
    private val db: FirebaseFirestore,
    wallpaperDb: WallpaperDatabase
) : MainRepository {

    private val wallpaperDao = wallpaperDb.wallpaperDao

    override suspend fun insertWallpaper(wallpaperEntity: WallpaperEntity) {
        wallpaperDao.insertWallpaper(wallpaperEntity)
    }

    override suspend fun deleteWallpaper(wallpaperEntity: WallpaperEntity) {
        wallpaperDao.deleteWallpaper(wallpaperEntity)
    }

    override suspend fun updateWallpaper(wallpaperEntity: WallpaperEntity) {
        wallpaperDao.updateWallpaper(wallpaperEntity)
    }

    override suspend fun getAllWallpapers(): Flow<List<WallpaperEntity>> {
        return flow {
            val wallpapers = wallpaperDao.getAll()
            emit(wallpapers)
            return@flow
        }
    }

    override suspend fun getWallpaperByUrl(imageUrl: String): Wallpaper {
        return wallpaperDao.getWallpaperByUrl(imageUrl)?.toWallpaper() ?: Wallpaper(
            "",
            emptyList(),
            "",
            "",
            "",
            "",
            emptyList()
        )
    }


    override suspend fun getFavorites(): Flow<List<WallpaperEntity>> {
        return flow {
            val favorites = wallpaperDao.getFavoriteWallpapers()
            emit(favorites)
            return@flow
        }
    }

    override suspend fun deleteAllFavorites(): Flow<List<WallpaperEntity>> {
        return flow {
            wallpaperDao.deleteAllFavorites()
            return@flow
        }
    }

    override suspend fun deleteAllWallpapers(): Flow<List<WallpaperEntity>> {
        return flow {
            wallpaperDao.deleteAll()
            return@flow
        }
    }


    override suspend fun fetchCategories(): Flow<Resource<List<Category>>> {
        return flow {
            val categories = mutableListOf<Category>()
            try {
                val querySnapshot = db.collectionGroup("categories").get().await()
                for (document in querySnapshot.documents) {
                    val bgUrl: String = document.getString("bgUrl") ?: ""
                    val name: String = document.getString("name") ?: ""
                    categories.add(Category(bgUrl, name))
                }
                // Fetch all wallpapers using a collection group query
                val wallpapersSnapshot = db.collectionGroup("wallpapers")
                    .get().await()

                val categoryCount = mutableMapOf<String, Int>()
                val category = wallpapersSnapshot.documents.map {
                    val categoryName = it.getString("category") ?: ""
                    val categoryNameCapital = categoryName.replaceFirstChar { char ->
                        char.uppercase()
                    }
                    categoryCount[categoryNameCapital] =
                        categoryCount.getOrDefault(categoryNameCapital, 0) + 1
                }
                for (category in categories) {
                    category.wallpaperCount = categoryCount.getOrDefault(category.name, 0)
                }

                emit(Resource.Success(categories))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@flow
        }
    }

    override suspend fun fetchWallpapersByCategory(categoryName: String): Flow<List<Wallpaper>> =
        flow {
            val matchingWallpapers = mutableListOf<Wallpaper>()
            try {
                val querySnapshot = db.collectionGroup("wallpapers")
                    .whereEqualTo("category", categoryName)
                    .get()
                    .await()


                for (document in querySnapshot.documents) {
                    val wallpaper = document.toWallpaper()
                    if (wallpaper != null) {
                        matchingWallpapers.add(wallpaper)
                    }
                }
                emit(matchingWallpapers)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@flow
        }


    override suspend fun fetchWallpapers(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Wallpaper>>> = flow {
        if (fetchFromRemote) {
            emit(Resource.Loading(true))
            val wallpapers = fetchAndCacheWallpapers()
            emit(Resource.Success(wallpapers))
            emit(Resource.Loading(false))
        } else {
            val wallpapers = fetchFromLocal()
            emit(Resource.Success(wallpapers))
        }
    }

    private suspend fun fetchFromLocal(): List<Wallpaper> {
        return wallpaperDao.getAll().map { it.toWallpaper() }
    }

    override suspend fun addOrRemoveFavorites(imageUrl: String, isFavorite: Boolean) {
        val wallpaper = wallpaperDao.getWallpaperByUrl(imageUrl)
        if (wallpaper != null) {
            wallpaper.isFavorite = true
            wallpaperDao.updateWallpaper(wallpaper)
        }
    }


    override suspend fun fetchAndCacheWallpapers(): List<Wallpaper> {
        val firestoreWallpapers = db.collectionGroup("wallpapers")
            .get()
            .await()
        val wallpapersResult = firestoreWallpapers.documents.mapNotNull { document ->
            document.toWallpaper()
        }

        val updatedWallpapers = wallpapersResult.map { firestoreWallpaper ->
            val existingWallpaper = wallpaperDao.getWallpaperByUrl(firestoreWallpaper.imageUrl)
            if (existingWallpaper != null) {
                // Preserve isFavorite status
                firestoreWallpaper.toWallpaperEntity(existingWallpaper.isFavorite)
            } else {
                // New wallpaper, default isFavorite to false
                firestoreWallpaper.toWallpaperEntity()
            }
        }

        wallpaperDao.insertWallpaperList(updatedWallpapers)
        return wallpapersResult
    }

    override suspend fun searchWallpapers(query: String): Flow<List<Wallpaper>> {
        return flow {
            val matchingWallpapers = mutableListOf<Wallpaper>()
            try {
                // Ensure Firestore is properly indexed for these queries
                val nameQuery = db.collectionGroup("wallpapers")
                    .whereGreaterThanOrEqualTo("name", query)
                    .whereLessThanOrEqualTo("name", query + "\uf8ff")
                    .get()
                    .await()

                val categoryQuery = db.collectionGroup("wallpapers")
                    .whereGreaterThanOrEqualTo("category", query)
                    .whereLessThanOrEqualTo("category", query + "\uf8ff")
                    .get()
                    .await()

                val tagsQuery = db.collectionGroup("wallpapers")
                    .whereArrayContains("tags", query)
                    .get()
                    .await()

                val colorsQuery = db.collectionGroup("wallpapers")
                    .whereArrayContains("colors", query)
                    // .whereArrayContains("colors", query.substring(0,2) )
                    .get()
                    .await()

                // Combine results from both queries
                val nameResults = nameQuery.documents.mapNotNull { document ->
                    document.toWallpaper()
                }
                val categoryResults = categoryQuery.documents.mapNotNull { document ->
                    document.toWallpaper()
                }

                val tagsResults = tagsQuery.documents.mapNotNull { document ->
                    document.toWallpaper()
                }
                val colorsResults = colorsQuery.documents.mapNotNull { document ->
                    document.toWallpaper()
                }

                matchingWallpapers.addAll(nameResults)
                matchingWallpapers.addAll(categoryResults)
                matchingWallpapers.addAll(tagsResults)
                matchingWallpapers.addAll(colorsResults)

                // Remove duplicates if any
                val uniqueWallpapers = matchingWallpapers.distinctBy { it.imageUrl }

                emit(uniqueWallpapers)
            } catch (e: Exception) {

                e.printStackTrace()
            }
            return@flow
        }
    }

    override suspend fun searchWallpapersByColor(targetColor: Color): Flow<List<Wallpaper>> {
        return flow {
            val colorInt = targetColor.toArgb()
            val wallpapersSnapshot = db.collectionGroup("wallpapers").get().await()
            val matchingWallpapers = wallpapersSnapshot.documents.mapNotNull { document ->
                val wallpaper = document.toWallpaper()
                wallpaper?.takeIf {
                    it.colors.any { colorString ->
                        val extractedColorInt = android.graphics.Color.parseColor(colorString)
                        colorDistance(
                            extractedColorInt,
                            colorInt
                        ) < 0.2 // Adjust the threshold as needed
                    }
                }
            }
            emit(matchingWallpapers)
        }
    }


    override suspend fun extractColorsForNewWallpapers(context: Context) {
        val wallpapersSnapshot = db.collectionGroup("wallpapers").get().await()
        val newWallpapers = wallpapersSnapshot.documents.mapNotNull { document ->
            val wallpaper = document.toWallpaper()
            wallpaper?.takeIf { it.colors.isEmpty() }
        }

        newWallpapers.forEach { wallpaper ->
            extractColorsAndUploadToFirestore(
                imageUrl = wallpaper.imageUrl,
                documentId = wallpaper.id,
                category = wallpaper.category,
                context = context
            )
        }
    }




    override suspend fun downloadWallpaper(
        context: Context,
        imageUrl: String,
        imageName: String,
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val appName = context.getString(R.string.app_name)
                val folderPath = Environment.DIRECTORY_PICTURES + "/" + appName

                val folder = File(folderPath)
                if (!folder.exists()) {
                    folder.mkdirs()
                }
                val file = File(folder, "$imageName.jpg")
                if (file.exists()) {
                    return@withContext true
                }

                val url = URL(imageUrl)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()

                val inputStream = connection.inputStream
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, "$imageName.jpg")
                    put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.Images.Media.RELATIVE_PATH, folderPath)
                        put(MediaStore.Images.Media.IS_PENDING, 1)
                    }
                }
                val resolver = context.contentResolver
                val uri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                if (uri != null) {
                    val outputStream: OutputStream? = resolver.openOutputStream(uri)
                    outputStream?.use {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        contentValues.clear()
                        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                        resolver.update(uri, contentValues, null, null)
                    }
                }

                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override suspend fun trackInteraction(
        category: String,
        wallpaperId: String,
        interactionType: String
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userName = FirebaseAuth.getInstance().currentUser?.displayName ?: "Anonymous"

        // Generate a custom ID based on the current timestamp and user ID
        val customId = "${userName}_${interactionType}_${System.currentTimeMillis()}"

        val wallpaperRef = db.collection("categories")
            .document(category)
            .collection("wallpapers")
            .document(wallpaperId)
        val interactionsRef = wallpaperRef.collection("interactions").document(customId)

        db.runTransaction { transaction ->
            val wallpaperSnapshot = transaction.get(wallpaperRef)
            val newCount = (wallpaperSnapshot.getLong(interactionType) ?: 0L) + 1
            transaction.update(wallpaperRef, interactionType, newCount)

            val interactionData = mapOf(
                "type" to interactionType,
                "timestamp" to FieldValue.serverTimestamp(),
                "userId" to userId,
                "userName" to userName
            )
            transaction.set(interactionsRef, interactionData)
        }.await()
    }

    override suspend fun getPopularWallpapers(minViews: Long): Flow<List<Wallpaper>> = callbackFlow {
        val query = db.collectionGroup("wallpapers")
            .whereGreaterThanOrEqualTo("views", minViews)

        val listenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val wallpapers = snapshot.documents.mapNotNull { it.toWallpaper() }
                trySend(wallpapers).isSuccess
            }
        }

        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun getMostDownloadedWallpapers(minDownloads: Long): Flow<List<Wallpaper>> =
        callbackFlow {
            val query = db.collectionGroup("wallpapers")
                .whereGreaterThanOrEqualTo("downloads", minDownloads)

            val listenerRegistration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val wallpapers = snapshot.documents.mapNotNull { it.toWallpaper() }
                    trySend(wallpapers).isSuccess
                }
            }

            awaitClose { listenerRegistration.remove() }
        }

    override suspend fun storeUserInfo(user: FirebaseUser): Flow<Resource<Unit>> {
        return flow {
            try {
                val userRef = db.collection("users").document(user.uid)
                val userInfo = mapOf(
                    "uid" to user.uid,
                    "name" to user.displayName,
                    "email" to user.email,
                    "profilePicUrl" to user.photoUrl.toString(),
                    "createdAt" to FieldValue.serverTimestamp()
                )

                userRef.set(userInfo, SetOptions.merge()).await()
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
            }
            return@flow
        }
    }

    override suspend fun getUserInfo(uid: String): Flow<Resource<User?>> {
        return flow {
            emit(Resource.Loading())
            try {
                val userRef = db.collection("users").document(uid)
                val document = userRef.get().await()
                if (document.exists()) {
                    val user = document.toUser()
                    emit(Resource.Success(user))
                } else {
                    emit(Resource.Error("User not found"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message.toString()))
            }
            return@flow
        }
    }
}
