package brass;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class BrassAICanal implements BrassAIStrategy
{
	private BrassGame brass_game;
	private BrassBoard brass_board;
	private boolean stopFirstSell;
	private boolean shipBuilt;
	private boolean firstLoanTaken;
	private boolean actualShipNotBuilt;
	private int coalTech;
	private int ironTech;
	private boolean can_still_make_shipyard;
	private boolean have_shipyard_card;
	
	public BrassAICanal(BrassGame bg, BrassBoard bd)
	{
		brass_game = bg;
		brass_board = bd;
		stopFirstSell = false;
		shipBuilt = false;
		firstLoanTaken = false;
		actualShipNotBuilt = false;
		coalTech = 1;
		ironTech = 1;
		can_still_make_shipyard = true;
	}
	
	public BrassComputerPlayerAction nextMove()
	{
		BrassComputerPlayerAction computer_move = new BrassComputerPlayerAction();
		int computer_player_id = brass_game.getActivePlayerID();
		int num_actions_already_taken = brass_game.getNumActionsTaken(computer_player_id);
		boolean can_sell_cotton = brass_game.canSellCotton(computer_player_id);
			
			if(brass_game.isFirstTurn())
			{
				System.out.println("First turn. Trying to start near Liverpool.");
				firstTurnCanal(computer_move, computer_player_id, BrassIndustryEnum.COTTON.getValue());
				if (computer_move.isActionSelected()) return computer_move;
				
			}
			if(!brass_board.isLinkConstructed(13))
			{
				System.out.println("Link to liverpool already build!");
				buildLinkToLiverpool(computer_move, computer_player_id);
				if(computer_move.isActionSelected()) return computer_move;
				if(num_actions_already_taken == 1)
				{
					buildLinkToLiverpool(computer_move, computer_player_id);
					
					if(computer_move.isActionSelected()) return computer_move;
				}
			
			}
			if(stopFirstSell && !shipBuilt)//try to get into liverpool
			{
				System.out.println("Upgrading Ship Tech");
				computer_move.selectTechUpgradeAction(getCardForNonBuildActionCanal(computer_player_id), BrassIndustryEnum.SHIP.getValue(), BrassIndustryEnum.SHIP.getValue());
				shipBuilt = true;
				stopFirstSell = false;
				return computer_move;
			}
			if(shipBuilt && !firstLoanTaken)
			{
				computer_move.selectTakeLoanAction(getCardForNonBuildActionCanal(computer_player_id), 1);
				firstLoanTaken = true;
				shipBuilt = false;
				return computer_move;
			}
			if(firstLoanTaken && !actualShipNotBuilt)
			{
				boolean shipyardBuilt = findCardForPort(computer_move, computer_player_id);
				if(shipyardBuilt)
				{
					actualShipNotBuilt = true;
					if(computer_move.isActionSelected()) return computer_move;	
				}
				else
				{
					int player_money = brass_game.getMoney(computer_player_id);
					if(coalTech == 1)
					{
						computer_move.selectTechUpgradeAction(getCardForNonBuildActionCanal(computer_player_id), BrassIndustryEnum.COAL.getValue(), BrassIndustryEnum.COAL.getValue());
						coalTech++;
						return computer_move;
					}
					else if(ironTech == 1)
					{
						computer_move.selectTechUpgradeAction(getCardForNonBuildActionCanal(computer_player_id), BrassIndustryEnum.IRON.getValue(), BrassIndustryEnum.IRON.getValue());
						ironTech++;
						return computer_move;
						
					}
					if(coalTech == 2 && ironTech == 2 && player_money <= 25)
					{
						computer_move.selectTakeLoanAction(getCardForNonBuildActionCanal(computer_player_id), 2);
					}
					else
					{
						boolean coalCheck = findCardForCoal(computer_move, computer_player_id);
						if(coalCheck)
						{
							if(computer_move.isActionSelected()) return computer_move;	
						}
						else
						{
							System.out.println("discard!");
							computer_move.selectDiscardAction(getCardForNonBuildActionCanal(computer_player_id));
							return computer_move;
						}
					}
				}
							
			}
			if(actualShipNotBuilt)
			{
					int player_money = brass_game.getMoney(computer_player_id);
					if(coalTech == 2 && ironTech == 2 && player_money <= 25)
					{
						computer_move.selectTakeLoanAction(getCardForNonBuildActionCanal(computer_player_id), 2);
						return computer_move;
					}
					else
					{
						boolean coalCheck = findCardForCoal(computer_move, computer_player_id);
						if(coalCheck)
						{
							if(computer_move.isActionSelected()) return computer_move;	
						}
						else
						{
							System.out.println("discard!");
							computer_move.selectDiscardAction(getCardForNonBuildActionCanal(computer_player_id));
							return computer_move;
						}
					}
				
			}
			if(brass_board.isLinkConstructed(13) && !stopFirstSell)
			{
				computer_move.selectSellCottonAction(getCardForNonBuildActionCanal(computer_player_id));
				stopFirstSell = true;
				return computer_move;
			}
		
	return computer_move;//should never get here. note if it does
	}
		
	private void firstTurnCanal(BrassComputerPlayerAction computer_move, int player_id, int industry_id)
	{
		//loop over computer player cards
		//find a card that corresponds to something the computer player can build
		//select that card and the build action
		int num_cards = brass_game.getNumCards(player_id);
		boolean foundClosest = false;
		int city_id;
		ArrayList<Integer> cards = new ArrayList<Integer>();
		for(int i = 1; i <= num_cards; i++)
		{
			int card = brass_game.getCardCityTechID(i);
			cards.add(card);
		}
		for (int i = 1; i <= num_cards; i++)
		{
			if (!brass_game.canSelectCard(i, player_id)) continue;
				
			int brass_card_city_tech_id = brass_game.getCardCityTechID(i);
			if (brass_card_city_tech_id == 3 || brass_card_city_tech_id == 18 || brass_card_city_tech_id == 15)
			{
				city_id = brass_card_city_tech_id;
				if (brass_game.canBuildIndustry(true, city_id, industry_id, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(city_id, industry_id, player_id);
					int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(i, city_id, industry_id, coal_city_id, iron_city_id);
						foundClosest = true;
						return;
					}
				}
			}
			else if (brass_card_city_tech_id == 21)
			{
				int card_industry_id = brass_card_city_tech_id - 19;
				if(industry_id == card_industry_id)
				{
					if (brass_game.canBuildIndustry(false, 18, industry_id, player_id))
					{
						int coal_city_id = brass_game.canMoveCoal(18, industry_id, player_id);
						int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
						if (coal_city_id >= 0 && iron_city_id >= 0) 
						{
							computer_move.selectBuildAction(i, 18, industry_id, coal_city_id, iron_city_id);
							foundClosest = true;
							return;
						}
					}
					if (brass_game.canBuildIndustry(false, 15, industry_id, player_id))
					{
						int coal_city_id = brass_game.canMoveCoal(15, industry_id, player_id);
						int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
						if (coal_city_id >= 0 && iron_city_id >= 0) 
						{
							computer_move.selectBuildAction(i, 15, industry_id, coal_city_id, iron_city_id);
							foundClosest = true;
							return;
						}
					}
					if (brass_game.canBuildIndustry(false, 3, industry_id, player_id))
					{
						int coal_city_id = brass_game.canMoveCoal(3, industry_id, player_id);
						int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
						if (coal_city_id >= 0 && iron_city_id >= 0) 
						{
							computer_move.selectBuildAction(i, 3, industry_id, coal_city_id, iron_city_id);
							foundClosest = true;
							return;
						}
					}
				}
			}
		}
		//If none of these options happen to build cotton mill I look for a port to build.
		Iterator iter = cards.iterator();
		int card = 1;
		while(iter.hasNext())
		{
			int cardID = (int)iter.next();
			if(cardID == 23)
			{
				if (brass_game.canBuildIndustry(false, 11, 4, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(11, cardID, player_id);
					int iron_city_id = brass_game.canMoveIron(cardID, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(card, 11, cardID, coal_city_id, iron_city_id);
						foundClosest = true;
						return;
					}
				}
				if (brass_game.canBuildIndustry(false, 15, 4, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(15, cardID, player_id);
					int iron_city_id = brass_game.canMoveIron(cardID, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(card, 15, cardID, coal_city_id, iron_city_id);
						foundClosest = true;
						return;
					}
				}
				if (brass_game.canBuildIndustry(false, 18, 4, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(18, cardID, player_id);
					int iron_city_id = brass_game.canMoveIron(cardID, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(card, 18, cardID, coal_city_id, iron_city_id);
						foundClosest = true;
						return;
					}
				}	
			}
			card++;
		}
		//If I cannot build a port I look for the closest city to the three main cities and build a cotton mill. (Blackburn, Warrington, Preston)
		Iterator newIter = cards.iterator();
		int card2 = 1;
		while(newIter.hasNext())
		{
			int card_id = (int)newIter.next();
			if(card_id == 10)
			{
				if (brass_game.canBuildIndustry(true, card_id, industry_id, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(card_id, industry_id, player_id);
					int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(card2, card_id, industry_id, coal_city_id, iron_city_id);
						foundClosest = true;
						return;
					}
				}	

			}
			if(card_id == 5)
			{
				if (brass_game.canBuildIndustry(true, card_id, industry_id, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(card_id, industry_id, player_id);
					int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(card2, card_id, industry_id, coal_city_id, iron_city_id);
						foundClosest = true;
						return;
					}
				}	

			}
			if(card_id == 13)
			{
				if (brass_game.canBuildIndustry(true, card_id, industry_id, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(card_id, industry_id, player_id);
					int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(card2, card_id, industry_id, coal_city_id, iron_city_id);
						foundClosest = true;
						return;
					}
				}	
				
			}
			
			card2++;
		}
		//By sheer bullshit of the draw of cards we don't get any city close I reused dr. boshart's algorithm for the 
		//ai's decision to put it wherever the card chooses if its not any of our cities.
		if(!foundClosest)
		{
			for (int i = 1; i <= num_cards; i++)
			{
				if (!brass_game.canSelectCard(i, player_id)) continue;
					
				int brass_card_city_tech_id = brass_game.getCardCityTechID(i);
				if (brass_card_city_tech_id <= 19)
				{
					city_id = brass_card_city_tech_id;
					if (brass_game.canBuildIndustry(true, city_id, industry_id, player_id))
					{
						int coal_city_id = brass_game.canMoveCoal(city_id, industry_id, player_id);
						int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
						if (coal_city_id >= 0 && iron_city_id >= 0) 
						{
							computer_move.selectBuildAction(i, city_id, industry_id, coal_city_id, iron_city_id);
							return;
						}
					}
				}
				else
				{
					int card_industry_id = brass_card_city_tech_id - 19;
					if (industry_id == card_industry_id)
					{
						//what cities can this industry be built in?
						for (int j = 1; j <= 19; j++)
						{
							city_id = j;
							if (brass_game.canBuildIndustry(false, city_id, industry_id, player_id))
							{
								int coal_city_id = brass_game.canMoveCoal(city_id, industry_id, player_id);
								int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
								if (coal_city_id >= 0 && iron_city_id >= 0) 
								{
									computer_move.selectBuildAction(i, city_id, industry_id, coal_city_id, iron_city_id);
									return;
								}
							}
						}
					}
				}
			}
			
		}
	}
	
	private void buildLinkToLiverpool(BrassComputerPlayerAction bcpa, int player_id)
	{
		ArrayList<Integer> goodLinks = new ArrayList<Integer>();
		fillGoodLinks(goodLinks);
		Iterator iter = goodLinks.iterator();
		while(iter.hasNext())
		{
			int linkID = (int)iter.next();
			if(brass_game.canBuildLink(linkID, player_id))
			{
				bcpa.selectLinkAction(getCardForNonBuildActionCanal(player_id), linkID);		
				return;
			}
		}
		int num_connections = brass_game.getNumLinks();
		for (int i = 1; i <= num_connections; i++)
		{
			boolean can_build_link = brass_game.canBuildLink(i, player_id);
			if (can_build_link)
			{
				bcpa.selectLinkAction(getCardForNonBuildActionCanal(player_id), i);
				System.out.println("Regular Link");					
				return;
			}
		}
		
		
	}
	private void fillGoodLinks(ArrayList<Integer> link)
	{
		link.add(13);
		link.add(2);
		link.add(9);
		link.add(17);
		link.add(7);
		link.add(1);
		link.add(12);

	}
	private int getCardForNonBuildActionCanal(int player_id)
	{
		int num_cards = brass_game.getNumCards(player_id);
		boolean brass_phase = brass_game.getBrassPhase();
		
		for (int i = 1; i <= num_cards; i++)
		{
			if (!brass_game.canSelectCard(i, player_id)) continue;
			int brass_card_city_tech_id = brass_game.getCardCityTechID(i);
			
			if (brass_card_city_tech_id < 20)
			{
				if(brass_card_city_tech_id == 23)
				{
					return i;
					
				}
				int city_id = brass_card_city_tech_id;
				if (brass_game.isCityFull(city_id)) return i;
				
				//duplicate city cards or already built in city (and canal phase)
				if (!brass_phase)
				{
					int num_tokens_in_city = brass_game.getNumTokensInCity(city_id, player_id);
					if (num_tokens_in_city > 0) return i;
					
					for (int j = i+1; j <= num_cards; j++)
					{
						int city_id_duplicate = brass_game.getCardCityTechID(j);
						if (city_id == city_id_duplicate) return i;
					}
				}
			}
		
			
			if (brass_card_city_tech_id == 1)  return i; //barrow
			if (brass_card_city_tech_id == 2) return i;  //birkenhead
			
			//duplicate industry cards
			if (brass_card_city_tech_id > 19)
			{
				int industry_id = brass_card_city_tech_id;// - 19;
				if(brass_card_city_tech_id == 1 || brass_card_city_tech_id == 9)
				{
					return i;
					
				}
				for (int j = i+1; j <= num_cards; j++)
				{
					int industry_id_duplicate = brass_game.getCardCityTechID(j);
					if (industry_id == industry_id_duplicate) return i;
				}
			}
		}
		
		//second pass through the cards
		for (int i = 1; i <= num_cards; i++)
		{
			if (!brass_game.canSelectCard(i, player_id)) continue;
			int brass_card_city_tech_id = brass_game.getCardCityTechID(i);
			
			if (brass_card_city_tech_id == 6) return i;  //bury
			if (brass_card_city_tech_id == 19) return i;  //wigan
			
			if (brass_card_city_tech_id == 22) return i;  //iron works
			if (brass_card_city_tech_id == 20) return i;  //coal
			
			if (brass_card_city_tech_id == 14) return i;  //oldham
			if (brass_card_city_tech_id == 5) return i;  //burnley
		}
		 
		 //pick a random card to use for the non build action
		util.Random rand = util.Random.getRandomNumberGenerator();
		int random_discard = rand.randomInt(1, num_cards);
		while (!brass_game.canSelectCard(random_discard, player_id))
		{
			random_discard = rand.randomInt(1, num_cards);
		}
	//System.out.println("random_discard: " + random_discard);
		return random_discard;
	}
	
	private boolean findCardForPort(BrassComputerPlayerAction action, int player_id)
	{
		int num_cards = brass_game.getNumCards(player_id);
		int city_id;
		ArrayList<Integer> cards = new ArrayList<Integer>();
		for(int i = 1; i <= num_cards; i++)
		{
			int card = brass_game.getCardCityTechID(i);
			cards.add(card);
		}
		Iterator iter = cards.iterator();
		int count = 1;
		while(iter.hasNext())
		{
			int card2 = (int)iter.next();
			if(card2 == 11)
			{
				if (brass_game.canBuildIndustry(true, 11, 5, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(11, 5, player_id);
					int iron_city_id = brass_game.canMoveIron(5, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						action.selectBuildAction(count, 11, 5, coal_city_id, iron_city_id);
						return true;
					}
				}	
				
			}
			if(card2 == 24)
			{
				if (brass_game.canBuildIndustry(false, 11, 5, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(11, 5, player_id);
					int iron_city_id = brass_game.canMoveIron(5, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						action.selectBuildAction(count, 11, 5, coal_city_id, iron_city_id);
						return true;
					}
				}		
			}
			count++;
			
		}
		return false;
		
	}
	private void getAllInfoForIndustrySpecificBuildAction(BrassComputerPlayerAction computer_move, int player_id, int industry_id)
	{
		//loop over computer player cards
		//find a card that corresponds to something the computer player can build
		//select that card and the build action
		int num_cards = brass_game.getNumCards(player_id);
		int city_id;
		
		for (int i = 1; i <= num_cards; i++)
		{
			if (!brass_game.canSelectCard(i, player_id)) continue;
				
			int brass_card_city_tech_id = brass_game.getCardCityTechID(i);
			if (brass_card_city_tech_id <= 19)
			{
				city_id = brass_card_city_tech_id;
				if (brass_game.canBuildIndustry(true, city_id, industry_id, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(city_id, industry_id, player_id);
					int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(i, city_id, industry_id, coal_city_id, iron_city_id);
						return;
					}
				}
			}
			else
			{
				int card_industry_id = brass_card_city_tech_id - 19;
				if (industry_id == card_industry_id)
				{
					//what cities can this industry be built in?
					for (int j = 1; j <= 19; j++)
					{
						city_id = j;
						if (brass_game.canBuildIndustry(false, city_id, industry_id, player_id))
						{
							int coal_city_id = brass_game.canMoveCoal(city_id, industry_id, player_id);
							int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
							if (coal_city_id >= 0 && iron_city_id >= 0) 
							{
								computer_move.selectBuildAction(i, city_id, industry_id, coal_city_id, iron_city_id);
								return;
							}
						}
					}
				}
			}
		}
	}
	
	private boolean findCardForCoal(BrassComputerPlayerAction action, int player_id)
	{
		int num_cards = brass_game.getNumCards(player_id);
		int city_id;
		ArrayList<Integer> cards = new ArrayList<Integer>();
		for(int i = 1; i <= num_cards; i++)
		{
			int card = brass_game.getCardCityTechID(i);
			cards.add(card);
		}
		Iterator iter = cards.iterator();
		int count = 1;
		while(iter.hasNext())
		{
			int card2 = (int)iter.next();
			if(card2 == 19)
			{
				if (brass_game.canBuildIndustry(true, 19, 1, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(19, 1, player_id);
					int iron_city_id = brass_game.canMoveIron(1, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						action.selectBuildAction(count, 19, 1, coal_city_id, iron_city_id);
						return true;
					}
				}	
				
			}
			if(card2 == 20)
			{
				if (brass_game.canBuildIndustry(false, 20, 1, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(20, 1, player_id);
					int iron_city_id = brass_game.canMoveIron(1, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						action.selectBuildAction(count, 20, 1, coal_city_id, iron_city_id);
						return true;
					}
				}		
			}
			count++;
			
		}
		return false;
		
	}
}