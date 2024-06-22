package br.com.acmattos.chatgpt.chat.completions.client

import br.com.acmattos.chatgpt.chat.completions.api.ChatCompletionApi
import br.com.acmattos.chatgpt.chat.completions.api.message.UserMessage
import br.com.acmattos.chatgpt.chat.completions.api.request.ChatCompletionRequest
import br.com.acmattos.chatgpt.chat.completions.api.request.GptModel
import br.com.acmattos.chatgpt.chat.completions.api.request.GptModel.GPT_35_TURBO
import br.com.acmattos.chatgpt.chat.completions.api.response.ChatCompletionResponse
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
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
class ChatCompletionClient(private val enableLocalServer: Boolean = false) {
    private val api: ChatCompletionApi
    private val accessToken: String = System.getenv("OPENAI_API_KEY")
        ?: "LOCAL_LLM_SERVER"

    init {
        val client = OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS) // Timeout for connecting to server
            .readTimeout(60, TimeUnit.SECONDS) // Timeout for reading data
            .build()
        val retrofit: Retrofit = Builder()
            .client(client)
            .baseUrl(getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ChatCompletionApi::class.java)
    }

    /**
     * Getts the base url based on local or remote server configuration.
     */
    private fun getBaseUrl() = if (enableLocalServer) {
        BASE_LOCAL_URL
    } else {
        BASE_REMOTE_URL
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
        private const val BASE_REMOTE_URL = "https://api.openai.com/"
        private const val BASE_LOCAL_URL = "http://localhost:1234/"
    }
}
