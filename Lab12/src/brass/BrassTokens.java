package brass;

import java.awt.Graphics;
import java.awt.Image;

import gui.PixelPoint;
import gui.PixelDimension;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

class BrassTokens
{
	private List<List<BrassToken>> brass_tokens; //each industry has a stack of tokens
	private List<PixelPoint> player_industry_locations;
	private List<BrassToken> all_brass_tokens;
	
	public int getTechLevel(int industry_id)
	{
		List<BrassToken> brass_industry_specific_tokens = brass_tokens.get(industry_id - 1);
		assert brass_industry_specific_tokens.size() > 0 : "No industry tokens remaining.";
		int tech_level = brass_industry_specific_tokens.get(0).getTechLevel();
		return tech_level;
	}
	
	public boolean isTechLevelRequirementMet(int industry_id, int brass_phase)
	{
		List<BrassToken> brass_industry_specific_tokens = brass_tokens.get(industry_id - 1);
		if (brass_industry_specific_tokens.size() == 0) return false;
		int tech_level = brass_industry_specific_tokens.get(0).getTechLevel();
		return (tech_level >= brass_phase);
	}
	
	public void discardToken(int token_id)
	{
		List<BrassToken> brass_industry_specific_tokens = brass_tokens.get(token_id - 1);
		assert brass_industry_specific_tokens.size() > 0 : "No token to discard.";
		brass_industry_specific_tokens.remove(0); 
	}
	
	public boolean isCoalRequired(int industry_id)
	{
		List<BrassToken> brass_industry_specific_tokens = brass_tokens.get(industry_id - 1);
		BrassToken brass_token = brass_industry_specific_tokens.get(0); 
		return brass_token.isCoalRequired();
	}
	
	public boolean isIronRequired(int industry_id)
	{
		List<BrassToken> brass_industry_specific_tokens = brass_tokens.get(industry_id - 1);
		BrassToken brass_token = brass_industry_specific_tokens.get(0); 
		return brass_token.isIronRequired();
	}
	
	//which industry token was selected (must have some of that industry remaining)
	public int getSelectedToken(int x, int y)
	{
		Iterator<List<BrassToken>> iter = brass_tokens.iterator();
		while(iter.hasNext())
		{
			List<BrassToken> industry_tokens = iter.next();
			int num_tokens = industry_tokens.size();
		
			if (num_tokens > 0)
			{
				BrassToken brass_token = industry_tokens.get(0);
				if (brass_token.isTokenSelected(x, y))
				{
					return brass_token.getIndustryID();
				}
			}
		}
		
		return 0;
	}
	
	public BrassToken removeTokenFromPlayer(int industry_id)
	{
		//assert that there are tokens in that list
		List<BrassToken> brass_industry_specific_tokens = brass_tokens.get(industry_id - 1);
		BrassToken brass_token = brass_industry_specific_tokens.remove(0); 
		return brass_token;
	}
	
	public int getIndustryCost(int industry_id)
	{
		List<BrassToken> brass_industry_specific_tokens = brass_tokens.get(industry_id - 1);
		BrassToken brass_token = brass_industry_specific_tokens.get(0); //get the top token
		int token_cost = brass_token.getTokenCost();
		return token_cost;
	}
	/*
	public boolean canPlayerAffordIndustry(int industry_id, int player_money)
	{
		List<BrassToken> brass_industry_specific_tokens = brass_tokens.get(industry_id - 1);
		BrassToken brass_token = brass_industry_specific_tokens.get(0); //get the top token
		int token_cost = brass_token.getTokenCost();
		return (player_money >= token_cost);
	}
	*/
	public void draw(Graphics g)
	{
		Iterator<List<BrassToken>> iter = brass_tokens.iterator();
		while(iter.hasNext())
		{
			List<BrassToken> industry_tokens = iter.next();
			int num_tokens = industry_tokens.size();
		
			if (num_tokens > 0)
			{
				BrassToken brass_token = industry_tokens.get(0);
				brass_token.drawPlayerToken(g);
			}
		}
	}
	
	public BrassTokens(int id, String color, BrassXML brass_xml)
	{
		all_brass_tokens = new ArrayList<BrassToken>();
		gui.ImageLoader il = gui.ImageLoader.getImageLoader();
		
		List<BrassToken> coal_tokens = new ArrayList<BrassToken>();
		List<BrassToken> cotton_tokens = new ArrayList<BrassToken>();
		List<BrassToken> iron_tokens = new ArrayList<BrassToken>();
		List<BrassToken> port_tokens = new ArrayList<BrassToken>();
		List<BrassToken> shipyard_tokens = new ArrayList<BrassToken>();

		PixelDimension token_big_dimension = brass_xml.getPixelDimension("big_tokens");
		PixelDimension token_small_dimension = brass_xml.getPixelDimension("small_tokens");
		player_industry_locations = brass_xml.getPixelCenters("big_tokens");
	
		int big_width = token_big_dimension.getWidth();
		int big_height = token_big_dimension.getHeight();
		int small_width = token_small_dimension.getWidth();
		int small_height = token_small_dimension.getHeight();
		
		PixelPoint player_coal_location = player_industry_locations.get(0);
		
		Image big_token = il.getImage("images/" + color + "/big/" + color + "_coal_1_big.jpg");
		Image small_token = il.getImage("images/" + color + "/small/" + color + "_coal_1_small.jpg");

		BrassToken coal_1 = new BrassToken(1, 1, 1, 5, 4, 1, 2, false, false, 1, id, big_token, big_width, big_height, small_token, small_width, small_height);
		coal_1.placePlayerToken(player_coal_location.getX(), player_coal_location.getY());
		coal_tokens.add(coal_1);
		all_brass_tokens.add(coal_1);
		
		big_token = il.getImage("images/" + color + "/big/" + color + "_coal_2_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_coal_2_small.jpg");
		BrassToken coal_2 = new BrassToken(2, 1, 2, 7, 7, 2, 3, false, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		coal_2.placePlayerToken(player_coal_location.getX(), player_coal_location.getY());
		coal_tokens.add(coal_2);
		all_brass_tokens.add(coal_2);
		
		coal_2 = new BrassToken(3, 1, 2, 7, 7, 2, 3, false, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		coal_2.placePlayerToken(player_coal_location.getX(), player_coal_location.getY());
		coal_tokens.add(coal_2);
		all_brass_tokens.add(coal_2);

		big_token = il.getImage("images/" + color + "/big/" + color + "_coal_3_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_coal_3_small.jpg");
		BrassToken coal_3 = new BrassToken(4, 1, 3, 8, 6, 3, 4, false, true, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		coal_3.placePlayerToken(player_coal_location.getX(), player_coal_location.getY());
		coal_tokens.add(coal_3);
		all_brass_tokens.add(coal_3);

		coal_3 = new BrassToken(5, 1, 3, 8, 6, 3, 4, false, true, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		coal_3.placePlayerToken(player_coal_location.getX(), player_coal_location.getY());
		coal_tokens.add(coal_3);
		all_brass_tokens.add(coal_3);

		big_token = il.getImage("images/" + color + "/big/" + color + "_coal_4_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_coal_4_small.jpg");
		BrassToken coal_4 = new BrassToken(6, 1, 4, 10, 5, 4, 5, false, true, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		coal_4.placePlayerToken(player_coal_location.getX(), player_coal_location.getY());
		coal_tokens.add(coal_4);
		all_brass_tokens.add(coal_4);

		coal_4 = new BrassToken(7, 1, 4, 10, 5, 4, 5, false, true, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		coal_4.placePlayerToken(player_coal_location.getX(), player_coal_location.getY());
		coal_tokens.add(coal_4);
		all_brass_tokens.add(coal_4);

		PixelPoint player_cotton_location = player_industry_locations.get(1);
		
		big_token = il.getImage("images/" + color + "/big/" + color + "_cotton_1_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_cotton_1_small.jpg");
		BrassToken cotton_1 = new BrassToken(8, 2, 1, 12, 5, 3, 0, false, false, 1, id, big_token, big_width, big_height, small_token, small_width, small_height);
		cotton_1.placePlayerToken(player_cotton_location.getX(), player_cotton_location.getY());
		cotton_tokens.add(cotton_1);
		all_brass_tokens.add(cotton_1);

		cotton_1 = new BrassToken(9, 2, 1, 12, 5, 3, 0, false, false, 1, id, big_token, big_width, big_height, small_token, small_width, small_height);
		cotton_1.placePlayerToken(player_cotton_location.getX(), player_cotton_location.getY());
		cotton_tokens.add(cotton_1);
		all_brass_tokens.add(cotton_1);

		cotton_1 = new BrassToken(10, 2, 1, 12, 5, 3, 0, false, false, 1, id, big_token, big_width, big_height, small_token, small_width, small_height);
		cotton_1.placePlayerToken(player_cotton_location.getX(), player_cotton_location.getY());
		cotton_tokens.add(cotton_1);
		all_brass_tokens.add(cotton_1);

		big_token = il.getImage("images/" + color + "/big/" + color + "_cotton_2_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_cotton_2_small.jpg");
		BrassToken cotton_2 = new BrassToken(11, 2, 2, 14, 4, 5, 0, true, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		cotton_2.placePlayerToken(player_cotton_location.getX(), player_cotton_location.getY());
		cotton_tokens.add(cotton_2);
		all_brass_tokens.add(cotton_2);

		cotton_2 = new BrassToken(12, 2, 2, 14, 4, 5, 0, true, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		cotton_2.placePlayerToken(player_cotton_location.getX(), player_cotton_location.getY());
		cotton_tokens.add(cotton_2);
		all_brass_tokens.add(cotton_2);

		cotton_2 = new BrassToken(13, 2, 2, 14, 4, 5, 0, true, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		cotton_2.placePlayerToken(player_cotton_location.getX(), player_cotton_location.getY());
		cotton_tokens.add(cotton_2);
		all_brass_tokens.add(cotton_2);

		big_token = il.getImage("images/" + color + "/big/" + color + "_cotton_3_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_cotton_3_small.jpg");
		BrassToken cotton_3 = new BrassToken(14, 2, 3, 16, 3, 9, 0, true, true, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		cotton_3.placePlayerToken(player_cotton_location.getX(), player_cotton_location.getY());
		cotton_tokens.add(cotton_3);
		all_brass_tokens.add(cotton_3);

		cotton_3 = new BrassToken(15, 2, 3, 16, 3, 9, 0, true, true, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		cotton_3.placePlayerToken(player_cotton_location.getX(), player_cotton_location.getY());
		cotton_tokens.add(cotton_3);
		all_brass_tokens.add(cotton_3);

		cotton_3 = new BrassToken(16, 2, 3, 16, 3, 9, 0, true, true, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		cotton_3.placePlayerToken(player_cotton_location.getX(), player_cotton_location.getY());
		cotton_tokens.add(cotton_3);
		all_brass_tokens.add(cotton_3);

		big_token = il.getImage("images/" + color + "/big/" + color + "_cotton_4_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_cotton_4_small.jpg");
		BrassToken cotton_4 = new BrassToken(17, 2, 4, 18, 2, 12, 0, true, true, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		cotton_4.placePlayerToken(player_cotton_location.getX(), player_cotton_location.getY());
		cotton_tokens.add(cotton_4);
		all_brass_tokens.add(cotton_4);

		cotton_4 = new BrassToken(18, 2, 4, 18, 2, 12, 0, true, true, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		cotton_4.placePlayerToken(player_cotton_location.getX(), player_cotton_location.getY());
		cotton_tokens.add(cotton_4);
		all_brass_tokens.add(cotton_4);

		cotton_4 = new BrassToken(19, 2, 4, 18, 2, 12, 0, true, true, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		cotton_4.placePlayerToken(player_cotton_location.getX(), player_cotton_location.getY());
		cotton_tokens.add(cotton_4);
		all_brass_tokens.add(cotton_4);

		PixelPoint player_iron_location = player_industry_locations.get(2);
		
		big_token = il.getImage("images/" + color + "/big/" + color + "_iron_1_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_iron_1_small.jpg");
		BrassToken iron_1 = new BrassToken(20, 3, 1, 5, 3, 3, 4, true, false, 1, id, big_token, big_width, big_height, small_token, small_width, small_height);
		iron_1.placePlayerToken(player_iron_location.getX(), player_iron_location.getY());
		iron_tokens.add(iron_1);
		all_brass_tokens.add(iron_1);

		big_token = il.getImage("images/" + color + "/big/" + color + "_iron_2_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_iron_2_small.jpg");
		BrassToken iron_2 = new BrassToken(21, 3, 2, 7, 3, 5, 4, true, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		iron_2.placePlayerToken(player_iron_location.getX(), player_iron_location.getY());
		iron_tokens.add(iron_2);
		all_brass_tokens.add(iron_2);

		big_token = il.getImage("images/" + color + "/big/" + color + "_iron_3_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_iron_3_small.jpg");
		BrassToken iron_3 = new BrassToken(22, 3, 3, 9, 2, 7, 5, true, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		iron_3.placePlayerToken(player_iron_location.getX(), player_iron_location.getY());
		iron_tokens.add(iron_3);
		all_brass_tokens.add(iron_3);

		big_token = il.getImage("images/" + color + "/big/" + color + "_iron_4_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_iron_4_small.jpg");
		BrassToken iron_4 = new BrassToken(23, 3, 4, 12, 1, 9, 6, true, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		iron_4.placePlayerToken(player_iron_location.getX(), player_iron_location.getY());
		iron_tokens.add(iron_4);
		all_brass_tokens.add(iron_4);

		PixelPoint player_port_location = player_industry_locations.get(3);
		
		big_token = il.getImage("images/" + color + "/big/" + color + "_port_1_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_port_1_small.jpg");
		BrassToken port_1 = new BrassToken(24, 4, 1, 6, 3, 2, 0, false, false, 1, id, big_token, big_width, big_height, small_token, small_width, small_height);
		port_1.placePlayerToken(player_port_location.getX(), player_port_location.getY());
		port_tokens.add(port_1);
		all_brass_tokens.add(port_1);

		port_1 = new BrassToken(25, 4, 1, 6, 3, 2, 0, false, false, 1, id, big_token, big_width, big_height, small_token, small_width, small_height);
		port_1.placePlayerToken(player_port_location.getX(), player_port_location.getY());
		port_tokens.add(port_1);
		all_brass_tokens.add(port_1);

		big_token = il.getImage("images/" + color + "/big/" + color + "_port_2_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_port_2_small.jpg");
		BrassToken port_2 = new BrassToken(26, 4, 2, 7, 3, 4, 0, false, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		port_2.placePlayerToken(player_port_location.getX(), player_port_location.getY());
		port_tokens.add(port_2);
		all_brass_tokens.add(port_2);

		port_2 = new BrassToken(27, 4, 2, 7, 3, 4, 0, false, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		port_2.placePlayerToken(player_port_location.getX(), player_port_location.getY());
		port_tokens.add(port_2);
		all_brass_tokens.add(port_2);

		big_token = il.getImage("images/" + color + "/big/" + color + "_port_3_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_port_3_small.jpg");
		BrassToken port_3 = new BrassToken(28, 4, 3, 8, 4, 6, 0, false, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		port_3.placePlayerToken(player_port_location.getX(), player_port_location.getY());
		port_tokens.add(port_3);
		all_brass_tokens.add(port_3);

		port_3 = new BrassToken(29, 4, 3, 8, 4, 6, 0, false, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		port_3.placePlayerToken(player_port_location.getX(), player_port_location.getY());
		port_tokens.add(port_3);
		all_brass_tokens.add(port_3);

		big_token = il.getImage("images/" + color + "/big/" + color + "_port_4_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_port_4_small.jpg");
		BrassToken port_4 = new BrassToken(30, 4, 4, 9, 4, 9, 0, false, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		port_4.placePlayerToken(player_port_location.getX(), player_port_location.getY());
		port_tokens.add(port_4);
		all_brass_tokens.add(port_4);

		port_4 = new BrassToken(31, 4, 4, 9, 4, 9, 0, false, false, 3, id, big_token, big_width, big_height, small_token, small_width, small_height);
		port_4.placePlayerToken(player_port_location.getX(), player_port_location.getY());
		port_tokens.add(port_4);
		all_brass_tokens.add(port_4);

		PixelPoint player_shipyard_location = player_industry_locations.get(4);
		
		big_token = il.getImage("images/" + color + "/big/" + color + "_shipyard_0_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_shipyard_0_small.jpg");
		BrassToken shipyard_1 = new BrassToken(32, 5, 0, 0, 0, 0, 0, false, false, 0, id, big_token, big_width, big_height, small_token, small_width, small_height);
		shipyard_1.placePlayerToken(player_shipyard_location.getX(), player_shipyard_location.getY());
		shipyard_tokens.add(shipyard_1);
		all_brass_tokens.add(shipyard_1);

		shipyard_1 = new BrassToken(33, 5, 0, 0, 0, 0, 0, false, false, 0, id, big_token, big_width, big_height, small_token, small_width, small_height);
		shipyard_1.placePlayerToken(player_shipyard_location.getX(), player_shipyard_location.getY());
		shipyard_tokens.add(shipyard_1);
		all_brass_tokens.add(shipyard_1);

		big_token = il.getImage("images/" + color + "/big/" + color + "_shipyard_1_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_shipyard_1_small.jpg");
		BrassToken shipyard_2 = new BrassToken(34, 5, 1, 16, 2, 10, 0, true, true, 1, id, big_token, big_width, big_height, small_token, small_width, small_height);
		shipyard_2.placePlayerToken(player_shipyard_location.getX(), player_shipyard_location.getY());
		shipyard_tokens.add(shipyard_2);
		all_brass_tokens.add(shipyard_2);

		shipyard_2 = new BrassToken(35, 5, 1, 16, 2, 10, 0, true, true, 1, id, big_token, big_width, big_height, small_token, small_width, small_height);
		shipyard_2.placePlayerToken(player_shipyard_location.getX(), player_shipyard_location.getY());
		shipyard_tokens.add(shipyard_2);
		all_brass_tokens.add(shipyard_2);

		big_token = il.getImage("images/" + color + "/big/" + color + "_shipyard_2_big.jpg");
		small_token = il.getImage("images/" + color + "/small/" + color + "_shipyard_2_small.jpg");
		BrassToken shipyard_3 = new BrassToken(36, 5, 2, 25, 1, 18, 0, true, true, 2, id, big_token, big_width, big_height, small_token, small_width, small_height);
		shipyard_3.placePlayerToken(player_shipyard_location.getX(), player_shipyard_location.getY());
		shipyard_tokens.add(shipyard_3);
		all_brass_tokens.add(shipyard_3);

		shipyard_3 = new BrassToken(37, 5, 2, 25, 1, 18, 0, true, true, 2, id, big_token, big_width, big_height, small_token, small_width, small_height);
		shipyard_3.placePlayerToken(player_shipyard_location.getX(), player_shipyard_location.getY());
		shipyard_tokens.add(shipyard_3);
		all_brass_tokens.add(shipyard_3);

		brass_tokens = new ArrayList<List<BrassToken>>();
		brass_tokens.add(coal_tokens);
		brass_tokens.add(cotton_tokens);
		brass_tokens.add(iron_tokens);
		brass_tokens.add(port_tokens);
		brass_tokens.add(shipyard_tokens);
	}
}
