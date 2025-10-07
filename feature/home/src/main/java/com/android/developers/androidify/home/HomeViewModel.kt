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
package com.android.developers.androidify.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developers.androidify.data.ConfigProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
@HiltViewModel
class HomeViewModel @Inject constructor(val configProvider: ConfigProvider) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isAppActive = !configProvider.isAppInactive(),
                dancingDroidLink = configProvider.getDancingDroidLink(),
                videoLink = configProvider.getPromoVideoLink(),
            )
        }
    }
}

data class HomeState(
    val isAppActive: Boolean = true,
    val videoLink: String? = null,
    val dancingDroidLink: String? = null,
)
