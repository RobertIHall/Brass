package brass;

import java.util.Comparator;

class BrassCompareLinksByCoins implements Comparator<BrassConnection>
{
	private BrassCities brass_cities;
	public BrassCompareLinksByCoins(BrassCities cities)
	{
		brass_cities = cities;
	}
	
   public int compare(BrassConnection brass_conn_1, BrassConnection brass_conn_2)
   {
      int link_coins_1 = brass_conn_1.getLinkCoins(brass_cities);
      int link_coins_2 = brass_conn_2.getLinkCoins(brass_cities);

      return link_coins_2 - link_coins_1;   //want descending order for this sort
   }
}
