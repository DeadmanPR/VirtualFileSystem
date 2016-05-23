package treeClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import treeInterfaces.Position;

/**
 * A tree.
 * @author Jose Antonio Rodriguez Rivera
 */
public class MyTree<E extends Serializable> extends AbstractTree<E>  implements Serializable{
	private static final long serialVersionUID = 1L;
	private Node<E> root; 
	private int size; 
	
	/**
	 * Creates a new tree.
	 */
	public MyTree() { 
		root = null; 
		size = 0; 
	}
	
	/**
	 * Validates the position.
	 * @param p the position to validate
	 * @return the Node representing the give position
	 * @throws IllegalArgumentException if the position is not valid
	 */
	private Node<E> validate(Position<E> p) throws IllegalArgumentException { 
		if (!(p instanceof Node<?>)) 
			throw new IllegalArgumentException("Invalid position type for this implementation."); 
		Node<E> np = (Node<E>) p; 

		
		return np; 
	}
	
	@Override
	public Position<E> root() {
		// TODO Auto-generated method stub
		return root;
	}

	@Override
	public Position<E> parent(Position<E> p) throws IllegalArgumentException {
		Node<E> np = this.validate(p); 
		return np.getParent(); 
	}

	@Override
	public Iterable<Position<E>> children(Position<E> p)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Node<E> np = this.validate(p);
		ArrayList<Position<E>> result = 
				new ArrayList<Position<E>>(); 
		if (np.getChildren() != null) 
			for(Position<E> cp : np.getChildren())
				result.add(cp); 
		return result; 
	}

	@Override
	public int numChildren(Position<E> p) throws IllegalArgumentException {
		Node<E> np = validate(p);  
		if (np.getChildren() != null) 
			return np.getChildren().size();
		else 
			return 0; 
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Position<E>> positions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * Adds a root to the tree.
	 * @param element the element to be inserted as the root
	 * @return the Position of the element added
	 * @throws IllegalStateException if the tree is not empty
	 */
	public Position<E> addRoot(E element) throws IllegalStateException { 
		if (this.root != null) 
			throw new IllegalStateException("Tree must be empty to add a root."); 
		root = new Node<>(element, null, null); 
		root.setParent(root);
		size++; 
		return root; 
	}

	/**
	 * Add a new element as a child to a given position in the tree
	 * @param p the position to be the parent of the new element position
	 * @param element the new element to add to the tree
	 * @return the Position<E> of where the new element is stored
	 * @throws IllegalStateException if the position is not valid.....
	 */
	public Position<E> addChild(Position<E> p, E element) throws IllegalStateException { 
		Node<E> np = validate(p);  
		Node<E> nuevo = new Node<>(element, np, null); 
		if (np.getChildren() == null)
			np.setChildren(new ArrayList<Node<E>>());
		np.getChildren().add(nuevo); 
		size++; 
		return nuevo; 
	}
	
	/**
	 * Removes a given Position and its subtree from the tree.
	 * @param p the Position to remove (the root of the subtree to remove)
	 * @return the Position removed
	 */
	public Position<E> remove (Position<E> p){
		Node<E> np = validate(p);
		Node<E> parent = (Node<E>) parent(np);
		parent.setChildren(null);
		
		return np;
	}
	
	/**
	 * Inner class representing nodes.
	 * @author pedroirivera-vega
	 *
	 * @param <E>
	 */
	private static class Node<E> implements Position<E>, Serializable {

		private E element; 
		private Node<E> parent; 
		private ArrayList<Node<E>> children; 
		
		public Node(E element, Node<E> parent, ArrayList<Node<E>> c) { 
			this.element = element; 
			this.parent = parent; 
			this.children = c; 
		}
		
		public Node(E element) { 
			this.element = element; 
			this.parent = this; 
			this.children = null; 
		}
		
		public void clear() { 
			this.element = null; 
			this.parent = this; 
			this.children = null; 
		}
		
		public Position<E> getParent() {
			return parent;
		}

		public void setParent(Node<E> parent) {
			this.parent = parent;
		}

		public ArrayList<Node<E>> getChildren() {
			return children;
		}

		public void setChildren(ArrayList<Node<E>> children) {
			this.children = children;
		}

		@Override
		public E getElement() {
			return element;
		} 
		
		public void setElement(E e) { 
			element = e; 
		}		
		
	}
}
