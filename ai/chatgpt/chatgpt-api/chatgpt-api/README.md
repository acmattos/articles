# Chat Completions API: Unleashing Unlimited Creativity With ChatGPT

## A Comprehensive Developer's Guide to Efficiently Using ChatGPT Chat Completions API

## Preparing your Environment to Use ChatGPT API
Using the ChatGPT API requires setting up your development environment correctly. 
Follow these steps to configure your environment and start leveraging the power 
of ChatGPT in your applications.

* _Step 1_: Sign Up for ChatGPT API Access
  Sign up for ChatGPT API access on the OpenAI platform. Go to the OpenAI 
* website and create an account if you haven't already. Navigate to the API 
* section. Follow the instructions to request access to ChatGPT API.

* _Step 2_: Obtain API Key
  After your access request is approved, you'll receive an API key. This key is 
* crucial for authenticating your API requests and accessing ChatGPT services. 
* Keep your API key secure and do not share it publicly.

* Step 3: Choose Your Programming Language
  Kotlin is our choice. ChatGPT API supports various programming languages, 
* including Python, JavaScript, Java, Ruby, and more.

* _Step 4_: Install Required Libraries or SDKs
  Install the necessary libraries and SDKs, if necessary, to interact with the 
* ChatGPT API.

* _Step 5_: Set Up Authentication
  In your application code, set up authentication using your API key. The sample 
* Kotlin code presented in this article takes advantage of hiding the API key 
* from the public audience. It´s stored in the system environment´s variables 
* (please, verify how to do it in your operating system).

* _Step 6_: Test ChatGPT´s Chat Completion API Integration
  After the authentication setup, you can start testing the Chat Completions API 
* integration by making sample requests to the ChatGPT API endpoint. This step 
* will be discussed in details below.

## Building the Code
### Sending the request to the Chat Completion API
```json
curl https://api.openai.com/v1/chat/completions \
 -H "Content-Type: application/json" \
 -H "Authorization: Bearer $OPENAI_API_KEY" \
 -d '{
  "model": "gpt-3.5-turbo",
  "messages": [{"role": "user", "content": "Say Hello World!"}],
  "temperature": 0.0
}'
```
From ChatGPT's API reference, you can get a sample request call like the one 
above. Here you can see the minimum arguments required to call the Chat 
Completions API. That information will guide us to build a useful request to get 
the same result as the curl call.

### Getting the response from the Chat Completion API
```json
{
 "choices": [{
  "finish_reason": "stop",
  "index": 0,
  "logprobs": null,
  "message": {
   "content": "Hello World! Nice to meet you!",
   "role": "assistant"
  }
 }],
 "created": 1710971433,
 "id": "chatcmpl-94y7N6H4pEGbey9s4yTpiHbmyq3HS",
 "model": "gpt-3.5-turbo-0125",
 "object": "chat.completion",
 "system_fingerprint": "fp_4f0b692a78",
 "usage": {
  "completion_tokens": 8,
  "prompt_tokens": 12,
  "total_tokens": 20
 }
}
```

This response can be retrieved from ChatGPT, using the previous curl request. 
Several attributes in the response are not the target for our discussion. We are 
going to focus on the choices attribute only.

Getting all the information needed to build a simple but useful Kotlin code to 
get the information needed from ChatGPT, let´s start the building work.

### Configuring build.gradle.kts file
```Gradle
plugins {
 kotlin("jvm") version "1.9.20"
}

dependencies {
 implementation ("com.squareup.retrofit2:retrofit:2.9.0")
 implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
}

repositories {
 mavenCentral()
}
```

We'll utilize [Retrofit](https://square.github.io/retrofit/) as our HTTP client
generator to construct a ChatGPT HTTP client to handle the [Chat Completion API](https://api.openai.com/v1/chat/completions) calls.

### Creating the Chat Completion Request
```Kotlin
class ChatCompletionRequest(
  val messages: List<Message,
  val model: String,
  val temperature: Double? = 0.0,
)

class Message(
 val role: String,
 val content: String,
 val name: String? = null,
)
```

The `ChatCompletionRequest` class provides attributes to handle the proper request
configuration. The helper Message class provides the role and content
configuration.

The `model` attribute connects you to a GPT model instance. "gpt-4" model would be
a nice choice, but it´s not available for free. So, let´s use "gpt-3.5-turbo".

The `temperature` attribute makes the model behaves more deterministically,
using values close to 0.0. Higher values (0.8 - 2.0) makes the model be more
creative in its responses.

The `messages` attribute contains a list of message objects used to "talk" to the
model. The message contains two attributes:
- The `role` attribute holds the role of the messenger (either system, user,
  assistant or tool);
- The `content` attribute holds the content of the message (e.g., Say Hello
  World!).

### Creating the Chat Completion Response
```Kotlin
class ChatCompletionResponse(
 val choices: List<Choice>,
)

class Choice(
 val message: Message,
)
```
The decision here is to keep the code simple. We will use just what matters to 
allow perfect communication with the Chat Completions API. To handle the 
responses, it is only necessary to implement the choices attribute. The element 
consists of a list containing completion objects in it. The list´s size will 
always be one because there will be no configuration on n request´s attribute 
(out of the scope of this post). The completion objects are the type of Messages, 
as you can see above.

### Creating the API Code  
```Kotlin
interface ChatCompletionApi {
  @POST("v1/chat/completions")
  fun getChatCompletion(
    @Header("Authorization") authorization: String,
    @Body chatCompletionRequest: ChatCompletionRequest
  ): Call<ChatCompletionResponse>
}
```

The `ChatCompletionApi` interface will be managed by `Retrofit` to create an  
objet responsible for the interaction with ChatGPT´s Chat Completion API. The 
API key needs to be sent here, otherwise you´ll not get a response from ChatGPT.

### Creating the Chat Completions API Code
```Kotlin
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
  
  fun getCompletion(
    prompt: String,
    model: String = "gpt-3.5-turbo",
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
```
The ChatCompletionClient acts as a facade to Chat Completions API. You can send 
the "prompt" message and retrieve the results. You are free to change the model 
and temperature of the response.  A null response will be the result of using 
"gpt-4" as the model and you are not allowed to work with it.

## Seeing the Magic Happening
```Kotlin
fun main() {
  val client = ChatCompletionClient()
  val prompt = "Say Hello World!"
  val response = client.getCompletion(prompt)
  // Prints: Hello World! Nice to meet you!
  println(response?.choices?.get(0)?.message?.content)
}
```
Now it´s time to see the result of your work. All designed classes will work 
together, to produce the desired result: a request was made to ChatGPT and the 
model answered it. Check it out!

## Conclusion

The code presented represents the minimum effort required to explore the 
capabilities of the Chat Completions API. Feel free to experiment, iterate, and 
innovate to fully leverage the potential of ChatGPT in your projects.

You can customize the request by modifying the prompt variable content. Unleash 
unlimited creativity with ChatGPT here! What response did you receive? Share 
your insights in the comments section below!

You can download a version of the code showcased in this article from my GitHub 
repository.

Thank you for reading this article!