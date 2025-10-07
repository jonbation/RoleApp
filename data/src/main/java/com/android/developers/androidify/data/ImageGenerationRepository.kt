/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.developers.androidify.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.android.developers.androidify.RemoteConfigDataSource
import com.android.developers.androidify.model.ValidatedDescription
import com.android.developers.androidify.model.ValidatedImage
import com.android.developers.androidify.util.LocalFileProvider
import com.android.developers.androidify.vertexai.FirebaseAiDataSource
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/*
* Copyright 2025 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
interface ImageGenerationRepository {
    suspend fun initialize()
    suspend fun generateFromDescription(description: String, skinTone: String): Bitmap
    suspend fun generateFromImage(file: File, skinTone: String): Bitmap
    suspend fun saveImage(imageBitmap: Bitmap): Uri
    suspend fun saveImageToExternalStorage(imageBitmap: Bitmap): Uri
    suspend fun saveImageToExternalStorage(imageUri: Uri): Uri
}

@Singleton
internal class ImageGenerationRepositoryImpl @Inject constructor(
    val remoteConfigDataSource: RemoteConfigDataSource,
    val localFileProvider: LocalFileProvider,
    val internetConnectivityManager: InternetConnectivityManager,
    val geminiNanoDataSource: GeminiNanoGenerationDataSource,
    val firebaseAiDataSource: FirebaseAiDataSource,
) : ImageGenerationRepository {

    override suspend fun initialize() {
        Log.d("ImageGenerationRepository", "Initializing")
        geminiNanoDataSource.initialize()
    }

    private suspend fun validatePromptHasEnoughInformation(inputPrompt: String): ValidatedDescription =
        firebaseAiDataSource.validatePromptHasEnoughInformation(inputPrompt)

    private suspend fun validateImageIsFullPerson(file: File): ValidatedImage =
        firebaseAiDataSource.validateImageHasEnoughInformation(
            BitmapFactory.decodeFile(
                file.absolutePath,
            ),
        )

    @Throws(InsufficientInformationException::class)
    override suspend fun generateFromDescription(
        description: String,
        skinTone: String,
    ): Bitmap {
        checkInternetConnection()
        if (description.isBlank()) throw InsufficientInformationException()
        val validatedPrompt = validatePromptHasEnoughInformation(description)
        if (!validatedPrompt.success || validatedPrompt.userDescription == null) {
            throw InsufficientInformationException()
        }
        return firebaseAiDataSource.generateImageFromPromptAndSkinTone(
            validatedPrompt.userDescription.toString(),
            skinTone,
        )
    }

    override suspend fun generateFromImage(
        file: File,
        skinTone: String,
    ): Bitmap {
        checkInternetConnection()
        val validatedImage = validateImageIsFullPerson(file)
        if (!validatedImage.success) {
            throw ImageValidationException(validatedImage.errorMessage?.toImageValidationError())
        }

        val imageDescription = firebaseAiDataSource.generateDescriptivePromptFromImage(
            BitmapFactory.decodeFile(file.absolutePath),
        )
        if (!imageDescription.success || imageDescription.userDescription == null) {
            throw ImageDescriptionFailedGenerationException()
        }
        return firebaseAiDataSource.generateImageFromPromptAndSkinTone(
            imageDescription.userDescription.toString(),
            skinTone,
        )
    }

    override suspend fun saveImage(imageBitmap: Bitmap): Uri {
        val cacheFile = localFileProvider.createCacheFile("shared_image_${UUID.randomUUID()}.jpg")
        val file = localFileProvider.saveBitmapToFile(imageBitmap, cacheFile)
        return localFileProvider.sharingUriForFile(file)
    }

    override suspend fun saveImageToExternalStorage(imageBitmap: Bitmap): Uri {
        val cacheFile = localFileProvider.createCacheFile("androidify_image_result_${UUID.randomUUID()}.jpg")
        localFileProvider.saveBitmapToFile(imageBitmap, cacheFile)
        return localFileProvider.saveToSharedStorage(cacheFile, cacheFile.name, "image/jpeg")
    }

    override suspend fun saveImageToExternalStorage(imageUri: Uri): Uri {
        return localFileProvider.saveUriToSharedStorage(
            imageUri,
            "androidify_image_original_${UUID.randomUUID()}.jpg",
            "image/jpeg",
        )
    }

    private fun checkInternetConnection() {
        if (!internetConnectivityManager.isInternetAvailable()) {
            throw NoInternetException()
        }
    }
}
