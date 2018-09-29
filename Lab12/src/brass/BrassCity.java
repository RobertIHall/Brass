package brass;

import java.awt.Graphics;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

class BrassCity implements util.Execute<BrassIndustry>
{
	private int brass_city_id;
	private String brass_city_name;
	private List<BrassIndustry> brass_industries;
	
	private boolean external_location;
	private int auto_connection_coin;
	
	public boolean isCityFull()
	{
		for (BrassIndustry brass_industry : brass_industries)
		{
			if (!brass_industry.isIndustryConstructed()) return false;
		}
		return true;
	}
	
	public void replaceTokenInCity(BrassToken brass_token)
	{
		for (BrassIndustry brass_industry : brass_industries)
		{
			boolean can_replace = brass_industry.canCityReplaceIndustry(brass_token);
			if (can_replace)
			{
				brass_industry.replaceTokenInCity(brass_token);
				return;
			}
		}
		//lab 8 assert
		assert false : "City cannot accommodate the requested industry.";
	}
	
	public void placeTokenInCity(BrassToken brass_token)
	{
		for (BrassIndustry brass_industry : brass_industries)
		{
			boolean can_accept = brass_industry.canCityAcceptIndustry(brass_token.getIndustryID());
			if (can_accept)
			{
				brass_industry.placeTokenInCity(brass_token);
				return;
			}
		}
		//lab 8 assert
		assert false : "City cannot accommodate the requested industry.";
	}
	
	public boolean canCityReplaceToken(int industry_id, int replace_tech_level, int player_id)
	{
		for (BrassIndustry brass_industry : brass_industries)
		{
			if (brass_industry.getPlayerID() == player_id && brass_industry.getIndustryType() == industry_id)
			{
				int current_tech_level = brass_industry.getTechLevel();
				if (current_tech_level < replace_tech_level) return true;
			}
		}
		return false;
	}
	
	public int getIndustryVictoryPoints(int player_id)
	{
		int victory_points = 0;
		for (BrassIndustry brass_industry : brass_industries)
		{
			if (brass_industry.isFlipped() && brass_industry.getPlayerID() == player_id)
			{
				victory_points += brass_industry.getVictoryPoints();
			}
		}
		
		return victory_points;
	}
	
	public int getConnectionVictoryPoints()
	{
		int connection_coin = auto_connection_coin;
		
		for (BrassIndustry brass_industry : brass_industries)
		{
			if (brass_industry.isFlipped())
			{
				connection_coin += 1;
			}
		}
		
		return connection_coin;
	}
	
	public void clearLevel1Industries()
	{
		for (BrassIndustry brass_industry : brass_industries)
		{
			brass_industry.clearLevel1Industry();
		}
	}
	
	public boolean isExternalLocation()
	{
		return external_location;
	}
	
	public void externalLocation()
	{
		external_location = true;
	}
	
	public void connectionCoin(int connection_coin)
	{
		auto_connection_coin = connection_coin;
	}
	
	public int getUnflippedIndustryPlayerID(int industry_id)
	{
		for (BrassIndustry brass_industry : brass_industries)
		{
			if (brass_industry.isUnflippedIndustry(industry_id))
			{
				return brass_industry.getPlayerID();
			}
		}
		
		assert false : "Industry of requested type not found.";
		return 0;
	}
	
	public void flipToken(int industry_id, int player_id, BrassPlayers brass_players, BrassTrack brass_track)
	{
		for (BrassIndustry brass_industry : brass_industries)
		{
			if (!brass_industry.isFlipped() && brass_industry.getIndustryType() == industry_id && brass_industry.getPlayerID() == player_id)
			{
				brass_industry.flipIndustry();
				brass_players.increaseIncomeIndex(player_id, brass_industry.getIncome(), brass_track);
				return;
			}
		}
		
		//should not reach this point
		assert false : "Requested token was not found.";
	}
	
	public void removeCubeFromCity(int industry_id, int player_id, BrassPlayers brass_players, BrassTrack brass_track)
	{
		int count = 1;
		int industry_loc = 0;
		Iterator<BrassIndustry> iter = brass_industries.iterator();
		while(iter.hasNext())
		{
			BrassIndustry brass_industry = iter.next();
			if (!brass_industry.isFlipped())
			{
				if (brass_industry.getIndustryType() == industry_id && brass_industry.getPlayerID() == player_id)
				{
					industry_loc = count;  //found the player's industry, so use that
					break;
				}
				
				else if (brass_industry.getIndustryType() == industry_id)
				{
					industry_loc = count;
				}
			}

			count++;
		}

		assert industry_loc > 0 : "Industry not found.";

		BrassIndustry brass_industry = brass_industries.get(industry_loc - 1);
		brass_industry.removeCube(brass_players, brass_track);
	}
	
	public void execute(util.Command<BrassIndustry> command)
	{
		Iterator<BrassIndustry> industry_iter = brass_industries.iterator();
		while(industry_iter.hasNext())
		{
			BrassIndustry brass_industry = industry_iter.next();
			command.execute(brass_industry);
		}
	}
	
	public boolean canCityAcceptIndustry(int industry_id)
	{
		Iterator<BrassIndustry> brass_industry_iter = brass_industries.iterator();
		while (brass_industry_iter.hasNext())
		{
			BrassIndustry brass_industry = brass_industry_iter.next();
			boolean can_accept = brass_industry.canCityAcceptIndustry(industry_id);
			if (can_accept) return true;
		}
		return false;
	}
	
	public void draw(Graphics g, gui.DrawRectangle resource_cube, List<gui.PixelPoint> resource_cube_centers, gui.DrawOval flip_circle, gui.PixelPoint flip_circle_center)
	{
		Iterator<BrassIndustry> industry_iter = brass_industries.iterator();
		while(industry_iter.hasNext())
		{
			BrassIndustry brass_industry = industry_iter.next();
			brass_industry.draw(g, resource_cube, resource_cube_centers, flip_circle, flip_circle_center);
		}
	}
	
	public BrassCity(int city_id, String city_name)
	{
		brass_city_id = city_id;
		brass_city_name = city_name;
		brass_industries = new ArrayList<BrassIndustry>();
		
		external_location = false;
		auto_connection_coin = 0;
	}
	
	//no city has more than 4 industry locations
	public void addIndustry(BrassIndustry brass_industry)
	{
		brass_industries.add(brass_industry);
		assert (brass_industries.size() <= 4) : "Add brass industry error.";
	}
	
	public boolean isCitySelected(int x, int y)
	{
		Iterator<BrassIndustry> industry_iter = brass_industries.iterator();
		while(industry_iter.hasNext())
		{
			BrassIndustry brass_industry = industry_iter.next();
			if (brass_industry.isIndustrySelected(x, y))
			{
				return true;
			}
		}
		
		return false;
	}
	
	//a player cannot have more than 4 tokens in a city (limited by number of industries)
	public int getNumTokensInCity(int player_id)
	{
		int count = 0;
		Iterator<BrassIndustry> industry_iter = brass_industries.iterator();
		while(industry_iter.hasNext())
		{
			BrassIndustry brass_industry = industry_iter.next();
			if (brass_industry.getPlayerID() == player_id)
			{
				count++;
			}
		}
		
		assert (count <= 4) : "Number of tokens in city error.";
		return count;
	}
	
	public int getCityID()
	{
		return brass_city_id;
	}
	
	public String getCityName()
	{
		return brass_city_name;
	}
	
	public int getSelectedIndustry(int x, int y)
	{
		Iterator<BrassIndustry> industry_iter = brass_industries.iterator();
		
		while(industry_iter.hasNext())
		{
			BrassIndustry brass_industry = industry_iter.next();
			boolean industry_selected = brass_industry.isIndustrySelected(x, y);
			if (industry_selected)
			{
				return brass_industry.getIndustryID();
			}
		}
		
		return 0;
	}
}
