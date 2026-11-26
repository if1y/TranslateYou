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

package net.youapps.translation_engines.ai.obj

import kotlinx.serialization.Serializable

@Serializable
data class AiModelsResponse(
    val data: List<AiModel> = emptyList()
)

@Serializable
data class AiModel(
    val id: String = ""
)

@Serializable
data class AiChatRequest(
    val model: String,
    val messages: List<AiMessage>
)

@Serializable
data class AiMessage(
    val role: String = "",
    val content: String = ""
)

@Serializable
data class AiChatResponse(
    val choices: List<AiChoice> = emptyList()
)

@Serializable
data class AiChoice(
    val message: AiMessage = AiMessage()
)

@Serializable
data class AiErrorResponse(
    val error: AiError? = null
)

@Serializable
data class AiError(
    val message: String? = null
)
