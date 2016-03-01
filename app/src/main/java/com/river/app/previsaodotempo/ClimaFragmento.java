package com.river.app.previsaodotempo;

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
    public static final String ARG_DAY = "day";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);

        Bundle args = getArguments();

        int i = args.getInt(ClimaFragmento.ARG_DAY);

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

        textOutlook.setText(ClimaData.outlookArray[i]);
        symbolView.setImageResource(ClimaData.symbolArray[i]);
        tempsView.setText(ClimaData.tempsArray[i] + "°C");
        realFeelView.setText(ClimaData.realFeelArray[i] + "°C");

        return rootView;
    }
}
