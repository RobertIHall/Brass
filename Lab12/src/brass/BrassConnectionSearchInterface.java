package brass;

import java.util.List;

interface BrassConnectionSearchInterface
{
	public table.TableInterface<Integer, Integer> connectionSearch(int start_city_id, table.Comparator<Integer, Integer> comp_city_ids);
}
