# MNIST 手写数字识别（Android）

![License](https://img.shields.io/badge/license-MIT-blue.svg)  
![GitHub repo size](https://img.shields.io/github/repo-size/Mefiny/mnist-handwriting-android)  
![Build](https://github.com/Mefiny/mnist-handwriting-android/actions/workflows/android-ci.yml/badge.svg)

> 一个基于 TensorFlow Lite 的 Android 手写数字识别练习项目，使用手写板实时识别 0–9 数字

## 特性

- 手写涂鸦区，支持手指/触控笔自由书写  
- 实时将画布内容 resize 到 28×28 并归一化输入模型  
- TensorFlow Lite `.tflite` 模型推理，10 类 softmax 输出  
- 一键清除画布，连续多次识别体验流畅  

## 安装与运行

1. 克隆项目：
   ```bash
   git clone https://github.com/Mefiny/mnist-handwriting-android.git
