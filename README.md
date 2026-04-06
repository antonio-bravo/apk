# APK TV

Aplicación para Android TV que reproduce canales IPTV desde una lista M3U alojada en GitHub, usando Acestream como player externo.

## Características

- Interfaz optimizada para Android TV usando Leanback.
- Carga automática de lista M3U desde URL de GitHub.
- Reproducción con player externo Acestream.
- Arquitectura MVVM para fácil mantenimiento.

## Configuración

1. Reemplaza la URL en `ChannelViewModel.kt` con tu lista M3U en GitHub.
2. Asegúrate de que Acestream esté instalado en el dispositivo.
3. Compila y instala la APK en tu Android TV.

## Compilación

Usa Android Studio o Gradle:

```bash
./gradlew build
```

## Releases

Para crear una release manual:

1. Ve a la pestaña "Actions" en GitHub.
2. Selecciona el workflow "Build and Release APK".
3. Haz clic en "Run workflow".
4. Ingresa la versión (ej. 1.0.0).
5. El workflow compilará el APK y creará una release con el archivo descargable.

## Estructura

- `MainActivity`: Actividad principal.
- `MainFragment`: Fragmento de navegación.
- `ChannelViewModel`: Maneja datos de canales.
- `ChannelRepository`: Descarga y parsea M3U.
- `CardPresenter`: Presenta tarjetas de canales.