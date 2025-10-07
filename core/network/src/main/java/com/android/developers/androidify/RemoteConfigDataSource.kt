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
package com.android.developers.androidify

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
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
interface RemoteConfigDataSource {
    fun isAppInactive(): Boolean
    fun textModelName(): String
    fun imageModelName(): String
    fun promptTextVerify(): String
    fun promptImageValidation(): String
    fun promptImageDescription(): String
    fun useGeminiNano(): Boolean
    fun generateBotPrompt(): String
    fun promptImageGenerationWithSkinTone(): String

    fun getPromoVideoLink(): String

    fun getDancingDroidLink(): String
}

@Singleton
class RemoteConfigDataSourceImpl @Inject constructor() : RemoteConfigDataSource {
    private val remoteConfig = Firebase.remoteConfig

    override fun isAppInactive(): Boolean {
        return remoteConfig.getBoolean("is_android_app_inactive")
    }

    override fun textModelName(): String {
        return remoteConfig.getString("text_model_name")
    }

    override fun imageModelName(): String {
        return remoteConfig.getString("image_model_name")
    }

    override fun promptTextVerify(): String {
        return remoteConfig.getString("prompt_text_verify")
    }

    override fun promptImageValidation(): String {
        return remoteConfig.getString("prompt_image_validation")
    }

    override fun promptImageDescription(): String {
        return remoteConfig.getString("prompt_image_description")
    }

    override fun useGeminiNano(): Boolean {
        return remoteConfig.getBoolean("use_gemini_nano")
    }

    override fun generateBotPrompt(): String {
        return remoteConfig.getString("generate_bot_prompt")
    }

    override fun promptImageGenerationWithSkinTone(): String {
        return remoteConfig.getString("prompt_image_generation_skin_tone")
    }

    override fun getPromoVideoLink(): String {
        return remoteConfig.getString("promo_video_link")
    }

    override fun getDancingDroidLink(): String {
        return remoteConfig.getString("dancing_droid_gif_link")
    }
}
