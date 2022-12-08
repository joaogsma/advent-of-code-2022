package org.joaogsma

fun rayTraceFindTreesTallerThan(grid: Grid<Int>, previousHeight: Int, ray: Ray): Set<Coordinates> {
    if (ray.origin.row !in 0 until grid.rows || ray.origin.col !in 0 until grid.cols)
        return emptySet()
    val currentHeight: Int = grid.get(ray.origin)
    if (previousHeight >= currentHeight)
        return rayTraceFindTreesTallerThan(grid, previousHeight, ray.increment())
    return setOf(ray.origin) + rayTraceFindTreesTallerThan(grid, currentHeight, ray.increment())
}

fun rayTraceFindTreesWithHeightUpTo(grid: Grid<Int>, maxHeight: Int, ray: Ray): Set<Coordinates> {
    if (ray.origin.row !in 0 until grid.rows || ray.origin.col !in 0 until grid.cols)
        return emptySet()
    val currentHeight: Int = grid.get(ray.origin)
    if (currentHeight >= maxHeight)
        return setOf(ray.origin)
    return setOf(ray.origin) + rayTraceFindTreesWithHeightUpTo(grid, maxHeight, ray.increment())
}

