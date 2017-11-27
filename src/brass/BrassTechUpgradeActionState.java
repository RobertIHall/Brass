package brass;

class BrassTechUpgradeActionState implements BrassState
{
	private BrassGame brass_game;
	private int num_upgrades_performed;
	
	public BrassTechUpgradeActionState(BrassGame game)
	{
		brass_game = game;
		num_upgrades_performed = 0;
	}

	public void mouseClicked(int x_click, int y_click)
	{
		//check to see if the player wants to cancel the action
		int brass_action_id = brass_game.getSelectedAction(x_click, y_click);
		if ((brass_action_id == BrassActionEnum.CANCEL.getValue()) && (num_upgrades_performed == 0))
		{
			brass_game.cancelCardSelection(); 
			brass_game.changeState(brass_game.getSelectCardState());
			return;
		}
		
		//player only wanted one tech upgrade, not two
		else if ((brass_action_id == BrassActionEnum.CANCEL.getValue()) && (num_upgrades_performed == 1))
		{
			num_upgrades_performed = 0;
			brass_game.playerActionCompleted();
			brass_game.changeState(brass_game.getSelectCardState());
			return;
		}
		
		int active_player_id = brass_game.getActivePlayerID();
		int develop_token_id = brass_game.getSelectedToken(x_click, y_click);

		if (develop_token_id > 0)
		{
			num_upgrades_performed++;
			brass_game.techUpgrade(develop_token_id, active_player_id);
			//check for iron again as that may have been the last iron on the board
			boolean can_player_tech_upgrade = brass_game.canTechUpgrade(active_player_id);
			
			//either two tokens have been selected, or one has been selected and player cannot upgrade the second one
			if ((num_upgrades_performed == 2) || (!can_player_tech_upgrade))
			{
				num_upgrades_performed = 0;
				brass_game.playerActionCompleted();
				brass_game.changeState(brass_game.getSelectCardState());
			}
		}
	}
}
