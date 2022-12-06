package org.joaogsma

import java.util.Stack

abstract class CraneCommand(protected val quantity: Int, protected val from: Int, protected val to: Int) {
    abstract fun run(stacks: List<Stack<Char>>)
}

typealias CraneCommandFactory = (quantity: Int, from: Int, to: Int) -> CraneCommand

class SingleCrateCraneCommand(quantity: Int, from: Int, to: Int): CraneCommand(quantity, from, to) {
    override fun run(stacks: List<Stack<Char>>) {
        (0 until quantity).forEach { _ -> stacks[to].push(stacks[from].pop()) }
    }
}

class MultiCrateCraneCommand(quantity: Int, from: Int, to: Int): CraneCommand(quantity, from, to) {
    override fun run(stacks: List<Stack<Char>>) {
        val temp: Stack<Char> = Stack()
        (0 until quantity).forEach { _ -> temp.push(stacks[from].pop()) }
        while (temp.isNotEmpty()) stacks[to].push(temp.pop())
    }
}
