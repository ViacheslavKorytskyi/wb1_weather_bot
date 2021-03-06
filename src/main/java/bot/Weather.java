package bot;

import java.io.IOException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.hc.client5.http.fluent.*;
import org.json.JSONObject;

public class Weather {
	private static String API__KEY = "3ae9fd3486ec25a1695539a527b60756";
	private static final String REQUEST_URL = "http://api.openweathermap.org/data/2.5/weather?" + "appid=%s&" + "q=%s&"
			+ "lang=ru&" + "units=metric";

	public static String getWeather(String city, Date date, String name) throws IOException {
		String requestUrl = String.format(REQUEST_URL, API__KEY, city);
		Response response = Request.get(requestUrl).execute();
		String jsonString = response.returnContent().toString();
		System.out.println(jsonString);
		JSONObject jsonObject = new JSONObject(jsonString);
		Map<String, Object> map = jsonObject.toMap();
		Map<String, Object> mainMap = (Map<String, Object>) map.get("main");
		Map<String, Object> sysMap = (Map<String, Object>) map.get("sys");
		Map<String, Object> coordMap = (Map<String, Object>) map.get("coord");
		ArrayList<HashMap<String, Object>> weatherList = (ArrayList<HashMap<String, Object>>) map.get("weather");

		Integer timezoneSource = (Integer) map.get("timezone");
		String timezoneFormatted = ZoneOffset.UTC.ofTotalSeconds(timezoneSource).toString();
		String timezoneFinal = timezoneFormatted.charAt(0) + " "
				+ timezoneFormatted.substring(1, timezoneFormatted.length());
		long secondsTimezone = date.getTime();
		LocalDateTime datetimeLocal = LocalDateTime.ofEpochSecond(secondsTimezone, 0,
				ZoneOffset.UTC.of(timezoneFormatted));
		DateTimeFormatter formatterTimezone = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String localdateTimezone = datetimeLocal.toString().substring(0, 10);
		localdateTimezone = formatterTimezone.format(datetimeLocal);
		DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MMMM");
		String monthTimezone = formatterMonth.format(datetimeLocal);
		String fulldateTimezone = localdateTimezone.substring(0, 2) + " " + monthTimezone + " "
				+ localdateTimezone.substring(6, 10) + " ????????";
		String localtimeTimezone = datetimeLocal.toString().substring(11, datetimeLocal.toString().length());

		Object longitude = coordMap.get("lon");
		if (longitude instanceof Integer) {
			longitude = ((Integer) longitude).doubleValue();
		}
		Object latitude = coordMap.get("lat");
		if (latitude instanceof Integer) {
			latitude = ((Integer) latitude).doubleValue();
		}
		String coordinates = "??.: " + longitude + ", ??.: " + latitude;

		String state = (String) sysMap.get("country");
		String cityName = (String) map.get("name");
		Object tempAverage = mainMap.get("temp");
		if (tempAverage instanceof Double) {
			tempAverage = ((Double) tempAverage).intValue();
		}
		String signTemp = ((Integer) tempAverage) > 0 ? "+" : "-";
		Object humidity = mainMap.get("humidity");
		if (humidity instanceof Double) {
			humidity = ((Double) humidity).intValue();
		}
		Object pressure = mainMap.get("pressure");
		if (tempAverage instanceof Double) {
			tempAverage = ((Double) tempAverage).intValue();
		}
		Integer visibility = (Integer) map.get("visibility");
		HashMap<String, Object> weatherMap = weatherList.get(0);
		String cloudiness = (String) weatherMap.get("description");

		long secondsMessage = date.getTime();
		LocalDateTime datetimeMessage = LocalDateTime.ofEpochSecond(secondsMessage, 0, ZoneOffset.UTC.of("+3"));
		DateTimeFormatter formatterMessage = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String dateMessage = formatterMessage.format(datetimeMessage);
		String monthMessage = formatterMonth.format(datetimeMessage);
		String fulldateMessage = dateMessage.substring(0, 2) + " " + monthMessage + " " + dateMessage.substring(6, 10)
				+ " ????????";
		int hour = datetimeMessage.getHour();

		String greeting1 = "";
		if (hour >= 0 && hour < 5) {
			greeting1 = "????????";
		} else if (hour >= 5 && hour < 12) {
			greeting1 = "????????";
		} else if (hour >= 12 && hour < 17) {
			greeting1 = "????????";
		} else {
			greeting1 = "??????????";
		}
		String greeting2 = "";
		if (greeting1.equalsIgnoreCase("????????") || greeting1.equalsIgnoreCase("????????")) {
			greeting2 = "??????";
		} else if (greeting1.equalsIgnoreCase("??????????")) {
			greeting2 = "????????????";
		} else {
			greeting2 = greeting1;
		}
		String good1 = "";
		if (greeting1.equalsIgnoreCase("????????")) {
			good1 = "????????????";
		} else if (greeting1.equalsIgnoreCase("????????")) {
			good1 = "????????????";
		} else {
			good1 = "????????????";
		}
		String good2 = "";
		if (greeting2.equalsIgnoreCase("????????")) {
			good2 = "????????????";
		} else {
			good2 = "????????????????";
		}
		String result = good1 + " " + greeting1 + ", " + name + "!\n?????? ?????????????? ???????????? ???? ?????????????? - " + fulldateMessage
				+ ":\n\n????????????: " + state + "\n??????????: " + cityName + "\n????????????????????: " + coordinates + "\n?????????????? ????????: "
				+ timezoneFinal + "\n?????????????? ????????: " + fulldateTimezone + "\n?????????????? ??????????: " + localtimeTimezone
				+ "\n?????????????? ??????????????????????: " + signTemp + " " + tempAverage + " ??C\n??????????????????: " + humidity
				+ " %\n????????????????: " + pressure + " ????\n??????????????????: " + visibility + " ??\n????????????????????: " + cloudiness
				+ "\n\n" + good2 + " " + greeting2 + "!";
		return result;
	}
}