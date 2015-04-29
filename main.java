/*

# khoja Algorithm (a command-line version)

A command line version of Koja algorithm (An Arabic rooting algorithm). The algorithm reduces Arabic words to their roots.

Version 1.0

Author: Motaz Saad (motaz dot saad at gmail dot com)


This software is a modification of Khoja algorithm for Arabic rooting. The original implementation is available at http://zeus.cs.pacificu.edu/shereen/ArabicStemmerCode.zip

The algorithm is described in:

- Khoja S., Garside R., "Stemming Arabic text", Computer Science Department, Lancaster University, Lancaster, UK, 1999.

- http://zeus.cs.pacificu.edu/shereen/research.htm#stemming

*/


import khoja.ArabicStemmerKhoja;
import myfileutil.myFileHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
		// check command-line argument
		if(args.length == 0) {
			System.out.println("Usage: java main <input file> <output file>");
			System.exit(0);
		}

		// file handler for I/O
		myfileutil.myFileHandler fr = new myFileHandler();

		// load stopwords
		String stopwords = fr.readEntilerFile("stopwords.txt");
		List<String> stopwordsList = Arrays.asList(stopwords.split("\n"));
		
		ArabicStemmerKhoja mystemmer = new ArabicStemmerKhoja(); 
				
		String fin = args[0];	// input file 
		String fout = args[1];	// output file 
				
		String s = fr.readEntilerFile(fin);	// read input file
		
		String[] lines = s.split("\n");
		StringBuilder sbuf = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
		    String[] tokens = lines[i].split("\\s");
		    for (int j = 0; j < tokens.length; j++) {
		        String t = tokens[j];
			if (! stopwordsList.contains(t)) {	// ignore stopwords 
				String resut = mystemmer.stem(t);	// Khoja rooting algorithm 
				sbuf.append(resut).append(" ");
			}

		    }
		    sbuf.append("\n");
		}
		
		fr.writeFileUTF(sbuf.toString(),fout);	// write results to the output file

	}

}

