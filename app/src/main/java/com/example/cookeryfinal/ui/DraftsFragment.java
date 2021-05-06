package com.example.cookeryfinal.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cookeryfinal.R;

public class DraftsFragment extends Fragment {

    public DraftsFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drafts, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        return root;
    }
}