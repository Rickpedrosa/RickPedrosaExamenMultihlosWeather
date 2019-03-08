package com.example.rickdam.rickpedrosaexamenmultihlosweather.data.model;

public class CustomWeather {
    private String city_name;
    private String weather_description;
    private String tempMin;
    private String tempMax;
    private String tempMedia;
    private String windSpeed;
    private String windDegrees;
    private String humidity;
    private String cloudAll;
    private String logo;
    private String rain;
    private String sunset;
    private String dawn;


    public CustomWeather(String city_name, String weather_description, String tempMin, String tempMax,
                         String tempMedia, String windSpeed, String windDegrees, String humidity,
                         String cloudAll, String logo, String rain, String sunset, String dawn) {
        this.city_name = city_name;
        this.weather_description = weather_description;
        this.tempMin = tempMin.concat("ºC");
        this.tempMax = tempMax.concat("ºC");
        this.tempMedia = tempMedia.concat("ºC");
        this.windSpeed = windSpeed.concat("mps");
        this.windDegrees = windDegrees.concat("º");
        this.humidity = humidity.concat("%");
        this.cloudAll = cloudAll.concat("%");
        this.logo = "http://openweathermap.org/img/w/".concat(logo).concat(".png");
        this.rain = rain;
        this.sunset = sunset;
        this.dawn = dawn;
    }

    public CustomWeather() {
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getWeather_description() {
        return weather_description;
    }

    public void setWeather_description(String weather_description) {
        this.weather_description = weather_description;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMedia() {
        return tempMedia;
    }

    public void setTempMedia(String tempMedia) {
        this.tempMedia = tempMedia;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDegrees() {
        return windDegrees;
    }

    public void setWindDegrees(String windDegrees) {
        this.windDegrees = windDegrees;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getCloudAll() {
        return cloudAll;
    }

    public void setCloudAll(String cloudAll) {
        this.cloudAll = cloudAll;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getDawn() {
        return dawn;
    }

    public void setDawn(String dawn) {
        this.dawn = dawn;
    }
}
