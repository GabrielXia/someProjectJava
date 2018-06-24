package stif;

import com.google.gson.JsonObject;

public class Departures {
	private transient Object stop_point;
	private transient Object route;
	private transient Object links;
	private JsonObject display_informations;
	private JsonObject stop_date_time;
	public JsonObject getDisplay_informations() {
		return display_informations;
	}
	public void setDisplay_informations(JsonObject display_informations) {
		this.display_informations = display_informations;
	}
	public JsonObject getStop_date_time() {
		return stop_date_time;
	}
	public void setStop_date_time(JsonObject stop_date_time) {
		this.stop_date_time = stop_date_time;
	}
	public String fetchDirection() {
		String direction = display_informations.get("direction").toString();
	    return direction.substring(1, direction.length()-1);
	}
	public String fetchTime() {
		String time = stop_date_time.get("arrival_date_time").toString();
		return time.substring(1, time.length()-1);
	}
	
}
