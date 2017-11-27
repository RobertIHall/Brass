package brass;

class BrassLinkActionState implements BrassState
{
	private BrassGame brass_game;
	
	public BrassLinkActionState(BrassGame game)
	{
		brass_game = game;
	}
	
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
		
		int link_id = brass_game.getSelectedLink(x_click, y_click);
		int active_player_id = brass_game.getActivePlayerID();

		if (link_id > 0)  
		{
			boolean can_build_link = brass_game.canBuildLink(link_id, active_player_id);
			if(can_build_link)
			{
				brass_game.buildLink(link_id, active_player_id);
				brass_game.playerActionCompleted();
			}
			else
			{
				brass_game.cancelCardSelection();
			}
			
			brass_game.changeState(brass_game.getSelectCardState());
		}
	}
}
