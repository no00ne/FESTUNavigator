package com.example.festunavigator.presentation.common.helpers;

import android.app.Activity;
import com.example.festunavigator.data.App;
import com.example.festunavigator.data.data_source.TreeNodeDto;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import java.util.List;

public class DrawerHelper {
    private final Activity activity;
    private final ArSceneView arSceneView;
    private com.google.ar.sceneform.Node originNode;
    private com.google.ar.sceneform.rendering.ModelRenderable sphereRenderable;

    public DrawerHelper(Activity activity, ArSceneView arSceneView) {
        this.activity = activity;
        this.arSceneView = arSceneView;
        // 创建默认绿色球体渲染模型
        MaterialFactory.makeOpaqueWithColor(activity, new Color(android.graphics.Color.GREEN))
                .thenAccept(material -> sphereRenderable = ShapeFactory.makeSphere(0.05f, Vector3.zero(), material));
    }

    public void setOriginAnchor(Anchor anchor) {
        // 建立锚点节点
        originNode = new com.google.ar.sceneform.AnchorNode(anchor);
        originNode.setParent(arSceneView.getScene());
    }

    public void drawPath(List<Integer> pathNodeIds) {
        // 获取路径节点对应的位置信息
        for (int nodeId : pathNodeIds) {
            TreeNodeDto node = findNodeById(nodeId);
            if (node != null) {
                Vector3 pos = new Vector3(node.x, node.y, node.z);
                drawSphere(pos, nodeId == pathNodeIds.get(pathNodeIds.size() - 1));
            }
        }
    }

    public void drawTree() {
        List<TreeNodeDto> allNodes = App.getGraphRepository().getNodes();
        for (TreeNodeDto node : allNodes) {
            Vector3 pos = new Vector3(node.x, node.y, node.z);
            drawSphere(pos, false);
        }
    }

    private void drawSphere(Vector3 position, boolean isDestination) {
        com.google.ar.sceneform.Node node = new com.google.ar.sceneform.Node();
        if (originNode != null) {
            node.setParent(originNode);
        } else {
            node.setParent(arSceneView.getScene());
        }
        node.setLocalPosition(position);
        node.setRenderable(sphereRenderable);
        if (isDestination) {
            // 终点标记为红色
            MaterialFactory.makeOpaqueWithColor(activity, new Color(android.graphics.Color.RED))
                    .thenAccept(material -> node.setRenderable(ShapeFactory.makeSphere(0.07f, Vector3.zero(), material)));
        }
    }

    private TreeNodeDto findNodeById(int id) {
        for (TreeNodeDto node : App.getGraphRepository().getNodes()) {
            if (node.id == id) {
                return node;
            }
        }
        return null;
    }
}
