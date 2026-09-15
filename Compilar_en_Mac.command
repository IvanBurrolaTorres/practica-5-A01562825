#!/bin/bash
# Auxiliar opcional. Descarga Gradle oficial, completa el wrapper y compila el APK.
# No publica nada ni modifica datos de un emulador.
set -euo pipefail
cd "$(dirname "$0")"
ROOT="$PWD"

if [[ "$(uname -s)" != "Darwin" ]]; then
  echo "Este auxiliar es para macOS. Abre el proyecto con Android Studio en tu equipo."
  exit 1
fi

JBR="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
if [[ -x "$JBR/bin/java" ]]; then
  export JAVA_HOME="$JBR"
  export PATH="$JAVA_HOME/bin:$PATH"
fi

SDK="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-$HOME/Library/Android/sdk}}"
if [[ ! -d "$SDK" ]]; then
  echo "No se encontró el Android SDK en: $SDK"
  echo "Abre Android Studio y completa la instalación del SDK del proyecto."
  exit 1
fi
# Archivo local, excluido por .gitignore. No se cambia la configuración de Gradle del curso.
printf 'sdk.dir=%s\n' "$SDK" > local.properties
export ANDROID_HOME="$SDK"

VERSION="9.7.1"
SHA256="acd53f1edaf02f1a8ff99879f8a34b302661a057d9b063ae9e35b552f804d20a"
CACHE="$HOME/.gradle/practica5-tools"
GRADLE="$CACHE/gradle-$VERSION/bin/gradle"
TEMP="$(mktemp -d "${TMPDIR:-/tmp}/inventario-gradle.XXXXXX")"
trap 'rm -rf "$TEMP"' EXIT
mkdir -p "$CACHE"

if [[ ! -x "$GRADLE" ]]; then
  echo "Descargando Gradle $VERSION desde su distribución oficial..."
  curl --fail --location --retry 2 --connect-timeout 15 --max-time 600 \
    "https://services.gradle.org/distributions/gradle-$VERSION-bin.zip" \
    --output "$TEMP/gradle.zip"
  printf '%s  %s\n' "$SHA256" "$TEMP/gradle.zip" | shasum -a 256 -c -
  unzip -q -o "$TEMP/gradle.zip" -d "$CACHE"
fi

# El ZIP de arranque no incluía gradlew, gradlew.bat ni gradle-wrapper.jar.
# Se generan con la tarea oficial en una carpeta vacía, sin inventar un JAR.
printf 'rootProject.name = "PreparacionWrapper"\n' > "$TEMP/settings.gradle.kts"
"$GRADLE" -p "$TEMP" --no-daemon wrapper \
  --gradle-version "$VERSION" --distribution-type bin \
  --gradle-distribution-sha256-sum "$SHA256"
cp "$TEMP/gradlew" "$TEMP/gradlew.bat" "$ROOT/"
mkdir -p "$ROOT/gradle/wrapper"
cp "$TEMP/gradle/wrapper/gradle-wrapper.jar" "$ROOT/gradle/wrapper/"
cp "$TEMP/gradle/wrapper/gradle-wrapper.properties" "$ROOT/gradle/wrapper/"
chmod +x "$ROOT/gradlew"

mkdir -p docs/evidencias
./gradlew :app:assembleDebug --console=plain 2>&1 | tee docs/evidencias/compilacion-mac.txt
APK="$ROOT/app/build/outputs/apk/debug/app-debug.apk"
if [[ -f "$APK" ]]; then
  echo "APK creado: $APK"
  open -R "$APK"
fi
