package de.upjoin.android.repository

interface SingleKeyRepository<T, V>: Repository<SingleKeyRepositoryKey, T, V> {

    fun get(): T? {
        return get(SingleKeyRepositoryKey)
    }

    fun set(repositoryObject: V) : Boolean {
        return set(SingleKeyRepositoryKey, repositoryObject)
    }

    fun remove(): Boolean {
        return remove(SingleKeyRepositoryKey)
    }

    override fun removeAll(): Boolean {
        return remove()
    }

    fun has(): Boolean {
        return has(SingleKeyRepositoryKey)
    }

}