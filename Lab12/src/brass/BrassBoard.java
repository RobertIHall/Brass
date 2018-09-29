package brass;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;

import gui.DrawFont;
import gui.DrawImage;
import gui.HotSpot;
import gui.PixelPoint;
import gui.PixelDimension;

class BrassBoard
{
	private DrawImage board_img;
	
	private BrassCities brass_cities;
	private BrassConnections brass_connections;
	private BrassConnections brass_rails;
	
	private List<HotSpot> action_hot_spots;
	
	private BrassDemandTrack brass_coal_demand_track;
	private BrassDemandTrack brass_iron_demand_track;
	private BrassCottonDemandTrack brass_cotton_demand_track;
	
	public List<Integer> getSortedLinks()
	{
		return brass_connections.getSortedLinks(brass_cities);
	}
	
	public int getNumLinks()
	{
		return brass_connections.getNumLinks();
	}
	
	public int countAllPlayersUnflippedIndustry(int industry_id, int player_id)
	{
		BrassCountAllPlayersUnflippedIndustry count_all_player_industry = new BrassCountAllPlayersUnflippedIndustry(industry_id, player_id);
		brass_cities.execute(count_all_player_industry);
		return count_all_player_industry.getCount();
	}
	
	public boolean isCityFull(int city_id)
	{
		return brass_cities.isCityFull(city_id);
	}
	
	//special computer player method
	public void removeTestLink(int link_id)
	{
		brass_connections.removeTestLink(link_id);
	}
	
	//special computer player method
	public void buildTestLink(int link_id, int computer_player_id)
	{
		brass_connections.buildTestLink(link_id, computer_player_id);
	}
	
	public void replaceTokenInCity(int city_id, BrassToken brass_token)
	{
		brass_cities.replaceTokenInCity(city_id, brass_token);
	}
	
	public void placeTokenInCity(int city_id, BrassToken brass_token)
	{
		brass_cities.placeTokenInCity(city_id, brass_token);
	}
	
	public boolean canCityReplaceToken(int city_id, int industry_id, int tech_level, int player_id)
	{
		return brass_cities.canCityReplaceToken(city_id, industry_id, tech_level, player_id);
	}
	
	//0 indicates coal is not required
	//>0 indicates that coal is required and can be taken from the city_id returned
	//-1 indicates that coal is required but cannot be shipped to the current location
	//20 indicates that coal can be obtained from the coal demand track
	public int canMoveCoalRailLink(boolean brass_phase, int link_id, int player_id)
	{
		if (!brass_phase) return 0; //canal phase, no coal required
				
		int[] connected_cities = brass_connections.getConnectedCities(link_id);
		int city_id_1 = connected_cities[0];
		int city_id_2 = connected_cities[1];
		
		int coal_city_id = 0;
		int depth_limit = 0;
		int num_explored_cities = 0;
		
		table.Comparator<Integer, Integer> comp_city_ids = new BrassCityIDComparator(true);
		
		while (coal_city_id == 0)
		{
			BrassConnectionSearchInterface depth_limit_search = brass_connections.createConnectionSearchStrategy(BrassConnectionSearchEnum.DEPTH_LIMIT, depth_limit);
			table.TableInterface<Integer, Integer> explored_cities_1 = depth_limit_search.connectionSearch(city_id_1, comp_city_ids);
			table.TableInterface<Integer, Integer> explored_cities_2 = depth_limit_search.connectionSearch(city_id_2, comp_city_ids);
			
			//merge tables
			Iterator<Integer> explored_cities_2_iter = explored_cities_2.iterator();
			while(explored_cities_2_iter.hasNext())
			{
				try
				{
					explored_cities_1.tableInsert(explored_cities_2_iter.next());
				}
				catch(table.TableException te)
				{
					//ignore duplicates
				}
			}
			
			int explored_size = explored_cities_1.tableSize();
			if (num_explored_cities == explored_size)
			{
				break;  //no new cities were added at the new depth, so quit searching
			}

			num_explored_cities = explored_size;

			//check the list for a coal city at each new depth to find the closest one
			//if the player has a coal city in the list, use that coal city first
			coal_city_id = getUnflippedCoalCity(player_id, explored_cities_1);
			depth_limit++;
		}
		
		if (coal_city_id == 0)  //no coal can be moved on the board
		{
			//coal is always required for a rail link
			boolean is_link_connected_to_constructed_port = isLinkConnectedToConstructedPort(link_id);
			if (is_link_connected_to_constructed_port)
			{
				coal_city_id = 20;
			}
			else
			{
				coal_city_id = -1;
			}
		}
		
		return coal_city_id;
	}

	//necessary for links in both phases
	//link must connect to player network
	public void buildLink(int coal_city_id, int link_id, int player_id, BrassPlayers brass_players, BrassTrack brass_track)
	{
		assert coal_city_id >= 0 : "Cannot move coal required by rail link.";
		
		if (coal_city_id == 20)
		{
			int coal_demand_cost = brass_coal_demand_track.getCostToBuyFromDemandTrack();
			brass_coal_demand_track.buyFromDemandTrack();
			brass_players.payForCube(coal_demand_cost, player_id);
		}
		else if (coal_city_id > 0)
		{
			brass_cities.moveCoal(coal_city_id, player_id, brass_players, brass_track);
		}
		
		brass_connections.buildLink(link_id, player_id);
	}
	
	public boolean isLinkConstructed(int link_id)
	{
		return brass_connections.isLinkConstructed(link_id);
	}
	
	//have to check both ends of the link for connection to player network
	public boolean isLinkConnectedToPlayerNetwork(int link_id, int player_id)
	{
		int[] connected_cities = brass_connections.getConnectedCities(link_id);
		int city_1_id = connected_cities[0];
		int city_2_id = connected_cities[1];
		
		//the city is connected if the player has a token into the city or has links into the city
		if (isCityConnectedToPlayerNetwork(city_1_id, player_id))
		{
			return true;
		}
		
		else if (isCityConnectedToPlayerNetwork(city_2_id, player_id))
		{
			return true;
		}
		
		return false;  //link is not connected
	}
	
	public void shipyardBuilt(int city_id, int player_id, BrassPlayers brass_players, BrassTrack brass_track)
	{
		brass_cities.shipyardBuilt(city_id, player_id, brass_players, brass_track);
	}
	
	public int getVictoryPoints(int player_id)
	{
System.out.println();
System.out.println("player id: " + player_id);
		int victory_points = 0;
		int connection_victory = brass_connections.getConnectionVictoryPoints(player_id, brass_cities);
System.out.println("connections: " + connection_victory);
		int industry_victory = brass_cities.getIndustryVictoryPoints(player_id);
System.out.println("industries: " + industry_victory);
		return connection_victory + industry_victory;
	}
	
	public void changePhase()
	{
		//after scoring victory points
		//clear level 1 technologies
		brass_cities.clearLevel1Industries();
		//clear canals
		brass_connections.clearCanals();
		//switch to rails
		brass_connections = brass_rails;
		
		brass_cotton_demand_track.reset();
	}
	
	public void sellCotton(int player_id, BrassPlayers brass_players, BrassTrack brass_track)
	{
		BrassFindPlayersUnflippedCottonMills find_player_cotton_mills = new BrassFindPlayersUnflippedCottonMills(player_id);
		brass_cities.execute(find_player_cotton_mills);
		
		List<Integer> player_cotton_mills = find_player_cotton_mills.getList();
		Iterator<Integer> cotton_mill_iter = player_cotton_mills.iterator();
		while(cotton_mill_iter.hasNext())
		{
			int cotton_city_id = cotton_mill_iter.next();
			
			table.Comparator<Integer, Integer> comp_city_ids = new BrassCityIDComparator(true);
			
			BrassConnectionSearchInterface breadth_first_search = brass_connections.createConnectionSearchStrategy(BrassConnectionSearchEnum.BREADTH_FIRST, -1);
			table.TableInterface<Integer, Integer> explored_cities = breadth_first_search.connectionSearch(cotton_city_id, comp_city_ids);

			boolean success = false;
			
			Iterator<Integer> explored_cities_iter = explored_cities.iterator();
			while(explored_cities_iter.hasNext())
			{
				int possible_port_city_id = explored_cities_iter.next();
				//try to sell to your ports first
				if (!success)
				{
					success = sellCottonToPort(cotton_city_id, possible_port_city_id, player_id, true, brass_players, brass_track);
				}
			}
			
			//try to sell to the cotton demand track if it is a safe sale
			boolean can_sell_to_cotton_demand_track = brass_cotton_demand_track.canSellCotton();
			boolean is_cotton_sale_safe = brass_cotton_demand_track.isCottonSaleSafe();
			if (!success && can_sell_to_cotton_demand_track && is_cotton_sale_safe)
			{
				int cotton_track_income = brass_cotton_demand_track.cottonTrackIncome();
				brass_cities.flipToken(cotton_city_id, BrassIndustryEnum.COTTON.getValue(), player_id, brass_players, brass_track);
				brass_players.increaseIncomeIndex(player_id, cotton_track_income, brass_track);
				success = true;
			}
			
			if (!success)
			{
				explored_cities_iter = explored_cities.iterator();
				while(explored_cities_iter.hasNext())
				{
					int possible_port_city_id = explored_cities_iter.next();
					//then try to sell cotton to other players' ports
					if (!success)
					{
						success = sellCottonToPort(cotton_city_id, possible_port_city_id, player_id, false, brass_players, brass_track);
					}
				}
			}
			
			//try to sell to the cotton demand track, taking a risk
			if (!success && can_sell_to_cotton_demand_track)
			{
				int cotton_track_income = brass_cotton_demand_track.cottonTrackIncome();
				success = (cotton_track_income >= 0);
				if (success)
				{
					brass_cities.flipToken(cotton_city_id, BrassIndustryEnum.COTTON.getValue(), player_id, brass_players, brass_track);
					brass_players.increaseIncomeIndex(player_id, cotton_track_income, brass_track);
				}
				else
				{
					//sale failed and the action counts (hopefully, some cotton mills succeeded)
					success = true;
				}
			}
		}
	}
	
	private boolean sellCottonToPort(int cotton_city_id, int possible_port_city_id, int player_id, boolean use_player_ports, BrassPlayers brass_players, BrassTrack brass_track)
	{
		//returns the player id of the port owner
		int unflipped_port_player_id = 0;
		if (use_player_ports)
		{
			if (brass_cities.hasPlayersUnflippedIndustry(possible_port_city_id, BrassIndustryEnum.PORT.getValue(), player_id))
			{
				unflipped_port_player_id = player_id;
			}
		}
		else
		{
			//uses the first unflipped port that it finds
			if (brass_cities.hasUnflippedIndustry(possible_port_city_id, BrassIndustryEnum.PORT.getValue()))
			{
				unflipped_port_player_id = brass_cities.getUnflippedIndustryPlayerID(possible_port_city_id, BrassIndustryEnum.PORT.getValue());
			}
		}
				
		if (unflipped_port_player_id > 0)
		{
			brass_cities.flipToken(cotton_city_id, BrassIndustryEnum.COTTON.getValue(), player_id, brass_players, brass_track);
			brass_cities.flipToken(possible_port_city_id, BrassIndustryEnum.PORT.getValue(), unflipped_port_player_id, brass_players, brass_track);
			return true;
		}
		
		return false;
	}
	
	public boolean canSellCotton(int player_id)
	{
		BrassFindPlayersUnflippedCottonMills find_player_cotton_mills = new BrassFindPlayersUnflippedCottonMills(player_id);
		brass_cities.execute(find_player_cotton_mills);  //search all cities for player cotton mills
		
		//player does not have any unflipped cotton mills
		List<Integer> player_cotton_mills = find_player_cotton_mills.getList();
		if (player_cotton_mills.size() == 0) return false;
		
		Iterator<Integer> cotton_mill_iter = player_cotton_mills.iterator();
		while(cotton_mill_iter.hasNext())
		{  
			int cotton_city_id = cotton_mill_iter.next();
			
			//check to see if the cotton mill is connected to a constructed port
			//if it is, check the cotton demand track to see if cotton can still be sold to it
			boolean is_connected_to_constructed_port = isCityConnectedToConstructedPort(cotton_city_id);
			boolean can_sell_to_cotton_demand_track = brass_cotton_demand_track.canSellCotton();
			if (is_connected_to_constructed_port && can_sell_to_cotton_demand_track)
			{
				return true;  //can sell to the cotton demand track
			}
			
			table.Comparator<Integer, Integer> comp_city_ids = new BrassCityIDComparator(true);
			//table.TableInterface<Integer, Integer> explored_cities = table.TableFactory.createTable(comp_city_ids);
			
			BrassConnectionSearchInterface breadth_first_search = brass_connections.createConnectionSearchStrategy(BrassConnectionSearchEnum.BREADTH_FIRST, -1);
			table.TableInterface<Integer, Integer> explored_cities = breadth_first_search.connectionSearch(cotton_city_id, comp_city_ids);
			
			Iterator<Integer> possible_port_iter = explored_cities.iterator();
			while (possible_port_iter.hasNext())
			{
				int possible_port_city_id = possible_port_iter.next();
				//util.CountCommand<BrassIndustry> unflipped_port_count_command = new BrassCountUnflippedIndustry(BrassIndustryEnum.PORT.getValue());
				boolean has_unflipped_port = brass_cities.hasUnflippedIndustry(possible_port_city_id, BrassIndustryEnum.PORT.getValue());
				if (has_unflipped_port) return true;  //can sell to an unflipped port
			}
		}
		return false;
	}
	
	public void moveIron(int iron_city_id, int player_id, BrassPlayers brass_players, BrassTrack brass_track)
	{
		if (iron_city_id < 20)
		{
			brass_cities.moveIron(iron_city_id, player_id, brass_players, brass_track);
		}
		else
		{
			int iron_demand_cost = brass_iron_demand_track.getCostToBuyFromDemandTrack();
			brass_iron_demand_track.buyFromDemandTrack();
			brass_players.payForCube(iron_demand_cost, player_id);
		}
	}
	
	public void moveCoal(int coal_city_id, int player_id, BrassPlayers brass_players, BrassTrack brass_track)
	{
		if (coal_city_id < 20)
		{
			brass_cities.moveCoal(coal_city_id, player_id, brass_players, brass_track);
		}
		else
		{
			int coal_demand_cost = brass_coal_demand_track.getCostToBuyFromDemandTrack();
			brass_coal_demand_track.buyFromDemandTrack();
			brass_players.payForCube(coal_demand_cost, player_id);
		}
	}
	
	public void moveCoalToDemandTrack(int player_id, BrassToken brass_token, BrassPlayers brass_players, BrassTrack brass_track)
	{
		int demand_track_money = 0;
		int num_coal_cubes = brass_token.getNumCubes();

		while(brass_coal_demand_track.canSellToDemandTrack() && num_coal_cubes > 0)
		{
			demand_track_money += brass_coal_demand_track.sellToDemandTrack();
			brass_token.removeCube(brass_players, brass_track);
			num_coal_cubes--;
		}
		
		brass_players.receiveDemandTrackMoney(demand_track_money, player_id);
	}
	
	public void moveIronToDemandTrack(int player_id, BrassToken brass_token, BrassPlayers brass_players, BrassTrack brass_track)
	{
		int demand_track_money = 0;
		int num_iron_cubes = brass_token.getNumCubes();

		while(brass_iron_demand_track.canSellToDemandTrack() && num_iron_cubes > 0)
		{
			demand_track_money += brass_iron_demand_track.sellToDemandTrack();
			brass_token.removeCube(brass_players, brass_track);
			num_iron_cubes--;
		}
		
		brass_players.receiveDemandTrackMoney(demand_track_money, player_id);
	}
	
	public boolean isLinkConnectedToConstructedPort(int link_id)
	{
		int[] connected_cities = brass_connections.getConnectedCities(link_id);
		boolean is_link_connected_to_constructed_port = isCityConnectedToConstructedPort(connected_cities[0]);
		if (!is_link_connected_to_constructed_port) is_link_connected_to_constructed_port = isCityConnectedToConstructedPort(connected_cities[1]);
		return is_link_connected_to_constructed_port;
	}
	
	public boolean isCityConnectedToConstructedPort(int city_id)
	{
		//find all connected cities to the specified city
		table.Comparator<Integer, Integer> comp_city_ids = new BrassCityIDComparator(true);
		int num_explored_cities = 0;
			
		BrassConnectionSearchInterface breadth_first_search = brass_connections.createConnectionSearchStrategy(BrassConnectionSearchEnum.BREADTH_FIRST, -1);
		table.TableInterface<Integer, Integer> explored_cities = breadth_first_search.connectionSearch(city_id, comp_city_ids);
		
		//look at each city to see if it has a constructed port
		//need to include the automatic ports
		Iterator<Integer> possible_port_cities_iter = explored_cities.iterator();
		while(possible_port_cities_iter.hasNext())
		{
			int possible_port_city_id = possible_port_cities_iter.next();
			if (brass_cities.hasConstructedPort(possible_port_city_id))
			{
				return true;
			}
		}
		
		return false;
	}

	//find closest coal location, preference given to the player's coal mine
	//returns -1 if no coal can be moved to the current location
	//if two or more players other than the active player have coal at the same depth
	//there is no way to specify the player's preference for taking the coal
	
	//keep calling the method below with an increasing depth
	//until whatever is being searched for is found or the depth limit is reached
	//in this starting case, want a city that contains a coal token with cubes still on it
	//simply look through the returned list to see if that city is present
	//also want to give preference to the current player's coal token if possible
	
	//a depth of zero will return the current city by itself
	public int canMoveCoal(int city_id, int player_id)
	{
		int coal_city_id = 0;
		int depth_limit = 0;
		
		table.Comparator<Integer, Integer> comp_city_ids = new BrassCityIDComparator(true);
		
		int num_explored_cities = 0;
		while (coal_city_id == 0)
		{
			BrassConnectionSearchInterface depth_limit_search = brass_connections.createConnectionSearchStrategy(BrassConnectionSearchEnum.DEPTH_LIMIT, depth_limit);
			table.TableInterface<Integer, Integer> explored_cities = depth_limit_search.connectionSearch(city_id, comp_city_ids);

			int table_size = explored_cities.tableSize();

			if (num_explored_cities == table_size)
			{
				return -1;  //no new cities were added at the new depth, so quit searching
			}
			num_explored_cities = table_size;
			
			//if the player has a coal city in the list, use that coal city first
			coal_city_id = getUnflippedCoalCity(player_id, explored_cities);
			depth_limit++;
		}
		
		return coal_city_id;
	}
	
	private int getUnflippedCoalCity(int player_id, table.TableInterface<Integer, Integer> explored_cities)
	{
		int coal_city_id = 0;
		Iterator<Integer> explored_cities_iter = explored_cities.iterator();
		while(explored_cities_iter.hasNext())
		{
			int test_city_id = explored_cities_iter.next();
			if (brass_cities.hasPlayersUnflippedIndustry(test_city_id, BrassIndustryEnum.COAL.getValue(), player_id))
			{
				return test_city_id;  //found a player coal mine so stop here
			}
			else if(brass_cities.hasUnflippedIndustry(test_city_id, BrassIndustryEnum.COAL.getValue()))
			{
				coal_city_id = test_city_id;
			}
		}
		
		return coal_city_id;
	}
	
	public int canMoveIron(int player_id)
	{
		return brass_cities.canMoveIron(player_id);
	}
	
	//can the player use an industry card to build in the city
	public boolean isCityConnectedToPlayerNetwork(int city_id, int player_id)
	{
		boolean is_player_present_in_city = (brass_cities.getNumTokensInCity(city_id, player_id) > 0);
		boolean does_player_have_link_into_city = brass_connections.doesPlayerHaveLinkIntoCity(city_id, player_id);
		
		return is_player_present_in_city || does_player_have_link_into_city;
	}

	public int getSelectedCity(int x, int y)
	{
		return brass_cities.getSelectedCity(x, y);
	}
	
	public int getNumTokensOnBoard(int player_id)
	{
		return brass_cities.getNumTokensOnBoard(player_id);
	}
	
	public int getNumTokensInCity(int city_id, int player_id)
	{
		return brass_cities.getNumTokensInCity(city_id, player_id);
	}
	
	public boolean canCityAcceptToken(int city_id, int industry_id)
	{
		return brass_cities.canCityAcceptIndustry(city_id, industry_id);
	}

	public BrassBoard(BrassXML brass_xml)  
	{
		gui.ImageLoader il = gui.ImageLoader.getImageLoader();
	   
		PixelDimension board_dimension = brass_xml.getPixelDimension("board");
		PixelPoint board_center = brass_xml.getPixelCenter("board");
       
		board_img = new DrawImage(il.getImage("images/brass_board.jpg"), "Brass Board", board_dimension.getWidth(), board_dimension.getHeight());
		board_img.showImage(board_center.getX(), board_center.getY());
		
		brass_connections = new BrassCanals(brass_xml);
		brass_rails = new BrassRails(brass_xml);
		
		brass_cities = new BrassCities(brass_xml);
		
		action_hot_spots = new ArrayList<HotSpot>();
		
		//create the action hot spots
		PixelDimension loan_dimension = brass_xml.getPixelDimension("loans");
		List<PixelPoint> loan_centers = brass_xml.getPixelCenters("loans");
		PixelPoint loan_center = loan_centers.get(0);
		HotSpot loan_ten = new HotSpot(BrassActionEnum.LOAN_10.getValue(), loan_center.getX(), loan_center.getY(), loan_dimension.getWidth(), loan_dimension.getHeight());
		action_hot_spots.add(loan_ten);
		
		loan_center = loan_centers.get(1);
		HotSpot loan_twenty = new HotSpot(BrassActionEnum.LOAN_20.getValue(), loan_center.getX(), loan_center.getY(), loan_dimension.getWidth(), loan_dimension.getHeight());
		action_hot_spots.add(loan_twenty);
		
		loan_center = loan_centers.get(2);
		HotSpot loan_thirty = new HotSpot(BrassActionEnum.LOAN_30.getValue(), loan_center.getX(), loan_center.getY(), loan_dimension.getWidth(), loan_dimension.getHeight());
		action_hot_spots.add(loan_thirty);
		
		PixelDimension build_dimension = brass_xml.getPixelDimension("build");
		PixelPoint build_center = brass_xml.getPixelCenter("build");
		HotSpot build_action = new HotSpot(BrassActionEnum.BUILD.getValue(), build_center.getX(), build_center.getY(), build_dimension.getWidth(), build_dimension.getHeight());
		action_hot_spots.add(build_action);
		
		PixelDimension link_dimension = brass_xml.getPixelDimension("link");
		PixelPoint link_center = brass_xml.getPixelCenter("link");
		HotSpot link_action = new HotSpot(BrassActionEnum.LINK.getValue(), link_center.getX(), link_center.getY(), link_dimension.getWidth(), link_dimension.getHeight());
		action_hot_spots.add(link_action);
		
		PixelDimension sell_dimension = brass_xml.getPixelDimension("sell_cotton");
		PixelPoint sell_center = brass_xml.getPixelCenter("sell_cotton");
		HotSpot sell_action = new HotSpot(BrassActionEnum.SELL.getValue(), sell_center.getX(), sell_center.getY(), sell_dimension.getWidth(), sell_dimension.getHeight());
		action_hot_spots.add(sell_action);
		
		PixelDimension tech_upgrade_dimension = brass_xml.getPixelDimension("tech_upgrade");
		PixelPoint tech_upgrade_center = brass_xml.getPixelCenter("tech_upgrade");
		HotSpot tech_upgrade_action = new HotSpot(BrassActionEnum.UPGRADE.getValue(), tech_upgrade_center.getX(), tech_upgrade_center.getY(), tech_upgrade_dimension.getWidth(), tech_upgrade_dimension.getHeight());
		action_hot_spots.add(tech_upgrade_action);
		
		PixelDimension double_card_dimension = brass_xml.getPixelDimension("double_card");
		PixelPoint double_card_center = brass_xml.getPixelCenter("double_card");
		HotSpot double_card_action = new HotSpot(BrassActionEnum.DOUBLE_CARD.getValue(), double_card_center.getX(), double_card_center.getY(), double_card_dimension.getWidth(), double_card_dimension.getHeight());
		action_hot_spots.add(double_card_action);
		
		PixelDimension double_rail_dimension = brass_xml.getPixelDimension("double_rail");
		PixelPoint double_rail_center = brass_xml.getPixelCenter("double_rail");
		HotSpot double_rail_action = new HotSpot(BrassActionEnum.DOUBLE_RAIL.getValue(), double_rail_center.getX(), double_rail_center.getY(), double_rail_dimension.getWidth(), double_rail_dimension.getHeight());
		action_hot_spots.add(double_rail_action);
		
		PixelDimension replace_dimension = brass_xml.getPixelDimension("replace");
		PixelPoint replace_center = brass_xml.getPixelCenter("replace");
		HotSpot replace_action = new HotSpot(BrassActionEnum.REPLACE.getValue(), replace_center.getX(), replace_center.getY(), replace_dimension.getWidth(), replace_dimension.getHeight());
		action_hot_spots.add(replace_action);
		
		PixelDimension discard_dimension = brass_xml.getPixelDimension("discard");
		PixelPoint discard_center = brass_xml.getPixelCenter("discard");
		HotSpot discard_action = new HotSpot(BrassActionEnum.DISCARD.getValue(), discard_center.getX(), discard_center.getY(), discard_dimension.getWidth(), discard_dimension.getHeight());
		action_hot_spots.add(discard_action);
		
		PixelDimension cancel_dimension = brass_xml.getPixelDimension("cancel");
		PixelPoint cancel_center = brass_xml.getPixelCenter("cancel");
		HotSpot cancel_action = new HotSpot(BrassActionEnum.CANCEL.getValue(), cancel_center.getX(), cancel_center.getY(), cancel_dimension.getWidth(), cancel_dimension.getHeight());
		action_hot_spots.add(cancel_action);
		
		PixelDimension coal_demand_track_dim = brass_xml.getPixelDimension("coal_demand");
		List<PixelPoint> coal_demand_track_centers = brass_xml.getPixelCenters("coal_demand");
		BrassDemandTrackFunction coal_function = new BrassDemandTrackCoalFunction();
		brass_coal_demand_track = new BrassDemandTrack(il.getImage("images/coal_demand_marker.jpg"), coal_demand_track_dim, coal_demand_track_centers, coal_function);
		
		PixelDimension iron_demand_track_dim = brass_xml.getPixelDimension("iron_demand");
		List<PixelPoint> iron_demand_track_centers = brass_xml.getPixelCenters("iron_demand");
		BrassDemandTrackFunction iron_function = new BrassDemandTrackIronFunction();
		brass_iron_demand_track = new BrassDemandTrack(il.getImage("images/iron_demand_marker.jpg"), iron_demand_track_dim, iron_demand_track_centers, iron_function);
		
		BrassDemandTrackFunction cotton_tile_function = new BrassDemandTrackCottonTileFunction();
		BrassDemandTrackFunction cotton_extra_income_function = new BrassDemandTrackCottonExtraIncomeFunction();
		brass_cotton_demand_track = new BrassCottonDemandTrack(brass_xml, cotton_tile_function, cotton_extra_income_function);
   }
   
	public void draw(Graphics g)
	{
		board_img.draw(g);
		brass_connections.draw(g);
		brass_cities.draw(g);
		
		brass_coal_demand_track.draw(g);
		brass_iron_demand_track.draw(g);
		brass_cotton_demand_track.draw(g);
	}
	
	public int getSelectedAction(int x, int y)
	{
		Iterator<HotSpot> action_iter = action_hot_spots.iterator();
		while(action_iter.hasNext())
		{
			HotSpot action_hot_spot = action_iter.next();
			if (action_hot_spot.isSelected(x, y))
			{
				return action_hot_spot.getHotSpotID();
			}
		}
		
		return 0;
	}
	
	public int getSelectedLink(int x, int y)
	{
		return brass_connections.getSelectedLink(x, y);
	}
	
	public int getCostToBuyFromCoalDemandTrack()
	{
		return brass_coal_demand_track.getCostToBuyFromDemandTrack();
	}
	
	public int getCostToBuyFromIronDemandTrack()
	{
		return brass_iron_demand_track.getCostToBuyFromDemandTrack();
	}
	
	public void buyFromCoalDemandTrack()
	{
		brass_coal_demand_track.buyFromDemandTrack();
	}
	
	public void buyFromIronDemandTrack()
	{
		brass_iron_demand_track.buyFromDemandTrack();
	}
}
