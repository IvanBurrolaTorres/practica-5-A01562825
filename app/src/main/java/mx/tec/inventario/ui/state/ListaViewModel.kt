package mx.tec.inventario.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import mx.tec.inventario.data.ProductoRepository
import mx.tec.inventario.domain.Producto

class ListaViewModel(repository: ProductoRepository) : ViewModel() {
    // null: cargando; emptyList(): consulta terminada sin productos.
    val productos: StateFlow<List<Producto>?> =
        repository.observarTodos().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(ESPERA_MS),
            initialValue = null
        )

    private companion object {
        const val ESPERA_MS = 5_000L
    }
}
