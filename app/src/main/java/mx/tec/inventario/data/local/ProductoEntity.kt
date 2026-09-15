package mx.tec.inventario.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Representa una fila de SQLite, no el modelo del dominio.
@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val precio: Double,
    val cantidad: Int
)
