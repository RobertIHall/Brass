package pqsort;

import java.util.Comparator;

class BinarySearchTree<E>
{
	private TreeNode<E> root;
	private Comparator<E> comp;
   private int size;
   
   public int size()
   {
	   return size;
   }
   
   public E remove()
   {
	   if (root == null) return null;
	   
	   E item;
	   if (root.getLeft() == null)
	   {
		   item = root.getItem();
		   root = root.getRight();
	   }
	   else
	   {
		   item = findLeftmost(root);
		   removeLeftmost(root);
	   }
	   
	   size--;
	   return item;
   }

   /**  Constructs an empty binary search tree. */
   public BinarySearchTree(Comparator<E> comp)
   {
      this.comp = comp;
   }

   public void insert(E item)
   {
      root = insertItem(root, item);
      size++;
   }  

   /**
    * Searches for the leaf insertion location for item. <br>
    * Precondition: item is not null
    * Postcondition: returns the current node so that it can be linked (or relinked) to its parent
    * depending on whether a left or a right was taken in obtaining the leaf insertion location.
    * Throws: TreeException if an attempt to insert a duplicate is detected.
    * Calls: insertLeft, insertRight, insertDuplicate depending on the item's sk
    */
   private TreeNode<E> insertItem(TreeNode<E> tNode, E item)
   {
      if (tNode == null) 
      {
         TreeNode<E> create_node = new TreeNode<E>(item);
		return create_node;
      }  

      E node_item = tNode.getItem();
      int comparison = comp.compare(item, node_item);

	  //duplicates placed on the right
      if (comparison < 0) 
      {
		TreeNode<E> subtree = insertItem(tNode.getLeft(), item);
		tNode.setLeft(subtree);
      }
      else if (comparison >= 0) 
      { 
         TreeNode<E> subtree = insertItem(tNode.getRight(), item);
		tNode.setRight(subtree);
      }  
	  
	  return tNode;
   } 

   private E findLeftmost(TreeNode<E> tNode)  
   {
      if (tNode.getLeft() == null) 
      {
         return tNode.getItem();
      }
      else 
      {
         return findLeftmost(tNode.getLeft());
      }  
   } 

   private TreeNode<E> removeLeftmost(TreeNode<E> tNode) 
   {
      if (tNode.getLeft() == null) 
      {
         return tNode.getRight();
      }
      else 
      {
         TreeNode<E> subtree = removeLeftmost(tNode.getLeft());
		tNode.setLeft(subtree);
		return tNode;
      }  
   }
} 
