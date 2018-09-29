package brass;

import java.awt.Graphics;
import java.awt.Image;

import gui.DrawImage;

class BrassCard
{
	private int card_id;
	private int city_tech_id;
	private String card_name;
	
	private DrawImage big_image;
	private DrawImage small_image;
	
	public void cancelCardSelection(int x, int y)
	{
		hideBigCard();
		showSmallCard(x, y);
	}
	
	public void selectCard(int x, int y)
	{
		hideSmallCard();
		showBigCard(x, y);
	}
	
	public void validCardSelected()
	{
		System.out.println(getCardName());
	}
	
	public int getCardCityTechID()
	{
		return city_tech_id;
	}

	//only the small image can be clicked on
	public boolean isCardSelected(int x_click, int y_click)
	{
		if (small_image.isSelected(x_click, y_click))
		{
			return true;
		}
		
		return false;
	}
	
	public String getCardName()
	{
		return card_name;
	}
	
	public BrassCard(int c_id, int c_t_id, String name, Image big, int w_b, int h_b, Image small, int w_s, int h_s)
	{
		card_id = c_id;
		city_tech_id = c_t_id;
		card_name = name;
		
		big_image = new DrawImage(big, name, w_b, h_b);
		small_image = new DrawImage(small, name, w_s, h_s);
	}
	
	public void showBigCard(int x, int y)
	{
		big_image.showImage(x, y);
	}
	
	public void hideBigCard()
	{
		big_image.hideImage();
	}
	
	public void showSmallCard(int x, int y)
	{
		small_image.showImage(x, y);
	}
	
	public void hideSmallCard()
	{
		small_image.hideImage();
	}
	
	public void draw(Graphics g)
	{
		big_image.draw(g);
		small_image.draw(g);
	}
}
