package fr.balthazarfrolin.kotlin.tools.others


fun <A> HashMap<A, Int>.appendValue(key: A, value: Int)
{
    val oldValue = this.getOrDefault(key, 0)
    this[key] = value + oldValue
}