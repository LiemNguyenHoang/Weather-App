package com.example.wt.Parameter.Forecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeatherForecast {
    private CityForecast cityForecast;
    private Integer cnt;
    private String cod;
    private List<HashMap<String,Object>> listForecast;

    public WeatherForecast() {
    }

    public WeatherForecast(CityForecast cityForecast, Integer cnt, String cod, List<HashMap<String,Object>> listForecast) {
        this.cityForecast = cityForecast;
        this.cnt = cnt;
        this.cod = cod;
        this.listForecast = listForecast;
    }

    public CityForecast getCityForecast() {
        return cityForecast;
    }

    public void setCityForecast(CityForecast cityForecast) {
        this.cityForecast = cityForecast;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public List<HashMap<String,Object>> getListForecast() {
        return listForecast;
    }

    public void setListForecast(List<HashMap<String,Object>> listForecast) {
        this.listForecast = listForecast;
    }

    public void fetchData(String json) {
        JSONObject jsonRoot = null;
        try {
            jsonRoot = new JSONObject(json);
            int i = 0;
            this.cityForecast = FetchCity(jsonRoot.getJSONObject("city"));
            this.listForecast = FetchList(jsonRoot.getJSONArray("list"));
            this.cnt = jsonRoot.getInt("cnt");
            this.cod = jsonRoot.getString("cod");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double convertFtoC(double temp){
        DecimalFormat df = new DecimalFormat("#.0");
        return Double.parseDouble(df.format((temp-273.15)));
    }

    // Start:
//*
    public MainForecast FetchMain(JSONObject jsonObject) {
        double grnd_level = 0.0, temp = 0.0, temp_max = 0.0, temp_min = 0.0;
        int humidity = 0, pressure = 0, sea_level = 0, temp_kf = 0;
        try {
            grnd_level = jsonObject.getDouble("grnd_level");
            humidity = jsonObject.getInt("humidity");
            pressure = jsonObject.getInt("pressure");
            sea_level = jsonObject.getInt("sea_level");
            temp = convertFtoC(jsonObject.getDouble("temp"))*1.0;
            temp_kf = jsonObject.getInt("temp_kf");
            temp_max = convertFtoC(jsonObject.getInt("temp_max"))*1.0;
            temp_min = convertFtoC(jsonObject.getInt("temp_min"))*1.0;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainForecast mainForecast = new MainForecast(grnd_level, temp, temp_max, temp_min, humidity, pressure, sea_level, temp_kf);

        return mainForecast;
    }

    //*
    public WindForecast FetchWind(JSONObject jsonObject) {
        double speed = 0.0;
        double deg = 0.0;
        try {
            speed = jsonObject.getDouble("speed");
            deg = jsonObject.getDouble("deg");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new WindForecast(speed, deg);
    }

    //*
    public RainForecast FetchRain(JSONObject jsonObject) {
        double db = 0.0;
        try {
            db = jsonObject.getDouble("3h");

        } catch (JSONException e) {
            db = 0;
            e.printStackTrace();
        }
        RainForecast rainForecast = new RainForecast(db);
        return rainForecast;
    }


    //*
    public SysForecast FetchSys(JSONObject jsonObject) {
        String db = "";
        try {
            db = jsonObject.getString("pod");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new SysForecast(db);
    }

    //*
    public CloudsForecast FetchClouds(JSONObject jsonObject) {
        Integer all = 0;
        try {
            all = jsonObject.getInt("all");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CloudsForecast(all);

    }

    //*
    public CityForecast FetchCity(JSONObject jsonObject) {
        CoordForecast coord = null;
        String country = null;
        int id = 0;
        String name = null;
        long sunrise = (long) 0.0;
        long sunset = (long) 0.0;
        int timezone = 0;

        try {
            coord = FetchCoord(jsonObject.getJSONObject("coord"));
            country = jsonObject.getString("country");
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");
            sunrise = jsonObject.getLong("sunrise");
            sunset = jsonObject.getLong("sunset");
            timezone = jsonObject.getInt("timezone");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CityForecast(id, name, coord, country, timezone, sunrise, sunset);
    }

//*
    public List<HashMap<String,Object>> FetchList(JSONArray jsonArray) {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        ArrayList<HashMap<String,Object>> lists = new ArrayList<>();
        HashMap<String,Object> hashMap = new HashMap<>();
        CloudsForecast clouds = new CloudsForecast();
        Integer dt = 0;
        String dt_tx = null;
        MainForecast main = new MainForecast();
        RainForecast rain = new RainForecast();
        SysForecast sys = new SysForecast();
        ArrayList<Weather> weathers = new ArrayList<>();
        WindForecast wind = new WindForecast();


        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObjects.add(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < jsonObjects.size(); i++) {
            try {
                hashMap = new HashMap<>();
                JSONObject jsonOj = jsonObjects.get(i);

                clouds = jsonOj.has("clouds")? FetchClouds(jsonOj.getJSONObject("clouds"))
                        :new CloudsForecast(0);

                hashMap.put("clouds",clouds);
                dt = jsonOj.getInt("dt");
                hashMap.put("dt",dt);
                dt_tx = jsonOj.getString("dt_txt");
                hashMap.put("dt_txt",dt_tx);
                main = FetchMain(jsonOj.getJSONObject("main"));
                hashMap.put("main",main);
                int asd = 0;

                HashMap<String,Double> hashMap1 = new HashMap<>();
                if(jsonOj.has("rain")){
                    double db = jsonOj.getJSONObject("rain").getDouble("3h");
                    hashMap1.put("3h",db);
                }else{
                    hashMap1.put("3h",0.0);
                }
                hashMap.put("rain",hashMap1);
                int asdi = 0;
                sys = FetchSys(jsonOj.getJSONObject("sys"));
                hashMap.put("sys",sys);
                weathers = FetchWeather(jsonOj.getJSONArray("weather"));
                hashMap.put("weather",weathers);
                wind = jsonOj.has("wind")? FetchWind(jsonOj.getJSONObject("wind"))
                        :new WindForecast(0.0,0.0);
                hashMap.put("wind",wind);

                lists.add(hashMap);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        int iasd = 0;
        return lists;

    }
    //*
    public ArrayList<Weather> FetchWeather(JSONArray jsonArray) {
        ArrayList<Weather> weathers = new ArrayList<>();
        try {

            JSONObject[] jsonObjects = new JSONObject[jsonArray.length()];
            for (int i = 0; i < jsonObjects.length; i++) {
                jsonObjects[i] = jsonArray.getJSONObject(i);
            }
            int id = 0;
            String main = "", description = "", icon = "";
            Weather weather = null;

            for (JSONObject jsonObject : jsonObjects) {
                id = jsonObject.getInt("id");
                main = jsonObject.getString("main");
                description = jsonObject.getString("description");
                icon = jsonObject.getString("icon");
                weather = new Weather(id, main, description, icon);
                weathers.add(weather);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weathers;
    }

    //*
    public static CoordForecast FetchCoord(JSONObject jsonObject) {
        double lon = 0.0, lat = 0.0;
        try {

            lon = jsonObject.getDouble("lon");
            lat = jsonObject.getDouble("lat");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CoordForecast(lon, lat);
    }

// End
}
