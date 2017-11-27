package brass;

import java.util.Comparator;

class BrassCompareAmountSpent implements Comparator<BrassPlayer>
{
	private boolean ascending;
	public BrassCompareAmountSpent(boolean asc)
	{
		ascending = asc;
	}
	
   public int compare(BrassPlayer brass_player_1, BrassPlayer brass_player_2)
   {
      int amount_spent_1 = brass_player_1.getAmountSpent();
      int amount_spent_2 = brass_player_2.getAmountSpent();
	  int comp = 0;
	  
	  if (ascending)
	  {
		comp = amount_spent_1 - amount_spent_2; 
	  }
	  else
	  {
		  comp = amount_spent_2 - amount_spent_1; 
	  }

      return comp;
   }
}
