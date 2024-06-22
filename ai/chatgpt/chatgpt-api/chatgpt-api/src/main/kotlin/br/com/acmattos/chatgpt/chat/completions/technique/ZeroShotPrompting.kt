package br.com.acmattos.chatgpt.chat.completions.technique

import br.com.acmattos.chatgpt.chat.completions.client.ChatCompletionClient

/**
 * Zero-shot prompting involves instructing a model to perform a task without
 * providing any examples or demonstrations within the prompt itself. This
 * method relies solely on the prompt's wording to guide the model's behavior,
 * without additional context or specific examples to steer its response.
 */
class ZeroShotPrompting(private val enableLocalServer: Boolean = false) {
    private val client = ChatCompletionClient(enableLocalServer)

    fun sumarizeText() {
        val text = """
    Kotlin is a modern, statically-typed programming language that runs on the 
    Java Virtual Machine (JVM). It was developed by JetBrains in 2011 and 
    officially released in 2016. Kotlin aims to be concise, expressive, and 
    safe, offering a range of features that make it a popular choice for 
    developing Android apps, server-side applications, and more.

    One of Kotlin's key features is its interoperability with Java, allowing 
    developers to easily integrate Kotlin code into existing Java projects and 
    vice versa. This makes it a smooth transition for Java developers looking to 
    adopt Kotlin or leverage its modern features.

    Kotlin's syntax is clean and intuitive, reducing boilerplate code and making
    codebases more readable and maintainable. It supports functional programming
    paradigms, higher-order functions, lambdas, and immutability by default, 
    promoting a more robust and scalable code structure.

    The language also includes features like null safety, extension functions, 
    coroutines for asynchronous programming, data classes for easy object 
    modeling, and smart type inference, enhancing productivity and reducing the 
    likelihood of runtime errors.

    Overall, Kotlin offers a powerful and efficient development experience, 
    making it a versatile choice for building a wide range of applications 
    across different platforms.
    """
        val prompt = """
    Summarize the text delimited by triple backticks into a single sentence.
    ```{$text}```
    """
        val response = client.getCompletion(prompt)
        println(response?.choices?.get(0)?.message?.content)
    }
}