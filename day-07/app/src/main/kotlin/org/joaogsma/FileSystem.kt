package org.joaogsma

class FileSystem {
    private val root: Directory = Directory("/", null)
    private var currentDir: Directory = root

    fun getTotalUsedDiskSpace(): Long = root.getSize()

    fun addFileChild(name: String, size: Long) { currentDir.addChild(File(name, size, currentDir)) }

    fun addDirectoryChild(name: String) { currentDir.addChild(Directory(name, currentDir)) }

    fun goToRoot() { currentDir = root }

    fun goToParent() {
        currentDir = currentDir.parent ?: throw IllegalStateException("Current node has no parent")
    }

    fun goToChildren(target: String) {
        val child: FileSystemNode =
            currentDir.getChildren().find { it.name == target }
                ?: throw IllegalArgumentException(
                    "Could not find child directory $target among ${currentDir.getChildren()}")
        if (child !is Directory)
            throw IllegalArgumentException("Child ${child.name} is not a directory")
        currentDir = child
    }

    fun toDrawer(): FileSystemDrawer = FileSystemDrawer(root)

    fun toAggregator(): FileSystemAggregator = FileSystemAggregator(root)
}

class FileSystemAggregator(private val root: Directory) {
    fun <T> dfsFold(
        predicate: (FileSystemNode) -> Boolean,
        initial: T,
        operation: (T, FileSystemNode) -> T
    ): T {
        return dfsFold(root, predicate, initial, operation)
    }

    private fun <T> dfsFold(
        node: FileSystemNode,
        predicate: (FileSystemNode) -> Boolean,
        initial: T,
        operation: (T, FileSystemNode) -> T
    ): T {
        val accumulator: T =
            if (node is Directory)
                node.getChildren().fold(initial) { acc, child -> dfsFold(child, predicate, acc, operation) }
            else initial
        if (predicate(node))
            return operation(accumulator, node)
        return accumulator
    }
}

class FileSystemDrawer(private val root: Directory) {
    fun draw() {
        draw(root, 0)
    }

    private fun draw(node: FileSystemNode, padding: Int) {
        (0 until padding).forEach { _ -> print(' ') }
        print("- ${node.name} ")
        when (node) {
            is Directory -> {
                println("(dir)")
                node.getChildren().forEach { draw(it, padding + 1) }
            }
            is File -> println("(file, size=${node.getSize()})")
        }
    }
}
