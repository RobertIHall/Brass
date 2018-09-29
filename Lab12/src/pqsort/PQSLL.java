package pqsort;

import java.util.Comparator;

class PQSLL<E> implements PQInterface<E>
{
	private SortedListLinked<E> sll;
	
	public PQSLL(Comparator<E> comp)
	{
		sll = new SortedListLinked<E>(comp);
	}
	
	public void pqInsert(E item)
	{
		sll.add(item);
	}
	
	public E pqRemove() throws PQException
	{
		E item = sll.remove();
		if (item == null)
		{
			throw new PQException("Priority queue is empty.");
		}
		
		return item;
	}
	
	public boolean pqIsEmpty()
	{
		return (sll.size() == 0);
	}
	
}
