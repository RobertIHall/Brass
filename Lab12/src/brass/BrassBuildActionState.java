package brass;

class BrassBuildActionState implements BrassState
{
	private BrassGame brass_game;
	
	private int brass_card_city_tech_id; //brass_card_id is the city/industry identifier for the card
	
	public BrassBuildActionState(BrassGame game)
	{
		brass_game = game;
		brass_card_city_tech_id = 0;
	}
	
	public void setCardSelected(int card_city_tech_id)
	{
		brass_card_city_tech_id = card_city_tech_id;
	}
	
	//1 through 19 are city cards, 20 through 24 are industry cards
	//20 = coal, 21 = cotton, 22 = iron works, 23 = port, 24 = shipyard
	public void mouseClicked(int x_click, int y_click)
	{
		//first, check to see if the player wants to cancel the action
		int brass_action_id = brass_game.getSelectedAction(x_click, y_click);
		if (brass_action_id == BrassActionEnum.CANCEL.getValue())
		{
			brass_game.cancelCardSelection(); 
			brass_game.changeState(brass_game.getSelectCardState());
			return;
		}
		
		//if the card is a city card, the player clicks on a token location as the city is implied by the card
		//if the card is an industry card, the player clicks on a city as the industry is implied by the card
		
		int player_id = brass_game.getActivePlayerID();
		boolean can_build_industry = false;
		int city_id = -1;
		int industry_id = -1;

		if (brass_card_city_tech_id < 20)  //a city card
		{
			city_id = brass_card_city_tech_id;  //using a city card, clicked on an industry
			//if the card is a city card, make sure that the city can accommodate the selected token
			//there must be a matching industry that as of yet has no token
			industry_id = brass_game.getSelectedToken(x_click, y_click);
			if (industry_id > 0) 
			{
				//we adopt the protocol that we check to make sure that the player's build is completely
				//valid before attempting any part of the build
				//asserts are included where possible to detect if the protocol has not been followed
				can_build_industry = brass_game.canBuildIndustry(true, city_id, industry_id, player_id);
			}
		}

		else //the card is an industry card which is like a city card when the player has no tokens on the board
		{
			city_id = brass_game.getSelectedCity(x_click, y_click);
			industry_id = brass_card_city_tech_id - 19;  //using an industry card, clicked on a city
			if (city_id > 0)
			{
				//we adopt the protocol that we check to make sure that the player's build is completely
				//valid before attempting any part of the build
				//asserts are included where possible to detect if the protocol has not been followed
				can_build_industry = brass_game.canBuildIndustry(false, city_id, industry_id, player_id);
			}
		}
		
		if (can_build_industry)
		{
			brass_game.buildIndustry(city_id, industry_id, player_id);  //take the token from the player and place it on the board
			brass_game.playerActionCompleted();  //next player
		}
		else  //cancel action as required build conditions are not met
		{
			brass_game.cancelCardSelection();
		}
		
		brass_game.changeState(brass_game.getSelectCardState());
		brass_card_city_tech_id = 0;
	}
}
