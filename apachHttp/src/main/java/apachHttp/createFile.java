package apachHttp;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.io.OutputStreamWriter;

public class createFile {
	public static void main(String[] arg) throws IOException {
		new File("uploads").mkdirs();
		//création du fichier texte
		final String TEXTFILENAME = "temp.txt";

		Writer out = new BufferedWriter(new OutputStreamWriter(
		       new FileOutputStream("uploads/"+TEXTFILENAME), "UTF-8"));
		try {
		   out.write("C'est un fichier encodé en UTF-8!");
		} finally {
		   out.close();
		}
		//fin création du fichier texte

		//création du fichier binaire
		final String FILE1_BIN = "file1.bin";
		final String FILE2_BIN = "file2.bin";
	       int i = 99;
	       DataOutputStream os = new DataOutputStream(new FileOutputStream("uploads/"+FILE1_BIN));
	       os.writeInt(i);
	       os.close();
	       i = 98;
	       os = new DataOutputStream(new FileOutputStream("uploads/"+FILE2_BIN));
	       os.writeInt(i);
	       os.close();
		//fin création du fichier binaire

	}
}
