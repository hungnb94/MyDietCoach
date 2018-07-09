package com.hb.mydietcoach.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hb.mydietcoach.R;
import com.hb.mydietcoach.utils.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FAQ2_Fragment extends Fragment {
    Unbinder unbinder;

    public FAQ2_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq2_, container, false);
        unbinder = ButterKnife.bind(this, view);

        TextView textView = view.findViewById(R.id.tvTypeHtml);
        textView.setText(Html.fromHtml(getString(R.string.faq2_answer3)));

        return view;
    }

    @OnClick(R.id.tvChangeSubcription)
    void clickChangeSubcription() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_CHANGE_SUBCRIPTION));
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
