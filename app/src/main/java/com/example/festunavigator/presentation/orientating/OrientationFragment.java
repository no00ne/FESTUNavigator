package com.example.festunavigator.presentation.orientating;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.activityViewModels;
import com.example.festunavigator.databinding.FragmentOrientationBinding;
import com.example.festunavigator.presentation.preview.MainShareViewModel;
import com.google.ar.core.Frame;
import com.google.ar.core.Plane;

public class OrientationFragment extends Fragment {

    private FragmentOrientationBinding binding;
    private final MainShareViewModel mainModel = activityViewModels().getValue();
    private boolean navigating = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrientationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每帧检查是否检测到垂直平面
        mainModel.getFrameLiveData().observe(getViewLifecycleOwner(), frame -> {
            if (frame != null && !navigating) {
                boolean hasVertical = false;
                for (Plane plane : frame.getSession().getAllPlanes()) {
                    if (plane.getType() == Plane.Type.VERTICAL && plane.getTrackingState() == com.google.ar.core.TrackingState.TRACKING) {
                        hasVertical = true;
                        break;
                    }
                }
                if (hasVertical) {
                    navigating = true;
                    // 导航到扫描界面
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(com.example.festunavigator.R.id.fragmentContainerView, new com.example.festunavigator.presentation.scanner.ScannerFragment())
                            .commit();
                }
            }
        });
    }
}
