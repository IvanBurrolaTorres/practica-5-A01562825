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

## C2 — Persistencia y actualización automática

El cambio técnico es sustituir la lista mutable por una base de datos en disco. La implementación ya no reinicia tres productos en cada proceso. Al guardar, el código realiza la escritura antes de ejecutar `alTerminar()`.

La cadena de la guía es: escritura → invalidación de la tabla → nueva consulta de Room → emisión del Flow → StateFlow → recomposición de Compose. No se añade una llamada manual para refrescar.

**Prueba en Android pendiente:** agregar un producto, terminar realmente el proceso y reabrir la aplicación. No se afirma haber visto persistencia en un emulador. La comprobación de archivo SQLite realizada aparte no sustituye esa prueba.

## Ejercicio D1 — Una escritura, dos pantallas

`venderUno()` toma el producto, evita vender si está agotado y actualiza la misma fila usando su ID. La cantidad baja en uno; el valor se obtiene de `precio * cantidad` en el dominio.

Según el mecanismo descrito en la guía, cuando la tabla cambia, **Room** vuelve a ejecutar las consultas observadas. La UI y el NavHost no llaman a `recargar()`. Si la lista está siendo observada durante una venta, se espera una nueva consulta de esa lista y la emisión de su resultado actualizado.

**Número de ejecuciones medido:** no disponible. No se utilizó Database Inspector aquí y no se inventa un conteo. Como `WhileSubscribed(5000)` puede detener la suscripción de una pantalla que ya no se observa, no se presenta «una consulta en todos los casos» como medición. Para la comprobación solicitada habría que observar una venta con el inspector abierto y registrar lo que ocurra en ese dispositivo.

## D2 — Borrado

El repositorio borra la fila y después ejecuta la navegación de regreso. El detalle acepta un producto nulo mientras llega la primera consulta o después de desaparecer la fila, como plantea la guía. La lista se alimenta del Flow del DAO, no de una lista en memoria.

## Separación entre lo implementado y lo observado

Los cambios A–C anteriores están en el código. Los ensayos visuales de cierre/reapertura, bloqueo temporal del botón Guardar e inspección de la tabla no se registraron en este entorno. La evidencia de ejecución debe obtenerse del proyecto real, no de una pantalla simulada.

## Ejercicio D3 — Cambiar el esquema

La rama `experimento-d3` conserva dos estados:

1. `88c637c`: `ProductoEntity` recibe `categoria`, pero Database continúa en versión 1.
2. `2b6b106`: Database pasa a versión 2 e incorpora `fallbackToDestructiveMigration(dropAllTables = true)`.

**Qué se comprobó aquí:** los dos cambios están en commits separados y ambos pasan el análisis de sintaxis Kotlin. La rama `main` conserva las cuatro columnas originales, versión 1 y ninguna migración destructiva. No se ha ejecutado el APK de estas variantes en un dispositivo.

**Resultado esperado según la guía:** en el primer estado, una instalación con la base anterior debe rechazar el esquema nuevo con `Room cannot verify the data integrity`. En el segundo, la migración destructiva recrea la base y pierde el inventario previo. Estos son resultados descritos por la guía, no una excepción ni pérdida de datos observada por el estudiante o el asistente.

**Por qué ocurre:** Room compara el esquema esperado con el guardado. Una columna nueva modifica el esquema; no basta con volver a compilar. Subir la versión permite tratar la transición, pero la opción destructiva no conserva las filas. Una migración real sería necesaria para conservar datos en producción; no se añadió porque es un reto opcional de esta práctica.

**Precaución técnica añadida:** volver a `main` cambia el código, no revierte el archivo de base de datos de un teléfono. Si se prueba versión 2 y después se instala versión 1 sobre esos datos, puede requerirse una instalación de prueba limpia. No se ejecuta ninguna limpieza de datos automáticamente.

## Cierre

Implementación principal: entidad separada, DAO, Database, mapeadores, contenedor, Factory, Flow y operaciones de alta/edición/venta/borrado. Los seis checkpoints son etapas técnicas preparadas por IA con sus fechas reales, no evidencia de trabajo personal en clase. La defensa oral y las comprobaciones visuales siguen siendo personales y no se sustituyen con este documento.

## Fuentes

- Guía adjunta: «Práctica 5 · Inventario: datos que sobreviven a cerrar la app», bloques 0, A, B, C y D.
- Proyecto de arranque del profesor: `rpversontec/practica-5-inventario`, commit `b515658`, incluido en el ZIP recibido.
- Asistencia de implementación y revisión: ChatGPT, declarada en el historial y en `docs/uso-de-ia.md`.
