package com.example.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StartNullFragment extends android.support.v4.app.Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.null_fragment, null);

        return root;
    }


}
