package brass;

import java.awt.Graphics;

import gui.HotSpot;
import gui.PixelPoint;
import gui.PixelDimension;
import gui.DrawOval;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

class BrassCities implements util.Execute<BrassCity>
{
	private List<BrassCity> brass_cities;
	
	private DrawOval flip_circle;
	private PixelPoint flip_circle_center;
	
	private gui.DrawRectangle resource_cube;
	private List<PixelPoint> resource_cube_centers;
	
	public int countPlayersUnflippedIndustry(int city_id, int industry_id, int player_id)
	{
		BrassCity brass_city = brass_cities.get(city_id - 1);
		BrassCountPlayersUnflippedIndustry count_player_industry = new BrassCountPlayersUnflippedIndustry(industry_id, player_id);
		brass_city.execute(count_player_industry);
		return count_player_industry.getCount();
	}
	
	public boolean hasPlayersUnflippedIndustry(int city_id, int industry_id, int player_id)
	{
		return (countPlayersUnflippedIndustry(city_id, industry_id, player_id) > 0);
	}
	
	public boolean isCityFull(int city_id)
	{
		BrassCity brass_city = brass_cities.get(city_id - 1);
		return brass_city.isCityFull();
	}
	
	public void replaceTokenInCity(int city_id, BrassToken brass_token)
	{
		BrassCity brass_city = brass_cities.get(city_id - 1);
		brass_city.replaceTokenInCity(brass_token);
	}
	
	public void placeTokenInCity(int city_id, BrassToken brass_token)
	{
		BrassCity brass_city = brass_cities.get(city_id - 1);
		brass_city.placeTokenInCity(brass_token);
	}
	
	public boolean canCityReplaceToken(int city_id, int industry_id, int tech_level, int player_id)
	{
		BrassCity brass_city = brass_cities.get(city_id - 1);
		return brass_city.canCityReplaceToken(industry_id, tech_level, player_id);
	}
	
	public void shipyardBuilt(int city_id, int player_id, BrassPlayers brass_players, BrassTrack brass_track)
	{
		BrassCity brass_city = brass_cities.get(city_id - 1);
		brass_city.flipToken(BrassIndustryEnum.SHIP.getValue(), player_id, brass_players, brass_track);
	}
	
	public int getIndustryVictoryPoints(int player_id)
	{
		int victory_points = 0;
		for (BrassCity brass_city : brass_cities)
		{
			victory_points += brass_city.getIndustryVictoryPoints(player_id);
		}
		return victory_points;
	}
	
	public int getConnectionVictoryPoints(int conn_city_1, int conn_city_2)
	{
		int victory_points = 0;
		BrassCity brass_city_1 = brass_cities.get(conn_city_1 - 1);
		victory_points += brass_city_1.getConnectionVictoryPoints();
		BrassCity brass_city_2 = brass_cities.get(conn_city_2 - 1);
		victory_points += brass_city_2.getConnectionVictoryPoints();
		return victory_points;
	}
	
	public void clearLevel1Industries()
	{
		for (BrassCity brass_city : brass_cities)
		{
			brass_city.clearLevel1Industries();
		}
	}
	
	public boolean hasConstructedPort(int city_id)
	{
		BrassCity brass_city = brass_cities.get(city_id - 1);
		if(brass_city.isExternalLocation()) return true;
		
		BrassCountConstructedPorts count_constructed_ports = new BrassCountConstructedPorts();
		brass_city.execute(count_constructed_ports);
		return (count_constructed_ports.getCount() > 0);
	}

	public boolean hasUnflippedIndustry(int city_id, int industry_id)
	{
		BrassCity brass_city = brass_cities.get(city_id - 1);
		BrassCountUnflippedIndustry count_industry = new BrassCountUnflippedIndustry(industry_id);
		brass_city.execute(count_industry);
		return (count_industry.getCount() > 0);
	}
	
	public void execute(util.Command<BrassCity> command)
	{
		Iterator<BrassCity> city_iter = brass_cities.iterator();
		while(city_iter.hasNext())
		{
			BrassCity brass_city = city_iter.next();
			command.execute(brass_city);
		}
	}
	
	public int getUnflippedIndustryPlayerID(int city_id, int industry_id)
	{
		BrassCity brass_city = brass_cities.get(city_id - 1);
		return brass_city.getUnflippedIndustryPlayerID(industry_id);
	}
	
	public void flipToken(int city_id, int industry_id, int player_id, BrassPlayers brass_players, BrassTrack brass_track)
	{
		BrassCity brass_city = brass_cities.get(city_id - 1);
		brass_city.flipToken(industry_id, player_id, brass_players, brass_track);
	}
	
	//check all cities, connections not required for iron
	public int canMoveIron(int player_id)
	{
		int iron_city_id = 0;
		Iterator<BrassCity> iron_city_iter = brass_cities.iterator();
		while(iron_city_iter.hasNext())
		{
			int test_city_id = iron_city_iter.next().getCityID();
			if (hasPlayersUnflippedIndustry(test_city_id, BrassIndustryEnum.IRON.getValue(), player_id))
			{
				return test_city_id;  //found a player iron works so stop here
			}
			else if(hasUnflippedIndustry(test_city_id, BrassIndustryEnum.IRON.getValue()))
			{
				iron_city_id = test_city_id;
			}
		}
		
		return iron_city_id;
	}
	
	public void moveIron(int iron_city_id, int player_id, BrassPlayers brass_players, BrassTrack brass_track)
	{
		BrassCity brass_city = brass_cities.get(iron_city_id - 1);
		brass_city.removeCubeFromCity(BrassIndustryEnum.IRON.getValue(), player_id, brass_players, brass_track);
	}
	
	public void moveCoal(int coal_city_id, int player_id, BrassPlayers brass_players, BrassTrack brass_track)
	{
		BrassCity brass_city = brass_cities.get(coal_city_id - 1);
		brass_city.removeCubeFromCity(BrassIndustryEnum.COAL.getValue(), player_id, brass_players, brass_track);
	}
	
	public int getNumTokensOnBoard(int player_id)
	{
		int count = 0;
		Iterator<BrassCity> city_iter = brass_cities.iterator();
		while(city_iter.hasNext())
		{
			BrassCity brass_city = city_iter.next();
			count += brass_city.getNumTokensInCity(player_id);
		}
		return count;
	}
	
	public int getNumTokensInCity(int city_id, int player_id)
	{
		BrassCity brass_city = brass_cities.get(city_id - 1);
		return brass_city.getNumTokensInCity(player_id);
	}
	
	public boolean canCityAcceptIndustry(int city_id, int industry_id)
	{
		BrassCity brass_city = brass_cities.get(city_id - 1);
		return brass_city.canCityAcceptIndustry(industry_id);
	}
	
	public String getCityName(int city_id)
	{
		BrassCity brass_city = brass_cities.get(city_id - 1);
		return brass_city.getCityName();
	}
	
	public void draw(Graphics g)
	{
		Iterator<BrassCity> city_iter = brass_cities.iterator();
		while(city_iter.hasNext())
		{
			BrassCity brass_city = city_iter.next();
			brass_city.draw(g, resource_cube, resource_cube_centers, flip_circle, flip_circle_center);
		}
	}

	//if any of the industry locations within the city are selected, then the city is selected (1 to 19) 
	public int getSelectedCity(int x, int y)
	{
		Iterator<BrassCity> city_iter = brass_cities.iterator();
		while(city_iter.hasNext())
		{
			BrassCity brass_city = city_iter.next();
			//checks for any of the industries in the city
			if (brass_city.isCitySelected(x, y))
			{
				int brass_city_id = brass_city.getCityID();
				assert (brass_city_id >= 1 && brass_city_id <= 19) : "Get selected city error.";
				return brass_city_id;
			}
		}
		
		return 0;
	}
	
	//returns the hot spot id of the selected industry (1 to 42)
	public int getSelectedIndustry(int x, int y)
	{
		Iterator<BrassCity> city_iter = brass_cities.iterator();
		
		while(city_iter.hasNext())
		{
			BrassCity brass_city = city_iter.next();
			int brass_industry_id = brass_city.getSelectedIndustry(x, y);
			if (brass_industry_id > 0)
			{
				return brass_industry_id;
			}
		}
		
		return 0;
	}
	
	public BrassCities(BrassXML brass_xml)
	{
		PixelDimension resource_cube_dimension = brass_xml.getPixelDimension("cubes");
		resource_cube_centers = brass_xml.getPixelCenters("cubes");
		resource_cube = new gui.DrawRectangle(resource_cube_dimension.getWidth(), resource_cube_dimension.getHeight());
		
		PixelDimension flip_circle_dimension = brass_xml.getPixelDimension("flip");
		flip_circle_center = brass_xml.getPixelCenter("flip");
		flip_circle = new DrawOval(flip_circle_dimension.getWidth(), flip_circle_dimension.getHeight());
		
		brass_cities = new ArrayList<BrassCity>();
		
		PixelDimension city_dimension = brass_xml.getPixelDimension("cities");
		List<PixelPoint> industry_centers = brass_xml.getPixelCenters("cities");
		List<HotSpot> industry_hot_spots = new ArrayList<HotSpot>();
		
		for (int i = 1; i <= 43; i++)
		{
			PixelPoint pixel_point = industry_centers.get(i-1);
			HotSpot industry_hot_spot = new HotSpot(i, pixel_point.getX(), pixel_point.getY(), city_dimension.getWidth(), city_dimension.getHeight());
			industry_hot_spots.add(industry_hot_spot);
		}

		BrassCity brass_city = new BrassCity(1, "Barrow");
		BrassIndustry brass_industry = new BrassIndustry(5, industry_hot_spots.get(0));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(3, industry_hot_spots.get(1));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(2, "Birkenhead");
		brass_industry = new BrassIndustry(5, industry_hot_spots.get(2));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(3, "Blackburn");
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(3));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(4));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(3, industry_hot_spots.get(5));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(4, "Bolton");
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(6));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(7));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(3, industry_hot_spots.get(8));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(5, "Burnley");
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(9));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(10));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(6, "Bury");
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(11));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(12));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(7, "Colne");
		brass_industry = new BrassIndustry(2, industry_hot_spots.get(13));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(2, industry_hot_spots.get(14));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(8, "Ellesmere");
		brass_industry = new BrassIndustry(4, industry_hot_spots.get(15));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(9, "Fleetwood");
		brass_industry = new BrassIndustry(4, industry_hot_spots.get(16));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(10, "Lancaster");
		brass_industry = new BrassIndustry(4, industry_hot_spots.get(17));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(7, industry_hot_spots.get(18));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(11, "Liverpool");
		brass_industry = new BrassIndustry(4, industry_hot_spots.get(19));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(4, industry_hot_spots.get(20));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(5, industry_hot_spots.get(21));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(4, industry_hot_spots.get(22));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(12, "Macclesfield");
		brass_industry = new BrassIndustry(2, industry_hot_spots.get(23));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(2, industry_hot_spots.get(24));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(13, "Manchester");
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(25));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(26));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(27));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(3, industry_hot_spots.get(28));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(14, "Oldham");
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(29));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(30));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(15, "Preston");
		brass_industry = new BrassIndustry(4, industry_hot_spots.get(31));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(7, industry_hot_spots.get(32));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(3, industry_hot_spots.get(33));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);

		brass_city = new BrassCity(16, "Rochdale");
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(34));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(35));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(3, industry_hot_spots.get(36));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(17, "Stockport");
		brass_industry = new BrassIndustry(2, industry_hot_spots.get(37));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(2, industry_hot_spots.get(38));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(18, "Warrington");
		brass_industry = new BrassIndustry(6, industry_hot_spots.get(39));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(4, industry_hot_spots.get(40));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);

		brass_city = new BrassCity(19, "Wigan");
		brass_industry = new BrassIndustry(1, industry_hot_spots.get(41));
		brass_city.addIndustry(brass_industry);
		brass_industry = new BrassIndustry(1, industry_hot_spots.get(42));
		brass_city.addIndustry(brass_industry);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(20, "Yorkshire");
		brass_city.externalLocation();
		brass_city.connectionCoin(2);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(21, "Northwich");
		brass_city.connectionCoin(2);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(22, "Midlands");
		brass_city.externalLocation();
		brass_city.connectionCoin(2);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(23, "Scotland");
		brass_city.externalLocation();
		brass_city.connectionCoin(2);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(24, "Blackpool");
		brass_city.connectionCoin(2);
		brass_cities.add(brass_city);
		
		brass_city = new BrassCity(25, "Southport");
		brass_city.connectionCoin(2);
		brass_cities.add(brass_city);
		
		assert (brass_cities.size() == 25) : "Number of cities error.";
	}

}
