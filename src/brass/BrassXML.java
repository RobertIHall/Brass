package brass;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import gui.PixelPoint;
import gui.PixelDimension;

class BrassXML
{
	private org.jdom2.Element root_element;
	
	public BrassXML(String file_name)
	{
		java.io.File file = new java.io.File(file_name);
      
		org.jdom2.input.SAXBuilder saxb = new org.jdom2.input.SAXBuilder();
        org.jdom2.Document doc = null;
		
		//modify the below to only catch the exceptions that must be caught
		//let any runtime exceptions crash the program
		//what should we do in the catch blocks for doc??
		try
		{
			doc = saxb.build(file);
		}
		catch(org.jdom2.JDOMException jdome) 
		{
			System.out.println(jdome.getMessage());
			//doc = some default file
		}
		catch(java.io.IOException ioe) 
		{
			System.out.println(ioe.getMessage());
			//doc = some default file
		}
		
		root_element = doc.getRootElement();
	}
	
	public PixelDimension getPixelDimension(String image_element_name)
	{
		if (root_element == null) return null;
		
		org.jdom2.Element brass_image_element = root_element.getChild(image_element_name);
		org.jdom2.Element dimensions_element = brass_image_element.getChild("dimensions");
		org.jdom2.Element width_element = dimensions_element.getChild("width");
		org.jdom2.Element height_element = dimensions_element.getChild("height");
	  
		int w = Integer.parseInt(width_element.getText());
		int h = Integer.parseInt(height_element.getText());

		return new PixelDimension(w, h);
	}
	
	public PixelPoint getPixelCenter(String image_element_name)
	{
		if (root_element == null) return null;
		
		org.jdom2.Element brass_image_element = root_element.getChild(image_element_name);
		org.jdom2.Element pixel_center_element = brass_image_element.getChild("center");
		org.jdom2.Element x_center_element = pixel_center_element.getChild("x");
		org.jdom2.Element y_center_element = pixel_center_element.getChild("y");
		
		int x = Integer.parseInt(x_center_element.getText());
		int y = Integer.parseInt(y_center_element.getText());
		return new PixelPoint(x, y);
	}
	
	public List<PixelPoint> getPixelCenters(String image_element_name)
	{
		if (root_element == null) return null;
		
		org.jdom2.Element brass_image_element = root_element.getChild(image_element_name);
		
		List<PixelPoint> pixel_centers = new ArrayList<PixelPoint>();
		
		List<org.jdom2.Element> pixel_centers_element = brass_image_element.getChildren("center");
		Iterator<org.jdom2.Element> pixel_centers_element_iter = pixel_centers_element.iterator();
		while(pixel_centers_element_iter.hasNext())
		{
			org.jdom2.Element pixel_center_element = pixel_centers_element_iter.next();
			org.jdom2.Element x_center_element = pixel_center_element.getChild("x");
			org.jdom2.Element y_center_element = pixel_center_element.getChild("y");
			int x = Integer.parseInt(x_center_element.getText());
			int y = Integer.parseInt(y_center_element.getText());
			
			PixelPoint pixel_point = new PixelPoint(x, y);
			pixel_centers.add(pixel_point);
		}
		
		return pixel_centers;
	}
	
	public int getTextSize(String image_element_name)
	{
		if (root_element == null) return 0;
		
		org.jdom2.Element brass_text_element = root_element.getChild(image_element_name);
		org.jdom2.Element text_size_element = brass_text_element.getChild("size");
		int size = Integer.parseInt(text_size_element.getText());
		return size;
	}
	
	public float[] getTrackOffsets()
	{
		if (root_element == null) return null;
		
		org.jdom2.Element brass_track_element = root_element.getChild("track");
		org.jdom2.Element offset_element = brass_track_element.getChild("offset");
		org.jdom2.Element x_element = offset_element.getChild("x");
		org.jdom2.Element y_element = offset_element.getChild("y");
		
		float x = Float.parseFloat(x_element.getText());
		float y = Float.parseFloat(y_element.getText());
		
		float[] offsets = new float[2];
		offsets[0] = x;
		offsets[1] = y;
		return offsets;
	}
	
	public int[] getTrackStartingLocation()
	{
		if (root_element == null) return null;
		
		org.jdom2.Element brass_track_element = root_element.getChild("track");
		org.jdom2.Element loc_element = brass_track_element.getChild("start_loc");
		org.jdom2.Element x_element = loc_element.getChild("x");
		org.jdom2.Element y_element = loc_element.getChild("y");
		
		int x = Integer.parseInt(x_element.getText());
		int y = Integer.parseInt(y_element.getText());
		
		int[] start_loc = new int[2];
		start_loc[0] = x;
		start_loc[1] = y;
		return start_loc;
	}
}
