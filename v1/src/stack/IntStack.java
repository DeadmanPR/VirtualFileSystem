package stack;

import exceptions.EmptyStackException;
import exceptions.FullStackException;

/**
 * Represents a stack of integers.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class IntStack {
	private static final int DS = 10;    // stack's default capacity
	private int[] element;               // the stack's content
	private int top; 
	
	/**
	 * Creates a new stack with the default capacity.
	 */
	public IntStack() { 
		element = new int[DS]; 
		top = -1; 
	}
	
	/**
	 * Creates a new stack with the given capacity.
	 * @param s the capacity of the stack
	 */
	public IntStack(int s) { 
		if (s<=0) s = DS; 
		element = new int[s]; 
		top = -1; 
	}
	
	/**
	 * Returns if the stack is empty.
	 * @return true if the stack is empty, false otherwise
	 */
	public boolean isEmpty() { 
		return top == -1; 
	}
	
	/**
	 * Returns the size of the stack.
	 * @return the size of the stack
	 */
	public int size() { 
		return top+1; 
	}
	
	/**
	 * Removes the element at the top of the stack and returns it.
	 * @return the element at the top of the stack
	 * @throws EmptyStackException if the stack is empty
	 */
	public int pop() throws EmptyStackException {
		if (isEmpty()) 
			throw new EmptyStackException(); 
		return element[top--]; 
	}
	
	/**
	 * Adds a new element at the top of the stack.
	 * @param n the element to add
	 * @throws FullStackException if the stack is full
	 */
	public void push(int n) throws FullStackException { 
		if (top == element.length - 1) 
			throw new FullStackException("Full stack in push..."); 
		else 
			element[++top] = n; 
	}
	
	/**
	 * Returns the element at the top of the stack (without removing it).
	 * @return the element at the top of the stack
	 * @throws EmptyStackException if the stack is empty
	 */
	public int top() throws EmptyStackException { 
		if (isEmpty())
			throw new EmptyStackException(); 
		return element[top]; 
	}

}
