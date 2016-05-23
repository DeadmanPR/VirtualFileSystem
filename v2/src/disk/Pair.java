package disk;

import java.io.Serializable;

/**
 * Represents a pair of objects.
 * @author Jose Antonio Rodriguez Rivera
 */
public class Pair<K, V> implements Serializable{
	private K a;
	private V b;
	
	/**
	 * Creates a Pair of objects.
	 * @param first the first object
	 * @param second the second object
	 */
	public Pair(K first, V second){
		a = first;
		b = second;
	}
	
	/**
	 * Returns the first object of the Pair.
	 * @return the first object
	 */
	public K getFirst(){
		return a;
	}
	
	/**
	 * Returns the second object of the Pair.
	 * @return the second object
	 */
	public V getSecond(){
		return b;
	}

}
