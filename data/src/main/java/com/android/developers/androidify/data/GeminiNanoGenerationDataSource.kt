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

import android.util.Log
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

interface GeminiNanoGenerationDataSource {
    suspend fun initialize()
    suspend fun generatePrompt(prompt: String): String?
}

@Singleton
class GeminiNanoGenerationDataSourceImpl @Inject constructor(val downloader: GeminiNanoDownloader) :
    GeminiNanoGenerationDataSource {

    override suspend fun initialize() {
        downloader.downloadModel()
    }

    /**
     * Generate a prompt to create an Android bot using Gemini Nano.
     * If Gemini Nano is not available, return null.
     */
    override suspend fun generatePrompt(prompt: String): String? {
        if (!downloader.isModelDownloaded()) return null
        val response = downloader.generativeModel?.generateContent(prompt)
        Log.d("GeminiNanoGenerationDataSource", "generatePrompt: ${response?.text}")
        return response?.text
    }
}
