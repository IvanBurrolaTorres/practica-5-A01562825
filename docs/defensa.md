# Recordatorio para la defensa oral

Basado en las preguntas de la guía. Son explicaciones para estudiar, no una constancia de defensa realizada.

**¿Por qué ProductoEntity y Producto?** La entidad describe las columnas de SQLite; el dominio describe el producto y sus propiedades calculadas. Los mapeadores traducen entre ambos. Cambiar el almacenamiento no obliga a cambiar el dominio.

**¿Por qué insertar es suspend y observarTodos no?** Insertar realiza una escritura asíncrona. ObservarTodos devuelve un Flow: los resultados se reciben al recolectarlo y cada vez que Room invalida la consulta.

**¿Por qué la lista se actualiza sola?** La escritura cambia la tabla; Room vuelve a consultar para los observadores; el Flow emite, stateIn actualiza el StateFlow y Compose recibe el nuevo estado con collectAsStateWithLifecycle.

**¿Qué significa ID 0?** Con autoGenerate es la señal usada aquí para que Room/SQLite asignen el ID. Un ID no cero solicita ese valor explícito; si ya existe, la inserción normal puede fallar por clave duplicada.

**¿Cuándo se detecta una columna inexistente en @Query?** La guía muestra que Room/KSP valida el SQL durante la compilación. No necesita esperar a abrir la pantalla.

**¿Qué pasa con la migración destructiva?** Si se necesita una migración y se aplica esa opción, las tablas se recrean y se pierden los datos. La rama main de esta implementación conserva versión 1 y no habilita esa opción.
