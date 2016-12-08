package cmd;/*

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class Main {

	/**
	 *
	 * @param args
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException, IOException, URISyntaxException {
		// check command-line argument
		if(args.length == 0) {
			System.out.println("Usage: java cmd.Main <input file> <output file>");
			System.exit(0);
		}

		// file handler for I/O
		// load stopwords
		String stopwords = new Scanner(Paths.get("stopwords.txt"), "UTF-8").useDelimiter("\\A").next();

		List<String> stopwordsList = Arrays.asList(stopwords.split("\n"));
		
		ArabicStemmerKhoja mystemmer = new ArabicStemmerKhoja(); 
				
		String fin = args[0];	// input file
		String fout = args[1];	// output file

		//String fin = "test_files/test-in.txt";
		//String fout = "test_files/out.txt";

		Scanner input; Formatter output;
		try{
			input = new Scanner(Paths.get(fin),"utf-8");
			output = new Formatter(fout, "utf-8");
			while (input.hasNextLine()) {
				String line = input.nextLine();
				String lineout = new String();
				String[] tokens = line.split("\\s");
				for (String token : tokens) {
					if (!stopwordsList.contains(token)) {    // ignore stopwords
						String result = mystemmer.stem(token);    // Khoja rooting algorithm
						lineout += result + " ";
					}
				}
				output.format("%s\n", lineout);
			}
			input.close();
			output.close();
		}
		catch (IOException ioException){
			System.err.println(ioException.getMessage());
			System.exit(-1);
		} catch(NoSuchElementException excp) {
			System.err.println(excp.getMessage());
		} catch (FormatterClosedException excp){
			System.err.println(excp.getMessage());
		} catch (IllegalStateException excp){
			System.err.println(excp.getMessage());
		}


	} // end main

} // end class

