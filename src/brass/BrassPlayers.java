package brass;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Comparator;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;

import gui.DrawImage;
import gui.DrawFont;
import gui.PixelPoint;
import gui.PixelDimension;
import gui.HotSpot;

import util.QueueLinked;

import pqsort.PQSort;

class BrassPlayers
{
	private List<BrassPlayer> brass_players;
	private List<HotSpot> display_player_hot_spots;
	private List<PixelPoint> turn_order_locations;
	
	private List<BrassPlayer> turn_order;
	
	private int active_player_id;
	private int view_player_id;
	private int turn_count;
	private boolean is_first_turn;
	private int game_round;
	
	public int getNumActionsTaken(int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		return brass_player.getNumActionsTaken();
	}
	
	public boolean canSelectCard(int card_index, int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		return brass_player.canSelectCard(card_index);
	}
	
	public int getNumCards(int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		return brass_player.getNumCards();
	}
	
	public int getTechLevel(int industry_id, int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		return brass_player.getTechLevel(industry_id);
	}

	public void payForLink(int link_cost, int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		brass_player.payForLink(link_cost);
	}
	
	public void cancelDoubleCardSelection()
	{
		BrassPlayer brass_player = brass_players.get(active_player_id - 1);
		brass_player.cancelDoubleCardSelection();
	}
	
	public void dealStartingHand(BrassDeck brass_deck)
	{
		//4 players, each player receives eight cards
		for (int i = 1; i <= 8; i++)
		{
			for (int j = 1; j <= 4; j++)
			{
				BrassPlayer brass_player = turn_order.get(j-1);
				brass_player.addCardToHand(brass_deck.deal());
			}
		}
		
		for (int i = 0; i < 4; i++)
		{
			BrassPlayer brass_player = brass_players.get(i);
			brass_player.showHand();
		}
		
		active_player_id = turn_order.get(0).getPlayerID();
		turn_count = 1;
		view_player_id = active_player_id;
		game_round = 1;
	}
	
	public void changePhase()
	{
		Iterator<BrassPlayer> brass_player_iter = brass_players.iterator();
		while(brass_player_iter.hasNext())
		{
			BrassPlayer brass_player = brass_player_iter.next();
			brass_player.changePhase();
		}
	}
	
	public void playerActionCompleted(BrassDeck brass_deck)
	{
		BrassPlayer current_player = brass_players.get(active_player_id - 1);

		if (!is_first_turn && current_player.getNumActionsTaken() < 2)
		{
			return;
		}

		current_player.discardCards(is_first_turn);
			
		if (!brass_deck.isDeckExhausted())
		{
			current_player.addCardToHand(brass_deck.deal());
			if (!is_first_turn)
			{
				current_player.addCardToHand(brass_deck.deal());
			}
		}
			
		current_player.showHand();
		
		turn_count++;
		
		if (turn_count == 5)
		{
			is_first_turn = false;
			turn_count = 1;
			game_round++;
			
			//sort the players by amount spent using PQSort
			Comparator<BrassPlayer> comp = new BrassCompareAmountSpent(true);
			turn_order = PQSort.pqSort(turn_order, comp, pqsort.PQType.TREE);
			
			Iterator<BrassPlayer> turn_order_iter = turn_order.iterator();
			int count = 1;
			while(turn_order_iter.hasNext())
			{
				BrassPlayer brass_player = turn_order_iter.next();
				brass_player.resetAmountSpent();
				brass_player.setTurnOrderImageLoc(turn_order_locations.get(count-1).getX(),turn_order_locations.get(count-1).getY());
				count++;
				brass_player.receiveIncome();
			}
		}
		
		BrassPlayer next_player = turn_order.get(turn_count - 1);
		active_player_id = next_player.getPlayerID();
		view_player_id = active_player_id;
	}
	
	public boolean isPhaseOver()
	{
		return (game_round == 9);
	}
	
	public int getIncome(int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		return brass_player.getIncome();
	}
	
	public void awardVictoryPoints(int victory_points, int player_id, BrassTrack brass_track)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		brass_player.awardVictoryPoints(victory_points, brass_track);
	}
	
	public boolean isTechLevelRequirementMet(int industry_id, int player_id, boolean brass_phase)
	{
		int phase = 1;  
		if (brass_phase == true) phase = 2;  //rail phase
		
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		return brass_player.isTechLevelRequirementMet(industry_id, phase);
	}
	
	public void discardToken(int token_id)
	{
		BrassPlayer brass_player = brass_players.get(getActivePlayerID() - 1);
		brass_player.discardToken(token_id);
	}
	
	public void payForCube(int cube_cost, int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		brass_player.payForCube(cube_cost);
	}
	
	public void receiveDemandTrackMoney(int demand_track_money, int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		brass_player.receiveDemandTrackMoney(demand_track_money);
	}
	
	public void increaseIncomeIndex(int player_id, int income_amt, BrassTrack brass_track)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		brass_player.increaseIncomeIndex(income_amt, brass_track);
	}
	
	public int getMoney(int player_id)
   {
	   BrassPlayer brass_player = brass_players.get(player_id - 1);
	   return brass_player.getMoney();
   }
	
	public boolean isCoalRequired(int industry_id, int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		return brass_player.isCoalRequired(industry_id);
	}
	
	public boolean isIronRequired(int industry_id, int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		return brass_player.isIronRequired(industry_id);
	}
	
	public BrassToken payForToken(int industry_id, int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		BrassToken brass_token = brass_player.payForToken(industry_id);
		return brass_token;
	}
	
	public int getIndustryCost(int industry_id, int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		return brass_player.getIndustryCost(industry_id);
	}

	public int getSelectedToken(int x, int y)
	{
		BrassPlayer brass_player = brass_players.get(getActivePlayerID() - 1);
		return brass_player.getSelectedToken(x, y);
	}
	
	
	public BrassPlayer getPlayer(int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		return brass_player;
	}
	
	
	public void cancelCardSelection()
	{
		BrassPlayer current_player = brass_players.get(active_player_id - 1);
		current_player.cancelCardSelection();
	}
	
	public boolean isFirstTurn()
	{
		return is_first_turn;
	}
		
	public void selectCard(int card_num)
	{
		BrassPlayer brass_player = brass_players.get(getActivePlayerID() - 1);
		brass_player.selectCard(card_num);
	}
	
	public void executeLoanAction(int loan_amount, BrassTrack brass_track)
	{
		BrassPlayer brass_player = brass_players.get(getActivePlayerID() - 1);
		brass_player.executeLoanAction(loan_amount, brass_track);
	}
	
	public int getCardCityTechID(int brass_card_num)
	{
		BrassPlayer brass_player = brass_players.get(getActivePlayerID() - 1);
		return brass_player.getCardCityTechID(brass_card_num);
	}
	
	public int getSelectedCard(int x, int y)
	{
		BrassPlayer brass_player = brass_players.get(getActivePlayerID() - 1);
		return brass_player.getSelectedCard(x, y);
	}

	public void payForDemandTrack(int demand_track_cost, int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		brass_player.payForDemandTrack(demand_track_cost);
	}
	
	public boolean canPlayerBuyFromDemandTrack(int demand_track_cost, int player_id)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		return brass_player.canPlayerBuyFromDemandTrack(demand_track_cost);
	}
	
	public int getActivePlayerID()
	{
		return active_player_id;
	}
	
	public int getSelectedPlayer(int x, int y)
	{
		Iterator<HotSpot> display_player_iter = display_player_hot_spots.iterator();
		int count = 1;
		int selected_player = 0;
		while(display_player_iter.hasNext())
		{
			HotSpot display_player_spot = display_player_iter.next();
			if (display_player_spot.isSelected(x, y))
			{
				selected_player = count;
			}
			count++;
		}
		
		return selected_player;
	}
	
	public void displayPlayer(int selected_player_id)
	{
		view_player_id = selected_player_id;
	}
	
	public int getSelectedCard(int player_id, int x, int y)
	{
		BrassPlayer brass_player = brass_players.get(player_id - 1);
		return brass_player.getSelectedCard(x, y);
	}
	
	public BrassPlayers(BrassDeck brass_deck, BrassXML brass_xml, BrassTrack brass_track)
	{
		brass_players = new ArrayList<BrassPlayer>();
		util.Permutation p = util.PermutationFactory.getPermutation("resources/brass_turn_order.txt", 4, 4);
		
		List<PixelPoint> amount_spent_centers = brass_xml.getPixelCenters("amount_spent");
		BrassPlayer red = new BrassPlayer(1, brass_xml, "red", amount_spent_centers.get(0), brass_track);
		brass_players.add(red);
		BrassPlayer purple = new BrassPlayer(2, brass_xml, "purple", amount_spent_centers.get(1), brass_track);
		brass_players.add(purple);
		BrassPlayer green = new BrassPlayer(3, brass_xml, "green", amount_spent_centers.get(2), brass_track);
		brass_players.add(green);
		BrassPlayer yellow = new BrassPlayer(4, brass_xml, "yellow", amount_spent_centers.get(3), brass_track);
		brass_players.add(yellow);
		
		PixelDimension display_player_dimension = brass_xml.getPixelDimension("amount_spent");
		display_player_hot_spots = new ArrayList<HotSpot>();
		for (int i = 1; i <= 4; i++)
		{
			PixelPoint player_center = amount_spent_centers.get(i-1);
			HotSpot display_player_hot_spot = new HotSpot(i, player_center.getX() + 25, player_center.getY() - 15, display_player_dimension.getWidth(), display_player_dimension.getHeight());
			display_player_hot_spots.add(display_player_hot_spot);
		}
		
		turn_order_locations = brass_xml.getPixelCenters("turn_order");
		
		turn_order = new ArrayList<BrassPlayer>();
		int count = 1;
		while(p.hasNext())
		{
			Integer i = p.next();
			BrassPlayer brass_player = brass_players.get(i - 1);
			turn_order.add(brass_player);
			brass_player.setTurnOrderImageLoc(turn_order_locations.get(count-1).getX(),turn_order_locations.get(count-1).getY());
			count++;
		}
		
		dealStartingHand(brass_deck);
		
		is_first_turn = true;
	}

	public void draw(Graphics g)
   {
		Iterator<BrassPlayer> player_iter = brass_players.iterator();
		while(player_iter.hasNext())
		{
			BrassPlayer brass_player = player_iter.next();
			brass_player.draw(g, active_player_id, view_player_id);
		}
   }
}
