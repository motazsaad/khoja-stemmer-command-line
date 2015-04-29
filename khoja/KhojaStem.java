
package khoja;

/*

Arabic Stemmer: This program stems Arabic words and returns their root.


This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

This code is adopted from Arabic Stemmer by Shereen Khoja
by Motaz K. Saad (motaz.saad@gmail.com)
*/

import java.util.Vector;

/**
 * The Arabic stemmer utility class for Arabic texts.
 *
 * @author Motaz K. Saad (motaz.saad@gmai.com)
 */






public class KhojaStem
{
    //--------------------------------------------------------------------------

    // the files containing prefixes, suffixes and so on
    private Vector<Vector<String>> staticFiles;




    //--------------------------------------------------------------------------

    // constructor
    public KhojaStem ( /*File fileToBeStemmed,*/ Vector<Vector<String>> statFiles )
    {

        // clone the static files
        staticFiles = (Vector<Vector<String>>) statFiles.clone ( );

    }




    // format the word by removing any punctuation, diacritics and non-letter charracters
    public String formatWord ( String currentWord )
    {

        // have the root, pattern, stopword or strange words been found
        boolean rootFound = false;
        boolean patternFound = false;
        boolean stopwordFound = false;
        boolean strangeWordFound = false;
        boolean rootNotFound = false;
        boolean fromSuffixes = false;

        boolean[] flags = {rootFound, patternFound, stopwordFound, strangeWordFound, rootNotFound, fromSuffixes};


        StringBuffer modifiedWord = new StringBuffer ( );

        // remove any diacritics (short vowels)
        removeDiacritics( currentWord, modifiedWord );


        // remove any punctuation from the word
         removePunctuation( currentWord, modifiedWord );

        // there could also be characters that aren't letters which should be removed
        removeNonLetter ( currentWord, modifiedWord );

        // check for stopwords
        if( !checkStrangeWords ( currentWord, flags ) )
            // check for stopwords
            if( !checkStopwords ( currentWord, flags ) )
                currentWord = stemWord ( currentWord, flags );

        return currentWord;
    }

    //--------------------------------------------------------------------------

    // stem the word
    private String stemWord ( String word, boolean[] flags )
    {
        // check if the word consists of two letters
        // and find it's root
        if ( word.length ( ) == 2 )
            word = isTwoLetters ( word, flags );

        // if the word consists of three letters
        //if( word.length ( ) == 3 && !rootFound )
        if( word.length ( ) == 3 && !flags[0] )
            // check if it's a root
            word = isThreeLetters ( word, flags );

        // if the word consists of four letters
        if( word.length ( ) == 4 )
            // check if it's a root
            isFourLetters ( word, flags );

        // if the root hasn't yet been found
        if( !flags[0] )
        {
            // check if the word is a pattern
            word = checkPatterns ( word, flags );
        }

        // if the root still hasn't been found
        if ( !flags[0] )
        {
            // check for a definite article, and remove it
            word = checkDefiniteArticle ( word, flags );
        }

        // if the root still hasn't been found
        if ( !flags[0] && !flags[2] )
        {
            // check for the prefix waw
            word = checkPrefixWaw ( word, flags );
        }

        // if the root STILL hasnt' been found
        if ( !flags[0] && !flags[2] )
        {
            // check for suffixes
            word = checkForSuffixes ( word, flags );
        }

        // if the root STILL hasn't been found
        if ( !flags[0] && !flags[2] )
        {
            // check for prefixes
            word = checkForPrefixes ( word, flags );
        }
        return word;
    }


    //--------------------------------------------------------------------------

    // check and remove any prefixes
    private String checkForPrefixes ( String word, boolean[] flags )
    {
        String prefix = "";
        String modifiedWord = word;
        Vector<String> prefixes = ( Vector<String> ) staticFiles.elementAt ( 10 );

        // for every prefix in the list
        for ( int i = 0; i < prefixes.size ( ); i++ )
        {
            prefix = ( String ) prefixes.elementAt ( i );
            // if the prefix was found
            if ( prefix.regionMatches ( 0, modifiedWord, 0, prefix.length ( ) ) )
            {
                modifiedWord = modifiedWord.substring ( prefix.length ( ) );

                // check to see if the word is a stopword
                if ( checkStopwords( modifiedWord, flags ) )
                    return modifiedWord;

                // check to see if the word is a root of three or four letters
                // if the word has only two letters, test to see if one was removed
                if ( modifiedWord.length ( ) == 2 )
                    modifiedWord = isTwoLetters ( modifiedWord, flags );
                else if ( modifiedWord.length ( ) == 3 && !flags[0] )
                    modifiedWord = isThreeLetters ( modifiedWord, flags );
                else if ( modifiedWord.length ( ) == 4 )
                    isFourLetters ( modifiedWord, flags );

                // if the root hasn't been found, check for patterns
                if ( !flags[0] && modifiedWord.length ( ) > 2 )
                    modifiedWord = checkPatterns ( modifiedWord, flags );

                // if the root STILL hasn't been found
                if ( !flags[0] && !flags[2] && !flags[5])
                {
                    // check for suffixes
                    modifiedWord = checkForSuffixes ( modifiedWord, flags );
                }

                if ( flags[2] )
                    return modifiedWord;

                // if the root was found, return the modified word
                if ( flags[0] && !flags[2] )
                {
                    return modifiedWord;
                }
            }
        }
        return word;
    }

    //--------------------------------------------------------------------------

    // METHOD CHECKFORSUFFIXES
    private String checkForSuffixes ( String word, boolean[] flags )
    {
        String suffix = "";
        String modifiedWord = word;
        Vector<String> suffixes = ( Vector<String> ) staticFiles.elementAt ( 14 );
        flags[5] = true;

        // for every suffix in the list
        for ( int i = 0; i < suffixes.size ( ); i++ )
        {
            suffix = ( String ) suffixes.elementAt ( i );

            // if the suffix was found
            if( suffix.regionMatches ( 0, modifiedWord, modifiedWord.length ( ) - suffix.length ( ), suffix.length ( ) ) )
            {
                modifiedWord = modifiedWord.substring ( 0, modifiedWord.length ( ) - suffix.length ( ) );

                // check to see if the word is a stopword
                if ( checkStopwords ( modifiedWord, flags ) )
                {
                	flags[5] = false;
                    return modifiedWord;
                }

                // check to see if the word is a root of three or four letters
                // if the word has only two letters, test to see if one was removed
                if ( modifiedWord.length ( ) == 2 )
                {
                    modifiedWord = isTwoLetters ( modifiedWord, flags );
                }
                else if ( modifiedWord.length ( ) == 3 )
                {
                    modifiedWord = isThreeLetters ( modifiedWord, flags );
                }
                else if ( modifiedWord.length ( ) == 4 )
                {
                    isFourLetters ( modifiedWord, flags );
                }

                // if the root hasn't been found, check for patterns
                if ( !flags[0] && modifiedWord.length( ) > 2 )
                {
                    modifiedWord = checkPatterns( modifiedWord, flags );
                }

                if ( flags[2] )
                {
                	flags[5] = false;
                    return modifiedWord;
                }

                // if the root was found, return the modified word
                if ( flags[0] )
                {
                	flags[5] = false;
                    return modifiedWord;
                }
            }
        }
        flags[5] = false;
        return word;
    }

    //--------------------------------------------------------------------------

    // check and remove the special prefix (waw)
    private String checkPrefixWaw ( String word, boolean[] flags )
    {
        String modifiedWord = "";

        if ( word.length ( ) > 3 && word.charAt ( 0 ) == '\u0648' )
        {
            modifiedWord = word.substring ( 1 );

            // check to see if the word is a stopword
            if ( checkStopwords ( modifiedWord, flags ) )
                return modifiedWord;

            // check to see if the word is a root of three or four letters
            // if the word has only two letters, test to see if one was removed
            if ( modifiedWord.length ( ) == 2 )
                modifiedWord = isTwoLetters( modifiedWord, flags );
            else if ( modifiedWord.length ( ) == 3 && !flags[0] )
                modifiedWord = isThreeLetters( modifiedWord, flags );
            else if ( modifiedWord.length ( ) == 4 )
                isFourLetters ( modifiedWord, flags );

            // if the root hasn't been found, check for patterns
            if ( !flags[0] && modifiedWord.length ( ) > 2 )
                modifiedWord = checkPatterns ( modifiedWord, flags );

            // if the root STILL hasnt' been found
            if ( !flags[0] && !flags[2] )
            {
                // check for suffixes
                modifiedWord = checkForSuffixes ( modifiedWord, flags );
            }

            // iIf the root STILL hasn't been found
            if ( !flags[0] && !flags[2] )
            {
                // check for prefixes
                modifiedWord = checkForPrefixes ( modifiedWord, flags );
            }

            if ( flags[2] )
                return modifiedWord;

            if ( flags[0] && !flags[2] )
            {
                return modifiedWord;
            }
        }
        return word;
    }

    //--------------------------------------------------------------------------

    // check and remove the definite article
    private String checkDefiniteArticle ( String word, boolean[] flags )
    {
        // looking through the vector of definite articles
        // search through each definite article, and try and
        // find a match
        String definiteArticle = "";
        String modifiedWord = "";
        Vector<String> definiteArticles = ( Vector<String> ) staticFiles.elementAt ( 0 );

        // for every definite article in the list
        for ( int i = 0; i < definiteArticles.size ( ); i++ )
        {
            definiteArticle = ( String ) definiteArticles.elementAt ( i );
            // if the definite article was found
            if ( definiteArticle.regionMatches ( 0, word, 0, definiteArticle.length ( ) ) )
            {
                // remove the definite article
                modifiedWord = word.substring ( definiteArticle.length ( ), word.length ( ) );

                // check to see if the word is a stopword
                if ( checkStopwords ( modifiedWord, flags ) )
                    return modifiedWord;

                // check to see if the word is a root of three or four letters
                // if the word has only two letters, test to see if one was removed
                if ( modifiedWord.length ( ) == 2 )
                    modifiedWord = isTwoLetters ( modifiedWord, flags );
                else if ( modifiedWord.length ( ) == 3 && !flags[0] )
                    modifiedWord = isThreeLetters ( modifiedWord, flags );
                else if ( modifiedWord.length ( ) == 4 )
                    isFourLetters ( modifiedWord, flags );

                // if the root hasn't been found, check for patterns
                if ( !flags[0] && modifiedWord.length ( ) > 2 )
                    modifiedWord = checkPatterns ( modifiedWord, flags );

                // if the root STILL hasnt' been found
                if ( !flags[0] && !flags[2] )
                {
                    // check for suffixes
                    modifiedWord = checkForSuffixes ( modifiedWord, flags );
                }

                // if the root STILL hasn't been found
                if ( !flags[0] && !flags[2] )
                {
                    // check for prefixes
                    modifiedWord = checkForPrefixes ( modifiedWord, flags );
                }


                if ( flags[2] )
                    return modifiedWord;


                // if the root was found, return the modified word
                if ( flags[0] && !flags[2] )
                {
                    return modifiedWord;
                }
            }
        }
        if ( modifiedWord.length ( ) > 3 )
            return modifiedWord;
        return word;
    }

    //--------------------------------------------------------------------------

    // if the word consists of two letters
    private String isTwoLetters ( String word, boolean[] flags )
    {
        // if the word consists of two letters, then this could be either
        // - because it is a root consisting of two letters (though I can't think of any!)
        // - because a letter was deleted as it is duplicated or a weak middle or last letter.

        word = duplicate ( word, flags );

        // check if the last letter was weak
        if ( !flags[0] )
            word = lastWeak ( word, flags );

        // check if the first letter was weak
        if ( !flags[0] )
            word = firstWeak ( word, flags );

        // check if the middle letter was weak
        if ( !flags[0] )
            word = middleWeak ( word, flags );

    return word;
    }

    //--------------------------------------------------------------------------

    // if the word consists of three letters
    private String isThreeLetters ( String word, boolean[] flags )
    {
        StringBuffer modifiedWord = new StringBuffer ( word );
        String root = "";
        // if the first letter is a 'ا', 'ؤ'  or '	ئ'
        // then change it to a 'أ'
        if ( word.length ( ) > 0 )
        {
            if ( word.charAt ( 0 ) == '\u0627' || word.charAt ( 0 ) == '\u0624' || word.charAt ( 0 ) == '\u0626' )
            {
                modifiedWord.setLength ( 0 );
                modifiedWord.append ( '\u0623' );
                modifiedWord.append ( word.substring ( 1 ) );
                root = modifiedWord.toString ( );
            }

            // if the last letter is a weak letter or a hamza
            // then remove it and check for last weak letters
            if ( word.charAt ( 2 ) == '\u0648' || word.charAt ( 2 ) == '\u064a' || word.charAt ( 2 ) == '\u0627' ||
                 word.charAt ( 2 ) == '\u0649' || word.charAt ( 2 ) == '\u0621' || word.charAt ( 2 ) == '\u0626' )
            {
                root = word.substring ( 0, 2 );
                root = lastWeak ( root, flags );
                if ( flags[0] )
                {
                    return root;
                }
            }

            // if the second letter is a weak letter or a hamza
            // then remove it
            if ( word.charAt ( 1 ) == '\u0648' || word.charAt ( 1 ) == '\u064a' || word.charAt ( 1 ) == '\u0627' || word.charAt ( 1 ) == '\u0626' )
            {
                root = word.substring ( 0, 1 );
                root = root + word.substring ( 2 );

                root = middleWeak ( root, flags );
                if ( flags[0] )
                {
                    return root;
                }
            }

            // if the second letter has a hamza, and it's not on a alif
            // then it must be returned to the alif
            if ( word.charAt ( 1 ) == '\u0624' || word.charAt ( 1 ) == '\u0626' )
            {
                if ( word.charAt ( 2 ) == '\u0645' || word.charAt ( 2 ) == '\u0632' || word.charAt ( 2 ) == '\u0631' )
                {
                    root = word.substring ( 0, 1 );
                    root = root + '\u0627';
                    root = root+ word.substring ( 2 );
                }
                else
                {
                    root = word.substring ( 0, 1 );
                    root = root + '\u0623';
                    root = root + word.substring ( 2 );
                }
            }

            // if the last letter is a shadda, remove it and
            // duplicate the last letter
            if ( word.charAt ( 2 ) == '\u0651')
            {
                root = word.substring ( 0, 1 );
                root = root + word.substring ( 1, 2 );
            }
        }

        // if word is a root, then rootFound is true
        if ( root.length ( ) == 0 )
        {
            if ( ( ( Vector<String> ) staticFiles.elementAt ( 16 ) ) .contains ( word ) )
            {
            	flags[0] = true;


                return word;
            }
        }
        // check for the root that we just derived
        else if ( ( ( Vector<String> ) staticFiles.elementAt ( 16 ) ) .contains ( root ) )
        {
        	flags[0] = true;

            return root;
        }


        return word;
    }

    //--------------------------------------------------------------------------

    // if the word has four letters
    private void isFourLetters ( String word, boolean[] flags )
    {
        // if word is a root, then rootFound is true
        if( ( ( Vector<String> ) staticFiles.elementAt ( 12 ) ) .contains ( word ) )
        {
            flags[0] = true;


        }
    }

    //--------------------------------------------------------------------------

    // check if the word matches any of the patterns
    private String checkPatterns ( String word, boolean[] flags )
    {
        StringBuffer root = new StringBuffer ( "" );
        // if the first letter is a hamza, change it to an alif
        if ( word.length ( ) > 0 )
            if ( word.charAt ( 0 ) == '\u0623' || word.charAt ( 0 ) == '\u0625' || word.charAt ( 0 ) == '\u0622' )
            {
                root.append ( "j" );
                root.setCharAt ( 0, '\u0627' );
                root.append ( word.substring ( 1 ) );
                word = root.toString ( );
            }

        // try and find a pattern that matches the word
        Vector<String> patterns = ( Vector<String> ) staticFiles.elementAt ( 15 );
        int numberSameLetters = 0;
        String pattern = "";
        String modifiedWord = "";

        // for every pattern
        for( int i = 0; i < patterns.size ( ); i++ )
        {
            pattern = ( String ) patterns.elementAt ( i );
            root.setLength ( 0 );
            // if the length of the words are the same
            if ( pattern.length ( ) == word.length ( ) )
            {
                numberSameLetters = 0;
                // find out how many letters are the same at the same index
                // so long as they're not a fa, ain, or lam
                for ( int j = 0; j < word.length ( ); j++ )
                    if ( pattern.charAt ( j ) == word.charAt ( j ) &&
                         pattern.charAt ( j ) != '\u0641'          &&
                         pattern.charAt ( j ) != '\u0639'          &&
                         pattern.charAt ( j ) != '\u0644'            )
                        numberSameLetters ++;

                // test to see if the word matches the pattern �����
                if ( word.length ( ) == 6 && word.charAt ( 3 ) == word.charAt ( 5 ) && numberSameLetters == 2 )
                {
                    root.append ( word.charAt ( 1 ) );
                    root.append ( word.charAt ( 2 ) );
                    root.append ( word.charAt ( 3 ) );
                    modifiedWord = root.toString ( );
                    modifiedWord = isThreeLetters ( modifiedWord, flags );
                    if ( flags[0] )
                        return modifiedWord;
                    else
                        root.setLength ( 0 );
                }


                // if the word matches the pattern, get the root
                if ( word.length ( ) - 3 <= numberSameLetters )
                {
                    // derive the root from the word by matching it with the pattern
                    for ( int j = 0; j < word.length ( ); j++ )
                        if ( pattern.charAt ( j ) == '\u0641' ||
                             pattern.charAt ( j ) == '\u0639' ||
                             pattern.charAt ( j ) == '\u0644'   )
                            root.append ( word.charAt ( j ) );

                    modifiedWord = root.toString ( );
                    modifiedWord = isThreeLetters ( modifiedWord, flags );

                    if ( flags[0] )
                    {
                        word = modifiedWord;
                        return word;
                    }
                }
            }
        }
        return word;
    }

    //--------------------------------------------------------------------------

    // remove non-letters from the word
    private boolean removeNonLetter ( String currentWord, StringBuffer modifiedWord )
    {
        boolean nonLetterFound = false;
        modifiedWord.setLength ( 0 );

        // if any of the word is not a letter then remove it
        for( int i = 0; i < currentWord.length ( ); i++ )
        {
            if ( Character.isLetter ( currentWord.charAt ( i ) ) )
            {
                modifiedWord.append ( currentWord.charAt ( i ) );

            }
            else
            {
                nonLetterFound = true;

            }
        }
        return nonLetterFound;
    }

    //--------------------------------------------------------------------------

    // handle duplicate letters in the word
    private String duplicate ( String word, boolean[] flags )
    {
        // check if a letter was duplicated
        if ( ( ( Vector<String> ) staticFiles.elementAt ( 1 ) ).contains ( word ) )
        {
            // if so, then return the deleted duplicate letter
            word = word + word.substring ( 1 );

            // root was found, so set variable
            flags[0] = true;



            return word;
        }
        return word;
    }

    //--------------------------------------------------------------------------

    // check if the last letter of the word is a weak letter
    private String lastWeak ( String word, boolean[] flags )
    {
        StringBuffer stemmedWord = new StringBuffer ( "" );
        // check if the last letter was an alif
        if ( ( ( Vector<String> )staticFiles.elementAt ( 4 ) ).contains ( word ) )
        {
            stemmedWord.append ( word );
            stemmedWord.append ( "\u0627" );
            word = stemmedWord.toString ( );
            stemmedWord.setLength ( 0 );

            // root was found, so set variable
            flags[0] = true;



            return word;
        }
        // check if the last letter was an hamza
        else if ( ( ( Vector<String> ) staticFiles.elementAt ( 5 ) ) .contains ( word ) )
        {
            stemmedWord.append ( word );
            stemmedWord.append ( "\u0623" );
            word = stemmedWord.toString ( );
            stemmedWord.setLength ( 0 );

            // root was found, so set variable
            flags[0] = true;



            return word;
        }
        // check if the last letter was an maksoura
        else if ( ( ( Vector<String> ) staticFiles.elementAt ( 6 ) ) .contains ( word ) )
        {
            stemmedWord.append ( word );
            stemmedWord.append ( "\u0649" );
            word = stemmedWord.toString ( );
            stemmedWord.setLength ( 0 );

            // root was found, so set variable
            flags[0] = true;



            return word;
        }
        // check if the last letter was an yah
        else if ( ( ( Vector<String> ) staticFiles.elementAt ( 7 ) ).contains ( word ) )
        {
            stemmedWord.append ( word );
            stemmedWord.append ( "\u064a" );
            word = stemmedWord.toString ( );
            stemmedWord.setLength ( 0 );

            // root was found, so set variable
            flags[0] = true;



            return word;
        }
        return word;
    }

    //--------------------------------------------------------------------------

    // check if the first letter is a weak letter
    private String firstWeak ( String word, boolean[] flags )
    {
        StringBuffer stemmedWord = new StringBuffer ( "" );
        // check if the firs letter was a waw
        if( ( ( Vector<String> ) staticFiles.elementAt ( 2 ) ) .contains ( word ) )
        {
            stemmedWord.append ( "\u0648" );
            stemmedWord.append ( word );
            word = stemmedWord.toString ( );
            stemmedWord.setLength ( 0 );

            // root was found, so set variable
            flags[0] = true;



            return word;
        }
        // check if the first letter was a yah
        else if ( ( ( Vector<String> ) staticFiles.elementAt ( 3 ) ) .contains ( word ) )
        {
            stemmedWord.append ( "\u064a" );
            stemmedWord.append ( word );
            word = stemmedWord.toString ( );
            stemmedWord.setLength ( 0 );

            // root was found, so set variable
            flags[0] = true;



            return word;
        }
    return word;
    }

    //--------------------------------------------------------------------------

    // check if the middle letter of the root is weak
    private String middleWeak ( String word, boolean[] flags )
    {
        StringBuffer stemmedWord = new StringBuffer ( "j" );
        // check if the middle letter is a waw
        if ( ( ( Vector<String> ) staticFiles.elementAt ( 8 ) ) .contains ( word ) )
        {
            // return the waw to the word
            stemmedWord.setCharAt ( 0, word.charAt ( 0 ) );
            stemmedWord.append ( "\u0648" );
            stemmedWord.append ( word.substring ( 1 ) );
            word = stemmedWord.toString ( );
            stemmedWord.setLength ( 0 );

            // root was found, so set variable
            flags[0] = true;



            return word;
        }
        // check if the middle letter is a yah
        else if ( ( ( Vector<String> ) staticFiles.elementAt ( 9 ) ) .contains ( word ) )
        {
            // return the waw to the word
            stemmedWord.setCharAt ( 0, word.charAt ( 0 ) );
            stemmedWord.append ( "\u064a" );
            stemmedWord.append ( word.substring ( 1 ) );
            word = stemmedWord.toString ( );
            stemmedWord.setLength ( 0 );

            // root was found, so set variable
            flags[0] = true;


            return word;
        }
        return word;
    }

    //--------------------------------------------------------------------------

    // remove punctuation from the word
    private boolean removePunctuation ( String currentWord, StringBuffer modifiedWord )
    {
        boolean punctuationFound = false;
        modifiedWord.setLength ( 0 );
        Vector<String> punctuations = ( Vector<String> ) staticFiles.elementAt ( 11 );

        // for every character in the current word, if it is a punctuation then do nothing
        // otherwise, copy this character to the modified word
        for ( int i = 0; i < currentWord.length ( ); i++ )
        {
            if ( ! ( punctuations.contains ( currentWord.substring ( i, i+1 ) ) ) )
            {
                modifiedWord.append ( currentWord.charAt ( i ) );

            }
            else
            {
                punctuationFound = true;

            }
        }

        return punctuationFound;
    }

    //--------------------------------------------------------------------------

    // remove diacritics from the word
    private boolean removeDiacritics ( String currentWord, StringBuffer modifiedWord )
    {

    	boolean diacriticFound = false;
        modifiedWord.setLength ( 0 );
        Vector<String> diacritics = ( Vector ) staticFiles.elementAt ( 17 );

        for ( int i = 0; i < currentWord.length ( ); i++ )
            // if the character is not a diacritic, append it to modified word
            if ( ! ( diacritics.contains ( currentWord.substring ( i, i+1 ) ) ) )
                modifiedWord.append ( currentWord.substring ( i, i+1 ) );
            else
            {
                diacriticFound = true;

            }
        return diacriticFound;
    }

    //--------------------------------------------------------------------------

    // check that the word is a stopword
    private boolean checkStopwords ( String currentWord, boolean[] flags )
    {
        Vector<String> v = ( Vector<String> ) staticFiles.elementAt ( 13 );
        //stopwordFound = v.contains ( currentWord );
        flags[2] = v.contains ( currentWord );


        return flags[2];
    }

    //--------------------------------------------------------------------------

    // check that the word is a strange word
    private boolean checkStrangeWords ( String currentWord, boolean[] flags )
    {
        Vector<String> v = ( Vector<String> ) staticFiles.elementAt ( 18 );
        //strangeWordFound = v.contains( currentWord );
        flags[3] = v.contains( currentWord );


        return flags[3];
    }



        //--------------------------------------------------------------------------


}
