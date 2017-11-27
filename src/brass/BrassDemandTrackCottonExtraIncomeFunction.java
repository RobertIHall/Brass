package brass;

class BrassDemandTrackCottonExtraIncomeFunction implements BrassDemandTrackFunction
{
	//can have a constructor and private variables
	
	public int getBrassDemandTrackValue(int demand_index)
	{
		assert demand_index >= 0 && demand_index <= 8 : "Invalid cotton demand extra income index.";
		return 3 - (demand_index)/2;
	}
}
