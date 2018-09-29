package brass;

public class BrassCountAllPlayersUnflippedIndustry implements util.CountCommand<BrassCity>
{
	private int player_unflipped_industry_count;
	private int player_id;
	private int industry_id;
	
	public BrassCountAllPlayersUnflippedIndustry(int ind_id, int p_id)
	{
		player_unflipped_industry_count = 0;
		player_id = p_id;
		industry_id = ind_id;
	}
	
	public int getCount()
	{
		return player_unflipped_industry_count;
	}
	
   public void execute(BrassCity brass_city)
   {
		BrassCountPlayersUnflippedIndustry count_player_industry = new BrassCountPlayersUnflippedIndustry(industry_id, player_id);
		brass_city.execute(count_player_industry);
		player_unflipped_industry_count += count_player_industry.getCount();
   }
}
