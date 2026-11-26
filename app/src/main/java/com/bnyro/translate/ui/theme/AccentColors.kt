/*
 * Copyright (c) 2026 You Apps
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

package com.bnyro.translate.ui.theme

import androidx.annotation.StringRes
import com.bnyro.translate.R

data class AccentPreset(
    val seed: String,
    @StringRes val nameRes: Int
)

object AccentColors {
    val presets = listOf(
        AccentPreset(defaultAccentColor, R.string.accent_color_default),
        AccentPreset("3B82F6", R.string.accent_color_blue),
        AccentPreset("6750A4", R.string.accent_color_purple),
        AccentPreset("E91E63", R.string.accent_color_pink),
        AccentPreset("D32F2F", R.string.accent_color_red),
        AccentPreset("F57C00", R.string.accent_color_orange),
        AccentPreset("2E7D32", R.string.accent_color_green),
        AccentPreset("00897B", R.string.accent_color_teal)
    )

    /**
     * Map a stored (possibly custom) color to one of the presets. Custom RGB values
     * are no longer editable, so pick the visually closest seed instead.
     */
    fun matchOrClosest(hex: String?): String {
        if (hex.isNullOrEmpty()) return defaultAccentColor

        presets.firstOrNull { it.seed.equals(hex, ignoreCase = true) }
            ?.let { return it.seed }

        if (hex.length != 6) return defaultAccentColor
        val rgb = runCatching {
            listOf(0, 2, 4).map { hex.substring(it, it + 2).toInt(16) }
        }.getOrElse { return defaultAccentColor }

        return presets.minBy { preset ->
            val presetRgb = listOf(0, 2, 4).map { preset.seed.substring(it, it + 2).toInt(16) }
            rgb.zip(presetRgb).sumOf { (a, b) -> (a - b) * (a - b) }
        }.seed
    }
}
