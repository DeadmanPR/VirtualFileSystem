package treeClasses;

import java.util.Iterator;

import treeInterfaces.Position;
import treeInterfaces.Tree;

public abstract class AbstractTree<E> implements Tree<E> {

	@Override
	public boolean isInternal(Position<E> p) throws IllegalArgumentException {
		return this.numChildren(p) > 0;
	}

	@Override
	public boolean isExternal(Position<E> p) throws IllegalArgumentException {
		return this.numChildren(p) == 0;
	}

	@Override
	public boolean isRoot(Position<E> p) throws IllegalArgumentException {
		return this.parent(p) == null; 
	}

	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	
	///////////////////////////////////////////////////////////////////////
	// The following are miscellaneous methods to display the content of 
	// the tree ....
	//////////////////////////////////////////////////////////////////////
	public void display() { 
		final int MAXHEIGHT = 100; 
		int[] control = new int[MAXHEIGHT]; 
		control[0]=0; 
		if (!this.isEmpty())
			recDisplay(this.root(), control, 0); 
		else 
			System.out.println("Tree is empty."); 
	}

	/**
	 * 
	 * @param t
	 * @param root
	 * @param control
	 * @param level
	 */
	private void recDisplay(Position<E> root, 
			int[] control, int level) 
	{
		printPrefix(level, control); 
		System.out.println(); 
		printPrefix(level, control); 
		System.out.println("__("+root.getElement()+")");
		control[level]--; 
		int nc = this.numChildren(root); 
		control[level+1] = nc; 
		for (Position<E>  p : this.children(root)) {
			recDisplay(p, control, level+1);  
		}
	}

	private static void printPrefix(int level, int[] control) { 
		for (int i=0; i<=level; i++)
			if (control[i] <= 0)
				System.out.print("    ");
			else 
				System.out.print("   |");
	}

}
