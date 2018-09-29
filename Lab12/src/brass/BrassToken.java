package brass;

import gui.DrawImage;
import gui.PixelPoint;

import java.awt.Image;
import java.awt.Graphics;
import java.util.List;

class BrassToken
{
	private DrawImage big_image;  //not yet built
	private DrawImage small_image;  //after built

	private int token_id;
	private int player_id;  	//owning player
	private int industry_id;
	
	private int cost;
	private int victory;
	private int cubes;
	private boolean coal_requirement;
	private boolean iron_requirement;
	private int income;
	private int tech_level;
	
	private boolean flipped;
	private int period;
	
	public int getTechLevel()
	{
		return tech_level;
	}
	
	public void flipToken()
	{
		flipped = true;
	}
	
	public int getIncome()
	{
		return income;
	}
	
	public int getIndustryType()
	{
		return industry_id;
	}
	
	public void removeCube(BrassPlayers brass_players, BrassTrack brass_track)
	{
		assert (cubes > 0) : "No cubes to remove.";
		cubes--;
		
		if (cubes == 0)
		{
			flipped = true;
			brass_players.increaseIncomeIndex(player_id, income, brass_track);
		}
	}
	
	public boolean isIronRequired()
	{
		return iron_requirement;
	}
	
	public boolean isCoalRequired()
	{
		return coal_requirement;
	}
	
	public int getNumCubes()
	{
		return cubes;
	}
	
	public int getTokenCost()
	{
		return cost;
	}
	
	public boolean isFlipped()
	{
		return flipped;
	}
	
	public int getPlayerID()
	{
		return player_id;
	}
	
	public int getVictoryPoints()
	{
		return victory;
	}
	
	public int getIndustryID()
	{
		return industry_id;
	}
	
	public boolean isTokenSelected(int x, int y)
	{
		return big_image.isSelected(x, y);
	}
	
	public void moveTokenFromPlayerToCity(int x, int y)
	{
		big_image.hideImage();
		small_image.showImage(x, y);
	}
	
	//put the token on the player's display in the correct location based on industry
	public void placePlayerToken(int x, int y)
	{
		big_image.showImage(x, y);
	}

	public void drawPlayerToken(Graphics g)
	{
		big_image.draw(g);
	}
	
	public void drawCityToken(Graphics g, gui.DrawRectangle resource_cube, List<gui.PixelPoint> resource_cube_centers, gui.DrawOval flip_circle, gui.PixelPoint flip_circle_center)
	{
		java.awt.Color flip_color = new java.awt.Color(229, 151, 212);

		java.awt.Color color = new java.awt.Color (0, 0, 0);
		java.awt.Color outline_color = new java.awt.Color (254, 124, 0);
		
		if (industry_id == 3 && !flipped)
		{
			java.awt.Color temp = outline_color;
			outline_color = color;
			color = temp;
		}
		
		small_image.draw(g);
		int top = small_image.getTop();
		int left = small_image.getLeft();
		for(int i = 1; i <= cubes; i++)
		{
			gui.PixelPoint cube_center = resource_cube_centers.get(i - 1);
			int cube_x = left + cube_center.getX();
			int cube_y = top + cube_center.getY();
			resource_cube.draw(g, color, cube_x, cube_y);
		}
		
		if (flipped)
		{
			flip_circle.draw(g, flip_color, color, left + flip_circle_center.getX(), top + flip_circle_center.getY());
		}
	}
	
	public BrassToken(int t_id, int industry, int tech, int cost, int income, int victory, int cubes, boolean coal, boolean iron, int period, int player, Image big, int w_b, int h_b, Image small, int w_s, int h_s)
	{	
		token_id = t_id;
		big_image = new DrawImage(big, "Big Token", w_b, h_b);
		small_image = new DrawImage(small, "Small Token", w_s, h_s);
		
		industry_id = industry;
		tech_level = tech;
		this.cost = cost;
		this.income = income;
		this.victory = victory;
		coal_requirement = coal;
		iron_requirement = iron;
		this.period = period;
		this.cubes = cubes;
		
		player_id = player;
		flipped = false;
	}
}
