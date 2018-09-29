package gui;

import java.awt.BorderLayout;

/**
	This class provides a very basic GUI.  The GUI is basically a panel that can have graphics drawn to
	it and will respond to mouse clicks and key presses.
*/
public class BasicGUI extends CenterFrame
{

   public BasicGUI(int width, int height, String title, String icon_file_name, Drawable d)
   {      
      super (width, height, title);

      setLayout(new BorderLayout());
      setSize(width, height);
      setResizable(false);

      DrawPanel draw = new DrawPanel();
      draw.setDrawable(d);
      add(draw, BorderLayout.CENTER);
	  
	  javax.swing.ImageIcon img_icon = new javax.swing.ImageIcon(icon_file_name);
	  setIconImage(img_icon.getImage());

      setVisible(true);
   }
   
}
