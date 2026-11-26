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

import net.youapps.translation_engines.gp.obj.GoogleResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val USER_AGENT =
    "GoogleTranslate/6.14.0.04.343003216 (Linux; U; Android 10; Redmi K20 Pro)"

interface Google {
    @GET("translate_a/single")
    @Headers("User-Agent: $USER_AGENT")
    suspend fun translate(
        @Query("q") query: String,
        @Query("sl") source: String,
        @Query("tl") target: String,
        @Query("ie") inputEncoding: String,
        @Query("oe") outputEncoding: String,
        @Query("client") client: String,
        @Query("dt") dataType: String,
        @Query("otf") otf: String,
        @Query("dj") dj: String
    ): GoogleResponse
}
