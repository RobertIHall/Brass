package brass;

import java.awt.Graphics;

import gui.DrawImage;
import gui.PixelPoint;
import gui.PixelDimension;

class BrassCottonDemandTile
{
	private DrawImage cotton_demand_tile;
	private PixelPoint cotton_demand_tile_center;
	
	private int tile_adjustment;
	private int tile_id;
	
	public void draw(Graphics g)
	{
		cotton_demand_tile.draw(g);
	}
	
	public int getAdjustment()
	{
		return tile_adjustment;
	}
	
	public void showCottonDemandTile()
	{
		cotton_demand_tile.showImage(cotton_demand_tile_center.getX(), cotton_demand_tile_center.getY());
	}
	
	public void hideCottonDemandTile()
	{
		cotton_demand_tile.hideImage();
	}
	
	//0: 2, 1: 2, 2: 4, 3: 3, 4: 1
	public BrassCottonDemandTile(int id, int adjust, PixelPoint tile_center, DrawImage tile_img)
	{				
		tile_id = id;
		tile_adjustment = adjust;
		
		cotton_demand_tile = tile_img;
		cotton_demand_tile_center = tile_center;
	}
}
