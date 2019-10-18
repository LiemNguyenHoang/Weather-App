package com.example.wt.Static;

public class FetchJSONForecast {
//    public static Main Main(JSONObject jsonObject) {
//        double temp = 0, temp_max = 0.0, temp_min = 0.0;
//        int pressure = 0, humidity = 0;
//        try {
//
//            temp = jsonObject.getDouble("temp");
//            temp_min = jsonObject.getDouble("temp_min");
//            temp_max = jsonObject.getDouble("temp_max");
//            pressure = jsonObject.getInt("pressure");
//            humidity = jsonObject.getInt("humidity");
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new Main(temp, temp_min, temp_max, pressure, humidity);
//    }
//
//    public static Wind Wind(JSONObject jsonObject) {
//        double speed = 0.0;
//        double deg = 0.0;
//        try {
//            speed = jsonObject.getDouble("speed");
//            deg = jsonObject.getDouble("deg");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new Wind(speed, deg);
//    }
//
//    public static double Rain(JSONObject jsonObject) {
//        double db = 0.0;
//        try {
//            db = jsonObject.getDouble("3h");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return db;
//    }
//
//    public static double Clouds(JSONObject jsonObject) {
//        double db = 0.0;
//        try {
//            db = jsonObject.getDouble("all");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return db;
//
//    }
//
//
//    public static City City(JSONObject jsonObject) {
//        int id = 0;
//        String name = null;
//        Coord coord = null;
//        String country = null;
//        int timezone = 0;
//        long sunrise = (long) 0.0;
//        long sunset = (long) 0.0;
//        long lat = (long) 0.0;
//        long lon = (long) 0.0;
//        int deg = 0;
//        try {
//            id = jsonObject.getInt("id");
//            name = jsonObject.getString("name");
//            // Start fetch coord
//            JSONObject jsonObject1 = jsonObject.getJSONObject("coord");
//            lon = jsonObject1.getLong("lon");
//            lat = jsonObject1.getLong("lat");
//            coord = new Coord(lon, lat);
//            // End
//            country = jsonObject.getString("country");
//            timezone = jsonObject.getInt("timezone");
//            sunrise = jsonObject.getLong("sunrise");
//            sunset = jsonObject.getLong("sunset");
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new City(id, name, coord, country, timezone, sunrise, sunset);
//    }
//
//    public static List[] List(JSONArray jsonArray) {
//        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
//        List[] lists = new List[jsonArray.length()];
//        String dt = null;
//        Main main = null;
//        ArrayList<WeatherPara> weathers = null;
//        double clouds = 0;
//        Wind wind = null;
//        String dt_tx = null;
//        double rain = 0.0;
//        for (int i = 0; i < jsonArray.length(); i++) {
//            try {
//                jsonObjects.add(jsonArray.getJSONObject(i));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        for (int i = 0; i < jsonObjects.size(); i++) {
//            try {
//                JSONObject jsonOj = jsonObjects.get(i);
//                dt = jsonOj.getString("dt");
//                main = FetchJSONForecast.Main(jsonOj.getJSONObject("main"));
//                weathers = FetchJSONForecast.Weather(jsonOj.getJSONArray("weather"));
//                clouds = FetchJSONForecast.Clouds(jsonOj.getJSONObject("clouds"));
//                wind = FetchJSONForecast.Wind(jsonOj.getJSONObject("wind"));
//                dt_tx = jsonOj.getString("dt_txt");
//                lists[i] = (new List(dt, main, weathers, clouds, rain, wind, dt_tx));
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return lists;
//
//    }
//
//    public static ArrayList<WeatherPara> Weather(JSONArray jsonArray) {
//        ArrayList<WeatherPara> weathers = new ArrayList<>();
//        try {
//
//            JSONObject[] jsonObjects = new JSONObject[jsonArray.length()];
//            for (int i = 0; i < jsonObjects.length; i++) {
//                jsonObjects[i] = jsonArray.getJSONObject(i);
//            }
//            int id_wearth = 0;
//            String main_wearth = null, description_wearth = null, icon_wearth = null;
//            WeatherPara weather_temp = null;
//            for (JSONObject jsonObject : jsonObjects) {
//                id_wearth = jsonObject.getInt("id");
//                main_wearth = jsonObject.getString("main");
//                description_wearth = jsonObject.getString("description");
//                icon_wearth = jsonObject.getString("icon");
//
//                weather_temp = new WeatherPara(id_wearth, main_wearth, description_wearth, icon_wearth);
//                weathers.add(weather_temp);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return weathers;
//    }
//
//    public static Coord Coord(JSONObject jsonObject) {
//        double lon = 0.0, lat = 0.0;
//        try {
//
//            lon = jsonObject.getDouble("lon");
//            lat = jsonObject.getDouble("lat");
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new Coord(lon, lat);
//    }
}
