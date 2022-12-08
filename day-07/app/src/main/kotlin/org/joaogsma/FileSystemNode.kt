package org.joaogsma

abstract class FileSystemNode(val name: String, val parent: Directory?) {
    abstract fun getSize(): Long
}

class Directory(name: String, parent: Directory?): FileSystemNode(name, parent) {
    private var children: MutableSet<FileSystemNode> = mutableSetOf()
    private var size: Long = 0

    override fun getSize(): Long = size

    fun getChildren(): Set<FileSystemNode> = children.toSet()

    fun addChild(child: FileSystemNode) {
        children.add(child)
        updateSize()
    }

    private fun updateSize() {
        size = children.sumOf { it.getSize() }
        parent?.updateSize()
    }
}

class File(name: String, private val size: Long, parent: Directory): FileSystemNode(name, parent) {
    override fun getSize(): Long = size
}
