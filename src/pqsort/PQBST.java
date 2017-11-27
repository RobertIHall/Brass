package pqsort;

import java.util.Comparator;

class PQBST<E> implements PQInterface<E>
{
	private BinarySearchTree<E> bst;
	
	public PQBST(Comparator<E> comp)
	{
		bst = new BinarySearchTree<E>(comp);
	}
	
	public void pqInsert(E item)
	{
		bst.insert(item);
	}
	
	public E pqRemove() throws PQException
	{
		E item = bst.remove();
		
		if (item == null)
		{
			throw new PQException("There are no items in the priority queue.");
		}
		
		return item;
	}
	
	public boolean pqIsEmpty()
	{
		return (bst.size() == 0);
	}
}
