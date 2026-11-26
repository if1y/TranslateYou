/*
 * Copyright (c) 2023 You Apps
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bnyro.translate.ui.views

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bnyro.translate.App
import com.bnyro.translate.R
import com.bnyro.translate.ext.capitalize
import com.bnyro.translate.ext.toastFromMainThread
import com.bnyro.translate.obj.ListPreferenceOption
import com.bnyro.translate.ui.components.prefs.DropDownSelectPreference
import com.bnyro.translate.ui.components.prefs.EditTextPreference
import com.bnyro.translate.ui.components.prefs.ListPreferenceDialog
import com.bnyro.translate.ui.components.prefs.ModelPreference
import com.bnyro.translate.ui.components.prefs.PreferenceItem
import com.bnyro.translate.util.Preferences
import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.TranslationEngine
import net.youapps.translation_engines.TranslationEngines

@Composable
fun EnginePref() {
    val engines = App.translationEngines

    var selectedName by remember {
        mutableStateOf(
            Preferences.get(
                Preferences.selectedEngineKey,
                TranslationEngines.DEFAULT_ENGINE_NAME
            )
        )
    }
    val selectedEngineIndex = engines.indexOfFirst { it.name == selectedName }
        .takeIf { it >= 0 } ?: 0
    val selectedEngine = engines[selectedEngineIndex]

    var fetchedModels by remember { mutableStateOf(emptyList<String>()) }
    var fetchedForEngine by remember { mutableStateOf<TranslationEngine?>(null) }
    LaunchedEffect(selectedEngine) {
        fetchedModels = emptyList()
        fetchedForEngine = null
        fetchedModels = selectedEngine.fetchSupportedModels()
        fetchedForEngine = selectedEngine
    }
    val modelOptions = (if (fetchedForEngine == selectedEngine) fetchedModels else emptyList())
        .ifEmpty { selectedEngine.supportedModels }

    DropDownSelectPreference(
        preferenceKey = Preferences.selectedEngineKey,
        title = stringResource(R.string.selected_engine),
        items = engines.map { it.name },
        defaultValue = TranslationEngines.DEFAULT_ENGINE_NAME,
        onSelect = { engineName ->
            selectedName = engineName

            selectedEngine.createOrRecreate()
        }
    )

    val context = LocalContext.current
    var engineModified = remember { false }
    for (engine in engines) {
        var instanceUrl by remember {
            mutableStateOf(engine.getUrl())
        }

        var apiKey by remember {
            mutableStateOf(engine.getApiKey())
        }

        DisposableEffect(Unit) {
            onDispose {
                // when the screen is closed, automatically refresh the engine if it was modified
                if (engineModified) {
                    try {
                        // append "/" suffix to base URL, otherwise Retrofit crashes when
                        // having base urls with paths, e.g. example.com/translate
                        if (!instanceUrl.endsWith("/")) {
                            Preferences.put(Preferences.apiUrlPrefKey(engine), "$instanceUrl/")
                        }

                        engine.createOrRecreate()
                    } catch (e: Exception) {
                        context.toastFromMainThread(e.localizedMessage.orEmpty())
                    }

                    engineModified = false
                }
            }
        }

        if (selectedEngine == engine) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(5.dp))

                if (engine.urlModifiable) {
                    EditTextPreference(
                        preferenceKey = Preferences.apiUrlPrefKey(engine),
                        value = instanceUrl,
                        labelText = stringResource(R.string.instance)
                    ) {
                        instanceUrl = it
                        engineModified = true
                    }
                }

                if (engine.apiKeyState != ApiKeyState.DISABLED) {
                    EditTextPreference(
                        preferenceKey = Preferences.apiKeyPrefKey(engine),
                        value = apiKey.orEmpty(),
                        labelText = stringResource(id = R.string.api_key) + when (engine.apiKeyState) {
                            ApiKeyState.REQUIRED -> " (${stringResource(R.string.required)})"
                            ApiKeyState.OPTIONAL -> " (${stringResource(R.string.optional)})"
                            else -> ""
                        }
                    ) {
                        apiKey = it
                        engineModified = true
                    }
                }

                when {
                    engine.supportedModels.isNotEmpty() && engine.allowCustomModel -> {
                        var model by remember(engine) {
                            mutableStateOf(
                                Preferences.get(
                                    Preferences.selectedModelPrefKey(engine),
                                    modelOptions.firstOrNull().orEmpty()
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        ModelPreference(
                            preferenceKey = Preferences.selectedModelPrefKey(engine),
                            value = model,
                            labelText = stringResource(R.string.model),
                            options = modelOptions
                        ) { model = it }
                    }

                    modelOptions.isNotEmpty() -> {
                        var showEngineSelDialog by remember {
                            mutableStateOf(false)
                        }

                        Spacer(
                            modifier = Modifier
                                .height(10.dp)
                        )

                        PreferenceItem(
                            title = stringResource(R.string.translation_api),
                            summary = stringResource(R.string.st_selected_engine)
                        ) {
                            showEngineSelDialog = true
                        }

                        if (showEngineSelDialog) {
                            var selectedAvailableEngine by remember(engine) {
                                mutableStateOf(
                                    Preferences.get(
                                        Preferences.selectedModelPrefKey(engine),
                                        modelOptions.first()
                                    )
                                )
                            }

                            ListPreferenceDialog(
                                preferenceKey = null,
                                onDismissRequest = { showEngineSelDialog = false },
                                options = modelOptions.mapIndexed { index, it ->
                                    ListPreferenceOption(
                                        it.replace("_", " ").capitalize(),
                                        value = index,
                                    )
                                },
                                currentValue = modelOptions.indexOf(selectedAvailableEngine)
                                    .takeIf { it >= 0 }
                            ) { selectedModel ->
                                val selectedModelName = modelOptions[selectedModel.value]
                                Preferences.put(
                                    Preferences.selectedModelPrefKey(engine),
                                    selectedModelName
                                )
                                selectedAvailableEngine = selectedModelName

                                engineModified = true
                            }
                        }
                    }
                }
            }
        }
    }
}
