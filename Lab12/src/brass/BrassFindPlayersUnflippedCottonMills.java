package brass;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class BrassFindPlayersUnflippedCottonMills implements util.FindCommand<BrassCity>
{
	private List<Integer> player_unflipped_cotton_mills;
	private int player_id;
	
	public BrassFindPlayersUnflippedCottonMills(int p_id)
	{
		player_unflipped_cotton_mills = new ArrayList<Integer>();
		player_id = p_id;
	}
	
	public List<Integer> getList()
	{
		return player_unflipped_cotton_mills;
	}
	
   public void execute(BrassCity brass_city)
   {
	   util.CountCommand<BrassIndustry> count_player_cotton_mills = new BrassCountPlayersUnflippedIndustry(2, player_id);
	   brass_city.execute(count_player_cotton_mills);
	   //a city can have multiple unflipped cotton mills in the rail phase
	   int count = count_player_cotton_mills.getCount();
	   while (count > 0)
	   {
		   player_unflipped_cotton_mills.add(brass_city.getCityID());
		   count--;
	   }
   }
}
