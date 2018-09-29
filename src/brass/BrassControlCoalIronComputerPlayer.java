package brass;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

class BrassControlCoalIronComputerPlayer implements BrassComputerPlayer
{
	private BrassGame brass_game;
	private BrassBoard brass_board;
	private int turn_count;
	private boolean stopFirstSell;
	private boolean shipBuilt;
	private boolean firstLoanTaken;
	private boolean actualShipNotBuilt;
	private int coalTech;
	private int ironTech;
	private boolean can_still_make_shipyard;
	private boolean have_shipyard_card;
	private int built_ironworks;
	private BrassPlayer this_player;

	public BrassControlCoalIronComputerPlayer(BrassGame bg, BrassBoard bd)
	{
		brass_game = bg;
		brass_board = bd;
		turn_count = 0;
		stopFirstSell = false;
		shipBuilt = false;
		firstLoanTaken = false;
		actualShipNotBuilt = false;
		coalTech = 1;
		ironTech = 1;
		can_still_make_shipyard = true;
		built_ironworks = 0;
		this_player = null;
	}
	
	
	public BrassComputerPlayerAction getBrassMove()
	{
		System.out.println("Inside My move");
		BrassComputerPlayerAction computer_move = new BrassComputerPlayerAction();
		int computer_player_id = brass_game.getActivePlayerID();
		int num_actions_already_taken = brass_game.getNumActionsTaken(computer_player_id);
		boolean can_sell_cotton = brass_game.canSellCotton(computer_player_id);
		
		if(!brass_game.getPhase())
		{
			
			if(brass_game.isFirstTurn())
			{
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
			if(stopFirstSell && !shipBuilt)
			{
				System.out.println("Inside");
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

		}
		else//Rail Phase
		{
			BrassComputerPlayerAction action = new BrassComputerPlayerAction();
			if(this_player == null)//considering removing 
			{
				this_player = brass_game.getBrassPlayer(computer_player_id);
			}
			if(turn_count == 0)
			{
				veryFirstTurn(action, computer_player_id, 1);
				return action;
			}
			if(turn_count == 1)
			{
				buildLinkToLiverpoolRail(action, computer_player_id);
				return action;
			}
			num_actions_already_taken = brass_game.getNumActionsTaken(computer_player_id);
			if (num_actions_already_taken == 0)
			{
				System.out.println("first turn");
				firstTurn(action);
				return action;
			}
			else
			{
				System.out.println("second turn");
				secondTurn(action);
				return action;
			}
		}

		return computer_move;
	}
	
	private void firstTurnCanal(BrassComputerPlayerAction computer_move, int player_id, int industry_id)
	{
		//loop over computer player cards
		//find a card that corresponds to something the computer player can build
		//select that card and the build action
		int num_cards = brass_game.getNumCards(player_id);
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
				if (brass_game.canBuildIndustry(false, 11, cardID, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(11, cardID, player_id);
					int iron_city_id = brass_game.canMoveIron(cardID, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(card, 11, cardID, coal_city_id, iron_city_id);
						return;
					}
				}
				if (brass_game.canBuildIndustry(false, 15, cardID, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(15, cardID, player_id);
					int iron_city_id = brass_game.canMoveIron(cardID, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(card, 15, cardID, coal_city_id, iron_city_id);
						return;
					}
				}
				if (brass_game.canBuildIndustry(false, 18, cardID, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(18, cardID, player_id);
					int iron_city_id = brass_game.canMoveIron(cardID, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(card, 18, cardID, coal_city_id, iron_city_id);
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
						return;
					}
				}	
				
			}
			
			card2++;
		}
		//By sheer bullshit of the draw of cards we don't get any city close I reused dr. browns algorithm for the 
		//ai's decision to put it wherever the card chooses if its not any of our cities.
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
				if(brass_card_city_tech_id == 11)
				{
					continue;
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
//--------------------------------------------------------------------------------------------------------------------
//RAIL THINGS / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / / /
//--------------------------------------------------------------------------------------------------------------------

private void veryFirstTurn(BrassComputerPlayerAction computer_move, int player_id, int industry_id)
{
		//loop over computer player cards
		//find a card that corresponds to something the computer player can build
		//select that card and the build action
		int num_cards = brass_game.getNumCards(player_id);
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
			if (brass_card_city_tech_id == 3 || brass_card_city_tech_id == 18 || brass_card_city_tech_id == 4) //cities near Liverpool with coal
			{
				city_id = brass_card_city_tech_id;
				System.out.println("Parameters");
				System.out.println(city_id);
				System.out.println(industry_id);
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
			else if (brass_card_city_tech_id == 20)//is coal
			{
				int card_industry_id = brass_card_city_tech_id - 19;
				if(industry_id == card_industry_id)
				{
					System.out.println("Parameters");
				System.out.println(industry_id);
					if (brass_game.canBuildIndustry(false, 18, industry_id, player_id))
					{
						int coal_city_id = brass_game.canMoveCoal(18, industry_id, player_id);
						int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
						if (coal_city_id >= 0 && iron_city_id >= 0) 
						{
							computer_move.selectBuildAction(i, 18, industry_id, coal_city_id, iron_city_id);
							return;
						}
					}
					System.out.println("Parameters");
				System.out.println(industry_id);
					if (brass_game.canBuildIndustry(false, 4, industry_id, player_id))
					{
						int coal_city_id = brass_game.canMoveCoal(4, industry_id, player_id);
						int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
						if (coal_city_id >= 0 && iron_city_id >= 0) 
						{
							computer_move.selectBuildAction(i, 4, industry_id, coal_city_id, iron_city_id);
							return;
						}
					}
					System.out.println("Parameters");
				System.out.println(industry_id);
					if (brass_game.canBuildIndustry(false, 3, industry_id, player_id))
					{
						int coal_city_id = brass_game.canMoveCoal(3, industry_id, player_id);
						int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
						if (coal_city_id >= 0 && iron_city_id >= 0) 
						{
							computer_move.selectBuildAction(i, 3, industry_id, coal_city_id, iron_city_id);
							return;
						}
					}
				}
			}
			else if (brass_card_city_tech_id == 19)//wigan
			{
				city_id = brass_card_city_tech_id;
				System.out.println("Parameters");
				System.out.println(city_id);
				System.out.println(industry_id);
				if (brass_game.canBuildIndustry(true, city_id, industry_id, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(city_id, industry_id, player_id);
					int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						System.out.println("Wigan");
						computer_move.selectBuildAction(i, city_id, industry_id, coal_city_id, iron_city_id);
						return;
					}
			}
		}
		System.out.println("Looking for port instead.");
		//If none of these options happen to build cotton mill I look for a port to build.
		Iterator iter = cards.iterator();
		int card = 1;
		while(iter.hasNext())
		{
			int cardID = (int)iter.next();
			if(cardID == 23)
			{
				cardID = cardID - 19;
				if (brass_game.canBuildIndustry(false, 11, cardID, player_id))
				{
					System.out.println("tried #1");
					int coal_city_id = brass_game.canMoveCoal(11, cardID, player_id);
					int iron_city_id = brass_game.canMoveIron(cardID, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(card, 11, cardID, coal_city_id, iron_city_id);
						return;
					}
				}
				if (brass_game.canBuildIndustry(false, 15, cardID, player_id))
				{
					System.out.println("tried #2");
					int coal_city_id = brass_game.canMoveCoal(15, cardID, player_id);
					int iron_city_id = brass_game.canMoveIron(cardID, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(card, 15, cardID, coal_city_id, iron_city_id);
						return;
					}
				}
				if (brass_game.canBuildIndustry(false, 18, cardID, player_id))
				{
					System.out.println("tried #3");
					int coal_city_id = brass_game.canMoveCoal(18, cardID, player_id);
					int iron_city_id = brass_game.canMoveIron(cardID, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(card, 18, cardID, coal_city_id, iron_city_id);
						return;
					}
				}	
			}
			card++;
		}
		//If I cannot build a port I look for the closest city to the three main cities and build a cotton mill. (Blackburn, Warrington, Preston)
		System.out.println("COTTON MILL OK?!");
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
						System.out.println("shreak");
						computer_move.selectBuildAction(card2, card_id, industry_id, coal_city_id, iron_city_id);
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
						System.out.println("dongkey");
						computer_move.selectBuildAction(card2, card_id, industry_id, coal_city_id, iron_city_id);
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
						System.out.println("puissantboots");
						computer_move.selectBuildAction(card2, card_id, industry_id, coal_city_id, iron_city_id);
						return;
					}
				}	
				
			}
			
			card2++;
		}
		
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		//By sheer bullshit of the draw of cards we don't get any city close I reused dr. browns algorithm for the 
		//ai's decision to put it wherever the card chooses if its not any of our cities.
		for (int j = 1; j <= num_cards; j++)
		{
			if (!brass_game.canSelectCard(i, player_id)) continue;
				
			int brass_card_city_tech_id_2 = brass_game.getCardCityTechID(j);
			if (brass_card_city_tech_id_2  <= 19)
			{
				city_id = brass_card_city_tech_id_2 ;
				if (brass_game.canBuildIndustry(true, city_id, industry_id, player_id))
				{
					int coal_city_id = brass_game.canMoveCoal(city_id, industry_id, player_id);
					int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
					if (coal_city_id >= 0 && iron_city_id >= 0) 
					{
						computer_move.selectBuildAction(j, city_id, industry_id, coal_city_id, iron_city_id);
						return;
					}
				}
			}
			else
			{
				int card_industry_id = brass_card_city_tech_id_2  - 19;
				if (industry_id == card_industry_id)
				{
					//what cities can this industry be built in?
					for (int k = 1; k <= 19; k++)
					{
						city_id = k;
						if (brass_game.canBuildIndustry(false, city_id, industry_id, player_id))
						{
							int coal_city_id = brass_game.canMoveCoal(city_id, industry_id, player_id);
							int iron_city_id = brass_game.canMoveIron(industry_id, player_id);
							if (coal_city_id >= 0 && iron_city_id >= 0) 
							{
								computer_move.selectBuildAction(k, city_id, industry_id, coal_city_id, iron_city_id);
								return;
							}
						}
					}
				}
			}
		}
	}
	System.out.println("Discard!");
	computer_move.selectDiscardAction(getCardForNonBuildAction(player_id));
	return;
	}

private void buildLinkToLiverpoolRail(BrassComputerPlayerAction bcpa, int player_id)
{
	ArrayList<Integer> goodLinks = new ArrayList<Integer>();
	fillGoodLinks(goodLinks);
	Iterator iter = goodLinks.iterator();
	List<Integer> make_rails = new ArrayList<Integer>();
	while(iter.hasNext()) //try to make first link
	{
		int linkID = (int)iter.next();
		if(brass_game.canBuildLink(linkID, player_id))
		{
			make_rails.add(linkID);
			brass_game.buildTestLink(linkID, player_id); //make temporary link
		}
	}
	goodLinks = new ArrayList<Integer>();
	fillGoodLinks(goodLinks);
	iter = goodLinks.iterator();
	while(iter.hasNext()) //try to make second link into liverpool
		{
			int linkID2 = (int)iter.next();
			if(brass_game.canBuildLink(linkID2, player_id))
			{
				make_rails.add(linkID2);
			}
		}
	int num_connections = brass_game.getNumLinks();
	for (int i = 1; i <= num_connections; i++) //just try to get as close as possible if good links dont work
	{
	if (make_rails.size() >= 2)
	{
		break;
	}
	boolean can_build_link = brass_game.canBuildLink(i, player_id);
	if (can_build_link)
	{
		make_rails.add(i);
		if (make_rails.size() < 2)
		{
			brass_game.buildTestLink(i, player_id); //make temporary link
		}
		System.out.println("Got into whatever the heck this is");
	}
	}
	if(make_rails.size() == 2)
		{
			int rail_1 = (int)make_rails.get(0);
			int rail_2 = (int)make_rails.get(1);
			bcpa.selectDoubleLinkAction(getCardForNonBuildAction(player_id), rail_1, rail_2);
			return;
		}
		else if(make_rails.size() == 1)
		{
			int rail_1 = (int)make_rails.get(0);
			bcpa.selectLinkAction(getCardForNonBuildAction(player_id), rail_1);
			return;
		}
	int delete_this = (int)make_rails.get(0);
	brass_game.removeTestLink(delete_this); //remove earlier test link
	System.out.println("Discard!");
	bcpa.selectDiscardAction(getCardForNonBuildAction(player_id));
	return;
}

private void firstTurn(BrassComputerPlayerAction computer_move)
{
	int computer_player_id = brass_game.getActivePlayerID();
	int player_money = brass_game.getMoney(computer_player_id);
	int player_income = brass_game.getIncome(computer_player_id);
	checkShipyard();
	if(can_still_make_shipyard)//see if it's still possible to make a shipyard in Liverpool
	{
		getAllInfoForIndustrySpecificBuildAction(computer_move, computer_player_id, BrassIndustryEnum.SHIP.getValue());
		if (computer_move.isActionSelected()) 
		{
			can_still_make_shipyard = false;
			return;
		}
			
		else//if it's not fix what's stopping you
		{
			prepareShipyard(computer_move);
			if (computer_move.isActionSelected())
			{
				return;
			}
		}
	}
		
	int num_computer_players_unflipped_coal_mines = brass_game.countAllPlayersUnflippedIndustry(BrassIndustryEnum.COAL.getValue(), computer_player_id);
	if (num_computer_players_unflipped_coal_mines <= 1) //try to build coal mine
	{
		getAllInfoForIndustrySpecificBuildAction(computer_move, computer_player_id, BrassIndustryEnum.COAL.getValue());
		if (computer_move.isActionSelected()) 
		{
			return;
		}
	}
		
	int num_computer_players_unflipped_ironworks = brass_game.countAllPlayersUnflippedIndustry(BrassIndustryEnum.IRON.getValue(), computer_player_id);
	if (num_computer_players_unflipped_ironworks == 0 && built_ironworks < 2)
	{
		getAllInfoForIndustrySpecificBuildAction(computer_move, computer_player_id, BrassIndustryEnum.IRON.getValue());
		if (computer_move.isActionSelected()) 
		{
		built_ironworks++;
		return;
		}
	}
		
	System.out.println("Discard!");
	computer_move.selectDiscardAction(getCardForNonBuildAction(computer_player_id));
	return;
}

private void checkShipyard() //see if it's still possible to make a shipyard
{
	int num_cards = this_player.getNumCards();
	BrassHand hand = this_player.getHand();
	boolean found_card = false;
	for(int i = 1; i <= 8; i++)
	{
		int a_card = hand.getCardCityTechID(i); //24 11
		switch (a_card) {
			case 11: System.out.println("Have Liverpool card");
					found_card = true;
					 break;
			case 24: System.out.println("Have Shipyard card");
					 found_card = true;
					 break;
			}
	if((num_cards < 8) && !(found_card))
	{
		System.out.println("RIP shipyard");
		can_still_make_shipyard = false;
	}
	}
}

private void prepareShipyard(BrassComputerPlayerAction action)
{
	int tech_level = this_player.getTechLevel(5);
	int computer_player_id = brass_game.getActivePlayerID();
	int player_money = brass_game.getMoney(computer_player_id);
	if(tech_level < 2)
	{
		action.selectTechUpgradeAction(getCardForNonBuildAction(computer_player_id), 5, 3);
	}
	else if(player_money < 25)
	{
		action.selectTakeLoanAction(getCardForNonBuildAction(computer_player_id), 3);
	}
}

	
	
private void secondTurn(BrassComputerPlayerAction computer_move)
{
		int computer_player_id = brass_game.getActivePlayerID();
		int player_money = brass_game.getMoney(computer_player_id);
		int player_income = brass_game.getIncome(computer_player_id);
			
		if (player_money < 15)
		{
			computer_move.selectTakeLoanAction(getCardForNonBuildAction(computer_player_id), 3);
			return;
		}
			
		if (player_money < 25 && turn_count == 4) //fix later
		{
			computer_move.selectTakeLoanAction(getCardForNonBuildAction(computer_player_id), 3);
			return;
		}
			
		checkShipyard();
		if(can_still_make_shipyard)//see if it's still possible to make a shipyard in Liverpool
		{
			getAllInfoForIndustrySpecificBuildAction(computer_move, computer_player_id, BrassIndustryEnum.SHIP.getValue());
			if (computer_move.isActionSelected()) 
			{
				can_still_make_shipyard = false;
				return;
			}
			else//if it's not fix what's stopping you
			{
				prepareShipyard(computer_move);
			if (computer_move.isActionSelected())
			{
				return;
			}
			}
		}
			
		getAllInfoForLinkToCoalMineAction(computer_move, computer_player_id, 1);//fix
		if (computer_move.isActionSelected()) 
		{	
			return;
		}
			
		getAllInfoForSortedLinkAction(computer_move, computer_player_id);
		if (computer_move.isActionSelected()) 
		{	
			return;
		}
			
		System.out.println("Discard!");
		computer_move.selectDiscardAction(getCardForNonBuildAction(computer_player_id));
		return;
}

private void getAllInfoForLinkToCoalMineAction(BrassComputerPlayerAction computer_move, int computer_player_id, int max_depth_limit)
{
	if (max_depth_limit < 1) max_depth_limit = 1;
	int num_connections = brass_game.getNumLinks();

	int curr_depth_limit = 1;
	int curr_depth = 1;
	List<Integer> make_rails = new ArrayList<Integer>();
		
	while((make_rails.size() < 2) && curr_depth_limit <= max_depth_limit)
	{
		//if a good link combination is found, this top level loop
		//determines the link to build
		for (int j = 1; j <= num_connections; j++)
		{
				//as soon as a link is found, stop
			if (make_rails.size() >= 2) 
			{	
				break; //not sure if break statement does what it's supposed to, namely stopping the for loop
			}
			boolean can_build_link = brass_game.canBuildLink(j, computer_player_id);
			if (can_build_link)
			{
				//temporarily build the link (by simply setting the player_id of the link to computer_player_id)
				brass_game.buildTestLink(j, computer_player_id);
				boolean can_player_spam_coal = false;
				for(int k = 1; k <= 25; k++)
				{
					if(brass_game.canBuildIndustry(false ,k ,3 ,computer_player_id))
					{
						can_player_spam_coal = true;
					}
				}
				if (can_player_spam_coal)
				{
					int link_id = j;
					make_rails.add(link_id);
				}
				else if (curr_depth < curr_depth_limit)
				{
					getAllInfoForLinkToCoalMineActionRec(make_rails, computer_player_id, curr_depth_limit, curr_depth + 1, num_connections, j);
				}
					//remove the temporary link (by simply setting the player_id of the link to 0)
					brass_game.removeTestLink(j);
				}
			}
			curr_depth_limit++;
		}
		if(make_rails.size() == 2)
		{
			int rail_1 = (int)make_rails.get(0);
			int rail_2 = (int)make_rails.get(1);
			computer_move.selectDoubleLinkAction(getCardForNonBuildAction(computer_player_id), rail_1, rail_2);
		}
		else if(make_rails.size() == 1)
		{
			int rail_1 = (int)make_rails.get(0);
			computer_move.selectLinkAction(getCardForNonBuildAction(computer_player_id), rail_1);
		}
}
	
private void getAllInfoForLinkToCoalMineActionRec(List make_rails, int computer_player_id, int max_depth, int curr_depth, int num_connections, int first_connection)
{
	for (int i = 1; i <= num_connections; i++)
	{
		//as soon as a link is found, stop
		if (make_rails.size() >= 2) 
		{
			return;
		}
		boolean can_build_link = brass_game.canBuildLink(i, computer_player_id);
		if (can_build_link)
		{
			//temporarily build the link (by simply setting the player_id of the link to computer_player_id)
			brass_game.buildTestLink(i, computer_player_id);
			boolean can_player_spam_coal = false;
			for(int j = 1; j <= 25; j++)
				{
					if(brass_game.canBuildIndustry(false ,j ,3 ,computer_player_id))
					{
						can_player_spam_coal = true;
					}
				}
				if(can_player_spam_coal)
				{
					//computer_move.selectLinkAction(getCardForNonBuildAction(computer_player_id), first_connection);
					make_rails.add(first_connection);
					System.out.println("Coalspam = " + curr_depth);					
				}
			else if(curr_depth < max_depth)
			{
				getAllInfoForLinkToCoalMineActionRec(make_rails, computer_player_id, max_depth, curr_depth + 1, num_connections, first_connection);
			}
			//remove the temporary link (by simply setting the player_id of the link to 0)
			brass_game.removeTestLink(i);
		}
	}
}

private void getAllInfoForSortedLinkAction(BrassComputerPlayerAction computer_move, int computer_player_id)
{	
	List<Integer> sorted_links = brass_game.getSortedLinks();
	int num_connections = sorted_links.size();
	for (int i = 1; i <= num_connections; i++)
	{
		int link_id = sorted_links.get(i-1);
		boolean can_build_link = brass_game.canBuildLink(link_id, computer_player_id);
		if (can_build_link)
		{
			computer_move.selectLinkAction(getCardForNonBuildAction(computer_player_id), link_id);
			System.out.println("Sorted links");				
			return;
		}
	}
}
	
private int getCardForNonBuildAction(int player_id)
{
	int num_cards = brass_game.getNumCards(player_id);
	boolean brass_phase = brass_game.getBrassPhase();
		
	for (int i = 1; i <= num_cards; i++)
	{
		if (!brass_game.canSelectCard(i, player_id)) continue;
		int brass_card_city_tech_id = brass_game.getCardCityTechID(i);
			
		if (brass_card_city_tech_id < 20)
		{
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
		
		if (brass_card_city_tech_id == 24) return i; //shipyard industry card
		if (brass_card_city_tech_id == 1)  return i; //barrow
		if (brass_card_city_tech_id == 2) return i;  //birkenhead
			
		//duplicate industry cards
		if (brass_card_city_tech_id > 19)
		{
			int industry_id = brass_card_city_tech_id;// - 19;
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
	
	/*private void getAllInfoForIndustrySpecificBuildAction(BrassComputerPlayerAction computer_move, int player_id, int industry_id)
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
	}*/
}
