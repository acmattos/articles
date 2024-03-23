package br.com.acmattos.chatgpt.chat.api

import br.com.acmattos.chatgpt.chat.api.request.ChatCompletionRequest
import br.com.acmattos.chatgpt.chat.api.response.ChatCompletionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * The Chat Completion API definitions.
 *
 * This class is configured by Retrofit to access Completion API.
 */
interface ChatCompletionApi {
    /**
     * Get chat completion call.
     *
     * @param authorization the Chat GPT user´s token.
     * @param chatCompletionRequest the request used to retrieve responses from chat.
     */
    @POST("v1/chat/completions")
    fun getChatCompletion(
        @Header("Authorization") authorization: String,
        @Body chatCompletionRequest: ChatCompletionRequest
    ): Call<ChatCompletionResponse>
}
