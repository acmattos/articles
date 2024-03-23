package br.com.acmattos.chatgpt.chat.client

import br.com.acmattos.chatgpt.chat.api.ChatCompletionApi
import br.com.acmattos.chatgpt.chat.api.message.UserMessage
import br.com.acmattos.chatgpt.chat.api.request.ChatCompletionRequest
import br.com.acmattos.chatgpt.chat.api.request.GptModel
import br.com.acmattos.chatgpt.chat.api.request.GptModel.GPT_35_TURBO
import br.com.acmattos.chatgpt.chat.api.response.ChatCompletionResponse
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A client to access completion´s  Chat GPT endpoint.
 *
 * This class uses the Chat GPT´s token to authenticate your calls to the API.
 * The token is stored in as an environment variable.
 *
 * @property OPENAI_API_KEY Authentication token set as an environment variable.
 */
class ChatCompletionClient {
    private val api: ChatCompletionApi
    private val accessToken: String = System.getenv("OPENAI_API_KEY")

    init {
        val retrofit: Retrofit = Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ChatCompletionApi::class.java)
    }

    /**
     * Get completion call.
     *
     * @param prompt the prompt to be sent to Chat GPT.
     * @param model the model to be accessed (The default is "gpt-3.5-turbo").
     * @param temperature what sampling temperature to use, between 0 and 2.
     *                    Higher values like 0.8 will make the output more
     *                    random, while lower values like 0.2 will make it more
     *                    focused and deterministic.
     */
    fun getCompletion(
        prompt: String,
        model: GptModel = GPT_35_TURBO,
        temperature: Double = 0.0,
    ): ChatCompletionResponse? {
        val messages = listOf(UserMessage(content = prompt))
        val request = ChatCompletionRequest(
            messages = messages,
            model = model.value,
            temperature = temperature,
        )
        return api.getChatCompletion(
            "Bearer $accessToken",
            request
        ).execute()
         .body()
    }

    companion object {
        private const val BASE_URL = "https://api.openai.com/"
    }
}
