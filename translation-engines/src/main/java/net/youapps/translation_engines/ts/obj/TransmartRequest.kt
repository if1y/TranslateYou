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

package net.youapps.translation_engines.ts.obj

import kotlinx.serialization.Serializable

@Serializable
data class TransmartRequest(
    val header: TransmartHeader,
    val source: TransmartSource,
    val target: TransmartTarget,
    val model_category: String = "normal",
    val text_domain: String = "",
    val type: String = "plain"
)

@Serializable
data class TransmartHeader(
    val client_key: String,
    val fn: String = "auto_translation",
    val session: String = "",
    val user: String = ""
)

@Serializable
data class TransmartSource(
    val lang: String,
    val text_list: List<String>
)

@Serializable
data class TransmartTarget(
    val lang: String
)
