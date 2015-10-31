package com.example.model;

import java.util.ArrayList;

public class ResBean {
	String name;
	ArrayList<Weather> weather;
	Temp main;
	 private int cod;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

}
