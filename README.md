Lunara: Bienestar Integral con IA para Mujeres 40+ 🌙

"Empoderando a mujeres en su transición hormonal mediante tecnología empática y adaptativa."

Lunara es una aplicación nativa de Android que combina la robustez de una arquitectura MVVM con la potencia de la IA Generativa (Gemini) para crear planes de bienestar personalizados en tiempo real.



El Desafío

Las mujeres en transición hormonal (perimenopausia, menopausia y postmenopausia) a menudo enfrentan una desconexión en el cuidado de su salud. Los síntomas son variables y personales, y las soluciones genéricas no suelen adaptarse a cómo se siente una mujer hoy. La falta de seguimiento diario y personalización puede llevar a una baja adherencia a hábitos saludables y a una sensación de soledad en el proceso.

La Solución: Lunara

Lunara aborda este problema mediante una arquitectura híbrida inteligente:

Base Médica Sólida: Un motor de reglas local asigna inicialmente un "Plan Base" seguro y validado médicamente según el perfil hormonal de la usuaria.

Adaptación Dinámica con IA: Integramos Google Gemini para procesar los registros diarios de la usuaria (síntomas, ánimo, energía). La IA no solo "escucha", sino que ajusta las recomendaciones del día y ofrece apoyo emocional personalizado en tiempo real, transformando un plan estático en un acompañante vivo y empático.

🛠️ Arquitectura y Tecnologías

El proyecto sigue una arquitectura Clean MVVM (Model-View-ViewModel) con un enfoque reactivo.

Tech Stack Principal

Lenguaje: Kotlin (100%)

UI Toolkit: Jetpack Compose (Material Design 3)

Backend as a Service: Firebase

Authentication: Login anónimo y gestión de sesiones.

Firestore: Base de datos NoSQL en tiempo real para perfiles y registros.

Inteligencia Artificial: Google Gemini SDK (Vertex AI for Firebase)

Inyección de Dependencias: (Manual / ViewModel Factory - o Hilt si lo implementamos a futuro)

Coroutines & Flow: Para manejo asíncrono y reactividad de estado.

🧠 Ingeniería de IA: El Cerebro de Lunara

La característica central de Lunara es su capacidad para adaptar el contenido según el estado diario de la usuaria.

Flujo de Datos Híbrido

Reglas Deterministas: Un motor local asigna un "Plan Base" seguro basado en criterios médicos (etapa hormonal, síntomas severos).

Capa Generativa (Gemini): Procesa los datos "blandos" (ánimo, energía del día) para personalizar la comunicación.

Prompt Engineering Dinámico

Utilizamos Structured Prompting para forzar a la IA a devolver respuestas en formato JSON predecible, que luego se mapean a componentes nativos de UI.


🎨 Diseño y UX (Vitalidad Serena)

La interfaz fue diseñada siguiendo principios de FemTech moderna, priorizando la calma y la claridad.

Tipografía: Lora (Serif) para humanidad y Poppins (Sans) para legibilidad.

Color System:

Coral Energético (Acción)

Lila Sereno (Contención)

Blanco Hueso (Calma)


Desarrollado con 💜 por Amarilis Darai
Estudiante del Bootcamp de Desarrollo Móvil UNAB
