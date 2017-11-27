package brass;

import java.awt.Graphics;

import gui.DrawImage;
import gui.PixelPoint;
import gui.PixelDimension;

class BrassDemandTrackItem
{
	private DrawImage demand_track_img;
	private PixelPoint demand_track_img_center;
	private int amount;
	private int item_id;
	
	public int getID()
	{
		return item_id;
	}
	
	public int getAmount()
	{
		return amount;
	}
	
	public void draw(Graphics g)
	{
		demand_track_img.draw(g);
	}
	
	public void showDemandTrackImage()
	{
		demand_track_img.showImage(demand_track_img_center.getX(), demand_track_img_center.getY());
	}
	
	public void hideDemandTrackImage()
	{
		demand_track_img.hideImage();
	}
	
	public BrassDemandTrackItem(int id, int amt, PixelPoint img_center, DrawImage track_img)
	{
		item_id = id;
		amount = amt;
		demand_track_img = track_img;
		demand_track_img_center = img_center;
	}
}
