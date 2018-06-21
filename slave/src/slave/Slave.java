package slave;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Slave {
	public static void main(String[] args) throws IOException {
		// Sx -> UMx
		if (args[0].equals("0")) {
			try {
				String file  = args[1];
				
				ProcessBuilder pbM = new ProcessBuilder("mkdir", "-p", "/tmp/jxia/maps");
				pbM = pbM.inheritIO();
				Process pM = pbM.start();			
				int len = file.length();
				
				//TODO more files in the  
				String x = file.substring(len-5, len-4);
				PrintWriter writer = new PrintWriter("/tmp/jxia/maps/UM" + x + ".txt", "UTF-8");
	
				BufferedReader br = null;
				FileReader fr = null;
				fr = new FileReader(file);
				br = new BufferedReader(fr);
	
				String sCurrentLine;
				Map<String, Integer> m = new HashMap();
				
				while ((sCurrentLine = br.readLine()) != null) {
					String[] parts = sCurrentLine.split("[^a-zA-Z0-9]");
					for (String s: parts) {
						if (s.equals("")) continue;
						writer.println(s + " 1");
						if(m.containsKey(s)) {
							m.put(s, m.get(s) +1);
						} else {
							m.put(s, 1);
						}
					}
				}
				
				for (Entry<String, Integer> e : m.entrySet()) {
					System.out.println(e.getKey());
				}
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("1")) { // UMx -> RM
			try {
				String key = args[1];
				String rm = args[2];
				int argsLen = args.length;
				ProcessBuilder pbM = new ProcessBuilder("mkdir", "-p", "/tmp/jxia/reduces");
				pbM = pbM.inheritIO();
				Process pM = pbM.start();
				pM.waitFor();
				List<String> Um = new ArrayList();
				PrintWriter writer = new PrintWriter(rm, "UTF-8");
				int count = 0;
				for (int i=3; i<argsLen; i++) {
					String file = args[i];
					Um.add(args[i]);
					BufferedReader br = null;
					FileReader fr = null;
					fr = new FileReader(file);
					br = new BufferedReader(fr);
					String sCurrentLine;
					while ((sCurrentLine = br.readLine()) != null) {
						if(sCurrentLine.split(" ")[0].equals(key)) {
							count ++;
						}
					}
				}
				writer.println(key + " " + count);
				writer.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("2")) {
			try {
				// 1 keyNum keys ... rmStartIndex umx ..
				int keySize = Integer.parseInt(args[1]);
				int index = 2;
				List<String> keys = new ArrayList();
				Map<String, List<String>> keyToUm = new HashMap();
				for(int i = 0; i < keySize; i ++) {
					keys.add(args[index++]);
				}
				for (int i = 0; i < keySize; i ++) {
					int umSize = Integer.parseInt(args[index++]);
					keyToUm.put(keys.get(i), new ArrayList());
					for (int j = 0; j < umSize; j++) {
						keyToUm.get(keys.get(i)).add(args[index++]);
					}
				}
				List<String> rms = new ArrayList(); 
				for(int i = 0; i < keySize; i ++) {
					rms.add(args[index++]);
				}
				if (index != args.length) {
					System.out.println("wrong format");
				}
				
				ProcessBuilder pbM = new ProcessBuilder("mkdir", "-p", "/tmp/jxia/reduces");
				pbM = pbM.inheritIO();
				Process pM = pbM.start();
				pM.waitFor();
				
				for(int i = 0; i < keySize; i ++) {
					String key = keys.get(i);
					List<String> ums = keyToUm.get(key);
					String rm = "/tmp/jxia/reduces/RM" + rms.get(i) + ".txt";
					PrintWriter writer = new PrintWriter(rm, "UTF-8");
					int count = 0;
					for (String um: ums) {
						String file = "/tmp/jxia/maps/" + um + ".txt"; 
						BufferedReader br = null;
						FileReader fr = null;
						fr = new FileReader(file);
						br = new BufferedReader(fr);
						String sCurrentLine;
						while ((sCurrentLine = br.readLine()) != null) {
							if(sCurrentLine.split(" ")[0].equals(key)) {
								count ++;
							}
						}
					}
					writer.println(key + " " + count);
					writer.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (args[0].equals("3")) {
			// read rm
			int start = Integer.parseInt(args[1]);
			int end = Integer.parseInt(args[2]);
			for (int i = start; i < end; i ++) {
				String file = "/tmp/jxia/reduces/RM" + i + ".txt";
				FileReader fr = null;
				BufferedReader br = null;
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				String sCurrentLine;
				while ((sCurrentLine = br.readLine()) != null) {
					System.out.println(sCurrentLine);
				}
			}
		}
	}
}
