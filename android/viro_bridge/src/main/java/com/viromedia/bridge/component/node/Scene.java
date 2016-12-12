/**
 * Copyright © 2016 Viro Media. All rights reserved.
 */
package com.viromedia.bridge.component.node;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.viro.renderer.jni.SceneJni;
import com.viro.renderer.jni.VideoTextureJni;
import com.viro.renderer.jni.RendererJni;
import com.viromedia.bridge.component.Camera;
import com.viromedia.bridge.component.OrbitCamera;

public class Scene extends Node implements SceneJni.SceneDelegate {
    private static final String TAG = Scene.class.getSimpleName();
    private final SceneJni mNativeScene;
    private RendererJni mNativeRenderer;
    private Camera mCamera;
    private boolean mReticleEnabled = true;

    public Scene(Context context) {
        this(context, null, -1 -1);
    }

    public Scene(Context context, AttributeSet attrs) {
        this(context, attrs, -1 -1);
    }

    public Scene(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public Scene(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mNativeScene = new SceneJni(getNodeJni());
        mNativeScene.registerDelegate(this);
    }

    public SceneJni getNativeScene(){
        return mNativeScene;
    }

    public void setBackgroundVideoTexture(VideoTextureJni videoTexture) {
        mNativeScene.setBackgroundVideoTexture(videoTexture);
    }

    public void setNativeRenderer(RendererJni nativeRenderer) {
        mNativeRenderer = nativeRenderer;
        mNativeRenderer.enableReticle(mReticleEnabled);
        setCameraIfPossible();
    }

    public void setReticleEnabled(boolean enable) {
        mReticleEnabled = enable;
        if (mNativeRenderer != null) {
            mNativeRenderer.enableReticle(enable);
        }
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);

        if (child instanceof Camera) {
            mCamera = (Camera) child;
            setCameraIfPossible();
        }
    }

    private void setCameraIfPossible() {
        if (mCamera != null && mNativeRenderer != null) {
            mNativeRenderer.setCameraPosition(mCamera.getPosition());
            mNativeRenderer.setCameraRotationType(mCamera.getRotationType());
            if (mCamera instanceof OrbitCamera) {
                mNativeRenderer.setOrbitCameraFocalPoint(((OrbitCamera) mCamera).getFocalPoint());
            }
        }
    }
    /**
     * Attach Renderer Delegate callbacks to the Scene to be propagated
     * across all of it's child views.
     */
    @Override
    public void onSceneWillAppear() {
        sceneWillAppear();
    }

    @Override
    public void onSceneDidAppear() {
        sceneDidAppear();
    }

    @Override
    public void onSceneWillDisappear() {
        sceneWillDisappear();
    }

    @Override
    public void onSceneDidDisappear() {
        sceneDidDisappear();
    }
}
