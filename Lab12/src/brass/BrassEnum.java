package brass;

enum BrassConnectionSearchEnum 
{
	DEPTH_LIMIT (1), BREADTH_FIRST (2);
	private int value;
	
	private BrassConnectionSearchEnum(int val) 
	{ 
		value = val; 
	}
	
	public int getValue() 
	{ 
		return value; 
	}
};

enum BrassActionEnum 
{
	LOAN_10 (1), LOAN_20 (2), LOAN_30 (3), BUILD (4), LINK (5), SELL (6), UPGRADE (7), DISCARD (8), CANCEL (9), DOUBLE_CARD(10), DOUBLE_RAIL(11), REPLACE(12);
	private int value;
	
	private BrassActionEnum(int val) 
	{ 
		value = val; 
	}
	
	public int getValue() 
	{ 
		return value; 
	}
};

enum BrassIndustryEnum 
{
	COAL (1), COTTON (2), IRON (3), PORT (4), SHIP (5);
	private int value;
	
	private BrassIndustryEnum(int val) 
	{ 
		value = val; 
	}
	
	public int getValue() 
	{ 
		return value; 
	}
};

enum BrassLinkCostEnum
{
	CANAL (3), RAIL (5), DOUBLE_RAIL(10);
	private int value;
	
	private BrassLinkCostEnum(int val) 
	{ 
		value = val; 
	}
	
	public int getValue() 
	{ 
		return value; 
	}
};
