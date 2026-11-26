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

package net.youapps.translation_engines.gp

import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine

/**
 * Google Translate using the endpoint of its Android application.
 */
class GoogleEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val name = "Google"
    override val defaultUrl = "https://translate.google.com/"
    override val urlModifiable = false
    override val apiKeyState = ApiKeyState.DISABLED

    private lateinit var api: Google

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> =
        GoogleLanguages.codes.map { Language(it, GoogleLanguages.displayName(it)) }

    override suspend fun translate(query: String, source: String?, target: String): Translation {
        val response = api.translate(
            query = query,
            source = source?.let(GoogleLanguages::toApiCode) ?: "auto",
            target = GoogleLanguages.toApiCode(target),
            inputEncoding = "UTF-8",
            outputEncoding = "UTF-8",
            client = "at",
            dataType = "t",
            otf = "2",
            dj = "1"
        )

        val translatedText = response.sentences.joinToString("") { it.translation }
        if (translatedText.isBlank()) throw Exception("Google Translate returned an empty translation.")

        return Translation(
            translatedText = translatedText,
            detectedLanguage = response.src?.takeIf { source == null }
        )
    }
}
