package com.river.app.previsaodotempo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fabrica on 29/02/16.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private Map<String, Bitmap> bitmaps = new HashMap<>();
    private List<Clima> list;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setList(List<Clima> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public Fragment getItem(int position) {
        if(list == null) {
            Clima fake = new Clima(Calendar.getInstance().getTimeInMillis()
                    , 0.0D, 0.0D, 0.0D, "sem informacao", null);
            list = new ArrayList<>();
            list.add(fake);
        };
        Fragment fragment = new ClimaFragmento();
        Clima clima = list.get(position);
        if(clima.iconUrl != null)
        new LoadImageAsyncTask().execute(clima.iconUrl);

        Bundle args = new Bundle();
        args.putString(Clima.ARG_OUTLOOK, clima.dayOfWeek);
        args.putParcelable(Clima.ARG_IMAGE, bitmaps.get(clima.iconUrl));
        args.putString(Clima.ARG_TEMP, clima.maxTemp);
        args.putString(Clima.ARG_MIN, clima.minTemp);
        args.putString(Clima.ARG_REAL_FEEL, clima.humidity);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        if(list == null) return 1;
        return list.size();
    }

    private class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)  url.openConnection();

                try ( InputStream inputStream = connection.getInputStream() ) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmaps.put(params[0], bitmap); // cache for later use
                }
                catch (Exception e) {
                    Log.e("LoadImageAsyncTask", e.getMessage());
                }
            } catch (Exception e) {
                Log.e("LoadImageAsyncTask",e.getMessage());

            } finally {
                connection.disconnect();
            }

            return null;
        }

    }

}
