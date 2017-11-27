package brass;

class BrassGameOverState implements BrassState
{
	private BrassGame brass_game;
	
	public BrassGameOverState(BrassGame game)
	{
		brass_game = game;
	}

	public void mouseClicked(int x_click, int y_click)
	{
		int display_player_id = brass_game.getSelectedPlayer(x_click, y_click);
		if (display_player_id > 0)
		{
			brass_game.displayPlayer(display_player_id); 
			return;
		}
	}
}
