package brass;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import java.awt.Graphics;
import java.awt.Image;

import gui.HotSpot;
import gui.DrawImage;
import gui.PixelPoint;
import gui.PixelDimension;

import util.QueueLinked;

class BrassRails extends BrassConnections
{
	
	public BrassRails(BrassXML brass_xml)
	{
		gui.ImageLoader il = gui.ImageLoader.getImageLoader();
		
		brass_connections = new ArrayList<BrassConnection>();
		
		connection_dimension = brass_xml.getPixelDimension("rails");
		List<PixelPoint> rail_centers = brass_xml.getPixelCenters("rails");
		List<HotSpot> rail_hot_spots = new ArrayList<HotSpot>();
		
		int count = 1;
		Iterator<PixelPoint> rail_centers_iter = rail_centers.iterator();
		while(rail_centers_iter.hasNext())
		{
			PixelPoint rail_center = rail_centers_iter.next();
			HotSpot rail_hot_spot = new HotSpot(count, rail_center.getX(), rail_center.getY(), connection_dimension.getWidth(), connection_dimension.getHeight());
			rail_hot_spots.add(rail_hot_spot);
			count++;
		}
		
		Image connection_bw_img = il.getImage("images/rail_small.jpg");
		
		DrawImage brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 1 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		BrassConnection brass_connection = new BrassConnection(1, rail_hot_spots.get(0), 3, 5, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 2 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(2, rail_hot_spots.get(1), 3, 19, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 3 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(3, rail_hot_spots.get(2), 5, 7, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 4 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(4, rail_hot_spots.get(3), 4, 6, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 5 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(5, rail_hot_spots.get(4), 4, 13, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 6 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(6, rail_hot_spots.get(5), 6, 13, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 7 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(7, rail_hot_spots.get(6), 10, 15, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 8 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(8, rail_hot_spots.get(7), 9, 15, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 9 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(9, rail_hot_spots.get(8), 15, 19, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 10 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(10, rail_hot_spots.get(9), 13, 14, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 11 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(11, rail_hot_spots.get(10), 13, 17, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 12 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(12, rail_hot_spots.get(11), 13, 18, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 13 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(13, rail_hot_spots.get(12), 11, 19, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 14 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(14, rail_hot_spots.get(13), 8, 18, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 15 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(15, rail_hot_spots.get(14), 14, 16, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 16 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(16, rail_hot_spots.get(15), 18, 19, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 17 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(17, rail_hot_spots.get(16), 12, 17, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 18 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(18, rail_hot_spots.get(17), 7, 20, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 19 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(19, rail_hot_spots.get(18), 16, 20, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 20 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(20, rail_hot_spots.get(19), 8, 21, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 21 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(21, rail_hot_spots.get(20), 21, 22, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 22 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(22, rail_hot_spots.get(21), 12, 22, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 23 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(23, rail_hot_spots.get(22), 1, 10, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 24 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(24, rail_hot_spots.get(23), 3, 15, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 25 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(25, rail_hot_spots.get(24), 5, 6, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 26 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(26, rail_hot_spots.get(25), 6, 16, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 27 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(27, rail_hot_spots.get(26), 3, 4, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 28 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(28, rail_hot_spots.get(27), 4, 19, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 29 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(29, rail_hot_spots.get(28), 11, 18, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 30 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(30, rail_hot_spots.get(29), 2, 8, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 31 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(31, rail_hot_spots.get(30), 10, 23, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 32 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(32, rail_hot_spots.get(31), 15, 24, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 33 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(33, rail_hot_spots.get(32), 15, 25, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 34 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(34, rail_hot_spots.get(33), 19, 25, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_bw_img = new DrawImage(connection_bw_img, "Brass Rail 35 Not Constructed", connection_dimension.getWidth(), connection_dimension.getHeight());
		brass_connection = new BrassConnection(35, rail_hot_spots.get(34), 11, 25, brass_connection_bw_img);
		brass_connections.add(brass_connection);
		
		brass_connection_images = new ArrayList<Image>();
		
		Image connection_img = il.getImage("images/red/red_rail_small.jpg");
		brass_connection_images.add(connection_img);
		
		connection_img = il.getImage("images/purple/purple_rail_small.jpg");
		brass_connection_images.add(connection_img);
		
		connection_img = il.getImage("images/green/green_rail_small.jpg");
		brass_connection_images.add(connection_img);
		
		connection_img = il.getImage("images/yellow/yellow_rail_small.jpg");
		brass_connection_images.add(connection_img);
	}
}
