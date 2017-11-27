package brass;

class BrassDoubleRailActionState implements BrassState
{
	private BrassGame brass_game;
	private boolean first_rail;
	
	public BrassDoubleRailActionState(BrassGame game)
	{
		brass_game = game;
		first_rail = true;
	}
	
	public void mouseClicked(int x_click, int y_click)
	{
		//first, check to see if the player wants to cancel the action
		int brass_action_id = brass_game.getSelectedAction(x_click, y_click);
		if (brass_action_id == BrassActionEnum.CANCEL.getValue())
		{
			first_rail = true;
			brass_game.cancelCardSelection(); 
			brass_game.changeState(brass_game.getSelectCardState());
			return;
		}
		
		int active_player_id = brass_game.getActivePlayerID();
		
		if (first_rail)
		{
			int link_id = brass_game.getSelectedLink(x_click, y_click);
			if (link_id > 0)
			{
				boolean can_build_link = brass_game.canBuildLink(link_id, active_player_id);
				if (can_build_link)
				{
					//build the first rail link for 5 + purchased coal
					brass_game.buildLink(link_id, active_player_id);
					first_rail = false;
				}
				else
				{
					first_rail = true;
					brass_game.cancelCardSelection();
					brass_game.changeState(brass_game.getSelectCardState());
				}
			}
		}
		
		else
		{
			int link_id = brass_game.getSelectedLink(x_click, y_click);
			if (link_id > 0)
			{
				boolean  can_build_link = brass_game.canBuildExpensiveLink(link_id, active_player_id);
				if (can_build_link)
				{
					//build the second rail link for 10 + purchased coal
					brass_game.buildExpensiveLink(link_id, active_player_id);
				}
				
				first_rail = true;
				brass_game.playerActionCompleted();  //next player
				brass_game.changeState(brass_game.getSelectCardState());
			}
		}
	}	
}
