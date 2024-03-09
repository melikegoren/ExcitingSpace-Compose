package com.melikegoren.excitingspace.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.graphics.painter.Painter
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toBitmapOrNull
import coil.compose.AsyncImagePainter
import coil.compose.ImagePainter
import com.melikegoren.excitingspace.common.Result
import com.melikegoren.excitingspace.data.local.ApodDao
import com.melikegoren.excitingspace.data.local.toApodPhotoModel
import com.melikegoren.excitingspace.data.local.toApodVideoModel
import com.melikegoren.excitingspace.data.remote.ApodApiService
import com.melikegoren.excitingspace.data.remote.dto.ToApodPhotoModel
import com.melikegoren.excitingspace.data.remote.dto.toApodEntityPhoto
import com.melikegoren.excitingspace.data.remote.dto.toApodEntityVideo
import com.melikegoren.excitingspace.data.remote.dto.toApodVideoModel
import com.melikegoren.excitingspace.data.remote.model.ApodPhotoModel
import com.melikegoren.excitingspace.data.remote.model.ApodVideoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class ApodRepositoryImpl @Inject constructor(
    private val network: ApodApiService,
): ApodRepository {

    override suspend fun getApodWithPhoto(apiKey: String): Flow<ApodPhotoModel> = flow {
        val apod = network.getApod(apiKey)
        emit(apod.ToApodPhotoModel())

    }.flowOn(Dispatchers.IO)

    override suspend fun getApodWithVideo(apiKey: String): Flow<ApodVideoModel> = flow {
        val apod = network.getApod(apiKey)
        emit(apod.toApodVideoModel())
    }.flowOn(Dispatchers.IO)

    override suspend fun saveImageToGallery(painter: AsyncImagePainter, context: Context, url: String, title: String){

        if (painter.state is AsyncImagePainter.State.Success) {
            val bitmapDrawable = (painter.state as AsyncImagePainter.State.Success).result.drawable
            val bitmap = bitmapDrawable.toBitmapOrNull()

            // Save the image to the gallery
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val imageFile = File(imagesDir, "$title.jpg")

            withContext(Dispatchers.IO) {
                val fos = FileOutputStream(imageFile)
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
                //Update the MediaStore
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, title)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
                    put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                    put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                }
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )

                Log.d("SaveImage", "Image file path: ${imageFile?.absolutePath.toString()}")
            }
        }


    }





}