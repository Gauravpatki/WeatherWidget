package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CoreGsonUtils
{

	public static <T> T fromJson(String string, Class<T> model)
	{

		Gson gson = new GsonBuilder().create();
		T gfromat = null;
		try
		{
			gfromat = gson.fromJson(string, model);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gfromat;
	}

	public static <T> String toJson(Object obj)
	{

		String gsonstr = "";
		Gson gson = new GsonBuilder().create();
		gsonstr = gson.toJson(obj);
		return gsonstr;
	}

}
