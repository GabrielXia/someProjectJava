package rest;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class CreateText {
	public static void main(String[] args) {
		//création du fichier texte
		try {
			Writer out = new BufferedWriter(new OutputStreamWriter(
		       new FileOutputStream("file.txt"), "UTF-8"));
			out.write("C'est un fichier encodé en UTF-8!");
			out.close();
			
			int i = 99;
			DataOutputStream os = new DataOutputStream(new FileOutputStream("file.bin"));
			os.writeInt(i);
			os.close();
		} catch (Exception e) {
		   e.printStackTrace();
		}
		//fin création du fichier texte
		//création du fichier binaire

	}
}
