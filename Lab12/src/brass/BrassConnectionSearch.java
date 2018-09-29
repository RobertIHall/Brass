package brass;

import java.util.List;
import java.util.Iterator;

abstract class BrassConnectionSearch implements BrassConnectionSearchInterface
{
	protected List<BrassConnection> search_connections;
	public BrassConnectionSearch(List<BrassConnection> connections)
	{
		search_connections = connections;
	}
	
	//counts for implementing the interface
	//assumed a child class will provide a concrete implementation
	public abstract table.TableInterface<Integer, Integer> connectionSearch(int start_city_id,  table.Comparator<Integer, Integer> comp_city_ids);
	
	//if one end of the link matches the city_id parameter, return the city_id of the other end
	protected int isLinkConnected(int city_id, int[] connected_cities)
	{
		int test_city_id = 0;
		//is this canal connected to the current city?
		if (connected_cities[0] == city_id)
		{
			test_city_id = connected_cities[1];
		}
		else if (connected_cities[1] == city_id)
		{
			test_city_id = connected_cities[0];
		}
		
		return test_city_id;
	}
}