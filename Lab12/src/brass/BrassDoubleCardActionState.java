package brass;

class BrassDoubleCardActionState implements BrassState
{
	private BrassGame brass_game;
	
	private boolean second_card;
	private int city_id;
	private int industry_id;
	
	private void reset()
	{
		second_card = false;
		city_id = -1;
		industry_id = -1;
	}
	
	public BrassDoubleCardActionState(BrassGame game)
	{
		brass_game = game;
		reset();
	}
	
	//there are several clicks necessary here
	//first, a second card must be selected
	//then, a city must be selected 
	//finally, a token must be selected
	public void mouseClicked(int x_click, int y_click)
	{
		//first, check to see if the player wants to cancel the action
		int brass_action_id = brass_game.getSelectedAction(x_click, y_click);
		if (brass_action_id == BrassActionEnum.CANCEL.getValue())
		{
			brass_game.cancelDoubleCardSelection(); 
			brass_game.changeState(brass_game.getSelectCardState());
			reset();
			return;
		}
		
		int active_player_id = brass_game.getActivePlayerID();
		
		if (!second_card)
		{
			int brass_card_index = brass_game.getSelectedCard(x_click, y_click);
			if (brass_card_index > 0)
			{
				//don't care what the card is, just whether a card was selected
				brass_game.selectCard(brass_card_index);
				second_card = true;
			}
			return;
		}
	
		else if (city_id == -1)
		{
			int brass_city_id = brass_game.getSelectedCity(x_click, y_click);
			if (brass_city_id > 0)
			{
				city_id = brass_city_id;
			}
			return;
		}
		
		else
		{
			int brass_token_id = brass_game.getSelectedToken(x_click, y_click);
			if (brass_token_id > 0)
			{
				industry_id = brass_token_id;
				//can the industry be built in the specified city
				//build it if possible, otherwise cancel the action
				boolean can_build_industry = brass_game.canBuildIndustry(true, city_id, industry_id, active_player_id);
				if (can_build_industry)
				{
					brass_game.buildIndustry(city_id, industry_id, active_player_id);
					brass_game.playerActionCompleted();  //next player
				}
				else
				{
					brass_game.cancelDoubleCardSelection(); 
				}
				
				reset();
				brass_game.changeState(brass_game.getSelectCardState());
			}
		}
	}	
}
