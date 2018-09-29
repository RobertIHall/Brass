package brass;

import java.awt.Graphics;

import gui.PixelPoint;
import gui.PixelDimension;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

class BrassHand
{
	private List<BrassCard> brass_hand;
	
	private List<PixelPoint> small_card_centers;
	private List<PixelPoint> big_card_centers;
	
	private int sel_card_1;  //the index 1-8 representing the first card selected
	private int sel_card_2;  //the index 1-8 representing the second card selected
	
	public int getNumCards()
	{
		return brass_hand.size();
	}
	
	public boolean canSelectCard(int card_index)
	{
		return (card_index <= getNumCards()) && (sel_card_1 != card_index);
	}
	
	public void cancelDoubleCardSelection()
	{
		BrassCard brass_card;
		
		brass_card = brass_hand.get(sel_card_1 - 1);
		//remember that the first set of small card centers is the small card reverse side
		brass_card.cancelCardSelection(small_card_centers.get(sel_card_1).getX(), small_card_centers.get(sel_card_1).getY());
		sel_card_1 = 0;

		//if two cards had already been selected (most likely) cancel them both
		if (sel_card_2 > 0)
		{
			brass_card = brass_hand.get(sel_card_2 - 1);
			//remember that the first set of small card centers is the small card reverse side
			brass_card.cancelCardSelection(small_card_centers.get(sel_card_2).getX(), small_card_centers.get(sel_card_2).getY());
			sel_card_2 = 0;
		}
	}
	
	public int getNumActionsTaken()
	{
		int action_num = 0;
		if (sel_card_1 > 0) action_num++;
		if (sel_card_2 > 0) action_num++;
		return action_num;
	}
	
	public boolean isCardAlreadySelected(int card_num)
	{
		return sel_card_1 == card_num;
	}
	
	public void discardCards(boolean first_turn)
	{
		if (first_turn)
		{
			BrassCard brass_card = brass_hand.get(sel_card_1 - 1);
			brass_card.hideBigCard();
			brass_hand.remove(sel_card_1 - 1);
			sel_card_1 = 0;
		}
		else
		{
			BrassCard brass_card_1 = brass_hand.get(sel_card_1 - 1);
			brass_card_1.hideBigCard();
			BrassCard brass_card_2 = brass_hand.get(sel_card_2 - 1);
			brass_card_2.hideBigCard();
				
			//remove the higher index card first
			//the remove operation renumbers the items
			if (sel_card_1 > sel_card_2)
			{
				brass_hand.remove(sel_card_1 - 1);
				brass_hand.remove(sel_card_2 - 1);
			}
			else
			{
				brass_hand.remove(sel_card_2 - 1);
				brass_hand.remove(sel_card_1 - 1);
			}
		}
		
		sel_card_1 = 0;
		sel_card_2 = 0;
	}
	
	public void cancelCardSelection()
	{
		if (sel_card_2 == 0)  //first action being cancelled
		{
			BrassCard brass_card = brass_hand.get(sel_card_1 - 1);
			//remember that the first set of small card centers is the small card reverse side
			brass_card.cancelCardSelection(small_card_centers.get(sel_card_1).getX(), small_card_centers.get(sel_card_1).getY());
			sel_card_1 = 0;
		}
		else  //second action being cancelled
		{
			BrassCard brass_card = brass_hand.get(sel_card_2 - 1);
			//remember that the first set of small card centers is the small card reverse side
			brass_card.cancelCardSelection(small_card_centers.get(sel_card_2).getX(), small_card_centers.get(sel_card_2).getY());
			sel_card_2 = 0;
		}
	}
	
	public void selectCard(int card_index)
	{
		if (sel_card_1 == 0)  //first card selected
		{
			BrassCard brass_card = brass_hand.get(card_index - 1);
			brass_card.selectCard(big_card_centers.get(0).getX(), big_card_centers.get(0).getY());
			sel_card_1 = card_index;  //have to remember this in case of a cancel
		}
		else  //second card selected
		{
			//AI cannot select the same card twice
			if (card_index == sel_card_1) return;
			
			BrassCard brass_card = brass_hand.get(card_index - 1);
			brass_card.selectCard(big_card_centers.get(1).getX(), big_card_centers.get(1).getY());
			sel_card_2 = card_index;  //have to remember this in case of a cancel
		}
	}
	
	public void validCardSelected(int card_id)
	{
		BrassCard brass_card = brass_hand.get(card_id - 1);
		brass_card.validCardSelected();
	}
	
	public int getSelectedCard(int x, int y)
	{
		Iterator<BrassCard> card_iter = brass_hand.iterator();
		int count = 1;
		while(card_iter.hasNext())
		{
			BrassCard brass_card = card_iter.next();
			if (brass_card.isCardSelected(x, y))
			{
				return count;
			}
			count++;
		}
		return 0;  //a card was not selected
	}

	public void draw(Graphics g)
	{
		Iterator<BrassCard> card_iter = brass_hand.iterator();
		while(card_iter.hasNext())
		{
			BrassCard brass_card = card_iter.next();
			brass_card.draw(g);
		}
	}
	
	public BrassHand(BrassXML brass_xml)
	{
		brass_hand = new ArrayList<BrassCard>();
		
		small_card_centers = brass_xml.getPixelCenters("small_cards");
		big_card_centers = brass_xml.getPixelCenters("big_cards");
		
		sel_card_1 = 0;
		sel_card_2 = 0;
	}
	
	public String getCardName(int card_num)
	{
		BrassCard brass_card = brass_hand.get(card_num - 1);
		return  brass_card.getCardName();
	}
	
	public int getCardCityTechID(int card_num)
	{
		BrassCard brass_card = brass_hand.get(card_num - 1);
		return  brass_card.getCardCityTechID();
	}
	
	public void showHand()
	{
		assignAllSmallCardLocations();
	}
	
	//the hand size should not be greater than 8, but can be less than 8 if the draw deck is empty
	public void addCardToHand(BrassCard brass_card)
	{
		brass_hand.add(brass_card);
	}
	
	public void assignAllSmallCardLocations()
	{
		int small_card_count = 1;
		Iterator<BrassCard> card_iter = brass_hand.iterator();
		while(card_iter.hasNext())
		{
			BrassCard brass_card = card_iter.next();
			//skip index 0 as it is for the card back
			PixelPoint small_card_center = small_card_centers.get(small_card_count);
			brass_card.showSmallCard(small_card_center.getX(), small_card_center.getY());
			small_card_count++;
		}
	}
}
