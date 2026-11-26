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

package net.youapps.translation_engines.ts

import java.util.Locale
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.abs
import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine
import net.youapps.translation_engines.ts.obj.TransmartHeader
import net.youapps.translation_engines.ts.obj.TransmartRequest
import net.youapps.translation_engines.ts.obj.TransmartSource
import net.youapps.translation_engines.ts.obj.TransmartTarget

class TransmartEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val name = "Transmart"
    override val defaultUrl = "https://transmart.qq.com/"
    override val urlModifiable = false
    override val apiKeyState = ApiKeyState.DISABLED

    private lateinit var api: Transmart

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> = languageCodes.map { code ->
        Language(code, Locale.forLanguageTag(code).displayLanguage)
    }

    override suspend fun translate(query: String, source: String?, target: String): Translation {
        val response = api.translate(
            TransmartRequest(
                header = TransmartHeader(client_key = generateClientKey()),
                source = TransmartSource(
                    lang = source?.takeIf { it in languageCodes } ?: "en",
                    text_list = query.split("\n")
                ),
                target = TransmartTarget(lang = target)
            )
        )

        val translatedText = response.auto_translation.joinToString("\n")
        if (translatedText.isBlank()) throw Exception("Transmart returned an empty translation.")

        return Translation(translatedText = translatedText)
    }

    private fun generateClientKey(): String {
        val random = ThreadLocalRandom.current()
        val browserVersion = "${abs(random.nextInt(17) + 100)}.${abs(random.nextInt(20))}.${abs(random.nextInt(20))}"
        val operatingSystem = if (random.nextBoolean()) "Mac OS" else "Windows"
        return "browser-chrome-$browserVersion-$operatingSystem-${UUID.randomUUID()}-${System.currentTimeMillis()}"
    }

    companion object {
        private val languageCodes = listOf(
            "ar", "fr", "fil", "lo", "ja", "it", "hi", "id", "vi", "de", "km", "ms",
            "th", "tr", "zh", "ru", "ko", "pt", "es"
        )
    }
}
