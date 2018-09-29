package brass;

import table.CompareIntegers;

class BrassCityIDComparator implements table.Comparator<Integer, Integer>
{
	CompareIntegers comp_city_ids;
	
	public BrassCityIDComparator(boolean asc)
	{
		comp_city_ids = new CompareIntegers(asc);
	}

	public int compareItemItem(Integer city_1_id, Integer city_2_id)
	{
		return comp_city_ids.compare(city_1_id, city_2_id);
	}
	
	public int compareKeyItem(Integer city_1_id, Integer city_2_id)
	{
		return comp_city_ids.compare(city_1_id, city_2_id);
	}
}
