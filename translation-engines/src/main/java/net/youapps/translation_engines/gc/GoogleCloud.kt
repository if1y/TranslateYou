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

import net.youapps.translation_engines.gc.obj.GoogleCloudResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GoogleCloud {
    @POST("language/translate/v2")
    @FormUrlEncoded
    suspend fun translate(
        @Field("q") query: String,
        @Field("target") target: String,
        @Field("format") format: String,
        @Field("key") key: String,
        @Field("source") source: String?
    ): Response<GoogleCloudResponse>
}
