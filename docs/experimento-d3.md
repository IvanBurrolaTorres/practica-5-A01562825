# Rama experimento-d3

Esta rama contiene dos cambios sucesivos tomados del ejercicio D3:

1. Se añade `categoria: String = "General"` a ProductoEntity sin cambiar la versión 1 de la base. La guía prevé un error de integridad al abrir una base antigua con el esquema nuevo.
2. Se eleva la versión a 2 y se incorpora `fallbackToDestructiveMigration(dropAllTables = true)`. Según la guía, cuando no hay ruta de migración, se recrea la base y se pierden sus datos.

**Estado de verificación:** ambas variantes se revisaron a nivel de sintaxis. No se instalaron sobre una aplicación con datos ni se observó el error de Room en este entorno. Los mensajes de la guía son resultados esperados, no logs de ejecución propios.

Esta variante no es la aplicación principal. `main` permanece en versión 1, sin categoría y sin migración destructiva.

**Precaución al volver a main:** cambiar de rama restaura archivos del proyecto, no el archivo SQLite ya existente en un dispositivo. Si el experimento llegó a crear una base versión 2, una app versión 1 puede rechazarla. Las pruebas deben usar una instalación de prueba separada o datos desechables; no se borran datos automáticamente en este proyecto.
