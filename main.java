
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
