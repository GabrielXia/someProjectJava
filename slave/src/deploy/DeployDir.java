package deploy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

public class DeployDir {

	private static final String FILENAME = "ip";
	
	public static void main(String[] args) {
		BufferedReader br = null;
		FileReader fr = null;
		try {
			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				ProcessBuilder pb = new ProcessBuilder("ssh", "jxia@"+ sCurrentLine, "hostname");
				pb = pb.inheritIO();
				Process p = pb.start();
				boolean b = p.waitFor(2, TimeUnit.SECONDS);
				
				if(!b) { 
					System.err.println(sCurrentLine + " has a problem");
				} else {
					ProcessBuilder pbM = new ProcessBuilder("ssh", "jxia@"+ sCurrentLine, "mkdir -p /tmp/jxia/splits/");
					pbM = pbM.inheritIO();
					Process pM = pbM.start();
					boolean bM = pM.waitFor(2, TimeUnit.SECONDS);
					
					ProcessBuilder pbC = new ProcessBuilder("scp", "-r", "/tmp/jxia/splits","jxia@"+ sCurrentLine+":/tmp/jxia/");
					pbC = pbC.inheritIO();
					Process pC = pbC.start();
					boolean bC = pC.waitFor(2, TimeUnit.SECONDS);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
