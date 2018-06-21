package gson;

public class ArgsWithMessages {
	public ArgsWithMessages(Messages args) {
	       this.args = args;
	   }

	   private Messages args;

	   @Override
	   public String toString() {
	       return "ArgsWithMessages{" +
	               "args=" + args +
	               '}';
	   }

	   public Messages getArgs() {
	       return args;
	   }

	   public void setArgs(Messages args) {
	       this.args = args;
	   }
}
