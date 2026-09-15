package mx.tec.inventario.ui.state

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import mx.tec.inventario.InventarioApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ListaViewModel(inventarioApplication().container.productoRepository)
        }
        initializer {
            DetalleViewModel(
                repository = inventarioApplication().container.productoRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }
        initializer {
            FormularioViewModel(
                repository = inventarioApplication().container.productoRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }
    }
}

private fun CreationExtras.inventarioApplication(): InventarioApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as InventarioApplication
