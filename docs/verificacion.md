# Verificación técnica y límites

## Hecho sobre el ZIP real

El original tenía un commit (`b515658`), almacenamiento en memoria y las pantallas del inventario. Se preservó ese historial y se implementaron los bloques de Room de la guía. No se sustituyó la app por una maqueta ni por otro proyecto.

### Controles ejecutados

| Comprobación | Resultado real |
|---|---|
| Parser de sintaxis Kotlin local sobre fuentes `.kt` y scripts `.kts` | 25 archivos, sin errores de sintaxis. |
| Análisis de las dos variantes D3 | Sin errores de sintaxis. |
| Catálogo TOML y manifiesto XML | Lectura válida; dependencias y Application presentes. |
| Comparación con el starter | Domain, FormularioUiState, pantallas, componentes y tema conservados. |
| Revisión del flujo reactivo | Cuatro ViewModels con Factory; dos recolecciones con ciclo de vida; ninguna llamada a recargar en el código principal. |
| DAO | Dos SELECT con Flow; tres escrituras suspend. |
| Base principal | Versión 1, archivo inventario.db, singleton y sin migración destructiva. |
| SQL en SQLite aislado | SELECT por nombre y por ID correctos, ID inexistente sin fila, error por columna `nombres`, actualización/borrado y reapertura de archivo comprobados. |
| Compilación del dominio sin Android | Los dos archivos originales compilaron con Kotlin/JVM 1.9.0. |
| Historial y limpieza | Seis checkpoints nuevos en main; rama D3 separada, autoría de IA y fechas reales. |

La auditoría de estructura/SQL reúne 36 controles. Sus salidas están en `docs/evidencias/`. **Estos controles no equivalen a ejecutar Room o Compose**: el parser no resuelve dependencias, y la base SQLite aislada no es la base de una app Android.

## No realizado

No había Gradle instalado, Android SDK ni emulador. La descarga de la distribución oficial de Gradle y del índice de Android SDK falló por resolución de nombres desde este entorno; se conserva el resultado en `preparacion-entorno.txt`.

Por ello no se ejecutaron sincronización Gradle del proyecto, compilación con KSP, generación de APK, apertura en Android Studio, CRUD en un dispositivo, prueba de muerte del proceso, Database Inspector ni reproducción del error D3 de Room. **No se adjunta APK ni video y no se afirma BUILD SUCCESSFUL.**

Tampoco se creó un repositorio en la cuenta de GitHub del estudiante ni se invitó al profesor. El remoto `upstream` identifica únicamente el código público de arranque; no constituye la entrega privada solicitada.

## Compatibilidad conservada

Se mantuvieron AGP, Kotlin, Compose, SDK y el criterio JDK 25 del ZIP recibido. Room 2.8.5 y KSP 2.3.12 son los valores de la guía. No se bajaron versiones ni se agregaron Hilt, red, pruebas instrumentadas, pre-carga o retos opcionales.

La documentación oficial se consultó para contrastar la existencia de Room 2.8.5/KSP 2.3.12 y el procedimiento/checksum de Gradle, no para sustituir las instrucciones de la actividad:

- Room: https://developer.android.com/jetpack/androidx/releases/room
- KSP: https://github.com/google/ksp/releases/tag/2.3.12
- Gradle Wrapper: https://docs.gradle.org/current/userguide/gradle_wrapper.html
- SHA-256 de Gradle: https://gradle.org/release-checksums/

## Auxiliar de compilación para Mac

El ZIP de arranque no incluía `gradlew`, `gradlew.bat` ni `gradle-wrapper.jar`. Se añadió `Compilar_en_Mac.command`: utiliza Gradle oficial verificado por SHA-256 para generar el wrapper y solicitar un APK debug. Su sintaxis Bash se revisó; su descarga/compilación no se pudo realizar desde este entorno. No modifica la base de un emulador ni publica nada.

## Sobre la entrega académica

Los seis checkpoints y D3 muestran lo que modificó el asistente. No se cambiaron fechas para simular una sesión de clase. La guía restringe la generación por IA y exige un video real, repositorio privado y defensa oral: este paquete técnico no prueba el cumplimiento de esas condiciones.
