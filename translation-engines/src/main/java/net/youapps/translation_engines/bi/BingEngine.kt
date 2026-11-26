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

package net.youapps.translation_engines.bi

import java.util.Locale
import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine

class BingEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val name: String = "Bing"
    override val defaultUrl: String = "https://www.bing.com"
    override val urlModifiable: Boolean = false
    override val apiKeyState: ApiKeyState = ApiKeyState.DISABLED

    private lateinit var api: Bing

    private val abuseParamsRegex =
        Regex("""params_AbusePreventionHelper\s*=\s*\[(\d+),"(\w+)",(\d+)]""")
    private val iidRegex = Regex("""data-iid="(.*?)"""")
    private val igRegex = Regex("""IG:"([A-F0-9]+)"""")

    private val languagesRegex = Regex("""languageMapping\s*=\s*\[(.*?)]""")

    @Volatile
    private var cachedConfig: BingConfig? = null

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi<Bing>(
            this,
            connectTimeout = 5_000,
            readTimeout = 5_000,
            logBodies = false
        )
        cachedConfig = null
    }

    override suspend fun getLanguages(): List<Language> {
        val translatorPage = getConfig().page
        val languageCodes = languagesRegex.find(translatorPage)?.groupValues?.get(1)
            ?.split(",")
            ?.map { it.trim('"') }
            ?: throw Exception("Bing: couldn't parse the language list.")

        return languageCodes.mapNotNull { code ->
            val languageName = Locale.forLanguageTag(code).displayLanguage
            languageName.takeIf { it != code }?.let { Language(code = code, name = it) }
        }
    }

    override suspend fun translate(
        query: String,
        source: String?,
        target: String
    ): Translation {
        val config = getConfig()
        val response = api.translate(
            ig = config.ig,
            iid = config.iid,
            key = config.key,
            token = config.token,
            fromLang = source ?: "auto-detect",
            toLang = target,
            text = query
        )[0]

        return Translation(
            translatedText = response.translations[0].text,
            detectedLanguage = response.detectedLanguage.language
        )
    }

    private suspend fun getConfig(): BingConfig {
        cachedConfig?.takeIf { System.currentTimeMillis() < it.expiresAt }?.let { return it }

        val translatorPage = api.getTranslatorPage().string()
        val params = abuseParamsRegex.find(translatorPage)
            ?: throw Exception("Bing: couldn't parse the anti-abuse parameters.")
        val iid = iidRegex.find(translatorPage)?.groupValues?.get(1)
            ?: throw Exception("Bing: couldn't parse the IID.")
        val ig = igRegex.find(translatorPage)?.groupValues?.get(1)
            ?: throw Exception("Bing: couldn't parse the IG.")

        val key = params.groupValues[1]
        val token = params.groupValues[2]
        val timestamp = key.toLongOrNull() ?: 0L
        val interval = params.groupValues[3].toLongOrNull() ?: 0L
        val expiresAt = maxOf(
            if (timestamp > 0 && interval > 0) timestamp + interval else 0L,
            System.currentTimeMillis() + 60_000L
        )

        return BingConfig(
            page = translatorPage,
            ig = ig,
            iid = iid,
            key = key,
            token = token,
            expiresAt = expiresAt
        ).also { cachedConfig = it }
    }

    private data class BingConfig(
        val page: String,
        val ig: String,
        val iid: String,
        val key: String,
        val token: String,
        val expiresAt: Long
    )
}
