package com.example.festunavigator.domain.ml;

import com.google.mlkit.vision.common.InputImage;

public interface ObjectDetector {
    // 抛出异常以标识可能的 MLKit 调用失败
    String analyze(InputImage image) throws Exception;
}
