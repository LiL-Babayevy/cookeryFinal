package com.example.cookeryfinal.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cookeryfinal.R;

public class SettingsFragment extends Fragment{
    public SettingsFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        android.view.View root = inflater.inflate(R.layout.fragment_settings, container, false);
        return root;
    }
}
