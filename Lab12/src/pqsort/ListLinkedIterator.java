package pqsort;

import util.QueueLinked;

class ListLinkedIterator<E>
{
   private QueueLinked<E> q;

   public ListLinkedIterator(Node<E> head)
   {
      q = new QueueLinked<E>();

      Node<E> next = head;
      //take a snapshot of the linked list contents
      while (next != null)
      {
         q.enqueue(next.getItem());
         next = next.getNext();
      }
   }

   public boolean hasNext()
   {
      return (!q.isEmpty());
   }

   public E next()
   {
      if (hasNext())  //should be used in conjunction with hasNext(), but just in case...
      {
         E item = q.dequeue();
         return item;
      }
      else
      {
         return null;
      }
   }
}