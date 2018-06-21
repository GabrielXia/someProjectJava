package master;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Master {
	
	private static final String FILENAME = "ip";
	
	public static int TIMEOUT = 4;
	
	public static String getUM(Map<String, String> m, String machine) {
		for (Entry<String, String> e: m.entrySet()) {
			if(e.getValue().equals(machine)) {
				return e.getKey();
			}
		}
		return "Not found";
	}
	
	public static void main(String[] args) {
		String[] sx = {"/tmp/jxia/splits/S4.txt", "/tmp/jxia/splits/S5.txt", "/tmp/jxia/splits/S6.txt"};
		Map<String, String> umToMachine = new HashMap<String, String>();
		Map<String, List<String>> keyToUm = new HashMap();
		
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			List<String> machines = new ArrayList();
			// read machines available
			ArrayList<Process> waitList = new ArrayList();
			while ((sCurrentLine = br.readLine()) != null) {
				ProcessBuilder pb = new ProcessBuilder("ssh", "jxia@"+ sCurrentLine, "hostname");
				Process p = pb.start();
				boolean b = p.waitFor(TIMEOUT, TimeUnit.SECONDS);
				if(!b) { 
					System.err.println(sCurrentLine + " has a problem");
				} else {
					machines.add(sCurrentLine);
				}
			}
			int machineNum = machines.size();
			
			// assigne task
			waitList.clear();
			System.out.println("start mapping");
			for (int i = 0; i < sx.length; i ++) {
				String file = sx[i];
				int len = file.length();
				String x = "UM" + file.substring(len-5, len-4); //TODO the size
				String machine = machines.get(i % machineNum); // assigne
				ProcessBuilder pbM = new ProcessBuilder("ssh", "jxia@"+ machine, "java -jar /tmp/jxia/slave.jar" + " 0 " + file);
				Process pM = pbM.start();
				umToMachine.put(x, machine);
				waitList.add(pM);
				InputStream input = pM.getInputStream();
				Scanner s = new Scanner(input).useDelimiter("\\A");
				String result = s.hasNext() ? s.next() : "";
				
				// construct key to um
				String[] parts = result.split("\n");
				String um = getUM(umToMachine, machine);
				for (String key : parts) {
					if(keyToUm.containsKey(key)) {
						keyToUm.get(key).add(um);
					} else {
						keyToUm.put(key, new ArrayList<String>());
						keyToUm.get(key).add(um);
					}
				}
			}
			for(Process p : waitList) {
				p.waitFor();
			}
			
			// shuffle
			System.out.println("start shuffling");
			waitList.clear();
			Map<String, String> keyToMachine = new HashMap();
			int wordIndex = 0;
			Map<String, List<String>> machineToUm = new HashMap();
			for (Entry<String, List<String>> e : keyToUm.entrySet()) {
				String machineDes = machines.get(wordIndex%machineNum);
				String word = e.getKey();
				keyToMachine.put(word, machineDes);
				List<String> ums = e.getValue();
				for (String um : ums) {
					if (!machineToUm.containsKey(machineDes)) {
						machineToUm.put(machineDes, new ArrayList());
					} 
				
					String machineSource = umToMachine.get(um);
					if (!machineSource.equals(machineDes)) {
						if(!machineToUm.get(machineDes).contains(um)) {
							ProcessBuilder pb = new ProcessBuilder("ssh", "jxia@"+ machineSource, 
									"scp /tmp/jxia/maps/" + um + ".txt" + " jxia@"+ machineDes + ":/tmp/jxia/maps/" + um + ".txt");
							Process p = pb.start();
							waitList.add(p);
							machineToUm.get(machineDes).add(um);
						}
					} else {
						if(!machineToUm.get(machineDes).contains(um)) {
							machineToUm.get(machineDes).add(um);
						}
					}
				}
				wordIndex ++;
			}
			for(Process p : waitList) {
				p.waitFor();
			}
			
			// remove source umx
			System.out.println("removing umx");
			waitList.clear();
			for (Entry<String, String> e : umToMachine.entrySet()) {
				if(!machineToUm.get(e.getValue()).contains(e.getKey())) {
					ProcessBuilder pb = new ProcessBuilder("ssh", "jxia@"+ e.getValue(), 
							"rm -rf /tmp/jxia/maps/" + e.getKey() + ".txt");
					Process p = pb.start();
					waitList.add(p);
				}
			}
			for(Process p : waitList) {
				p.waitFor();
			}
			
			// reduce
			System.out.println("start reducing");
			waitList.clear();
			Map<String, Integer> machineToSm = new HashMap();
			for(Entry<String, List<String>> e : machineToUm.entrySet()) {
				machineToSm.put(e.getKey(), 0);
			}
			for (Entry<String, String> e : keyToMachine.entrySet()) {
				String key = e.getKey();
				String machine = e.getValue();
				List<String> umx = keyToUm.get(key);
				String commandEnd = "";
				for (String ux: umx) {
					commandEnd += (" /tmp/jxia/maps/" + ux + ".txt");
				}
				int rmIndex = machineToSm.get(machine);
				String rmx = "/tmp/jxia/reduces/RM" + machineToSm.get(machine) + ".txt";
				machineToSm.put(machine, machineToSm.get(machine) +1);
				ProcessBuilder pb = new ProcessBuilder("ssh", "-o StrictHostKeyChecking=no", "jxia@"+ machine, 
						"java -jar /tmp/jxia/slave.jar " + "1 " + key + " "+ rmx  + commandEnd);
				pb.inheritIO();
				Process p = pb.start();
				waitList.add(p);
			}
			for(Process p : waitList) {
				p.waitFor();
			}
			
			// print result
			System.out.println("start printing");
			for (Entry<String, List<String>> e : machineToUm.entrySet()) {
				String machine = e.getKey();
				int rmNum = machineToSm.get(machine);
				for(int i = 0; i < rmNum; i++) {
					ProcessBuilder pb = new ProcessBuilder("ssh", "jxia@"+ machine, 
							"cat " + "/tmp/jxia/reduces/RM" + i + ".txt");
					pb.inheritIO();
					Process p = pb.start();
					boolean b = p.waitFor(10, TimeUnit.SECONDS);
				}
			}
			
			// print
			for (Entry<String, String> e : umToMachine.entrySet()) {
				System.out.println(e.getKey() + "-" + e.getValue());
			}
			for (Entry<String, List<String>> e : keyToUm.entrySet()) {
				System.out.println(e.getKey() + " " + e.getValue().toString());
			}
			for (Entry<String, String> e : keyToMachine.entrySet()) {
				System.out.println(e.getKey() + " " + e.getValue().toString());
			}
			for (Entry<String, List<String>> e : machineToUm.entrySet()) {
				System.out.println(e.getKey() + " " + e.getValue().toString());
			}
			System.out.println("Phase de MAP terminée");
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
