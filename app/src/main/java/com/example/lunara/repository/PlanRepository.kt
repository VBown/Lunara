package com.example.lunara.repository

import com.example.lunara.data.PlanBase
import com.example.lunara.data.PlanSection

object PlanRepository {
    // Aquí definimos los 9 planes base
    private val planes = mapOf(
        1 to PlanBase(
            id = 1,
            titulo = "Plan Perimenopausia Leve",
            enfoquePrincipal = "Educación, inicio actividad ligera",
            secciones = listOf(
                PlanSection(
                    "Nutrición",
                    listOf(
                        "Introduce alimentos ricos en fitoestrógenos (soja, linaza).",
                        "Aumenta frutas y verduras frescas.",
                        "Limita grasas saturadas pero mantén dieta balanceada."
                    ),
                    "Estás dando un gran paso hacia el bienestar. Sigue nutriendo tu cuerpo y mente con pequeños hábitos diarios." // [cite: 433]
                ),
                PlanSection(
                    "Ejercicio",
                    listOf(
                        "Comienza con caminatas suaves 2 veces por semana, 20 minutos.",
                        "Movilidad articular ligera y estiramientos diarios."
                    ),
                    "El movimiento es medicina. Que cada paso te acerque a una versión más fuerte."
                ),
                PlanSection(
                    "Manejo de Síntomas",
                    listOf("Autoobservación de síntomas leves.", "Higiene de sueño básica (ambiente, horarios)."),
                    "Escucha a tu cuerpo, él te guía."
                ),
                PlanSection(
                    "Bienestar Emocional",
                    listOf("Prácticas simples de respiración y relajación.", "Registro de emociones en diario personal."),
                    "Tu mente merece cuidados tanto como tu cuerpo."
                )
            )
        ),
        2 to PlanBase(
            id = 2,
            titulo = "Plan Perimenopausia Moderado",
            enfoquePrincipal = "Manejo síntomas, autocuidado emocional",
            secciones = listOf(
                PlanSection(
                    "Nutrición",
                    listOf("Incrementa fitoestrógenos, antioxidantes, proteínas magras.", "Reduce azúcares refinados y procesados."),
                    "Nutre tu cuerpo con amor y verás los resultados en tu energía."
                ),
                PlanSection(
                    "Ejercicio",
                    listOf("Caminar 3-4 veces/semana 30 min.", "Yoga/pilates 1-2 veces/semana para manejo estrés."),
                    "El movimiento es medicina para tu cuerpo y alma."
                ),
                PlanSection(
                    "Manejo de Síntomas",
                    listOf("Técnicas de respiración y meditación para sofocos.", "Rutina de higiene del sueño."),
                    "Cada pequeño alivio cuenta. Cuida tus síntomas con paciencia."
                ),
                PlanSection(
                    "Bienestar Emocional",
                    listOf("Práctica de Mindfulness y journaling.", "Considerar apoyo profesional si es necesario."),
                    "Reconocer tus retos es valentía. Cada desafío que superas fortalece tu camino." // [cite: 434]
                )
            )
        ),
        3 to PlanBase(
            id = 3,
            titulo = "Plan Perimenopausia Severo",
            enfoquePrincipal = "Intervención intensiva, apoyo emocional",
            secciones = listOf(
                PlanSection(
                    "Nutrición",
                    listOf(
                        "Dieta antiinflamatoria con énfasis en omega-3.",
                        "Eliminación de alimentos procesados y azúcares.",
                        "Consultar médico para posible suplementación."
                    ),
                    "Estás tomando decisiones valientes por tu salud. Estamos aquí para apoyarte."
                ),
                PlanSection(
                    "Ejercicio",
                    listOf(
                        "Actividad ligera diaria según tolerancia (ej. estiramientos, yoga suave).",
                        "Evitar ejercicio intenso hasta estabilizar síntomas."
                    ),
                    "Sé amable con tu cuerpo. Hoy, un movimiento suave es un gran logro."
                ),
                PlanSection(
                    "Manejo de Síntomas",
                    listOf(
                        "Buscar apoyo médico para síntomas severos es fundamental.",
                        "Técnicas avanzadas de manejo de estrés (meditación guiada).",
                        "Control estricto de la higiene del sueño."
                    ),
                    "Pedir ayuda es un acto de fortaleza. Estás tomando el control."
                ),
                PlanSection(
                    "Bienestar Emocional",
                    listOf("Terapia psicológica es altamente recomendada.", "Explorar grupos de apoyo (online o presenciales)."),
                    "No estás sola en esto. Paso a paso, con apoyo y cuidado, encontrarás equilibrio y serenidad." // [cite: 435]
                )
            )
        ),
        4 to PlanBase(
            id = 4,
            titulo = "Plan Menopausia Leve",
            enfoquePrincipal = "Optimización salud ósea y sueño",
            secciones = listOf(
                PlanSection(
                    "Nutrición",
                    listOf(
                        "Mantén una dieta rica en calcio y vitamina D para la salud ósea.",
                        "Continúa con frutas, verduras y proteínas magras."
                    ),
                    "Tu compromiso es tu poder. Mantente enfocada en tus objetivos."
                ),
                PlanSection(
                    "Ejercicio",
                    listOf(
                        "Rutina cardiovascular moderada 3-4 veces por semana.",
                        "Entrenamiento de fuerza leve 2 veces por semana para tus huesos."
                    ),
                    "Sigue celebrando cada avance hacia una mejor calidad de vida." // [cite: 436]
                ),
                PlanSection(
                    "Bienestar Emocional",
                    listOf("Mantén prácticas de meditación y relajación.", "Fomenta tu actividad social y recreativa."),
                    "Tu bienestar es un equilibrio. Sigue cultivando momentos de paz y alegría."
                )
            )
        ),
        5 to PlanBase(
            id = 5,
            titulo = "Plan Menopausia Moderado",
            enfoquePrincipal = "Control síntomas, manejo ansiedad",
            secciones = listOf(
                PlanSection(
                    "Nutrición",
                    listOf("Dieta antiinflamatoria equilibrada.", "Reducción de azúcares y procesados para manejar síntomas."),
                    "Cada elección saludable te acerca a la serenidad que buscas."
                ),
                PlanSection(
                    "Ejercicio",
                    listOf(
                        "Caminatas diarias suaves para despejar la mente.",
                        "Yoga o ejercicios suaves enfocados en el manejo del estrés."
                    ),
                    "El movimiento consciente es un bálsamo para la mente y el cuerpo."
                ),
                PlanSection(
                    "Manejo de Síntomas",
                    listOf(
                        "Practica activamente técnicas para sofocos y ansiedad.",
                        "Establece una rutina de sueño estricta y relajante."
                    ),
                    "Tienes las herramientas para gestionar esto. Confía en ti."
                ),
                PlanSection(
                    "Bienestar Emocional",
                    listOf("Considera terapia o coaching emocional.", "Practica Mindfulness y journaling diariamente."),
                    "Reconocer tus retos es valentía. Cada desafío que superas fortalece tu camino." // [cite: 434]
                )
            )
        ),
        6 to PlanBase(
            id = 6,
            titulo = "Plan Menopausia Severo",
            enfoquePrincipal = "Manejo intensivo de síntomas y emociones",
            secciones = listOf(
                PlanSection(
                    "Nutrición",
                    listOf(
                        "Dieta especializada, preferiblemente con apoyo nutricional profesional.",
                        "Suplementación dirigida según análisis y recomendación médica."
                    ),
                    "Buscar apoyo profesional es el acto de autocuidado más importante."
                ),
                PlanSection(
                    "Ejercicio",
                    listOf("Actividades muy suaves y supervisadas, como fisioterapia o yoga restaurativo."),
                    "Escucha a tu cuerpo por encima de todo. La suavidad es tu fortaleza ahora."
                ),
                PlanSection(
                    "Manejo de Síntomas",
                    listOf(
                        "Control médico intensivo para tus síntomas.",
                        "Terapias de apoyo psicológico son cruciales en esta etapa."
                    ),
                    "Estamos contigo en este proceso. No estás sola."
                ),
                PlanSection(
                    "Bienestar Emocional",
                    listOf("Apoyo terapéutico continuo es fundamental.", "Enfócate en un día a la vez."),
                    "No estás sola en esto. Paso a paso, con apoyo y cuidado, encontrarás equilibrio y serenidad." // [cite: 435]
                )
            )
        ),
        7 to PlanBase(
            id = 7,
            titulo = "Plan Postmenopausia Leve",
            enfoquePrincipal = "Mantenimiento, prevención enfermedades",
            secciones = listOf(
                PlanSection(
                    "Nutrición",
                    listOf(
                        "Mantenimiento de dieta saludable rica en calcio y vitamina D.",
                        "Considerar suplementos preventivos si es necesario (consultar médico)."
                    ),
                    "Tu dedicación por cuidar de ti misma es admirable."
                ),
                PlanSection(
                    "Ejercicio",
                    listOf(
                        "Actividades físicas variadas: resistencia y fuerza 4-5 veces por semana.",
                        "Incorporar ejercicios para flexibilidad y equilibrio."
                    ),
                    "Sigue cultivando salud y energía cada día, con amor y paciencia." // [cite: 437]
                ),
                PlanSection(
                    "Bienestar Emocional",
                    listOf("Mantener prácticas de autocuidado emocional.", "Vigilancia preventiva de la salud mental."),
                    "Has construido una base sólida. ¡Sigue floreciendo!"
                )
            )
        ),
        8 to PlanBase(
            id = 8,
            titulo = "Plan Postmenopausia Moderado",
            enfoquePrincipal = "Recuperación, manejo emocional",
            secciones = listOf(
                PlanSection(
                    "Nutrición",
                    listOf(
                        "Dieta antiinflamatoria.",
                        "Refuerzo en alimentos para salud cardiovascular y ósea."
                    ),
                    "Tu cuerpo es resiliente. Démosle los nutrientes que necesita para sanar."
                ),
                PlanSection(
                    "Ejercicio",
                    listOf("Combinación de cardio moderado (3x/sem) y entrenamiento de fuerza (2x/sem)."),
                    "Poco a poco, recupera tu fuerza. Cada día cuenta."
                ),
                PlanSection(
                    "Bienestar Emocional",
                    listOf(
                        "Apoyo emocional y coaching para la reinvención personal.",
                        "Contactar con equipo sanitario para manejo de síntomas."
                    ),
                    "Eres fuerte y capaz de transformar tus días. Permítete tiempo para sanar y crecer." // [cite: 438]
                )
            )
        ),
        9 to PlanBase(
            id = 9,
            titulo = "Plan Postmenopausia Severo",
            enfoquePrincipal = "Intervención integral y soporte médico",
            secciones = listOf(
                PlanSection(
                    "Nutrición",
                    listOf(
                        "Dieta supervisada estricta con suplementación.",
                        "Enfoque en salud digestiva y antiinflamación."
                    ),
                    "El cuidado intensivo de hoy es la base de tu bienestar de mañana."
                ),
                PlanSection(
                    "Ejercicio",
                    listOf("Actividades muy ligeras y adaptadas, posiblemente con guía profesional (fisioterapeuta)."),
                    "El movimiento más pequeño es un paso hacia la recuperación."
                ),
                PlanSection(
                    "Manejo de Síntomas",
                    listOf("Control médico intensivo.", "Terapia psicológica para el manejo del dolor crónico o malestar."),
                    "No dudes en apoyarte en tu equipo de salud. Estamos para ayudarte."
                ),
                PlanSection(
                    "Bienestar Emocional",
                    listOf("Soporte terapéutico constante.", "Técnicas de aceptación y manejo del estrés crónico."),
                    "No estás sola en esto. Paso a paso, con apoyo y cuidado, encontrarás equilibrio." // [cite: 435]
                )
            )
        )
    )

    fun getPlanById(id: Int): PlanBase? {
        return planes[id]
    }
}

