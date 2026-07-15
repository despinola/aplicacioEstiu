# 📱 Instrucciones para la App de Vacaciones

## 🎉 ¡Enhorabuena! La estructura base está creada

He creado una aplicación Android completa con:
- ✅ Sistema de login con 4 usuarios
- ✅ Calendario de agosto interactivo
- ✅ 31 juegos (3 completamente implementados + 28 placeholders)
- ✅ Sistema de registro de actividades diarias
- ✅ Base de datos para guardar el progreso

## 🚀 Pasos para ejecutar la app

### 1. Instalar Android Studio
- Descarga Android Studio desde: https://developer.android.com/studio
- Instala y configura el SDK de Android

### 2. Abrir el proyecto
1. Abre Android Studio
2. Selecciona "Open an existing project"
3. Navega a: `/Users/david.espinola/git/aplicacioEstiu/VacacionesApp`
4. Espera a que Gradle sincronice (puede tardar unos minutos la primera vez)

### 3. Configurar un emulador
1. En Android Studio: Tools → Device Manager
2. Crea un nuevo dispositivo virtual (AVD)
3. Recomendado: Pixel 5 con Android 13 (API 33)

### 4. Ejecutar la app
1. Selecciona el emulador en la parte superior
2. Pulsa el botón Run ▶️ (o Shift+F10)
3. ¡Espera a que compile y se instale!

## 🎮 Juegos implementados

### Totalmente funcionales:
1. **Día 1 - Adivinar Banderas**: 5 preguntas con banderas de países
2. **Día 2 - Matemáticas**: Sumas y restas
3. **Día 3 - Capitales**: Capitales de Europa
4. **Días 6, 7, 10, 18, 24, 27**: Variantes de banderas y capitales

### Placeholders (para implementar):
- Snake, Memory, Sopa de letras, Ahorcado, Puzzle...
- Están como botones simples pero podéis mejorarlos

## 🔧 Cómo mejorar los juegos

### Ejemplo: Implementar el juego Snake

1. Abre: `app/src/main/java/com/vacaciones/app/games/SimpleGames.kt`
2. Busca la función `SnakeGame`
3. Reemplaza el `PlaceholderGame` con tu implementación

```kotlin
@Composable
fun SnakeGame(onGameComplete: (Int) -> Unit) {
    // Aquí va tu lógica del Snake
    var score by remember { mutableStateOf(0) }
    
    Card(...) {
        Column(...) {
            // Canvas para dibujar la serpiente
            // Controles de dirección
            // Lógica del juego
        }
    }
}
```

### Recursos útiles para aprender:
- **Jetpack Compose**: https://developer.android.com/jetpack/compose/tutorial
- **Canvas en Compose**: https://developer.android.com/jetpack/compose/graphics/draw/overview
- **Animaciones**: https://developer.android.com/jetpack/compose/animation

## 📊 Base de datos

La app guarda automáticamente:
- Juegos completados por día y usuario
- Puntuación de cada juego
- Checklist de actividades (lectura, dientes, TV, piscina)
- Notas personales de cada día

Todo se guarda localmente en el teléfono con Room Database.

## 🎨 Personalización

### Cambiar colores
Edita: `app/src/main/java/com/vacaciones/app/ui/theme/Theme.kt`

```kotlin
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0288D1),      // Color principal
    secondary = Color(0xFFFFA726),    // Color secundario
    tertiary = Color(0xFF66BB6A)      // Color terciario
)
```

### Añadir más actividades al checklist
Edita: `app/src/main/java/com/vacaciones/app/ui/screens/DayDetailScreen.kt`

### Cambiar usuarios
Edita: `app/src/main/java/com/vacaciones/app/data/AppDatabase.kt`
- Función `populateDatabase()`

## 💡 Ideas para mejorar

1. **Añadir avatares personalizados** en lugar de emojis
2. **Gráficos de progreso** (cuántos días completados)
3. **Sistema de puntos acumulados** y ranking familiar
4. **Notificaciones** recordando hacer las actividades
5. **Modo offline** (ya funciona!)
6. **Exportar/compartir** el progreso del mes
7. **Temas personalizados** por usuario
8. **Fotos del día** guardadas con las notas

## 🐛 Solución de problemas

### Error: "SDK location not found"
- Edita `local.properties`
- Asegúrate de que apunta a tu SDK de Android

### Error de compilación
- File → Invalidate Caches / Restart
- Build → Clean Project
- Build → Rebuild Project

### El emulador no arranca
- Asegúrate de tener virtualización habilitada en BIOS
- Prueba con un dispositivo físico conectado por USB

## 📚 Recursos de aprendizaje

### Para los peques (8-10 años):
- **Scratch**: https://scratch.mit.edu - Programación visual
- **Code.org**: Cursos interactivos de programación

### Para aprender Android:
- **Curso oficial de Google**: https://developer.android.com/courses
- **Kotlin Playground**: https://play.kotlinlang.org

## 🎯 Próximos pasos sugeridos

1. ✅ **Ejecutar la app** y probarla
2. 🎮 **Implementar 1-2 juegos sencillos** (Memory, Simon Dice)
3. 🎨 **Personalizar colores** y tema
4. 📊 **Añadir pantalla de estadísticas** del mes
5. 🏆 **Sistema de logros** y recompensas

## ❓ ¿Necesitas ayuda?

Si tenéis dudas o problemas:
1. Revisad los logs en Android Studio (Logcat)
2. Buscad el error en Google o Stack Overflow
3. Preguntad en foros de Android

## 🎉 ¡Disfrutad programando juntos!

Esta app es perfecta para aprender Android y pasar tiempo en familia.
¡Que tengáis unas vacaciones geniales! 🏖️

---
Creado con ❤️ para Blai, Rita, Miriam y David
