package brass;

class BrassSelectCardState implements BrassState
{
	private BrassGame brass_game;
	
	public BrassSelectCardState(BrassGame game)
	{
		brass_game = game;
	}
	
	// BrassGame tells the State class where the mouse was clicked
	public void mouseClicked(int x_click, int y_click)
	{
		int active_player_id = brass_game.getActivePlayerID();
		int display_player_id = brass_game.getSelectedPlayer(x_click, y_click);
		if (display_player_id > 0)
		{
			brass_game.displayPlayer(display_player_id); //perhaps player wants to view a different player
			return;
		}
		
		//brass card id is the place in the player's hand containing the selected card (1-8)
		int brass_card_index = brass_game.getSelectedCard(x_click, y_click);

		if (brass_card_index > 0)
		{
			//now, brass_card_id is the city/industry identifier for the card
			//int brass_card_city_tech_id = brass_game.getCardCityTechID(brass_card_num);
			brass_game.selectCard(brass_card_index);
			int brass_card_city_tech_id = brass_game.getCardCityTechID(brass_card_index);
			
			//change the state and forward the card that was selected
			BrassSelectActionState brass_select_action_state = (BrassSelectActionState) brass_game.getSelectActionState();
			brass_select_action_state.setCardSelected(brass_card_city_tech_id);
			brass_game.changeState(brass_select_action_state);
			
			return;
		}
		
		//a valid card was not selected, so do nothing, stay in this state
	}
}
