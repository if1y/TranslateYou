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

import net.youapps.translation_engines.lc.obj.LingoRequest
import net.youapps.translation_engines.lc.obj.LingoResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

private const val USER_AGENT =
    "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 " +
        "(KHTML, like Gecko) Version/10.0 Mobile/14A5297c Safari/602.1"

interface Lingo {
    @POST("v1/translator")
    @Headers("User-Agent: $USER_AGENT")
    suspend fun translate(
        @Header("X-Authorization") authorization: String,
        @Body request: LingoRequest
    ): LingoResponse
}
