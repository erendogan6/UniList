package com.erendogan6.unilist.network

import com.erendogan6.unilist.model.ProvinceData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("page-{page}.json") suspend fun getUniversities(@Path("page") page: Int): Response<ProvinceData>
}