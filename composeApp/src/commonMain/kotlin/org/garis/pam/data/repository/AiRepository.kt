package org.garis.pam.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.garis.pam.core.network.NetworkResult
import org.garis.pam.data.model.remote.*
import org.garis.pam.data.remote.GeminiService

class AiRepository(private val geminiService: GeminiService) {

    private val json = Json { 
        ignoreUnknownKeys = true 
        coerceInputValues = true
    }

    suspend fun summarizeNote(content: String): NetworkResult<String> = withContext(Dispatchers.Default) {
        val systemPrompt = "Anda adalah asisten ringkasan yang handal. Buat ringkasan singkat dan padat dari teks berikut dalam Bahasa Indonesia. Gunakan poin-poin jika perlu."
        
        val request = GeminiRequest(
            contents = listOf(
                Content(role = "user", parts = listOf(Part(text = content)))
            ),
            systemInstruction = Content(parts = listOf(Part(text = systemPrompt))),
            generationConfig = GenerationConfig(
                temperature = 0.5f,
                responseMimeType = "text/plain"
            )
        )

        val result = geminiService.generateContent(request)
        
        when (result) {
            is NetworkResult.Success -> {
                val summary = result.data.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (summary != null) {
                    NetworkResult.Success(summary)
                } else {
                    NetworkResult.Error(message = "Gemini returned empty summary")
                }
            }
            is NetworkResult.Error -> result
            is NetworkResult.Loading -> NetworkResult.Loading
        }
    }

    suspend fun analyzeImage(base64Image: String, mimeType: String): NetworkResult<String> = withContext(Dispatchers.Default) {
        val systemPrompt = """
            Anda adalah asisten cerdas yang mahir dalam Optical Character Recognition (OCR) dan analisis gambar.
            Tugas Anda adalah:
            1. Mengambil semua teks dari gambar.
            2. Menjelaskan elemen visual penting (jika ada).
            3. Memformat hasilnya dalam Markdown yang sangat rapi dan terstruktur.
            4. Gunakan Bahasa Indonesia.
            
            Berikan output langsung dalam format Markdown.
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                Content(role = "user", parts = listOf(
                    Part(inlineData = InlineData(mimeType = mimeType, data = base64Image))
                ))
            ),
            systemInstruction = Content(parts = listOf(Part(text = systemPrompt))),
            generationConfig = GenerationConfig(
                temperature = 0.4f,
                responseMimeType = "text/plain"
            )
        )

        val result = geminiService.generateContent(request)
        
        when (result) {
            is NetworkResult.Success -> {
                val markdown = result.data.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (markdown != null) {
                    NetworkResult.Success(markdown)
                } else {
                    NetworkResult.Error(message = "Gemini failed to analyze image")
                }
            }
            is NetworkResult.Error -> result
            is NetworkResult.Loading -> NetworkResult.Loading
        }
    }

    suspend fun getNoteInsights(content: String): NetworkResult<AiInsightResponse> = withContext(Dispatchers.Default) {
        val systemPrompt = """
            Anda adalah analis cerdas untuk aplikasi 'News & Notes'.
            Tugas Anda adalah menganalisis teks catatan dan mengekstrak 4 hal:
            1. 'title': Judul singkat yang sangat menarik (max 5 kata).
            2. 'insights': Daftar poin-poin kunci atau fakta menarik dari teks (max 3 poin).
            3. 'actions': Daftar aksi yang disarankan berdasarkan konteks (max 3 aksi).
            4. 'suggestedTheme': Sarankan tema warna UI yang cocok dengan MOOD catatan ini.
            
            Format 'suggestedTheme' harus memiliki:
            - 'name': Nama tema yang unik.
            - 'primaryHex': Warna utama (Hex).
            - 'secondaryHex': Warna sekunder/teks (Hex).
            - 'accentHex': Warna aksen/border (Hex).
            - 'backgroundHex': Warna background gelap (Hex).

            Format 'actions' harus memiliki:
            - 'label': Teks pendek untuk tombol.
            - 'type': Salah satu dari ["TODO", "CALENDAR", "SEARCH"].
            - 'value': Konteks untuk aksi tersebut.

            Wajib mengembalikan format STRICT JSON sesuai skema berikut:
            {
              "title": "...",
              "insights": ["...", "..."],
              "actions": [{"label": "...", "type": "...", "value": "..."}],
              "suggestedTheme": {
                 "name": "Midnight Ocean",
                 "primaryHex": "#1A237E",
                 "secondaryHex": "#8C9EFF",
                 "accentHex": "#304FFE",
                 "backgroundHex": "#0A0E21"
              }
            }
            Jangan memberikan teks penjelasan apapun di luar JSON.
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                Content(role = "user", parts = listOf(Part(text = content)))
            ),
            systemInstruction = Content(parts = listOf(Part(text = systemPrompt))),
            generationConfig = GenerationConfig(
                temperature = 0.2f, // Sangat rendah untuk akurasi format JSON
                responseMimeType = "application/json"
            )
        )

        val result = geminiService.generateContent(request)
        
        when (result) {
            is NetworkResult.Success -> {
                val jsonText = result.data.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (jsonText != null) {
                    try {
                        // Bersihkan JSON dari markdown formatting jika ada (```json ... ```)
                        val cleanedJson = jsonText
                            .replace("```json", "")
                            .replace("```", "")
                            .trim()
                        
                        val insight = json.decodeFromString<AiInsightResponse>(cleanedJson)
                        NetworkResult.Success(insight)
                    } catch (e: Exception) {
                        NetworkResult.Error(message = "Format AI tidak valid: ${e.message}")
                    }
                } else {
                    NetworkResult.Error(message = "Gemini memberikan respon kosong")
                }
            }
            is NetworkResult.Error -> result
            is NetworkResult.Loading -> NetworkResult.Loading
        }
    }
}
