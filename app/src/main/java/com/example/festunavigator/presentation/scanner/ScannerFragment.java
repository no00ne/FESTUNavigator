package com.example.festunavigator.presentation.scanner;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.activityViewModels;
import com.example.festunavigator.R;
import com.example.festunavigator.databinding.FragmentScannerBinding;
import com.example.festunavigator.presentation.preview.MainShareViewModel;
import com.google.ar.core.Frame;
import com.google.ar.core.Image;
import com.google.mlkit.vision.common.InputImage;

public class ScannerFragment extends Fragment {

    private FragmentScannerBinding binding;
    private final MainShareViewModel mainModel = activityViewModels().getValue();
    private final Handler scanHandler = new Handler(Looper.getMainLooper());
    private boolean analyzing = false;
    private Runnable scanTask;

    public static final String SCAN_TYPE = "SCAN_TYPE";
    public static final int TYPE_INITIALIZE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScannerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 定时从最新相机帧执行文本识别
        scanTask = new Runnable() {
            @Override
            public void run() {
                Frame frame = mainModel.getLatestFrame();
                if (frame != null && !analyzing) {
                    try {
                        analyzing = true;
                        Image image = frame.acquireCameraImage();
                        InputImage inputImage = InputImage.fromMediaImage(image, frame.getCamera().getImageIntrinsics().getImageDimensions().getRotation());
                        String text = mainModel.getTextAnalyzer().analyze(inputImage);
                        image.close();
                        analyzing = false;
                        if (text != null && !text.isEmpty()) {
                            binding.textResult.setText(text);
                            binding.buttonConfirm.setEnabled(true);
                            // 找到文本后停止继续扫描
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        analyzing = false;
                    }
                }
                // 再次调度
                scanHandler.postDelayed(this, 500);
            }
        };
        scanHandler.post(scanTask);

        binding.buttonConfirm.setOnClickListener(v -> {
            String resultText = binding.textResult.getText().toString();
            if (!resultText.isEmpty()) {
                try {
                    int scannedId = Integer.parseInt(resultText);
                    boolean success = mainModel.setCurrentPosition(scannedId);
                    if (success) {
                        // 进入搜索界面
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainerView, new com.example.festunavigator.presentation.search.SearchFragment())
                                .commit();
                    } else {
                        Toast.makeText(getContext(), R.string.error_invalid_scan, Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), R.string.error_invalid_scan, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        scanHandler.removeCallbacks(scanTask);
    }
}
