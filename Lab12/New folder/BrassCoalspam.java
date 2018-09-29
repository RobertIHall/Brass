package brass;

import java.util.List;
import java.uril.ArrayList;

class BrassSellCottonComputerPlayer implements BrassComputerPlayer
{
	private BrassGame brass_game;
	private int turn_count;
	private boolean can_still_make_shipyard;
	private boolean have_shipyard_card;
	private int built_ironworks;
	private BrassPlayer this_player;
	
	public BrassSellCottonComputerPlayer(BrassGame bg)
	{
		brass_game = bg;
		turn_count = 0;
		can_still_make_shipyard = true;
		built_ironworks = 0;
		this_player = null;
	}

	public BrassComputerPlayerAction getBrassMove()
	{
		if(this_player == null)
		{
			int computer_player_id = brass_game.getActivePlayerID();
			this_player = brass_game.getBrassPlayer(computer_player_id);
		}
		if(turn_count < 2)
		{

			return earlyGame();
		}
		int num_actions_already_taken = brass_game.getNumActionsTaken(computer_player_id);
		if(num_actions_already_taken == 0)
		{
			System.out.println("first turn");
			return firstTurn();
		}
		else
		{
			System.out.println("second turn");
			return secondTurn();
		}
	}

	private BrassComputerPlayerAction earlyGame()
	{
		BrassComputerPlayerAction computer_move = new BrassComputerPlayerAction();
		int computer_player_id = brass_game.getActivePlayerID();
		int num_actions_already_taken = brass_game.getNumActionsTaken(computer_player_id);
		if(num_actions_already_taken == 0)
		{
			System.out.println("Building close to Liverpool. Trying to, at least.");
			return startBuild();
		}
		if(num_actions_already_taken == 1)
		{
			System.out.println("Attempting to link to Liverpool");
			return makeLinks();
		}
	}
	
	
	private BrassComputerPlayerAction firstTurn()
	{
		BrassComputerPlayerAction computer_move = new BrassComputerPlayerAction();
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
				return computer_move;
			}
			else//if it's not fix what's stopping you
			{
				prepareShipyard(computer_move);
				if (computer_move.isActionSelected())
				{
					return computer_move;
				}
			}
		}
		
		int num_computer_players_unflipped_coal_mines = brass_game.countAllPlayersUnflippedIndustry(BrassIndustryEnum.COAL.getValue(), computer_player_id);
		if (num_computer_players_unflipped_coal_mines <= 1) //try to build coal mine
		{
			getAllInfoForIndustrySpecificBuildAction(computer_move, computer_player_id, BrassIndustryEnum.COAL.getValue());
			if (computer_move.isActionSelected()) 
			{
				return computer_move;
			}
		}
		
		int num_computer_players_unflipped_ironworks = brass_game.countAllPlayersUnflippedIndustry(BrassIndustryEnum.IRON.getValue(), computer_player_id);
		if (num_computer_players_unflipped_ironworks == 0 && built_ironworks < 2)
		{
			getAllInfoForIndustrySpecificBuildAction(computer_move, computer_player_id, BrassIndustryEnum.IRON.getValue());
			if (computer_move.isActionSelected()) 
			{
			built_ironworks++;
			return computer_move;
			}
		}
		
		System.out.println("Discard!");
		computer_move.selectDiscardAction(getCardForNonBuildAction(computer_player_id));
		return computer_move;
		
	}
	
	private void checkShipyard() //see if it's still possible to make a shipyard
	{
		int num_cards == this_player.getNumCards();
		BrassHand hand = this_player.getHand();
		boolean found_card = false;
		for(int i = 1; i <= 8; i++)
		{
			a_card = hand.getCardCityTechID(i) //24 11
			switch (a_card) {
				case 11: System.out.println("Have Liverpool card");
						found_card = true;
						 break;
				case 24: System.out.println("Have Shipyard card");
						 found_card = true;
						 break
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
		int player_money = brass_game.getMoney(computer_player_id);
		if(tech_level < 2)
		{
			action.selectTechUpgradeAction(getCardForNonBuildAction(computer_player_id), 5, 3)
		}
		else if(player_money < 25)
		{
			action.selectTakeLoanAction(getCardForNonBuildAction(computer_player_id), 3);
		}
	}
	
	private BrassComputerPlayerAction secondTurn()
	{
			BrassComputerPlayerAction computer_move = new BrassComputerPlayerAction();
			int computer_player_id = brass_game.getActivePlayerID();
			int player_money = brass_game.getMoney(computer_player_id);
			int player_income = brass_game.getIncome(computer_player_id);
			
			if (player_money < 15)
			{
				computer_move.selectTakeLoanAction(getCardForNonBuildAction(computer_player_id), 3);
				return computer_move;
			}
			
			if (player_money < 25 && turn_count == 4) //fix later
			{
				computer_move.selectTakeLoanAction(getCardForNonBuildAction(computer_player_id), 3);
				return computer_move;
			}
			
			checkShipyard();
			if(can_still_make_shipyard)//see if it's still possible to make a shipyard in Liverpool
			{
				getAllInfoForIndustrySpecificBuildAction(computer_move, computer_player_id, BrassIndustryEnum.SHIP.getValue());
				if (computer_move.isActionSelected()) 
				{
					can_still_make_shipyard = false;
					return computer_move;
				}
				else//if it's not fix what's stopping you
				{
					prepareShipyard(computer_move);
				if (computer_move.isActionSelected())
				{
					return computer_move;
				}
				}
			}
			
			getAllInfoForLinkToSellCottonAction(computer_move, computer_player_id, 1);//fix
			if (computer_move.isActionSelected()) 
			{	
				return computer_move;
			}
			
			getAllInfoForSortedLinkAction(computer_move, computer_player_id);
			if (computer_move.isActionSelected()) 
			{	
				return computer_move;
			}
			
			System.out.println("Discard!");
			computer_move.selectDiscardAction(getCardForNonBuildAction(computer_player_id));
			return computer_move;
	}
	
	private int getCardForNonBuildActionRail(int player_id)
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
	
	private void getAllInfoForIndustrySpecificBuildActionRail(BrassComputerPlayerAction computer_move, int player_id, int industry_id)
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
	
	private void getAllInfoForSortedLinkActionRail(BrassComputerPlayerAction computer_move, int computer_player_id)
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

	
	private void buildLinkToLiverpoolRail(BrassComputerPlayerAction bcpa, int player_id)
{
	ArrayList<Integer> goodLinks = new ArrayList<Integer>();
	fillGoodLinks(goodLinks);
	Iterator iter = goodLinks.iterator();
	List<int> make_rails = new ArrayList<int>();
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
				make_rails.add(linkID);
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
			brass_game.buildTestLink(linkID, player_id); //make temporary link
		}
		System.out.println("Got into whatever the heck this is");
	}
	}
	if(make_rails.size() == 2)
		{
			rail_1 = make_rails.get(0);
			rail_2 = make_rails.get(1);
			computer_move.selectDoubleLinkAction(getCardForNonBuildAction(computer_player_id), rail_1, rail_2);
		}
		else if(make_rails.size() == 1)
		{
			rail_1 = make_rails.get(0)
			computer_move.selectLinkAction(getCardForNonBuildAction(computer_player_id), rail_1)
		}
		delete_this = make_rails.get(0);
		brass_game.removeTestLink(delete_this); //remove earlier test link
		
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


private void getAllInfoForLinkToCoalMineActionRail(BrassComputerPlayerAction computer_move, int computer_player_id, int max_depth_limit)
{
	if (max_depth_limit < 1) max_depth_limit = 1;
	int num_connections = brass_game.getNumLinks();

	int curr_depth_limit = 1;
	int curr_depth = 1;
	List<int> make_rails = new ArrayList<int>();
		
	while((make_rails.size() < 2) && curr_depth_limit <= max_depth_limit)
	{
		//if a good link combination is found, this top level loop
		//determines the link to build
		for (int i = 1; i <= num_connections; i++)
		{
				//as soon as a link is found, stop
			if (make_rails.size() >= 2) 
			{	
				break; //not sure if break statement does what it's supposed to, namely stopping the for loop
			}
			boolean can_build_link = brass_game.canBuildLink(i, computer_player_id);
			if (can_build_link)
			{
				//temporarily build the link (by simply setting the player_id of the link to computer_player_id)
				brass_game.buildTestLink(i, computer_player_id);
				boolean can_player_spam_coal = false
				for(int i = 1; i <= 25; i++)
				{
					if(brass_game.canBuildIndustry(false ,i ,3 ,computer_player_id)
					{
						can_player_spam_coal = true
					}
				}
				if (can_player_spam_coal)
				{
					int link_id = i;
					make_rails.add(link_id);
				}
				else if (curr_depth < curr_depth_limit)
				{
					getAllInfoForLinkToCoalMineActionRec(make_rails, computer_player_id, curr_depth_limit, curr_depth + 1, num_connections, i);
				}
					//remove the temporary link (by simply setting the player_id of the link to 0)
					brass_game.removeTestLink(i);
				}
			}
			curr_depth_limit++;
		}
		if(make_rails.size() == 2)
		{
			rail_1 = make_rails.get(0);
			rail_2 = make_rails.get(1);
			computer_move.selectDoubleLinkAction(getCardForNonBuildAction(computer_player_id), rail_1, rail_2);
		}
		else if(make_rails.size() == 1)
		{
			rail_1 = make_rails.get(0)
			computer_move.selectLinkAction(getCardForNonBuildAction(computer_player_id), rail_1)
		}
	}
	
	private void getAllInfoForLinkToCoalMineActionRecRail(List make_rails, int computer_player_id, int max_depth, int curr_depth, int num_connections, int first_connection)
	{
		for (int i = 1; i <= num_connections; i++)
		{
			//as soon as a link is found, stop
			if (make_rails.size() >= 2) return;
			
			boolean can_build_link = brass_game.canBuildLink(i, computer_player_id);
			if (can_build_link)
			{
				//temporarily build the link (by simply setting the player_id of the link to computer_player_id)
				brass_game.buildTestLink(i, computer_player_id);
				for(int i = 1; i <= 25; i++)
					{
						if(brass_game.canBuildIndustry(false ,i ,3 ,computer_player_id)
						{
							can_player_spam_coal = true
						}
					}
					if(can_player_spam_coal)
					{
						computer_move.selectLinkAction(getCardForNonBuildAction(computer_player_id), first_connection);
						System.out.println("Coalspam = " + curr_depth);					
					}
				else if(curr_depth < max_depth)
				{
					getAllInfoForLinkToCoalMineActionRec(computer_move, computer_player_id, max_depth, curr_depth + 1, num_connections, first_connection);
				}
				//remove the temporary link (by simply setting the player_id of the link to 0)
				brass_game.removeTestLink(i);
			}
		}
	}
}

