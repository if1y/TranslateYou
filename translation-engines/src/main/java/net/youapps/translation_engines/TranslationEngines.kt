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

import net.youapps.translation_engines.ai.AiEngine
import net.youapps.translation_engines.bi.BingEngine
import net.youapps.translation_engines.deepl.DeeplAuthenticatedFreeApiEngine
import net.youapps.translation_engines.deepl.DeeplAuthenticatedPaidApiEngine
import net.youapps.translation_engines.deepl.DeeplBrowserEngine
import net.youapps.translation_engines.gc.GoogleCloudEngine
import net.youapps.translation_engines.gp.GoogleEngine
import net.youapps.translation_engines.iciba.IcibaEngine
import net.youapps.translation_engines.lc.LingoEngine
import net.youapps.translation_engines.lv.LVEngine
import net.youapps.translation_engines.mm.MMEngine
import net.youapps.translation_engines.na.PapagoEngine
import net.youapps.translation_engines.or.OneRingEngine
import net.youapps.translation_engines.po.PonsEngine
import net.youapps.translation_engines.st.STEngine
import net.youapps.translation_engines.ts.TransmartEngine
import net.youapps.translation_engines.ya.YandexEngine

object TranslationEngines {
    /** Engine selected on a fresh installation. */
    const val DEFAULT_ENGINE_NAME = "Google"

    fun getAllEngines(settingsProvider: EngineSettingsProvider) = listOf(
        AiEngine(settingsProvider),
        GoogleEngine(settingsProvider),
        GoogleCloudEngine(settingsProvider),
        BingEngine(settingsProvider),
        DeeplBrowserEngine(settingsProvider),
        DeeplAuthenticatedFreeApiEngine(settingsProvider),
        DeeplAuthenticatedPaidApiEngine(settingsProvider),
        YandexEngine(settingsProvider),
        IcibaEngine(settingsProvider),
        LingoEngine(settingsProvider),
        TransmartEngine(settingsProvider),
        LVEngine(settingsProvider),
        MMEngine(settingsProvider),
        STEngine(settingsProvider),
        OneRingEngine(settingsProvider),
        PonsEngine(settingsProvider),
        PapagoEngine(settingsProvider),
    )
}
