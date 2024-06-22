package br.com.acmattos.chatgpt

import br.com.acmattos.chatgpt.chat.completions.technique.ZeroShotPrompting

fun main() {
    println("Zero-Shot Prompting Examples")
    val zsp = ZeroShotPrompting(true)
    println("1. Sumarize Text:")
    zsp.sumarizeText()
    System.exit(0)
}
