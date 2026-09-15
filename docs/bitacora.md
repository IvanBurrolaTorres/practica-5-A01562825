# Bitácora — Práctica 5: Inventario

**Estudiante:** Iván Burrola Torres · **Matrícula:** A01562825  
**Curso:** TC2007B.402 · Tecnológico de Monterrey, Campus Monterrey

## Alcance de esta bitácora

Documento preparado con asistencia de IA. Las respuestas siguientes se apoyan en la guía adjunta y en el código de arranque `b515658` del ZIP. No son un testimonio de pruebas personales del estudiante. Cuando una observación requiere emulador o Database Inspector y no se ha realizado aquí, se señala.

## Ejercicio 0 — Qué va en la tabla

| Miembro | ¿Columna? | Motivo |
|---|---|---|
| `id` | Sí | Es la clave primaria; identifica una fila y permite editarla o borrarla sin confundirla con otra. SQLite genera el ID al insertar un producto con ID 0. |
| `nombre` | Sí | Es información capturada y debe conservarse. |
| `precio` | Sí | Se necesita para consultar y calcular el valor del inventario. Se conserva `Double`, tal como viene en el ejercicio. |
| `cantidad` | Sí | Es la existencia que cambia al editar o vender. |
| `agotado` | No | Se obtiene de `cantidad == 0`; guardarlo duplicaría información. |
| `valorEnInventario` | No | Se obtiene de `precio * cantidad`; se recalcula en el dominio. |

`ProductoEntity` tiene los cuatro datos que guarda SQLite. `Producto` conserva, además, las propiedades calculadas. Así el dominio no depende de Room.

## Ejercicio B1 — El problema de recargar

En el código de arranque hay **cuatro llamadas** a `recargar()` (sin contar las dos declaraciones de la función ni los comentarios):

| Archivo | Llamadas | Ubicación |
|---|---:|---|
| `ui/navigation/InventarioNavHost.kt` | 2 | Un `LaunchedEffect(Unit)` en la lista y otro en el detalle. |
| `ui/state/DetalleViewModel.kt` | 2 | Una en `init` y otra después de `venderUno()`. |

Si se elimina la llamada del detalle en el NavHost y luego se edita un producto, **se espera** que al volver el detalle conserve valores anteriores: su ViewModel sigue guardando la copia anterior, aunque el repositorio ya cambió. Es una conclusión del código y del ejercicio; no se registró esta interacción en un emulador aquí.

La actualización depende de quien coordina cada acción: en la navegación se acuerda quien programa el NavHost; en la venta lo hace el ViewModel. Las pantallas del starter solo reciben datos. Al migrar a Flow desaparece esa coordinación manual: Room vuelve a consultar cuando invalida la tabla y hay una suscripción activa.

## A3 — Columna mal escrita

El DAO final usa `nombre`, la columna que existe. La guía pide probar `nombres` y observar un error de KSP al compilar; esa ejecución de KSP queda pendiente. La comprobación aislada de SQL realizada por el asistente se documentará por separado y no sustituye la compilación Android.
