package com.ventthos.quizzappbasic

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import  com.ventthos.quizzappbasic.Category
class Roulette(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().apply { isAntiAlias = true }
    private val wheelPaint = Paint()
    private val textPaint = Paint()
    private val arrowPaint = Paint()
    private val numVueltas = 5
    val science = Category.SCIENCE
    // Asumiendo que cada categoría tiene una imagen correspondiente en el directorio 'res/drawable'
    private val sections = listOf(
        Category.SCIENCE,
        Category.GEOGRAPHY,
        Category.HISTORY,
        Category.MOVIES,
        Category.CULTURE
    )
    // Opciones en la ruleta
    private var angle = 0f  // Ángulo actual de la ruleta
    private var animator: ValueAnimator? = null
    private val colors = listOf(  // Colores fijos para cada sección
        Color.parseColor("#43c05f"), Color.BLUE, Color.parseColor("#f7dc43"), Color.MAGENTA, Color.RED
    )

    init {
        wheelPaint.color = Color.parseColor("#FF4081")  // Color de fondo de la ruleta
        textPaint.color = Color.WHITE
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = 40f
        arrowPaint.color = Color.BLACK  // Color de la flecha
        arrowPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (Math.min(width, height) / 2.0 * 0.8).toFloat()
        val sectionAngle = 360f / sections.size  // Ángulo de cada sección

        // Dibujar la ruleta
        for (i in sections.indices) {
            paint.color = colors[i]  // Usar colores fijos
            val startAngle = i * sectionAngle + angle
            val sweepAngle = sectionAngle

            val path = Path()
            path.arcTo(RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius), startAngle, sweepAngle)
            path.lineTo(centerX, centerY)
            path.close()

            canvas.drawPath(path, paint)

            // Dibujar la imagen correspondiente de cada categoría
            val bitmap = getCategoryImage(sections[i])  // Método que obtiene la imagen para la categoría
            val imageX = centerX + Math.cos(Math.toRadians((startAngle + sweepAngle / 2).toDouble())) * (radius / 1.5)
            val imageY = centerY + Math.sin(Math.toRadians((startAngle + sweepAngle / 2).toDouble())) * (radius / 1.5)

            // Ajustar el tamaño de la imagen
            val imageSize = 12f
            val matrix = Matrix()
            val scaleFactor = imageSize / bitmap.width.toFloat() * 5 // Escala la imagen para que se ajuste al tamaño deseado
            matrix.postScale(scaleFactor, scaleFactor)

            val scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            canvas.drawBitmap(scaledBitmap, (imageX - imageSize / 2).toFloat(), (imageY - imageSize / 2).toFloat(), null)


        }

        // Dibujar la flecha en la parte derecha de la ruleta
        val arrowSize = 40f  // Tamaño de la flecha
        val arrowX = centerX - radius - arrowSize  // Posición en X
        val arrowY = centerY  // Posición en Y
        val arrowPath = Path().apply {
            moveTo(arrowX, arrowY - arrowSize)
            lineTo(arrowX, arrowY + arrowSize)
            lineTo(arrowX + arrowSize, arrowY)
            close()
        }
        canvas.drawPath(arrowPath, arrowPaint)
    }



    // Método para girar la ruleta y calcular la sección ganadora
    fun spinWheel(onSpinEnd: (Category) -> Unit) {
        val randomAngle = (0..350).random().toFloat()  // Ángulo aleatorio entre 0° y 350°
        val finalAngle = angle + 360f * numVueltas + randomAngle  // Agrega múltiples vueltas

        animator?.cancel()
        val animator = ValueAnimator.ofFloat(angle, finalAngle).apply {
            duration = 2000
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                angle = animation.animatedValue as Float
                invalidate()
            }
            doOnEnd {
                val winningSection = getWinningSection()  // Determina la sección ganadora
                onSpinEnd(winningSection)  // Devuelve la sección al finalizar
            }
        }
        animator.start()
    }

    // Método para obtener la sección ganadora según la flecha en la derecha
    private fun getWinningSection(): Category {
        val normalizedAngle = (angle % 360 + 360) % 360  // Asegura un ángulo entre 0° y 360°
        val sectionAngle = 360f / sections.size

        // La flecha está en la **derecha**, lo que significa que apunta a **90°**
        val index = (((180 - normalizedAngle + 360) % 360) / sectionAngle).toInt() % sections.size
        return sections[index]  // Retorna la categoría ganadora
    }


    // Guardar el estado de la animación
    fun saveState(outState: Bundle) {
        outState.putFloat("angle", angle)
        outState.putBoolean("isAnimating", animator?.isRunning == true)  // Guardar si la animación está en curso
    }

    // Restaurar el estado de la animación
    fun restoreState(savedInstanceState: Bundle) {
        angle = savedInstanceState.getFloat("angle", 0f)
        invalidate()  // Redibujar la vista con el estado restaurado
        // Si hay una animación en curso, reanudarla desde el ángulo actual
        if (savedInstanceState.getBoolean("isAnimating", false)) {
            spinWheel {}
        }
    }
    // Método para obtener la imagen correspondiente a cada categoría
    private fun getCategoryImage(category: Category): Bitmap {
        return when (category) {
            Category.SCIENCE -> BitmapFactory.decodeResource(resources, Category.SCIENCE.image)
            Category.GEOGRAPHY -> BitmapFactory.decodeResource(resources, Category.GEOGRAPHY.image)
            Category.HISTORY -> BitmapFactory.decodeResource(resources, Category.HISTORY.image)
            Category.MOVIES -> BitmapFactory.decodeResource(resources, Category.MOVIES.image)
            Category.CULTURE -> BitmapFactory.decodeResource(resources, Category.CULTURE.image)
            // Agregar más casos según las categorías
        }
    }
}
