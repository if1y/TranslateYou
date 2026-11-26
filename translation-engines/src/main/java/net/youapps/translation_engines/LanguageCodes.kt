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

package net.youapps.translation_engines

/**
 * Cross-engine language code normalization.
 *
 * Translation engines use different codes for the same language (legacy Java/Google
 * codes, region tags, script tags, ...). These helpers are used to *match* a language
 * between engines; the codes sent to each engine are still the ones that engine
 * published in [TranslationEngine.getLanguages].
 */
object LanguageCodes {
    // Legacy/vendor-specific codes -> their common ISO code.
    private val aliases = mapOf(
        "iw" to "he", // Hebrew (Google/Java legacy)
        "ji" to "yi", // Yiddish (Java legacy)
        "in" to "id", // Indonesian (Java legacy)
        "jw" to "jv", // Javanese (Google legacy)
        "tl" to "fil", // Tagalog (Google/Microsoft)
        "mo" to "ro", // Moldovan
        "nb" to "no", // Norwegian Bokmål
        "nn" to "no" // Norwegian Nynorsk
    )

    // Regions that imply a script but don't carry a 4-letter script subtag.
    private val regionScripts = mapOf(
        "cn" to "Hans",
        "sg" to "Hans",
        "tw" to "Hant",
        "hk" to "Hant",
        "mo" to "Hant"
    )

    private fun parts(code: String): List<String> =
        code.trim().lowercase().replace('_', '-').split('-').filter { it.isNotEmpty() }

    /** Base language code, with aliases unified and region/script stripped. */
    fun base(code: String): String {
        val normalized = parts(code)
        val first = normalized.firstOrNull() ?: return ""
        return aliases[first] ?: first
    }

    /**
     * A finer-grained match key: base language plus script when the code carries one
     * (e.g. zh-CN -> zh-Hans, sr-Latn -> sr-Latn). Falls back to [base].
     */
    fun matchKey(code: String): String {
        val normalized = parts(code)
        val base = base(code)
        if (base.isEmpty()) return base

        val script = normalized.firstOrNull { it.length == 4 }
            ?.replaceFirstChar { it.uppercase() }
            ?: normalized.firstNotNullOfOrNull { regionScripts[it] }

        return if (script == null) base else "$base-$script"
    }

    /** Whether two codes identify the same language, at the most specific level available. */
    fun matches(first: String, second: String): Boolean =
        first.equals(second, ignoreCase = true) ||
            matchKey(first) == matchKey(second) ||
            base(first) == base(second)
}
