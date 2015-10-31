package com.example.model;

import java.util.ArrayList;

public class ForcastBean {
	ArrayList<Weather> weather;
	Temp main;
	public ArrayList<Weather> getWeather() {
		return weather;
	}
	public void setWeather(ArrayList<Weather> weather) {
		this.weather = weather;
	}
	public Temp getMain() {
		return main;
	}
	public void setMain(Temp main) {
		this.main = main;
	}
}
