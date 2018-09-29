package brass;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class BrassAICoalStrategy implements BrassComputerPlayer
{
	private BrassAIStrategy current_phase;
	private BrassAIStrategy canal_phase;
	private BrassAIStrategy rail_phase;
	private boolean swapped;
	private BrassGame brass_game;
	
	public BrassAICoalStrategy(BrassGame bg, BrassBoard bd)
	{
		brass_game = bg;
		canal_phase = new BrassAICanal(bg, bd);
		rail_phase = new BrassAIRail(bg);
		current_phase = canal_phase;
	}
	
	public BrassComputerPlayerAction getBrassMove()
	{
		System.out.println("Currently tested AI running.");
		if((!swapped && brass_game.getPhase()))
		{
			System.out.println("The Rail Phase has begun.");
			current_phase = rail_phase;
		}
		return current_phase.nextMove();
	}
}