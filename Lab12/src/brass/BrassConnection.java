package brass;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import java.awt.Graphics;
import java.awt.Image;

import gui.DrawImage;
import gui.HotSpot;

class BrassConnection
{
	private DrawImage connection_image;
	private DrawImage unconstructed_image;
	
	private HotSpot connection_spot;
	private int player_id;
	private int connection_id;
	private int[] city_connections;
	
	public int getLinkCoins(BrassCities brass_cities)
	{
		int conn_city_1 = city_connections[0];
		int conn_city_2 = city_connections[1];
		return brass_cities.getConnectionVictoryPoints(conn_city_1, conn_city_2);
	}
	
	public void buildTestLink(int link_id, int computer_player_id)
	{
		assert player_id == 0 : "Test link already exists.";
		player_id = computer_player_id;
	}
	
	public void removeTestLink(int link_id)
	{
		assert player_id > 0 : "Test link doesn't exist.";
		player_id = 0;
	}
	
	public void removeLink()
	{
		player_id = 0;
		connection_image = unconstructed_image;
	}
	
	public boolean isLinkConstructed()
	{
		return (player_id > 0);
	}
	
	public BrassConnection(int id, HotSpot hot_spot, int city_1, int city_2, DrawImage bw_img)
	{
		connection_id = id;
		connection_spot = hot_spot;
		player_id = 0;
		
		city_connections = new int[2];
		city_connections[0] = city_1;
		city_connections[1] = city_2;
		
		connection_image = bw_img;
		unconstructed_image = bw_img;
		connection_image.showImage(connection_spot.getHotX(), connection_spot.getHotY());
	}
	
	public void draw(Graphics g)
	{
		connection_image.draw(g);
	}
	
	public void buildLink(int id, DrawImage connection_img)
	{
		if (!isConstructed())
		{
			player_id = id;
			connection_image = connection_img;
			connection_image.showImage(connection_spot.getHotX(), connection_spot.getHotY());
		}
	}
	
	public int[] getConnectedCities()
	{
		return city_connections;
	}
	
	public int getPlayerID()
	{
		return player_id;
	}

	public int getConnectionID()
	{
		return connection_id;
	}
	
	public boolean isConstructed()
	{
		return (player_id > 0);
	}
	
	public boolean isSelected(int x, int y)
	{
		return connection_spot.isSelected(x, y);
	}
}
