package gson;

import com.google.gson.annotations.SerializedName;

public class Messages {
	   @SerializedName("message1")
	   private String message1;

	   @SerializedName("message2")
	   private String message2;

	   @SerializedName("mess age 3")
	   private String message3;

	   @SerializedName("m e s s a g e 4")
	   private String message4;

	   @Override
	   public String toString() {
	       return "Messages{" +
	               "message1='" + message1 + '\'' +
	               ", message2='" + message2 + '\'' +
	               ", message3='" + message3 + '\'' +
	               ", message4='" + message4 + '\'' +
	               '}';
	   }

	   public String getMessage1() {
	       return message1;
	   }

	   public void setMessage1(String message1) {
	       this.message1 = message1;
	   }

	   public String getMessage2() {
	       return message2;
	   }

	   public void setMessage2(String message2) {
	       this.message2 = message2;
	   }

	   public String getMessage3() {
	       return message3;
	   }

	   public void setMessage3(String message3) {
	       this.message3 = message3;
	   }

	   public String getMessage4() {
	       return message4;
	   }

	   public void setMessage4(String message4) {
	       this.message4 = message4;
	   }

	   public Messages(String message1, String message2, String message3, String message4) {
	       this.message1 = message1;
	       this.message2 = message2;
	       this.message3 = message3;
	       this.message4 = message4;
	   }
}
