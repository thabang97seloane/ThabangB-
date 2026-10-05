package com.example.data.ai

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiRequest(
  val contents: List<GeminiContent>,
  val generationConfig: GeminiGenConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
  val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
  val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiGenConfig(
  val temperature: Float? = 0.7f
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
  val candidates: List<GeminiCandidate>? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
  val content: GeminiContent? = null
)

interface GeminiApiService {
  @POST("v1beta/models/gemini-3.5-flash:generateContent")
  suspend fun generateContent(
    @Query("key") apiKey: String,
    @Body request: GeminiRequest
  ): GeminiResponse
}

object GeminiClient {
  private const val BASE_URL = "https://generativelanguage.googleapis.com/"

  private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

  private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  val service: GeminiApiService by lazy {
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()
      .create(GeminiApiService::class.java)
  }

  suspend fun queryGemini(prompt: String): String? = withContext(Dispatchers.IO) {
    val apiKey = try {
      BuildConfig.GEMINI_API_KEY
    } catch (_: Exception) {
      ""
    }
    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
      return@withContext null
    }

    try {
      val request = GeminiRequest(
        contents = listOf(
          GeminiContent(parts = listOf(GeminiPart(text = prompt)))
        ),
        generationConfig = GeminiGenConfig(temperature = 0.7f)
      )
      val response = service.generateContent(apiKey, request)
      response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
    } catch (e: Exception) {
      null
    }
  }
}
