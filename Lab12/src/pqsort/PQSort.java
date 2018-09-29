package pqsort;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
	Sorts by inserting all of the items into a min priority queue and then removing them 
	from the priority queue, placing the items in a List as they are removed.  
	The sort method is determined by the Comparator passed to the constructor.
	The original unsorted list is not affected.
*/
public class PQSort
{
	public static <E> List<E> pqSort(List<E> unsorted_items, Comparator<E> comp, PQType pq_type)
	{
		List<E> sorted_items = new ArrayList<E>();
		PQInterface<E> pq = PQFactory.createPQ(comp, pq_type);
		
		for (E item : unsorted_items)
		{
			pq.pqInsert(item);
		}
		
		while(!pq.pqIsEmpty())
		{
			E item = pq.pqRemove();
			sorted_items.add(item);
		}
		
		return sorted_items;
	}
}
