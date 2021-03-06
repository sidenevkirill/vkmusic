package com.sidenev.plus.vkmusic.slider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sidenev.plus.vkmusic.MainActivity;
import com.example.root.vkcoffee.R;


@SuppressLint("ValidFragment")
public class Fragment1 extends Fragment{

    Context context;
    MainActivity activity;
    View v;
    TextView tv_next;

    public Fragment1(Context context) {
        this.context = context;
        activity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_1, container, false);
        init();
        return v;
    }

    private void init(){
        tv_next = (TextView) v.findViewById(R.id.textView3);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.nextFragment();
            }
        });
    }

}
