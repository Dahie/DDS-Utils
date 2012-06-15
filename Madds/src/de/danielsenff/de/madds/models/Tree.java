package de.danielsenff.de.madds.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple tree class
 * @author root
 *
 * @param <T>
 */
public class Tree<T>
{
	private Map<T, T> parents;
	private Map<T, List<T>> children;
	
	private T root;
	private T previous;
	
	/**
	 * Creates a new empty tree
	 */
	public Tree()
	{
		this.children 	= new HashMap<>();
		this.parents	= new HashMap<>();
	}
	
	/**
	 * Inserts a node into this tree
	 * 
	 * @param t
	 */
	public void insertNode(final T t) {
		if(root == null) 
		{
			this.root = t;
			this.previous = this.root;
			parents.put(this.root, null);
			children.put(this.root, new ArrayList<T>());
		}
		else 
		{
			List<T> childnodes = children.get(this.previous);
			if(childnodes == null) {
				childnodes = new ArrayList<>();
			}
			if(!childnodes.contains(t))
			{
				childnodes.add(t);
			}
			
			this.parents.put(t, previous);
		} 
	}
	
	/**
	 * Inserts a node right after at
	 * @param t
	 * @param at
	 */
	public void insertNodeAt(final T t, final T at) {
		this.previous = at;
		insertNode(t);
	}
	
	/**
	 * Returns the successors for an element or an empty list
	 * @param t
	 * @return
	 */
	public List<T> getSuccessors(final T t) {
		return children.get(t) != null ? children.get(t) : new ArrayList<T>();
	}
	
	/**
	 * Returns the parent for an element or null if element is root
	 * @param t
	 * @return
	 */
	public T getParent(final T t) {
		return parents.get(t);
	}
	
	/**
	 * Returns the root node of the tree
	 * @return
	 */
	public T getRoot()
	{
		return root;
	}
	
	
}
