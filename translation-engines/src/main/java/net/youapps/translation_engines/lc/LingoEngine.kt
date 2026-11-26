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

package net.youapps.translation_engines.lc

import java.util.UUID
import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine
import net.youapps.translation_engines.lc.obj.LingoRequest

/**
 * LingoCloud (Caiyun) translator. The bundled token is a default only;
 * users can provide their own token in the settings.
 */
class LingoEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val name = "LingoCloud"
    override val defaultUrl = "https://api.interpreter.caiyunai.com/"
    override val urlModifiable = false
    override val apiKeyState = ApiKeyState.OPTIONAL

    private lateinit var api: Lingo

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi<Lingo>(this, logBodies = false)
    }

    override suspend fun getLanguages(): List<Language> = listOf(
        Language("zh", "Chinese"),
        Language("en", "English"),
        Language("es", "Spanish"),
        Language("fr", "French"),
        Language("ja", "Japanese"),
        Language("ru", "Russian")
    )

    override suspend fun translate(query: String, source: String?, target: String): Translation {
        val token = getApiKey() ?: DEFAULT_TOKEN
        val response = api.translate(
            authorization = "token $token",
            request = LingoRequest(
                source = query.split("\n"),
                trans_type = "${source ?: "auto"}2$target",
                request_id = UUID.randomUUID().toString()
            )
        )

        val translatedText = response.target.joinToString("\n") { line ->
            // The service sometimes returns this orphan surrogate as a whole line.
            if (line == "\ud835") "" else line
        }
        if (translatedText.isBlank()) throw Exception("LingoCloud returned an empty translation.")

        return Translation(translatedText = translatedText)
    }

    companion object {
        private const val DEFAULT_TOKEN = "9sdftiq37bnv410eon2l"
    }
}
