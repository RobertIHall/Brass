package brass;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import java.awt.Graphics;
import java.awt.Image;

import gui.HotSpot;
import gui.DrawImage;
import gui.ImageLoader;
import gui.PixelPoint;
import gui.PixelDimension;

import util.QueueLinked;

abstract class BrassConnections
{
	protected List<BrassConnection> brass_connections;
	protected List<Image> brass_connection_images;
	protected PixelDimension connection_dimension;
	
	public List<Integer> getSortedLinks(BrassCities brass_cities)
	{
		java.util.Comparator<BrassConnection> compare_links_by_coins = new BrassCompareLinksByCoins(brass_cities);
		List<BrassConnection> sorted_links = pqsort.PQSort.pqSort(brass_connections, compare_links_by_coins, pqsort.PQType.TREE);
		List<Integer> sorted_link_ids = new ArrayList<Integer>();
		
		for (BrassConnection conn : sorted_links)
		{
			sorted_link_ids.add(conn.getConnectionID());
		}
		return sorted_link_ids;
	}
	
	public void buildTestLink(int link_id, int computer_player_id)
	{
		BrassConnection brass_connection = brass_connections.get(link_id - 1);
		brass_connection.buildTestLink(link_id, computer_player_id);
	}
	
	public void removeTestLink(int link_id)
	{
		BrassConnection brass_connection = brass_connections.get(link_id - 1);
		brass_connection.removeTestLink(link_id);
	}
	
	public int getConnectionVictoryPoints(int player_id, BrassCities brass_cities)
	{
		int victory_points = 0;
		
		for (BrassConnection brass_connection : brass_connections)
		{
			if (brass_connection.getPlayerID() == player_id)
			{
				int[] connected_cities = brass_connection.getConnectedCities();
				victory_points += brass_cities.getConnectionVictoryPoints(connected_cities[0], connected_cities[1]);
			}
		}
		
		return victory_points;
	}
	
	public void clearCanals()
	{
		for (BrassConnection brass_connection : brass_connections)
		{
			brass_connection.removeLink();
		}
	}
		
	public BrassConnectionSearchInterface createConnectionSearchStrategy(BrassConnectionSearchEnum search_strategy, int depth_limit)
	{
		if (search_strategy == BrassConnectionSearchEnum.BREADTH_FIRST)
		{
			return new BrassBreadthFirstConnectionSearch(brass_connections);
		}
		else
		{
			return new BrassDepthLimitedConnectionSearch(depth_limit, brass_connections);
		}
	}

	public boolean isLinkConstructed(int link_id)
	{
		BrassConnection brass_connection = brass_connections.get(link_id - 1);
		return brass_connection.isLinkConstructed();
	}
	
	public boolean doesPlayerHaveLinkIntoCity(int city_id, int player_id)
	{
		Iterator<BrassConnection> connection_iter = brass_connections.iterator();
		while(connection_iter.hasNext())
		{
			BrassConnection brass_connection = connection_iter.next();
			if (brass_connection.isLinkConstructed())
			{
				int p_id = brass_connection.getPlayerID();
				if (p_id == player_id)
				{
					int[] connected_cities = brass_connection.getConnectedCities();
					
					int city_1_id = connected_cities[0];
					int city_2_id = connected_cities[1];
					
					if (city_1_id == city_id || city_2_id == city_id)
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public int getNumLinks()
	{
		return brass_connections.size();
	}
	
	public void buildLink(int link_id, int player_id)
	{
		//lab 8 assert
		assert !isConstructed(link_id) : "Link already constructed.";

		BrassConnection brass_connection = brass_connections.get(link_id - 1);
		DrawImage brass_connection_img = new DrawImage(brass_connection_images.get(player_id - 1), "Brass Canal " + link_id, connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection.buildLink(player_id, brass_connection_img);
	}
	
	public void draw(Graphics g)
	{
		Iterator<BrassConnection> connections_iter = brass_connections.iterator();
		while (connections_iter.hasNext())
		{
			BrassConnection brass_connection = connections_iter.next();
			brass_connection.draw(g);
		}
	}
	
	public boolean isConstructed(int link_id)
	{
		BrassConnection brass_connection = brass_connections.get(link_id - 1);
		return brass_connection.isConstructed();
	}
	
	public int[] getConnectedCities(int link_id)
	{
		BrassConnection brass_connection = brass_connections.get(link_id - 1);
		return brass_connection.getConnectedCities();
	}
	
	public int getSelectedLink(int x, int y)
	{
		Iterator<BrassConnection> brass_connections_iter = brass_connections.iterator();
		while(brass_connections_iter.hasNext())
		{
			BrassConnection brass_connection = brass_connections_iter.next();
			if (brass_connection.isSelected(x, y) && !brass_connection.isConstructed())
			{
				return brass_connection.getConnectionID();
			}
		}
		
		return 0;
	}
}
