package mx.tec.inventario.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mx.tec.inventario.data.ProductoRepository
import mx.tec.inventario.domain.Producto
import mx.tec.inventario.domain.ProductoError
import mx.tec.inventario.domain.ProductoValidator
import mx.tec.inventario.ui.navigation.Route

/**
 * Lo que el usuario lleva tecleado. Todo es texto: un campo vacío no es un
 * cero, y "12.9." no es un número todavía.
 */
data class FormularioUiState(
    val nombre: String = "",
    val precio: String = "",
    val cantidad: String = "",
    val guardando: Boolean = false
) {
    // Estado DERIVADO: se calcula, no se guarda. No se marca en rojo un campo
    // que el usuario todavía no ha tocado.
    val nombreError: ProductoError? =
        if (nombre.isEmpty()) null else ProductoValidator.validarNombre(nombre)

    val precioError: ProductoError? =
        if (precio.isEmpty()) null else ProductoValidator.validarPrecio(precio)

    val cantidadError: ProductoError? =
        if (cantidad.isEmpty()) null else ProductoValidator.validarCantidad(cantidad)

    val puedeGuardar: Boolean =
        ProductoValidator.esValido(nombre, precio, cantidad) && !guardando
}

/**
 * El mismo formulario sirve para dar de alta y para editar. La única diferencia
 * es si venía un id en la ruta.
 */
class FormularioViewModel(
    private val repository: ProductoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val productoId: Int? = savedStateHandle.get<Int>(Route.ARG_PRODUCTO_ID)

    val esEdicion: Boolean = productoId != null

    var uiState by mutableStateOf(FormularioUiState())
        private set

    init {
        if (productoId != null) cargar(productoId)
    }

    // Una sola lectura: no sobrescribir lo que el usuario esté tecleando.
    private fun cargar(id: Int) {
        viewModelScope.launch {
            val producto = repository.observarPorId(id).filterNotNull().first()
            uiState = FormularioUiState(
                nombre = producto.nombre,
                precio = producto.precio.toString(),
                cantidad = producto.cantidad.toString()
            )
        }
    }

    fun onNombreChange(texto: String) {
        uiState = uiState.copy(nombre = texto)
    }

    fun onPrecioChange(texto: String) {
        uiState = uiState.copy(precio = texto)
    }

    fun onCantidadChange(texto: String) {
        uiState = uiState.copy(cantidad = texto)
    }

    fun guardar(alTerminar: () -> Unit) {
        if (!uiState.puedeGuardar) return
        viewModelScope.launch {
            uiState = uiState.copy(guardando = true)
            val producto = Producto(
                id = productoId ?: 0,
                nombre = uiState.nombre.trim(),
                precio = uiState.precio.toDouble(),
                cantidad = uiState.cantidad.toInt()
            )
            if (productoId == null) repository.agregar(producto)
            else repository.actualizar(producto)
            uiState = uiState.copy(guardando = false)
            // Regresar a la lista solamente después de escribir en SQLite.
            alTerminar()
        }
    }
}
