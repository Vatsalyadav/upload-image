package com.vatsalyadav.uploadimage.view;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.vatsalyadav.uploadimage.R


class CameraActivity : AppCompatActivity(), LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
    }

    // TODO: Ask Camera permissions
    // TODO: Setup CameraX
    // TODO: Implement camera functions like flash, zoom
}