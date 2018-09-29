package brass;

import java.util.List;

class BrassSellCottonComputerPlayer implements BrassComputerPlayer
{
	private BrassGame brass_game;
	
	public BrassSellCottonComputerPlayer(BrassGame bg)
	{
		brass_game = bg;
	}
	
	public BrassComputerPlayerAction getBrassMove()
	{
		BrassComputerPlayerAction computer_move = new BrassComputerPlayerAction();
		int computer_player_id = brass_game.getActivePlayerID();
		
		int num_actions_already_taken = brass_game.getNumActionsTaken(computer_player_id);
		boolean can_sell_cotton = brass_game.canSellCotton(computer_player_id);
		
		//can the computer player sell cotton for their second action?
		if (num_actions_already_taken == 1 && can_sell_cotton)
		{
			computer_move.selectSellCottonAction(getCardForNonBuildAction(computer_player_id));
			return computer_move;
		}
		
		//the take loan/sell cotton sequence
		if (num_actions_already_taken == 0 && can_sell_cotton)
		{
			int player_money = brass_game.getMoney(computer_player_id);
			int player_income = brass_game.getIncome(computer_player_id);
			if (player_money < 12 && player_income <= 10)
			{
				computer_move.selectTakeLoanAction(getCardForNonBuildAction(computer_player_id), 3);
				return computer_move;
			}
		}
		
		//if the computer player doesn't have an unflipped port, try to build one
		int num_computer_players_unflipped_ports = brass_game.countAllPlayersUnflippedIndustry(BrassIndustryEnum.PORT.getValue(), computer_player_id);
		if (num_computer_players_unflipped_ports == 0)
		{
			getAllInfoForIndustrySpecificBuildAction(computer_move, computer_player_id, BrassIndustryEnum.PORT.getValue());
			if (computer_move.isActionSelected()) return computer_move;
		}
		
		//if the computer player doesn't have an unflipped cotton mill, try to build one
		int num_computer_players_unflipped_cotton_mills = brass_game.countAllPlayersUnflippedIndustry(BrassIndustryEnum.COTTON.getValue(), computer_player_id);
		if (num_computer_players_unflipped_cotton_mills == 0)
		{
			getAllInfoForIndustrySpecificBuildAction(computer_move, computer_player_id, BrassIndustryEnum.COTTON.getValue());
			if (computer_move.isActionSelected()) return computer_move;
		}
		
		//can the computer player place a link that will help sell cotton?
		if (!can_sell_cotton)
		{
			getAllInfoForLinkToSellCottonAction(computer_move, computer_player_id, 1);
			if (computer_move.isActionSelected()) return computer_move;
		}
		
		//sort links by victory points (coins)
		getAllInfoForSortedLinkAction(computer_move, computer_player_id);
		if (computer_move.isActionSelected()) return computer_move;
		
		//take a loan if necessary (the constants 12 and 10 may need to be changed)
		int player_money = brass_game.getMoney(computer_player_id);
		int player_income = brass_game.getIncome(computer_player_id);
		if (player_money < 12 && player_income <= 10)
		{
			computer_move.selectTakeLoanAction(getCardForNonBuildAction(computer_player_id), 3);
			return computer_move;
		}
		
	System.out.println("Discard!");
		computer_move.selectDiscardAction(getCardForNonBuildAction(computer_player_id));
		return computer_move;
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
	
	//handles an arbitrary number of connected links
	private void getAllInfoForLinkToSellCottonAction(BrassComputerPlayerAction computer_move, int computer_player_id, int max_depth_limit)
	{
		if (max_depth_limit < 1) max_depth_limit = 1;
		int num_connections = brass_game.getNumLinks();

		int curr_depth_limit = 1;
		int curr_depth = 1;
		
		while(!computer_move.isActionSelected() && curr_depth_limit <= max_depth_limit)
		{
			//if a good link combination is found, this top level loop
			//determines the link to build
			for (int i = 1; i <= num_connections; i++)
			{
				//as soon as a link is found, stop
				if (computer_move.isActionSelected()) return;
				
				boolean can_build_link = brass_game.canBuildLink(i, computer_player_id);
				if (can_build_link)
				{
					//temporarily build the link (by simply setting the player_id of the link to computer_player_id)
					brass_game.buildTestLink(i, computer_player_id);
					boolean can_player_sell_cotton = brass_game.canSellCotton(computer_player_id);
					if (can_player_sell_cotton)
					{
						computer_move.selectLinkAction(getCardForNonBuildAction(computer_player_id), i);
	System.out.println("Sell cotton link top level");					
					}
					else if (curr_depth < curr_depth_limit)
					{
						getAllInfoForLinkToSellCottonActionRec(computer_move, computer_player_id, curr_depth_limit, curr_depth + 1, num_connections, i);
					}
					//remove the temporary link (by simply setting the player_id of the link to 0)
					brass_game.removeTestLink(i);
				}
			}
			curr_depth_limit++;
		}
	}
	
	private void getAllInfoForLinkToSellCottonActionRec(BrassComputerPlayerAction computer_move, int computer_player_id, int max_depth, int curr_depth, int num_connections, int first_connection)
	{
		for (int i = 1; i <= num_connections; i++)
		{
			//as soon as a link is found, stop
			if (computer_move.isActionSelected()) return;
			
			boolean can_build_link = brass_game.canBuildLink(i, computer_player_id);
			if (can_build_link)
			{
				//temporarily build the link (by simply setting the player_id of the link to computer_player_id)
				brass_game.buildTestLink(i, computer_player_id);
				boolean can_player_sell_cotton = brass_game.canSellCotton(computer_player_id);
				if (can_player_sell_cotton)
				{
					computer_move.selectLinkAction(getCardForNonBuildAction(computer_player_id), first_connection);
	System.out.println("Sell cotton link depth = " + curr_depth);					
				}
				else if (curr_depth < max_depth)
				{
					getAllInfoForLinkToSellCottonActionRec(computer_move, computer_player_id, max_depth, curr_depth + 1, num_connections, first_connection);
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
	
	private void getAllInfoForLinkAction(BrassComputerPlayerAction computer_move, int computer_player_id)
	{
		int num_connections = brass_game.getNumLinks();
		for (int i = 1; i <= num_connections; i++)
		{
			boolean can_build_link = brass_game.canBuildLink(i, computer_player_id);
			if (can_build_link)
			{
				computer_move.selectLinkAction(getCardForNonBuildAction(computer_player_id), i);
System.out.println("Regular Link");					
				return;
			}
		}
	}
	
}
