package com.example.wt.Parameter.Current;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class WeatherCurrent {
    private String base;
    private CloudsCurrent clouds;
    private Integer cod;
    private CoordCurrent coord;
    private Integer dt;
    private Integer id;
    private MainCurrent main;
    private String name;
    private HashMap<String, Double> rain;
    private SysCurrent sys;
    private String timezone;
    private ArrayList<Weather> weathers;
    private WindCurrent wind;

    public WeatherCurrent() {
    }

    public WeatherCurrent(String base, CloudsCurrent clouds, Integer cod, CoordCurrent coord, Integer dt, Integer id, MainCurrent main, String name, HashMap<String, Double> rain, SysCurrent sys, String timezone, ArrayList<Weather> weathers, WindCurrent wind) {
        this.base = base;
        this.clouds = clouds;
        this.cod = cod;
        this.coord = coord;
        this.dt = dt;
        this.id = id;
        this.main = main;
        this.name = name;
        this.rain = rain;
        this.sys = sys;
        this.timezone = timezone;
        this.weathers = weathers;
        this.wind = wind;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public CloudsCurrent getClouds() {
        return clouds;
    }

    public void setClouds(CloudsCurrent clouds) {
        this.clouds = clouds;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public CoordCurrent getCoord() {
        return coord;
    }

    public void setCoord(CoordCurrent coord) {
        this.coord = coord;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MainCurrent getMain() {
        return main;
    }

    public void setMain(MainCurrent main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Double> getRain() {
        return rain;
    }

    public void setRain(HashMap<String, Double> rain) {
        this.rain = rain;
    }

    public SysCurrent getSys() {
        return sys;
    }

    public void setSys(SysCurrent sys) {
        this.sys = sys;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public ArrayList<Weather> getWeather() {
        return weathers;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weathers = weather;
    }

    public WindCurrent getWind() {
        return wind;
    }

    public void setWind(WindCurrent wind) {
        this.wind = wind;
    }

    // Start
    public void fetchData(String json) {
        JSONObject jsonRoot = null;
        HashMap<String, Object> hashMap = new HashMap<>();

        try {
            jsonRoot = new JSONObject(json);
            int i = 0;
            this.base = jsonRoot.getString("base");
            hashMap.put("base", this.base);
            this.clouds = FetchClouds(jsonRoot.getJSONObject("clouds"));
            this.cod = jsonRoot.getInt("cod");
            this.coord = FetchCoord(jsonRoot.getJSONObject("coord"));
            this.dt = jsonRoot.getInt("dt");
            this.id = jsonRoot.getInt("id");
            this.main = FetchMain(jsonRoot.getJSONObject("main"));
            this.name = jsonRoot.getString("name");
            // rain
            HashMap<String, Double> hashMap1 = new HashMap<>();

            double d = jsonRoot.has("rain")?jsonRoot.getJSONObject("rain").getDouble("3h"):0.0;
            hashMap1.put("3h", d);
            this.rain = hashMap1;
            //
            this.sys = FetchSys(jsonRoot.getJSONObject("sys"));
            this.timezone = jsonRoot.getString("timezone");
            this.weathers = FetchWeather(jsonRoot.getJSONArray("weather"));
            this.wind = jsonRoot.has("wind")? FetchWind(jsonRoot.getJSONObject("wind"))
                    :new WindCurrent(0.0,0.0);
            this.wind = FetchWind(jsonRoot.getJSONObject("wind"));


            int isad = 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double convertFtoC(double temp) {
        DecimalFormat df = new DecimalFormat("#.0");
        return Double.parseDouble(df.format((temp - 273.15)));
    }


    public MainCurrent FetchMain(JSONObject jsonObject) {
        double grnd_level = 0.0, temp = 0.0, temp_max = 0.0, temp_min = 0.0;
        int humidity = 0, pressure = 0, sea_level = 0, temp_kf = 0;
        try {
            grnd_level = 0.0;
            sea_level = 0;

            temp = convertFtoC(jsonObject.getDouble("temp")) * 1.0;
            pressure = jsonObject.getInt("pressure");
            humidity = jsonObject.getInt("humidity");
            temp_min = convertFtoC(jsonObject.getInt("temp_min")) * 1.0;
            temp_max = convertFtoC(jsonObject.getInt("temp_max")) * 1.0;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainCurrent mainCurrent = new MainCurrent(grnd_level, temp, temp_max, temp_min, humidity, pressure, sea_level);

        return mainCurrent;
    }


    public WindCurrent FetchWind(JSONObject jsonObject) {
        double speed = 0.0;
        double deg = 1.0;
        try {
            speed = jsonObject.getDouble("speed");
            deg = jsonObject.getDouble("deg");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new WindCurrent(speed, deg);
    }


    public RainCurrent FetchRain(JSONObject jsonObject) {
        double db = 0.0;
        try {
            db = jsonObject.getDouble("3h");

        } catch (JSONException e) {
            db = 0;
            e.printStackTrace();
        }
        RainCurrent rainCurrent = new RainCurrent(db);
        return rainCurrent;
    }


    public SysCurrent FetchSys(JSONObject jsonObject) {
        String country = "", sunrise = "", sunset = "";
        try {
            country = jsonObject.getString("country");
            sunrise = jsonObject.getString("sunrise");
            sunset = jsonObject.getString("sunset");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new SysCurrent(country, sunrise, sunset);
    }


    public CloudsCurrent FetchClouds(JSONObject jsonObject) {
        Integer all = 0;
        try {
            all = jsonObject.getInt("all");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CloudsCurrent(all);

    }

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


    public static CoordCurrent FetchCoord(JSONObject jsonObject) {
        double lon = 0.0, lat = 0.0;
        try {

            lon = jsonObject.getDouble("lon");
            lat = jsonObject.getDouble("lat");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CoordCurrent(lon, lat);
    }
// End
}
