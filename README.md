# Práctica 5 — Inventario con Room

**Iván Burrola Torres · A01562825 · TC2007B.402**

Implementación con asistencia de IA sobre el proyecto de arranque del profesor (`b515658`). Se conservan el paquete `mx.tec.inventario`, las pantallas, el tema, las validaciones y el dominio originales.

## Qué contiene

- Una tabla `productos` con ID automático, nombre, precio y cantidad.
- DAO con consultas observables (`Flow`) y escrituras `suspend`.
- `inventario.db`, una sola instancia con `applicationContext`.
- Repositorio y mapeadores separados del dominio.
- `Application`, contenedor y Factory para los tres ViewModels.
- Lista y detalle con `stateIn` y `collectAsStateWithLifecycle`, sin `recargar()`.
- Escrituras de alta, edición, venta y borrado en corrutinas.
- Estado inicial de carga, inventario vacío y lista con datos.

La base nueva comienza **vacía**. Los tres productos de ejemplo solo se conservan en las vistas previas del código original, no se insertan al arrancar.

## Abrir el proyecto

Descomprime el ZIP y abre esta carpeta en Android Studio. No la pegues encima del proyecto original: así conservas tu respaldo.

Se mantienen las versiones del proyecto y las de la guía: AGP 9.3.1, Kotlin 2.4.10, Gradle 9.7.1, SDK 37, Room 2.8.5 y KSP 2.3.12. El archivo de criterios de Gradle del starter pide JDK 25. No se incluyen `local.properties`, cachés ni rutas del equipo original.

El starter no traía `gradlew`, `gradlew.bat` ni el JAR del wrapper. El auxiliar opcional `Compilar_en_Mac.command` descarga la distribución oficial de Gradle con comprobación SHA-256, genera esos archivos y solicita `assembleDebug`. No se pudo completar ese proceso desde este entorno. Requiere Android Studio/SDK, Java compatible y acceso a los repositorios de dependencias.

## Estado real

El código de Room está implementado. Se realizó revisión estática, análisis de sintaxis Kotlin y comprobación aislada de consultas SQL. **No se generó un APK, no se ejecutó el proyecto en un emulador ni se grabó un video.** Lee `docs/verificacion.md` para el detalle; no confundas esos controles con una prueba de persistencia en Android.

## Entrega y autoría

`docs/bitacora.md` contiene respuestas basadas en el código y la guía, diferenciando lo esperado de lo observado. `docs/video.md` es un guion, no un video. `docs/defensa.md` resume los conceptos de la defensa.

La carpeta `.git` conserva el commit original y los cambios del asistente, con autoría y fechas reales. El remoto del profesor queda como `upstream`; no se creó ni se publicó un repositorio privado y no hay un `origin` ficticio.

La guía limita IA a tutoría: esta implementación generada no acredita por sí misma esa condición, la sesión personal ni la defensa oral. La declaración completa está en `docs/uso-de-ia.md`.

## Fuente principal

Guía adjunta «Práctica 5 · Inventario: datos que sobreviven a cerrar la app». Se implementan sus bloques A–D; no los retos opcionales para casa.
