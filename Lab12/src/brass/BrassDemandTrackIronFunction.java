package brass;

class BrassDemandTrackIronFunction implements BrassDemandTrackFunction
{
	//can have a constructor and private variables
	
	public int getBrassDemandTrackValue(int demand_index)
	{
		if (demand_index > 8 || demand_index < 0) return 0;
		return ((9 - demand_index) + 1)/2;
	}
}
