package com.river.app.previsaodotempo;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    private static final String API_KEY = "32a5720536d3f7b5c7a59170e653f531";
    private static final String web_service_url =
            "http://api.openweathermap.org/data/2.5/forecast/daily?q=";
    private static final String location = "Goiania, BR";
    private static int notificationId;
    private ViewPager viewPager;
    private ActionBar actionBar;
    private List<Clima> climaList;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

/*
        Calendar calendar = Calendar.getInstance();
        String weekDay;
        SimpleDateFormat dayFormat;

        dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
*/

        actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("Hoje").setTabListener(this));

        URL url = createURL(location);

        new GetClimaTask().execute(url);

        if(notificationId == 0){
            postAlert(0);
        }

       //detect page changes

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void postAlert(int i) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Alerta do Tempo")
                //.setContentText(climaList.get(i).description)
                .setSmallIcon(R.drawable.small_icon)
                .setAutoCancel(true)
                .setTicker("Prepare-se para se esquentar!");

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.snow_scene));

        builder.setStyle(bigPictureStyle);

        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class)
                .addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
        notificationId++;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    // create openweathermap.org web service URL using city
    private URL createURL(String city) {
        String apiKey = API_KEY;
        String baseUrl = web_service_url;

        try {
            // create URL for specified city and imperial units (Fahrenheit)
            String urlString = baseUrl + URLEncoder.encode(city, "UTF-8") +
                    "&units=imperial&cnt=16&APPID=" + apiKey;
            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // URL was malformed
    }

    // create Weather objects from JSONObject containing the forecast
    private void convertJSONtoArrayList(JSONObject forecast) {
        if (climaList != null)
            climaList.clear(); // clear old weather data
        else
            climaList = new ArrayList<>();
        try {
            JSONArray list = forecast.getJSONArray("list");
            for (int i = 0; i < list.length(); ++i) {
                JSONObject day = list.getJSONObject(i); // get one day's data
                JSONObject temperatures = day.getJSONObject("temp");
                JSONObject weather =
                        day.getJSONArray("weather").getJSONObject(0);
                climaList.add(new Clima(
                        day.getLong("dt"), // date/time timestamp
                        temperatures.getDouble("min"), // minimum temperature
                        temperatures.getDouble("max"), // maximum temperature
                        day.getDouble("humidity"), // percent humidity
                        weather.getString("description"), // weather conditions
                        weather.getString("icon"))); // icon name
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class GetClimaTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {

                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }

                    } catch (IOException e) {
                        Toast.makeText(MainActivity.this,
                                " erro ao ler dados do webservice", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    return new JSONObject(builder.toString());
                } else {
                    Toast.makeText(MainActivity.this,
                            " erro webservice indisponivel", Toast.LENGTH_LONG).show();

                }

            } catch (Exception e) {
                Toast.makeText(MainActivity.this,
                        " erro ao conectar no webservice", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                connection.disconnect(); // close the HttpURLConnection
            }
            return null;
        }

        // process JSON response and update ListView
        @Override
        protected void onPostExecute(JSONObject weather) {
            convertJSONtoArrayList(weather); // repopulate weatherList
            if (climaList != null) {
                adapter.setList(climaList);
                for (Clima clima : climaList) {
                    actionBar.addTab(actionBar.newTab().setText(clima.dayOfWeek).setTabListener(MainActivity.this));
                }
            } else {
                throw new RuntimeException("Não consegui nenhum lista de objetos de clima");
            }

        }
    }
}

