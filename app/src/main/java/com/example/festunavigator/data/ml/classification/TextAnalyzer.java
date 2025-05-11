package com.example.festunavigator.data.ml.classification;

import com.example.festunavigator.domain.ml.ObjectDetector;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.Text.TextBlock;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.util.List;

public class TextAnalyzer implements ObjectDetector {
    private final com.google.mlkit.vision.text.TextRecognizer recognizer;

    public TextAnalyzer() {
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    @Override
    public String analyze(InputImage image) throws Exception {
        // 使用 ML Kit 进行文本识别（同步等待结果）
        Text result = Tasks.await(recognizer.process(image));
        List<TextBlock> blocks = result.getTextBlocks();
        TextBlock bestBlock = null;
        for (TextBlock block : blocks) {
            if (filter(block)) {
                if (bestBlock == null ||
                        (block.getBoundingBox() != null && bestBlock.getBoundingBox() != null
                                && block.getBoundingBox().height() > bestBlock.getBoundingBox().height())) {
                    bestBlock = block;
                }
            }
        }
        return bestBlock != null ? bestBlock.getText() : null;
    }

    private boolean filter(TextBlock block) {
        String text = block.getText();
        return text != null && !text.isEmpty() && Character.isDigit(text.charAt(0));
    }
}
