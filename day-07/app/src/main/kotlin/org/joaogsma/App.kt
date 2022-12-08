package org.joaogsma

import kotlin.math.min

fun main(args: Array<String>) {
    val result: Long =
        when (val version = args[1]) {
            "1" -> runProblem1(args[0])
            "2" -> runProblem2(args[0])
            else -> throw IllegalArgumentException("Unknown variation $version")
        }
    println("Result is $result")
}

fun runProblem1(filename: String): Long {
    val lines: List<String> = readResource(filename)
    return parseFileSystem(lines)
        .toAggregator()
        .dfsFold(
            predicate = { it is Directory && it.getSize() <= 100000 },
            initial = 0L,
            operation = { count, node -> count + node.getSize() })
}

const val TOTAL_DISK_SPACE: Long = 70000000
const val REQUIRED_DISK_SPACE: Long = 30000000

fun runProblem2(filename: String): Long {
    val lines: List<String> = readResource(filename)
    val fs = parseFileSystem(lines)
    val unusedSpace = TOTAL_DISK_SPACE - fs.getTotalUsedDiskSpace()
    return fs.toAggregator()
        .dfsFold(
            predicate = { it is Directory && unusedSpace + it.getSize() >= REQUIRED_DISK_SPACE },
            initial = Long.MAX_VALUE,
            operation = { acc, node -> min(acc, node.getSize()) })
}

fun parseFileSystem(lines: List<String>): FileSystem {
    val fs = FileSystem()
    var currentCommand: String = lines.first()
    val output: MutableList<String> = mutableListOf()
    for (line in lines.drop(1)) {
        if (isNotCommand(line)) {
            output.add(line)
            continue
        }
        parse(currentCommand, output.toList(), fs)
        currentCommand = line
        output.clear()
    }
    parse(currentCommand, output.toList(), fs)
    return fs
}

fun isNotCommand(line: String): Boolean = line[0] != '$'

val COMMAND_REGEX: Regex = "\\$ +(\\w+) *(/|\\.\\.|\\w+)?".toRegex()

fun parse(
    command: String,
    output: List<String>,
    navigator: FileSystem
) {
    val matchResult: MatchResult =
        COMMAND_REGEX.matchEntire(command) ?: throw IllegalArgumentException("Not a valid command: $command")
    val (_, commandType: String, commandArgs) = matchResult.groupValues
    when (commandType) {
        "cd" -> parseCdCommand(commandArgs, navigator)
        "ls" -> parseLsCommand(output, navigator)
    }
}

fun parseCdCommand(commandArg: String, navigator: FileSystem) {
    when (commandArg) {
        "/" -> navigator.goToRoot()
        ".." -> navigator.goToParent()
        else -> navigator.goToChildren(commandArg)
    }
}

fun parseLsCommand(output: List<String>, navigator: FileSystem) {
    val directoryPrefix = "dir "
    output.forEach { line ->
        if (line.startsWith(directoryPrefix))
            return@forEach navigator.addDirectoryChild(line.removePrefix(directoryPrefix))
        val (sizeStr, name) = line.split(" ")
        navigator.addFileChild(name, sizeStr.toLong())
    }
}
