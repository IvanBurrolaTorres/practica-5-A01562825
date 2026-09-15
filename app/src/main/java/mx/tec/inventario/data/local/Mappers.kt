package mx.tec.inventario.data.local

import mx.tec.inventario.domain.Producto

fun ProductoEntity.toDomain() = Producto(
    id = id,
    nombre = nombre,
    precio = precio,
    cantidad = cantidad
)

fun Producto.toEntity() = ProductoEntity(
    id = id,
    nombre = nombre,
    precio = precio,
    cantidad = cantidad
)
