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

package net.youapps.translation_engines.gc

import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.JsonHelper
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine
import net.youapps.translation_engines.gc.obj.GoogleCloudError
import net.youapps.translation_engines.gp.GoogleLanguages

class GoogleCloudEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val name = "Google Cloud"
    override val defaultUrl = "https://translation.googleapis.com/"
    override val urlModifiable = false
    override val apiKeyState = ApiKeyState.REQUIRED

    private lateinit var api: GoogleCloud

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi<GoogleCloud>(this, logBodies = false)
    }

    override suspend fun getLanguages(): List<Language> =
        GoogleLanguages.codes.map { Language(it, GoogleLanguages.displayName(it)) }

    override suspend fun translate(query: String, source: String?, target: String): Translation {
        val apiKey = getApiKey() ?: throw Exception("Please provide a Google Cloud API key.")

        val response = api.translate(
            query = query,
            target = GoogleLanguages.toApiCode(target),
            format = "text",
            key = apiKey,
            source = source?.let(GoogleLanguages::toApiCode)
        )

        val body = response.body()
        if (!response.isSuccessful || body == null) {
            val errorBody = response.errorBody()?.string().orEmpty()
            val message = runCatching {
                JsonHelper.json.decodeFromString<GoogleCloudError>(errorBody).message
            }.getOrNull()
            throw Exception("Google Cloud error ${response.code()}: ${message ?: errorBody}")
        }

        val translation = body.data?.translations?.firstOrNull()
            ?: throw Exception("Google Cloud returned no translation: $body")

        return Translation(
            translatedText = translation.translatedText,
            detectedLanguage = translation.detectedSourceLanguage?.takeIf { source == null }
        )
    }
}
