/*

Version 1.0

Author: Motaz Saad (motaz dot saad at gmail dot com)


This software is a modification of Khoja algorithm for Arabic rooting. The original implementation is available at http://zeus.cs.pacificu.edu/shereen/ArabicStemmerCode.zip

Khoja algorithm reduces words to their roots. The algorithm is described in:

Khoja S., Garside R., "Stemming Arabic text", Computer Science Department, Lancaster University, Lancaster, UK, 1999.

http://zeus.cs.pacificu.edu/shereen/research.htm#stemming

 


*/






public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArabicStemmerKhoja mystemmer = new ArabicStemmerKhoja(); 
		
		String sentence = "السلام عليكم قام المسافرون بالسفر على الطائرة";
		String[] words = sentence.split(" ");
		for (String word : words) {
			System.out.println(mystemmer.stem(word));
		}
		
				
		

	}

}

