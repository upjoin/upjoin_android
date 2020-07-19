package de.upjoin.android.repository

interface Repository<K, T, V> {

    fun get(key: K): T?

    fun set(key: K, repositoryObject: V) : Boolean

    fun remove(key: K): Boolean

    fun removeAll(): Boolean

    fun has(key: K): Boolean {
        return get(key) != null
    }

}