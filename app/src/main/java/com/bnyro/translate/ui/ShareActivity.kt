/*
 * Copyright (c) 2024 You Apps
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

package com.bnyro.translate.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bnyro.translate.R
import com.bnyro.translate.ext.toastFromMainThread
import com.bnyro.translate.ui.components.AppHeader
import com.bnyro.translate.ui.components.DialogButton
import com.bnyro.translate.ui.views.TranslationComponent

class ShareActivity : TranslationActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        showContent {
            val context = LocalContext.current
            val configuration = LocalConfiguration.current
            val screenHeight = with(LocalDensity.current) {
                LocalWindowInfo.current.containerSize.height.toDp()
            }

            LaunchedEffect(Unit) {
                translationModel.refresh()
            }

            Dialog(
                onDismissRequest = { finish() },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ) {
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .height(
                            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                                screenHeight * 2 / 3 else screenHeight * 4 / 5
                        ),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppHeader(modifier = Modifier.weight(1f)) {
                                val intent = Intent(this@ShareActivity, MainActivity::class.java)
                                    .putExtra(Intent.EXTRA_TEXT, translationModel.insertedText)
                                this@ShareActivity.startActivity(intent)
                            }
                        }

                        TranslationComponent(
                            modifier = Modifier.weight(1f),
                            viewModel = translationModel,
                            showLanguageSelector = true,
                            largeTextFields = false,
                            focusInputOnLaunch = false,
                            splitView = true
                        ) { e, important ->
                            context.toastFromMainThread(e)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            DialogButton(text = stringResource(R.string.clear)) {
                                translationModel.clearTranslation()
                            }
                            DialogButton(text = stringResource(R.string.okay)) {
                                finish()
                            }
                        }
                    }
                }
            }
        }

        setFinishOnTouchOutside(true)
    }
}
