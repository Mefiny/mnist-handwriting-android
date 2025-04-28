package com.example.myapplication

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ModelHandler(context: Context) {
    private var interpreter: Interpreter? = null
    private val TAG = "ModelHandler"

    init {
        try {
            interpreter = Interpreter(loadModelFile(context))
            Log.d(TAG, "Model successfully loaded.")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing interpreter: ${e.message}")
            throw RuntimeException("Failed to initialize model: ${e.message}")
        }
    }

    // 将 assets 下的 .tflite 文件映射到内存
    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("mnist_model.tflite")
        FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
            val channel = inputStream.channel
            return channel.map(
                FileChannel.MapMode.READ_ONLY,
                fileDescriptor.startOffset,
                fileDescriptor.declaredLength
            )
        }
    }

    /**
     * 运行模型
     * @param inputData 灰度化并归一化之后的 28×28=784 个像素值
     */
    fun runModel(inputData: FloatArray): FloatArray {
        // 1) 准备一个 direct ByteBuffer，容量 = 4 bytes per float × batch × height × width × channels
        val byteBuffer = ByteBuffer.allocateDirect(4 * 1 * 28 * 28 * 1)
            .order(ByteOrder.nativeOrder())
        // 2) 逐个写入 float 值
        for (value in inputData) {
            byteBuffer.putFloat(value)
        }
        byteBuffer.rewind()

        // 3) 准备输出容器：[1][10]
        val output = Array(1) { FloatArray(10) }

        // 4) 真正调用 TFLite 进行推理
        try {
            interpreter?.run(byteBuffer, output)
            Log.d(TAG, "Model run successfully.")
        } catch (e: Exception) {
            Log.e(TAG, "Error running model: ${e.message}")
        }

        return output[0]
    }

    /** 释放资源 */
    fun close() {
        interpreter?.close()
        interpreter = null
    }

    /** 在日志里打印出模型实际的输入输出形状，方便校验 */
    fun logInputOutputShapes() {
        try {
            interpreter?.let {
                val inTensor = it.getInputTensor(0)
                val outTensor = it.getOutputTensor(0)
                Log.d(TAG, "Input tensor shape: ${inTensor.shape().contentToString()}")
                Log.d(TAG, "Output tensor shape: ${outTensor.shape().contentToString()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking I/O shapes: ${e.message}")
        }
    }
}
