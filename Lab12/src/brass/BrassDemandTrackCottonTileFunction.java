package brass;

class BrassDemandTrackCottonTileFunction implements BrassDemandTrackFunction
{
	//can have a constructor and private variables
	
	public int getBrassDemandTrackValue(int demand_index)
	{
		assert demand_index >= 1 && demand_index <= 12 : "Invalid cotton tile demand index.";
		
		if (demand_index <= 2) return 0;
		else if (demand_index <= 4) return 1;
		else if (demand_index <= 8) return 2;
		else if (demand_index <= 11) return 3;
		else return 4;
	}
}
