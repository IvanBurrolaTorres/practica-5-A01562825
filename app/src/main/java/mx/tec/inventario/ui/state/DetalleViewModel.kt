package mx.tec.inventario.ui.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mx.tec.inventario.data.ProductoRepository
import mx.tec.inventario.domain.Producto
import mx.tec.inventario.ui.navigation.Route

class DetalleViewModel(
    private val repository: ProductoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val productoId: Int =
        checkNotNull(savedStateHandle.get<Int>(Route.ARG_PRODUCTO_ID))

    val producto: StateFlow<Producto?> =
        repository.observarPorId(productoId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(ESPERA_MS),
            initialValue = null
        )

    fun venderUno() {
        val actual = producto.value ?: return
        if (actual.agotado) return
        viewModelScope.launch {
            repository.actualizar(actual.copy(cantidad = actual.cantidad - 1))
        }
    }

    fun borrar(alTerminar: () -> Unit) {
        val actual = producto.value ?: return
        viewModelScope.launch {
            repository.borrar(actual)
            alTerminar()
        }
    }

    private companion object {
        const val ESPERA_MS = 5_000L
    }
}
