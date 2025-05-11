package com.example.festunavigator.presentation.preview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.festunavigator.R;
import com.example.festunavigator.data.App;
import com.example.festunavigator.data.data_source.TreeNodeDto;
import com.example.festunavigator.databinding.FragmentPreviewBinding;
import com.example.festunavigator.presentation.common.helpers.DrawerHelper;
import com.google.ar.sceneform.ArSceneView;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.activityViewModels;

public class PreviewFragment extends Fragment {

    private FragmentPreviewBinding binding;
    private final MainShareViewModel mainModel = activityViewModels().getValue();
    private ArSceneView arSceneView;
    private DrawerHelper drawerHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPreviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        arSceneView = binding.arSceneView;
        drawerHelper = new DrawerHelper(requireActivity(), arSceneView);

        if (!BuildConfig.BUILD_TYPE.equals("admin")) {
            arSceneView.getPlaneRenderer().setEnabled(false);
        }

        // 更新 AR 帧到 ViewModel
        arSceneView.getScene().addOnUpdateListener(frameTime -> {
            Frame frame = arSceneView.getArFrame();
            if (frame != null) {
                mainModel.setFrame(frame);
            }
        });

        // 绘制所有节点（管理员模式）
        if (BuildConfig.BUILD_TYPE.equals("admin")) {
            drawerHelper.drawTree();
        }

        // 绘制路径（用户模式）
        List<Integer> path = mainModel.getPathNodeIds();
        if (!path.isEmpty()) {
            drawerHelper.drawPath(path);
        }
    }
}
