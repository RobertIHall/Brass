package brass;

public class BrassCountPlayersUnflippedIndustry implements util.CountCommand<BrassIndustry>
{
	private int player_unflipped_industry_count;
	private int player_id;
	private int industry_id;
	
	public BrassCountPlayersUnflippedIndustry(int ind_id, int p_id)
	{
		player_unflipped_industry_count = 0;
		player_id = p_id;
		industry_id = ind_id;
	}
	
	public int getCount()
	{
		return player_unflipped_industry_count;
	}
	
   public void execute(BrassIndustry brass_industry)
   {
		if (brass_industry.isUnflippedIndustry(industry_id) && brass_industry.getPlayerID() == player_id)
		{
			player_unflipped_industry_count++;
		}
   }
}
