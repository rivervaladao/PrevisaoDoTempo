package com.river.app.previsaodotempo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by fabrica on 29/02/16.
 */
public class ClimaFragmento extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);

        TextView textOutlook = ((TextView)
                rootView.findViewById(R.id.text_outlook));
        ImageView symbolView = ((ImageView)
                rootView.findViewById(R.id.image_symbol));
        TextView tempsView = ((TextView)
                rootView.findViewById(R.id.text_temp));
        TextView windView = ((TextView)
                rootView.findViewById(R.id.text_min));
        TextView realFeelView = ((TextView)
                rootView.findViewById(R.id.text_real_feel));

        Bundle args = getArguments();

        textOutlook.setText(args.getString(Clima.ARG_OUTLOOK));
        symbolView.setImageBitmap((Bitmap) args.getParcelable(Clima.ARG_IMAGE));
        tempsView.setText(args.getString(Clima.ARG_TEMP));
        windView.setText(args.getString(Clima.ARG_MIN));
        realFeelView.setText(args.getString(Clima.ARG_REAL_FEEL));

        return rootView;
    }
}
