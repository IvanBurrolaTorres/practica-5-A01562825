package mx.tec.inventario

import android.app.Application
import android.content.Context
import mx.tec.inventario.data.ProductoRepository
import mx.tec.inventario.data.local.InventarioDatabase

class AppContainer(private val context: Context) {
    val productoRepository: ProductoRepository by lazy {
        ProductoRepository(InventarioDatabase.obtener(context).productoDao())
    }
}

class InventarioApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
