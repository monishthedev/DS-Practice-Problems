package lse;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {

	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;

	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 *
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile)
			throws FileNotFoundException {
		HashMap<String,Occurrence> temporary = new HashMap<String,Occurrence>(1000);
		Scanner scanMach= new Scanner(new File(docFile));
		while (scanMach.hasNext()) {
			String nextScan = scanMach.next();
			String scanInput = getKeyword(nextScan);
			Occurrence newNew = new Occurrence(docFile, 1);
			if (scanInput == null) {
				continue;
			}
			else {
				if (temporary.containsKey(scanInput)) {
					temporary.get(scanInput).frequency++;
				}
				else {
					temporary.put(scanInput, newNew);
				}
			}
		}
		scanMach.close();
		return temporary;
	}

	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table.
	 * This is done by calling the insertLastOccurrence method.
	 *
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		for(String setter: kws.keySet()){
			Occurrence thingo=kws.get(setter);
			if(!keywordsIndex.containsKey(setter)){
				ArrayList<Occurrence> list= new ArrayList<Occurrence>();
				list.add(thingo);
				keywordsIndex.put(setter, list);}
			else{
				keywordsIndex.get(setter).add(thingo);
				insertLastOccurrence(keywordsIndex.get(setter));}
		}
	}

	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 *
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 *
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 *
	 * See assignment description for examples
	 *
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		int size = word.length();
		boolean letters = true;
		int lastLet = size;
		for(int i = 0; i < size; i++) {
			if (letters) {
				if (Character.isAlphabetic(word.charAt(i))) {
					continue;
				}
				else {
					letters = false;
					lastLet = i;
				}
			}
			else {
				if (Character.isAlphabetic(word.charAt(i))) {
					return null;
				}
				else {
					continue;
				}
			}
		}
		String substr = word.substring(0,lastLet).toLowerCase();
		if (noiseWords.contains(substr)) {
			return null;
		}
		return  substr;

	}

	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 *
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		occs.trimToSize();
		Occurrence actual = occs.get(occs.size() - 1);

		int actFreq = actual.frequency;
		occs.remove(occs.size() - 1);
		ArrayList<Integer> finale = new ArrayList<Integer>(1);
		int first = 0;
		int last = occs.size()-1;
		while(first <= last) {
			int centerB = (first + last)/2;
			finale.add(centerB); 

			if(first == last) { 
				if (actFreq == occs.get(centerB).frequency) {
					occs.add(centerB, actual);
					break;	
				}
				else if (actFreq > occs.get(centerB).frequency) {
					occs.add(centerB, actual);
					break;
				}
				else if (actFreq < occs.get(centerB).frequency) {
					occs.add(centerB + 1, actual);
					break;
				}
			}

			else if(actFreq == occs.get(centerB).frequency) {
				occs.add(centerB, actual);
				break;
			}

			else if (actFreq > occs.get(centerB).frequency) {
				last = centerB;
	
			}

			else if (actFreq < occs.get(centerB).frequency) {
				first = centerB + 1;
			
			}
		}
		return finale;
	}

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 *
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile)
			throws FileNotFoundException {
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}

	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies.
	 *
	 * Note that a matching document will only appear once in the result.
	 *
	 * Ties in frequency values are broken in favor of the first keyword.
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same
	 * frequency f1, then doc1 will take precedence over doc2 in the result.
	 *
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 *
	 * See assignment description for examples
	 *
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches,
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		ArrayList<Occurrence> kw1Items = keywordsIndex.get(kw1);
		ArrayList<Occurrence> kw2Items = keywordsIndex.get(kw2);
		ArrayList<String> finals = new ArrayList<String>(5);
		if(kw1Items == null && kw2Items == null) {
			return finals;
		}
		int firstInd = 0;
		int secondInd = 0;
		int counter = 0;
		if (kw1Items == null && kw2Items != null) {
			kw2Items.trimToSize();
			while (secondInd < kw2Items.size() && counter < 5) {

				if (finals.isEmpty()) {
					finals.add(kw2Items.get(secondInd).document);
					counter++;
					secondInd++;
				}
				else if(finals.contains(kw2Items.get(secondInd).document)) {
					secondInd++;
					continue;
				}
				else {
					finals.add(kw2Items.get(secondInd).document);
					counter++;
					secondInd++;
				}
			}
		}

		if (kw2Items == null && kw1Items != null) {
			kw1Items.trimToSize();
			while (firstInd < kw1Items.size() && counter < 5) {
				if (finals.isEmpty()) {
					finals.add(kw1Items.get(firstInd).document);
					counter++;
					firstInd++;
				}
				else if(finals.contains(kw1Items.get(firstInd).document)) {
					firstInd++;
					continue;
				}
				else {
					finals.add(kw1Items.get(firstInd).document);
					counter++;
					firstInd++;
				}
			}
		}
		if(kw2Items != null && kw1Items != null) {
			kw1Items.trimToSize();
			kw2Items.trimToSize();
			while (counter < 5) {
				if (firstInd >= kw1Items.size() && secondInd >= kw2Items.size()) {
					break;
				}
				else if (firstInd < kw1Items.size() && secondInd >= kw2Items.size()) {
					if (finals.isEmpty()) {
						finals.add(kw1Items.get(firstInd).document);
						counter++;
						firstInd++;
					}
					else if(finals.contains(kw1Items.get(firstInd).document)) {
						firstInd++;
						continue;
					}
					else {
						finals.add(kw1Items.get(firstInd).document);
						counter++;
						firstInd++;
					}
				}
				else if (firstInd >= kw1Items.size() && secondInd < kw2Items.size()) {
					if (finals.isEmpty()) {
						finals.add(kw2Items.get(secondInd).document);
						counter++;
						secondInd++;
					}
					else if(finals.contains(kw2Items.get(secondInd).document)) {
						secondInd++;
						continue;
					}
					else {
						finals.add(kw2Items.get(secondInd).document);
						counter++;
						secondInd++;
					}
				}
				else if (firstInd < kw1Items.size() && secondInd < kw2Items.size()) {
					if (kw1Items.get(firstInd).frequency >= kw2Items.get(secondInd).frequency) {
						if (finals.isEmpty()) {
							finals.add(kw1Items.get(firstInd).document);
							counter++;
							firstInd++;
						}
						else if(finals.contains(kw1Items.get(firstInd).document)) {
							firstInd++;
							continue;
						}
						else {
							finals.add(kw1Items.get(firstInd).document);
							counter++;
							firstInd++;
						}
					}
				
					else {
						if (finals.isEmpty()) {
							finals.add(kw2Items.get(secondInd).document);
							counter++;
							secondInd++;
						}
						else if(finals.contains(kw2Items.get(secondInd).document)) {
							secondInd++;
							continue;
						}
						else {
							finals.add(kw2Items.get(secondInd).document);
							counter++;
							secondInd++;
						}
					}
					
				}
				else {
					System.out.println("there was an error somewhere");
					break;
				}
			}
		}
		return finals;

	}




}

