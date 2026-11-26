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

package com.bnyro.translate.ui.components.prefs

import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.bnyro.translate.R
import com.bnyro.translate.ext.hexToColor
import com.bnyro.translate.ui.MainActivity
import com.bnyro.translate.ui.components.DialogButton
import com.bnyro.translate.ui.theme.AccentColors
import com.bnyro.translate.ui.theme.defaultAccentColor
import com.bnyro.translate.util.Preferences

@Composable
fun AccentColorPrefDialog(
    onDismissRequest: () -> Unit
) {
    val activity = LocalActivity.current as MainActivity
    val supportsDynamicColors = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    var color by remember {
        mutableStateOf(
            Preferences.getAccentColor()?.let(AccentColors::matchOrClosest)
                ?: if (supportsDynamicColors) null else defaultAccentColor
        )
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            DialogButton(text = stringResource(R.string.okay)) {
                Preferences.prefs.edit(true) { putString(Preferences.accentColorKey, color) }
                activity.accentColor = color
                onDismissRequest.invoke()
            }
        },
        dismissButton = {
            DialogButton(text = stringResource(R.string.cancel)) {
                onDismissRequest.invoke()
            }
        },
        title = {
            Text(stringResource(R.string.accent_color))
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                if (supportsDynamicColors) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.dynamic_colors)
                        )
                        Switch(
                            checked = color == null,
                            onCheckedChange = { newValue ->
                                color = defaultAccentColor.takeIf { !newValue }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                AccentColors.presets.forEach { preset ->
                    val selected = color == preset.seed
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { color = preset.seed }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .size(36.dp)
                                .background(preset.seed.hexToColor(), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(preset.nameRes)
                        )
                        RadioButton(
                            selected = selected,
                            onClick = { color = preset.seed }
                        )
                    }
                }
            }
        }
    )
}
