package brass;

import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;

import util.QueueLinked;

import gui.PixelPoint;
import gui.PixelDimension;
import gui.DrawImage;

class BrassCottonDemandTrack
{
	private int cotton_demand_index;

	private List<PixelPoint> cotton_demand_track_locs;
	private DrawImage cotton_demand_track_image;
	
	private List<BrassDemandTrackItem> brass_cotton_demand_tiles_sorted;
	private BrassDemandTrackItem current_cotton_demand_tile;
	private BrassDemandTrackItem back_cotton_demand_tile;
	
	private util.StackLinked<BrassDemandTrackItem> unflipped_stack;
	private util.StackLinked<BrassDemandTrackItem> flipped_stack;
	
	BrassDemandTrackFunction cotton_extra_income_function;
	
	//draw current cotton demand location and the cotton demand change tiles
	public void draw(Graphics g)
	{
		cotton_demand_track_image.draw(g);
		current_cotton_demand_tile.draw(g);
	}
	
	public int getCottonDemandIndex()
	{
		return cotton_demand_index;
	}
	/*
	public int getBrassDemandTrackValue(int demand_index)
	{
		if (demand_index <= 2) return 0;
		else if (demand_index <= 4) return 1;
		else if (demand_index <= 8) return 2;
		else if (demand_index <= 11) return 3;
		else return 4;
	}
	*/
	public BrassCottonDemandTrack(BrassXML brass_xml, BrassDemandTrackFunction cotton_tile_function, BrassDemandTrackFunction extra_income_function)
	{
		cotton_extra_income_function = extra_income_function;
		cotton_demand_track_locs = brass_xml.getPixelCenters("cotton_demand_track");
		PixelPoint cotton_demand_track_loc = cotton_demand_track_locs.get(0);
		PixelDimension cotton_demand_track_dim = brass_xml.getPixelDimension("cotton_demand_track");
		
		PixelPoint cotton_demand_tile_loc = brass_xml.getPixelCenter("cotton_demand_tiles");
		PixelDimension cotton_demand_tile_dim = brass_xml.getPixelDimension("cotton_demand_tiles");
		
		brass_cotton_demand_tiles_sorted = new ArrayList<BrassDemandTrackItem>();
		
		gui.ImageLoader il = gui.ImageLoader.getImageLoader();
		java.awt.Image cotton_demand_image = il.getImage("images/cotton_demand_marker.png");
		cotton_demand_track_image = new DrawImage(cotton_demand_image, "Cotton Demand Location", cotton_demand_track_dim.getWidth(), cotton_demand_track_dim.getHeight());
		cotton_demand_track_image.showImage(cotton_demand_track_loc.getX(), cotton_demand_track_loc.getY());
		
		DrawImage[] cotton_demand_tile_images = new DrawImage[5];
		cotton_demand_tile_images[0] = new DrawImage(il.getImage("images/cotton_demand_tile_0.jpg"), "Cotton Demand Tile: 0", cotton_demand_tile_dim.getWidth(), cotton_demand_tile_dim.getHeight());
		cotton_demand_tile_images[1] = new DrawImage(il.getImage("images/cotton_demand_tile_1.jpg"), "Cotton Demand Tile: -1", cotton_demand_tile_dim.getWidth(), cotton_demand_tile_dim.getHeight());
		cotton_demand_tile_images[2] = new DrawImage(il.getImage("images/cotton_demand_tile_2.jpg"), "Cotton Demand Tile: -2", cotton_demand_tile_dim.getWidth(), cotton_demand_tile_dim.getHeight());
		cotton_demand_tile_images[3] = new DrawImage(il.getImage("images/cotton_demand_tile_3.jpg"), "Cotton Demand Tile: -3", cotton_demand_tile_dim.getWidth(), cotton_demand_tile_dim.getHeight());
		cotton_demand_tile_images[4] = new DrawImage(il.getImage("images/cotton_demand_tile_4.jpg"), "Cotton Demand Tile: -4", cotton_demand_tile_dim.getWidth(), cotton_demand_tile_dim.getHeight());
		
		for (int i = 1; i <= 12; i++)
		{
			int value = cotton_tile_function.getBrassDemandTrackValue(i);
			DrawImage draw_img = cotton_demand_tile_images[value];
			BrassDemandTrackItem cotton_demand_tile = new BrassDemandTrackItem(i, value, cotton_demand_tile_loc, draw_img);
			brass_cotton_demand_tiles_sorted.add(cotton_demand_tile);
		}
		
		DrawImage cotton_demand_tile_img = new DrawImage(il.getImage("images/cotton_demand_tile_back.jpg"), "Cotton Demand Tile: Back", cotton_demand_tile_dim.getWidth(), cotton_demand_tile_dim.getHeight());
		back_cotton_demand_tile = new BrassDemandTrackItem(0, -1, cotton_demand_tile_loc, cotton_demand_tile_img);
		
		reset();
	}
	
	public void reset()
	{
		cotton_demand_index = 0;
		
		unflipped_stack = new util.StackLinked<BrassDemandTrackItem>();
		flipped_stack = new util.StackLinked<BrassDemandTrackItem>();
		
		util.Permutation permute = util.PermutationFactory.getPermutation("resources/brass_cotton_demand_tile_shuffle.txt", 12, 12);
		while(permute.hasNext())
		{
			int adjust_index = permute.next();
			flipped_stack.push(brass_cotton_demand_tiles_sorted.get(adjust_index - 1));
		}
		
		while(!flipped_stack.isEmpty())
		{
			unflipped_stack.push(flipped_stack.pop());
		}

		PixelPoint cotton_demand_track_loc = cotton_demand_track_locs.get(cotton_demand_index);
		cotton_demand_track_image.showImage(cotton_demand_track_loc.getX(), cotton_demand_track_loc.getY());
		current_cotton_demand_tile = back_cotton_demand_tile;
		current_cotton_demand_tile.showDemandTrackImage();
	}
	
	public boolean canSellCotton()
	{
		return (cotton_demand_index < 8);
	}
	
	public boolean isCottonSaleSafe()
	{
		return (cotton_demand_index <= 3);
	}

	private boolean adjustCottonTrack()
	{
		if (cotton_demand_index == 8) return false;
		
		current_cotton_demand_tile.hideDemandTrackImage();

		current_cotton_demand_tile = unflipped_stack.pop();
		flipped_stack.push(current_cotton_demand_tile);
		current_cotton_demand_tile.showDemandTrackImage();
		
		int adjust = current_cotton_demand_tile.getAmount();
		cotton_demand_index += adjust;
		
		PixelPoint cotton_demand_track_loc;

		if (cotton_demand_index >= 8) 
		{
			cotton_demand_index = 8;
			cotton_demand_track_loc = cotton_demand_track_locs.get(cotton_demand_index);
			cotton_demand_track_image.showImage(cotton_demand_track_loc.getX(), cotton_demand_track_loc.getY());
			return false;
		}
		else 
		{
			cotton_demand_track_loc = cotton_demand_track_locs.get(cotton_demand_index);
			cotton_demand_track_image.showImage(cotton_demand_track_loc.getX(), cotton_demand_track_loc.getY());
			return true;
		}
	}
	
	public int cottonTrackIncome()
	{
		if (!adjustCottonTrack()) return -1;
		
		int extra_income = cotton_extra_income_function.getBrassDemandTrackValue(cotton_demand_index); //3 - (cotton_demand_index)/2;
		return extra_income;
	}
}
