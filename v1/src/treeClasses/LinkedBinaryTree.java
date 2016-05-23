package treeClasses;

import java.util.Iterator;

import treeInterfaces.Position;

public class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {

	private TNode<E> root; 
	int size; 

	public LinkedBinaryTree() { 
		root = null; 
		size = 0; 
	}
	
	private TNode<E> validate(Position<E> p) throws IllegalArgumentException { 
		if (!(p instanceof TNode<?>)) 
			throw new IllegalArgumentException("Position is not of righ data type."); 
		
		TNode<E> ptn = (TNode<E>) p; 
		if (ptn.getParent() == p) 
			throw new IllegalArgumentException("Invalid position --- not a tree position."); 
		
		return ptn; 
	}
	
	@Override
	public Position<E> left(Position<E> p) throws IllegalArgumentException {
		TNode<E> ptn = validate(p); 
		return ptn.getLeft();
	}

	@Override
	public Position<E> right(Position<E> p) throws IllegalArgumentException {
		TNode<E> ptn = validate(p); 
		return ptn.getRight();
	}

	@Override
	public Position<E> root() {
		return root;
	}

	@Override
	public Position<E> parent(Position<E> p) throws IllegalArgumentException {
		TNode<E> ptn = validate(p); 
		return ptn.getParent();
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

	private static class TNode<E> implements Position<E> { 
		private E element; 
		private TNode<E> parent, left, right; 
		public TNode() {}
		public TNode(E element, TNode<E> parent, TNode<E> left, TNode<E> right) { 
			this.element = element; 
			this.parent = parent; 
			this.left = left; 
			this.right = right; 
		}
		public E getElement() { 
			return element; 
		}
		public TNode<E> getParent() {
			return parent;
		}
		public void setParent(TNode<E> parent) {
			this.parent = parent;
		}
		public TNode<E> getLeft() {
			return left;
		}
		public void setLeft(TNode<E> left) {
			this.left = left;
		}
		public TNode<E> getRight() {
			return right;
		}
		public void setRight(TNode<E> right) {
			this.right = right;
		}
		public void setElement(E element) {
			this.element = element;
		}
		
	}
}
