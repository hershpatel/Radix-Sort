package apps;

import java.io.IOException;
import java.util.Scanner;
import structures.Node;

/**
 * This class sorts a given list of strings which represent numbers in
 * the given radix system. For instance, radix=10 means decimal numbers;
 * radix=16 means hexadecimal numbers. 
 */
public class Radixsort {

	/**
	 * Master list that holds all items, starting with input, and updated after every pass
	 * of the radixsort algorithm. Holds sorted result after the final pass. This is a
	 * circular linked list in which every item is stored in its textual string form (even
	 * though the items represent numbers). This masterListRear field points to the last 
	 * node in the CLL.
	 */
	Node<String> masterListRear;
	
	/**
	 * Array of linked lists that holds the digit-wise distribution of the items during
	 * each pass of the radixsort algorithm. 
	 */
	Node<String>[] buckets;
	
	/** 
	 * The sort radix, defaults to 10.
	 */
	int radix=10;
	
	/**
	 * Initializes this object with the given radix (10 or 16)
	 * 
	 * @param radix
	 */
	public Radixsort() {
		masterListRear = null;
		buckets = null;
	}
	
	public Node<String> sort(Scanner sc) 
	throws IOException {
		// first line is radix
		if (!sc.hasNext()) return null;
		
		// read radix from file, and set up buckets for linked lists
		radix = sc.nextInt();
		buckets = (Node<String>[])new Node[radix];
		
		// create master list from input
		createMasterListFromInput(sc);
		
		// find the string with the maximum length
		int maxDigits = getMaxDigits();
		
		for (int i=0; i < maxDigits; i++) {
			scatter(i);
			gather();
		}
	
		return masterListRear;
	}
	
	/**
	 * Reads entries to be sorted from input file and stores them as 
	 * strings in the master CLL (pointed by the instance field masterListRear, 
	 * in the order in which they are read. In other words, the first entry in the linked 
	 * list is the first entry in the input, the second entry in the linked list is the 
	 * second entry in the input, and so on. 
	 */
	public void createMasterListFromInput(Scanner sc) 
	throws IOException {
		while(sc.hasNext()) {
			String a = sc.next();
			if (masterListRear == null) { 
				masterListRear = new Node<String> (a,null);
				masterListRear.next=masterListRear;
			}
			else { 
				masterListRear.next= new Node<String> (a,masterListRear.next);
				masterListRear=masterListRear.next;
			}
		}
	}
	
	/**
	 * Determines the maximum number of digits over all the entries in the master list
	 */
	public int getMaxDigits() {
		int maxDigits = masterListRear.data.length();
		Node<String> ptr = masterListRear.next;
		while (ptr != masterListRear) {
			int length = ptr.data.length();
			if (length > maxDigits) 
				maxDigits = length;
			ptr = ptr.next;
		}
		return maxDigits;
	}
	
	/**
	 * Scatters entries of master list (referenced by instance field masterListReat) 
	 * to buckets for a given pass.
	 * 
	 * Passes are digit by digit, starting with the rightmost digit -
	 * the rightmost digit is the "0-th", i.e. pass=0 for rightmost digit, pass=1 for 
	 * second to rightmost, and so on. 
	 * 
	 * Each digit is extracted as a character, 
	 * then converted into the appropriate numeric value in the given radix
	 * using the java.lang.Character.digit(char ch, int radix) method
	 */
	public void scatter(int pass) {
		Node<String> ptr = null;
		do {
			ptr = masterListRear.next;
			masterListRear.next=ptr.next;
			int digit=0;
			int a = ptr.data.length()-1-pass;
			if (a<0) digit=0;
			else digit = Character.digit(ptr.data.charAt(a), radix);
			if (buckets[digit] == null) { 
				buckets[digit] = ptr;
				buckets[digit].next=ptr;
			}
			else { 
				ptr.next=buckets[digit].next;
				buckets[digit].next=ptr;
				buckets[digit]=ptr;
			} 
		} while (ptr != masterListRear);
		masterListRear=null;
	}

	/**
	 * Gathers all the CLLs in all the buckets into the master list, referenced
	 * by the instance field masterListRear
	 */
	public void gather() {
		for (int i=0; i<buckets.length; i++) { 
			if(buckets[i]!=null) {
				if (masterListRear==null)
					masterListRear=buckets[i];
				else { 
					Node<String> ptr = masterListRear.next;
					masterListRear.next=buckets[i].next;
					buckets[i].next=ptr;
					masterListRear=buckets[i];
				}
			}
			buckets[i]=null;
		}
	}		
}

