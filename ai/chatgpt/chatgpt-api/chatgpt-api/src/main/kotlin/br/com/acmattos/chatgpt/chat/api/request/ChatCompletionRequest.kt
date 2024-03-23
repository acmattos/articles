package br.com.acmattos.chatgpt.chat.api.request

import br.com.acmattos.chatgpt.chat.api.message.Message

/**
 * Available GPT models.
 */
enum class GptModel(val value: String) {
    GPT_4("gpt-4"),
    GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview"),
    GPT_4_VISION_PREVIEW(" gpt-4-vision-preview"),
    GPT_4_32K("gpt-4-32k"),
    GPT_35_TURBO("gpt-3.5-turbo"),
    GPT_35_TURBO_16K("gpt-3.5-turbo-16k"),
}

/**
 * Represents a chat completion request used to access the model.
 *
 * @property messages a list of messages comprising the conversation so far.
 * @property model ID of the model to use. See the model endpoint compatibility
 *                 table for details on which models work with the Chat API.
 * @property temperature (optional - defaults to 1) what sampling temperature
 *                       to use, between 0 and 2. Higher values like 0.8 will
 *                       make the output more random, while lower values like
 *                       0.2 will make it more focused and deterministic.
 *                       We generally recommend altering this or
 *                       <code>topP</code> but not both.
 * @property topP (optional - defaults to null) an alternative to sampling with
 *                temperature, called nucleus sampling, where the model
 *                considers the results of the tokens with top_p probability
 *                mass. So 0.1 means only the tokens comprising the top 10%
 *                probability mass are considered.
 *                We generally recommend altering this or
 *                <code>temperature</code> but not both.
 */
class ChatCompletionRequest(
    val messages: List<Message<*>>,
    val model: String,
    val temperature: Double? = 0.0,
    val topP: Double? = null,
)
