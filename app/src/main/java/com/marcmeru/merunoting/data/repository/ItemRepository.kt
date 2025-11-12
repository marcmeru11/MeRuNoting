package com.marcmeru.merunoting.data.repository

import com.marcmeru.merunoting.data.dao.ItemDao
import com.marcmeru.merunoting.data.entity.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repositorio encargado de gestionar las operaciones de acceso a datos para los [Item].
 *
 * Abstrae la lógica de origen de datos y ofrece una interfaz para el ViewModel u otras capas.
 *
 * @property itemDao DAO para acceso directo a la base de datos Room.
 */
class ItemRepository(private val itemDao: ItemDao) {

    /**
     * Obtiene una lista de items raíz (sin padre).
     *
     * @return Flujo que emite la lista de items raíz.
     */
    fun getRootItems(): Flow<List<Item>> = flow {
        emit(itemDao.getRootItems())
    }

    /**
     * Obtiene los hijos directos de un item padre identificado por [parentId].
     *
     * @param parentId ID del item padre.
     * @return Flujo que emite la lista de hijos.
     */
    fun getChildren(parentId: Long): Flow<List<Item>> = flow {
        emit(itemDao.getChildren(parentId))
    }

    /**
     * Inserta un nuevo item en la base de datos.
     *
     * @param item Item a insertar.
     * @return ID generado del item insertado.
     */
    suspend fun insert(item: Item): Long = itemDao.insert(item)

    /**
     * Actualiza un item existente en la base de datos.
     *
     * @param item Item a actualizar.
     */
    suspend fun update(item: Item) = itemDao.update(item)

    /**
     * Elimina un item de la base de datos.
     *
     * @param item Item a eliminar.
     */
    suspend fun delete(item: Item) = itemDao.delete(item)

    /**
     * Elimina un item y todos sus descendientes recursivamente.
     *
     * @param item Item a eliminar.
     */
    suspend fun deleteWithChildren(item: Item) = itemDao.deleteWithChildren(item)

    /**
     * Obtiene un item por su ID.
     *
     * @param id ID del item buscado.
     * @return Item si se encuentra; null en caso contrario.
     */
    suspend fun getById(id: Long): Item? = itemDao.getById(id)
}
