package com.example.myapplication

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var modelHandler: ModelHandler
    private lateinit var drawView: DrawView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化模型处理类
        modelHandler = ModelHandler(this)

        // 打印模型的输入输出形状到 Logcat（过滤 TAG 为 "ModelHandler"）
        modelHandler.logInputOutputShapes()

        // 获取 DrawView 和按钮
        drawView = findViewById(R.id.drawView)
        val clearButton: Button = findViewById(R.id.clearButton)

        // 设置清除画布按钮点击事件
        clearButton.setOnClickListener {
            drawView.clearCanvas()
        }
    }

    /** 当 DrawView 绘制完成后会回调此方法 */
    fun recognizeDigit(bitmap: Bitmap) {
        // 将图像缩放到模型输入大小 28x28
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 28, 28, false)
        val input = preprocessImage(scaledBitmap)

        // 使用模型进行推理
        val output = modelHandler.runModel(input)
        val predictedDigit = getMaxPrediction(output)

        // 显示结果
        displayResult(predictedDigit)
    }

    /** 灰度化并归一化为 FloatArray */
    private fun preprocessImage(bitmap: Bitmap): FloatArray {
        val input = FloatArray(28 * 28)
        val pixels = IntArray(28 * 28)
        bitmap.getPixels(pixels, 0, 28, 0, 0, 28, 28)

        for (i in pixels.indices) {
            // 取最低 8 位作为灰度值，反色后归一化
            input[i] = (255 - (pixels[i] and 0xFF)) / 255.0f
        }
        return input
    }

    /** 从模型输出中找到置信度最高的索引 */
    private fun getMaxPrediction(output: FloatArray): Int {
        var maxIndex = 0
        var maxVal = output[0]
        for (i in 1 until output.size) {
            if (output[i] > maxVal) {
                maxVal = output[i]
                maxIndex = i
            }
        }
        return maxIndex
    }

    /** 通过 Toast 显示识别结果 */
    private fun displayResult(predictedDigit: Int) {
        Toast.makeText(this, "Predicted Digit: $predictedDigit", Toast.LENGTH_LONG).show()
    }

    /** 在 Activity 销毁时关闭模型释放资源 */
    override fun onDestroy() {
        super.onDestroy()
        modelHandler.close()
    }
}
