package com.marcmeru.merunoting.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcmeru.merunoting.data.entity.Item
import com.marcmeru.merunoting.data.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lógica y datos relacionados con los [Item].
 *
 * Expone listas de items raíz y de hijos para la UI, y proporciona
 * funciones para cargar, agregar, actualizar o eliminar items y carpetas.
 *
 * @property repository Instancia del [ItemRepository] para acceso a datos.
 */
class ItemViewModel(private val repository: ItemRepository) : ViewModel() {

    private val _rootItems = MutableStateFlow<List<Item>>(emptyList())
    /**
     * Flujo de estado con la lista actual de items raíz (sin padre).
     */
    val rootItems: StateFlow<List<Item>> = _rootItems

    private val _children = MutableStateFlow<Map<Long, List<Item>>>(emptyMap())
    /**
     * Flujo de estado que mapea el id del item padre a la lista de sus hijos.
     */
    val children: StateFlow<Map<Long, List<Item>>> = _children

    /**
     * Carga y actualiza la lista de items raíz desde el repositorio.
     */
    fun loadRootItems() {
        viewModelScope.launch {
            repository.getRootItems().collect {
                _rootItems.value = it
            }
        }
    }

    /**
     * Carga y actualiza la lista de hijos de un item padre identificado por [parentId].
     *
     * @param parentId ID del item padre.
     */
    fun loadChildren(parentId: Long) {
        viewModelScope.launch {
            repository.getChildren(parentId).collect { list ->
                _children.value = _children.value.toMutableMap().also {
                    it[parentId] = list
                }
            }
        }
    }

    /**
     * Inserta un nuevo [item] en la base de datos.
     *
     * @param item Item a insertar.
     * @param onComplete Callback opcional llamado tras completar la inserción.
     */
    fun addItem(item: Item, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.insert(item)
            onComplete?.invoke()
        }
    }

    /**
     * Actualiza un [item] existente en la base de datos.
     *
     * @param item Item a actualizar.
     * @param onComplete Callback opcional llamado tras completar la actualización.
     */
    fun updateItem(item: Item, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.update(item)
            onComplete?.invoke()
        }
    }

    /**
     * Elimina un [item] y todos sus descendientes (si es carpeta).
     *
     * @param item Item a eliminar.
     * @param onComplete Callback opcional llamado tras completar la eliminación.
     */
    fun deleteItem(item: Item, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            repository.deleteWithChildren(item)
            onComplete?.invoke()
        }
    }

    /**
     * Obtiene un [Item] por su [id].
     *
     * Esta función es suspend para evitar bloqueos y debe llamarse desde coroutine o LaunchedEffect.
     *
     * @param id ID del item a obtener.
     * @return El [Item] correspondiente al ID proporcionado, o null si no existe.
     */
    suspend fun getItemById(id: Long): Item {
        return repository.getById(id)
    }
}
