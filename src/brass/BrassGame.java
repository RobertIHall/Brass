package brass;

import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

import gui.BasicGUI;
import gui.DrawImage;
import gui.Drawable;
import gui.PixelPoint;
import gui.PixelDimension;

public class BrassGame implements Drawable
{
	private DrawImage board_img;
	private BasicGUI brass_gui;
	private BrassDeck brass_deck;
	private BrassPlayers brass_players;
	private BrassBoard brass_board;
	private BrassTrack brass_track;
	
	private boolean brass_phase;
	
	private BrassState select_card_state;
	private BrassState select_action_state;
	private BrassState link_action_state;
	private BrassState build_action_state;
	private BrassState tech_upgrade_action_state;
	private BrassState double_card_action_state;
	private BrassState double_rail_action_state;
	private BrassState replace_action_state;
	
	private BrassState game_over_state;
	private BrassState current_state;
	
	private int num_players;
	private int num_computer_players;
	private BrassComputerPlayer[] brass_computer_players;
	
	public List<Integer> getSortedLinks()
	{
		return brass_board.getSortedLinks();
	}
	public boolean getPhase()
	{
		return brass_phase;
		
	}
	
	public int getMoney(int player_id)
	{
		return brass_players.getMoney(player_id);
	}
	
	public int getIncome(int player_id)
	{
		return brass_players.getIncome(player_id);
	}

	public int getNumLinks()
	{
		return brass_board.getNumLinks();
	}
	
	public int countAllPlayersUnflippedIndustry(int industry_id, int player_id)
	{
		return brass_board.countAllPlayersUnflippedIndustry(industry_id, player_id);
	}
	
	public int getNumActionsTaken(int player_id)
	{
		return brass_players.getNumActionsTaken(player_id);
	}
	
	public int getNumTokensInCity(int city_id, int player_id)
	{
		return brass_board.getNumTokensInCity(city_id, player_id);
	}
	
	public boolean isCityFull(int city_id)
	{
		return brass_board.isCityFull(city_id);
	}
	
	public boolean canSelectCard(int card_index, int player_id)
	{
		return brass_players.canSelectCard(card_index, player_id);
	}
	
	public int getNumCards(int player_id)
	{
		return brass_players.getNumCards(player_id);
	}
	
	public boolean isComputerPlayerTurn(int active_player_id)
   {
	   if ((active_player_id > num_players) && !isGameOver())
	   {
		   return true;
	   }
	   return false;
   }
   
   
   public BrassPlayer getBrassPlayer(int id)
	{
		return brass_players.getPlayer(id);
	}
   
   public void computerPlayerTurn(int computer_player_id)
   {
System.out.println();
System.out.println("Computer Player: " + computer_player_id);
		BrassComputerPlayer brass_computer_player = brass_computer_players[computer_player_id - (num_players + 1)];
		BrassComputerPlayerAction computer_move = brass_computer_player.getBrassMove();
		//computer_move.displayContents();
		
		int card_index = computer_move.getCardIndex();
		System.out.println("The selected card index is: " + card_index);
		selectCard(card_index);
System.out.println("Card Selected: " + card_index);
int brass_card_city_tech_id = getCardCityTechID(card_index);
System.out.println("Card City/Tech: " + brass_card_city_tech_id);

		int action_id = computer_move.getActionID();
System.out.println("Action Selected: " + action_id);			
		if (action_id == BrassActionEnum.BUILD.getValue())
		{
System.out.println("Build Info City: " + computer_move.getCityID());	
System.out.println("Build Info Industry: " + computer_move.getIndustryID());	
			computerBuildIndustry(computer_move, computer_player_id);
		}
		
		else if (action_id == BrassActionEnum.LINK.getValue())
		{
			int link_id = computer_move.getLinkID();
			buildLink(link_id, computer_player_id);
		}
		
		else if (action_id == BrassActionEnum.SELL.getValue())
		{
			sellCotton(computer_player_id);
		}
		
		else if (action_id == BrassActionEnum.UPGRADE.getValue())
		{
			int industry_id_1 = computer_move.getFirstTechUpgrade();
			int industry_id_2 = computer_move.getSecondTechUpgrade();
			techUpgrade(industry_id_1, computer_player_id);
			boolean can_tech_upgrade = canTechUpgrade(computer_player_id);
			if (can_tech_upgrade)
			{
				techUpgrade(industry_id_2, computer_player_id);
			}
		}
		
		else if (action_id >= BrassActionEnum.LOAN_10.getValue() && action_id <= BrassActionEnum.LOAN_30.getValue())
		{
			int loan_amount = action_id*10;
			loanActionSelected(loan_amount);
		}
		
		playerActionCompleted(); 
		
		if (isGameOver())
		{
			awardVictoryPoints();
			current_state = game_over_state;
		}
   }
   
   private void computerBuildIndustry(BrassComputerPlayerAction bcpa, int computer_player_id)
	{
		int city_id = bcpa.getCityID();
		int industry_id = bcpa.getIndustryID();
			
		int coal_city_id = bcpa.getCoalCityID();
		int iron_city_id = bcpa.getIronCityID();
		
		assert coal_city_id >= 0 : "Cannot move coal.";
		assert iron_city_id >= 0 : "Cannot move iron.";
		
		BrassToken brass_token = brass_players.payForToken(industry_id, computer_player_id);
		brass_board.placeTokenInCity(city_id, brass_token);
		
		if (coal_city_id > 0)
		{
			brass_board.moveCoal(coal_city_id, computer_player_id, brass_players, brass_track);
		}
		if (iron_city_id > 0)
		{
			brass_board.moveIron(iron_city_id, computer_player_id, brass_players, brass_track);
		}
			
		if (industry_id == BrassIndustryEnum.COAL.getValue())
		{
			if (brass_board.isCityConnectedToConstructedPort(city_id))
			{
				brass_board.moveCoalToDemandTrack(computer_player_id, brass_token, brass_players, brass_track);
			}
		}
			
		else if (industry_id == BrassIndustryEnum.IRON.getValue())
		{
			brass_board.moveIronToDemandTrack(computer_player_id, brass_token, brass_players, brass_track);
		}
				
		//if the industry built was a shipyard, it flips immediately
		else if (industry_id == BrassIndustryEnum.SHIP.getValue())
		{
			brass_board.shipyardBuilt(city_id, computer_player_id, brass_players, brass_track);
		}
	}
	
	public void mouseClicked(int x_click, int y_click)
	{
		int active_player_id = brass_players.getActivePlayerID();
		if (isComputerPlayerTurn(active_player_id))
		{
			computerPlayerTurn(active_player_id);
		}
		else
		{
			current_state.mouseClicked(x_click, y_click);
		}
	}
	
	//can only be called by the computer players who are experimenting with links
	//and have created a test link which must be removed
	public void buildTestLink(int link_id, int computer_player_id)
	{
		brass_board.buildTestLink(link_id, computer_player_id);
	}
	
	public void removeTestLink(int link_id)
	{
		brass_board.removeTestLink(link_id);
	}
	
	public boolean canReplaceIndustry(boolean city_card, int city_id, int industry_id, int player_id)
	{	
		int tech_level = brass_players.getTechLevel(industry_id, player_id);
		boolean can_city_replace = brass_board.canCityReplaceToken(city_id, industry_id, tech_level, player_id);
		int industry_cost = brass_players.getIndustryCost(industry_id, player_id);
		int player_money = brass_players.getMoney(player_id);
		
		int coal_city_id = canMoveCoal(city_id, industry_id, player_id);
		if (coal_city_id == 20)
		{
			industry_cost += brass_board.getCostToBuyFromCoalDemandTrack();
		}
		
		int iron_city_id = canMoveIron(industry_id, player_id);
		if (iron_city_id == 20)
		{
			industry_cost += brass_board.getCostToBuyFromIronDemandTrack();
		}
		
		boolean can_player_afford = (player_money >= industry_cost);
		return coal_city_id >= 0 && iron_city_id >= 0 && can_city_replace && can_player_afford;
	}
	
	public void replaceIndustry(int city_id, int industry_id, int player_id)
	{
		//repeat the search
		int coal_city_id = canMoveCoal(city_id, industry_id, player_id);
		int iron_city_id = canMoveIron(industry_id, player_id);
		
		assert coal_city_id >= 0 : "Cannot move coal.";
		assert iron_city_id >= 0 : "Cannot move iron.";
		
		BrassToken brass_token = brass_players.payForToken(industry_id, player_id);
		brass_board.replaceTokenInCity(city_id, brass_token);

		if (coal_city_id > 0)
		{
			brass_board.moveCoal(coal_city_id, player_id, brass_players, brass_track);
		}
		if (iron_city_id > 0)
		{
			brass_board.moveIron(iron_city_id, player_id, brass_players, brass_track);
		}
			
		if (industry_id == BrassIndustryEnum.COAL.getValue())
		{
			if (brass_board.isCityConnectedToConstructedPort(city_id))
			{
				brass_board.moveCoalToDemandTrack(player_id, brass_token, brass_players, brass_track);
			}
		}
		
		else if (industry_id == BrassIndustryEnum.IRON.getValue())
		{
			brass_board.moveIronToDemandTrack(player_id, brass_token, brass_players, brass_track);
		}
			
		//if the industry built was a shipyard, it flips immediately
		else if (industry_id == BrassIndustryEnum.SHIP.getValue())
		{
			brass_board.shipyardBuilt(city_id, player_id, brass_players, brass_track);
		}
	}
	
	public boolean canBuildIndustry(boolean city_card, int city_id, int industry_id, int player_id)
	{	
		//can assert the next two conditions if the protocol is not followed
		//but the subsequent statements there is no easy way to assert
		boolean can_city_accept = brass_board.canCityAcceptToken(city_id, industry_id); //can that industry be placed in the city
		//boolean can_player_afford = canPlayerAffordIndustry(industry_id, player_id); //does the player have enough money
		int industry_cost = brass_players.getIndustryCost(industry_id, player_id);
		int player_money = brass_players.getMoney(player_id);
		int num_tokens_in_city = brass_board.getNumTokensInCity(city_id, player_id);
		int num_tokens_on_board = brass_board.getNumTokensOnBoard(player_id);
		
		//can't really assert the following checks if the protocol is not followed
		//in the canal phase, players can only have one token in a city
		boolean can_player_place_in_city = (num_tokens_in_city == 0 || brass_phase);
		//players can use industry cards similar to city cards if they have no tokens on the board
		boolean is_connected_to_network = brass_board.isCityConnectedToPlayerNetwork(city_id, player_id);
		boolean can_player_use_industry_card = (city_card || (num_tokens_on_board == 0) || is_connected_to_network);
		
		int coal_city_id = canMoveCoal(city_id, industry_id, player_id);
		if (coal_city_id == 20)
		{
			industry_cost += brass_board.getCostToBuyFromCoalDemandTrack();
		}
		
		int iron_city_id = canMoveIron(industry_id, player_id);
		if (iron_city_id == 20)
		{
			industry_cost += brass_board.getCostToBuyFromIronDemandTrack();
		}
		
		boolean can_player_afford = (player_money >= industry_cost);
		//just added!
		boolean is_tech_level_requirement_met = brass_players.isTechLevelRequirementMet(industry_id, player_id, brass_phase);
		
		//shipyard card being used in Birkenhead or Liverpool
		//or port card being used to build in Liverpool
		//currently appears that player cannot use an industry card to build
		boolean check_virtual_link = (!can_player_use_industry_card) && (((industry_id == 5) && ((city_id == 2) || (city_id == 11))) || ((industry_id == 4) && (city_id == 11)));
		
		if (check_virtual_link)
		{
			System.out.println("check virtual link");
			//player building in Birkenhead
			if (city_id == 2)
			{
				//is player connected to Liverpool
				can_player_use_industry_card = brass_board.isCityConnectedToPlayerNetwork(11, player_id);
	System.out.println("virtual link: " + can_player_use_industry_card);
			}
			else
			{
				//is player connected to Birkenhead
				can_player_use_industry_card =brass_board.isCityConnectedToPlayerNetwork(2, player_id);
	System.out.println("virtual link: " + can_player_use_industry_card);
			}
		}
		
		return coal_city_id >= 0 && iron_city_id >= 0 && can_city_accept && can_player_afford && can_player_place_in_city && can_player_use_industry_card && is_tech_level_requirement_met;
	}
	
	public void buildIndustry(int city_id, int industry_id, int player_id)
	{
		//repeat the search
		int coal_city_id = canMoveCoal(city_id, industry_id, player_id);
		int iron_city_id = canMoveIron(industry_id, player_id);
		
		assert coal_city_id >= 0 : "Cannot move coal.";
		assert iron_city_id >= 0 : "Cannot move iron.";
		
		BrassToken brass_token = brass_players.payForToken(industry_id, player_id);
		brass_board.placeTokenInCity(city_id, brass_token);

		if (coal_city_id > 0)
		{
			brass_board.moveCoal(coal_city_id, player_id, brass_players, brass_track);
		}
		if (iron_city_id > 0)
		{
			brass_board.moveIron(iron_city_id, player_id, brass_players, brass_track);
		}
			
		if (industry_id == BrassIndustryEnum.COAL.getValue())
		{
			if (brass_board.isCityConnectedToConstructedPort(city_id))
			{
				brass_board.moveCoalToDemandTrack(player_id, brass_token, brass_players, brass_track);
			}
		}
		
		else if (industry_id == BrassIndustryEnum.IRON.getValue())
		{
			brass_board.moveIronToDemandTrack(player_id, brass_token, brass_players, brass_track);
		}
			
		//if the industry built was a shipyard, it flips immediately
		else if (industry_id == BrassIndustryEnum.SHIP.getValue())
		{
			brass_board.shipyardBuilt(city_id, player_id, brass_players, brass_track);
		}
	}
	
	public boolean canBuildLink(int link_id, int player_id)
	{
		//has the link already been constructed?
		if (brass_board.isLinkConstructed(link_id)) 
		{
			return false;
		}
		
		int link_cost = BrassLinkCostEnum.CANAL.getValue();
		if (brass_phase) link_cost = BrassLinkCostEnum.RAIL.getValue();

		int coal_city_id = brass_board.canMoveCoalRailLink(brass_phase, link_id, player_id);
		if (coal_city_id == 20)
		{
			link_cost += brass_board.getCostToBuyFromCoalDemandTrack();
		}
		int player_money = brass_players.getMoney(player_id);
		boolean can_pay_for_link = (player_money >= link_cost);
		//check for connection to player network
		boolean is_link_connected_to_player_network = brass_board.isLinkConnectedToPlayerNetwork(link_id, player_id);
		
		return (coal_city_id >= 0) && can_pay_for_link && is_link_connected_to_player_network;
	}
	
	public void buildLink(int link_id, int player_id)
	{
		int coal_city_id = brass_board.canMoveCoalRailLink(brass_phase, link_id, player_id);
		int link_cost;
		if (!brass_phase)
		{
			link_cost = BrassLinkCostEnum.CANAL.getValue();
		}
		else
		{
			link_cost = BrassLinkCostEnum.RAIL.getValue();
		}
		brass_players.payForLink(link_cost, player_id);
		brass_board.buildLink(coal_city_id, link_id, player_id, brass_players, brass_track);
	}
	
	public boolean canBuildExpensiveLink(int link_id, int player_id)
	{
		//has the link already been constructed?
		if (brass_board.isLinkConstructed(link_id)) 
		{
			return false;
		}
		
		int link_cost = BrassLinkCostEnum.DOUBLE_RAIL.getValue();

		int coal_city_id = brass_board.canMoveCoalRailLink(brass_phase, link_id, player_id);
		if (coal_city_id == 20)
		{
			link_cost += brass_board.getCostToBuyFromCoalDemandTrack();
		}
		int player_money = brass_players.getMoney(player_id);
		boolean can_pay_for_link = (player_money >= link_cost);
		//check for connection to player network
		boolean is_link_connected_to_player_network = brass_board.isLinkConnectedToPlayerNetwork(link_id, player_id);
		
		return (coal_city_id >= 0) && can_pay_for_link && is_link_connected_to_player_network;
	}
	
	public void buildExpensiveLink(int link_id, int player_id)
	{
		int coal_city_id = brass_board.canMoveCoalRailLink(brass_phase, link_id, player_id);
		brass_players.payForLink(BrassLinkCostEnum.DOUBLE_RAIL.getValue(), player_id);
		brass_board.buildLink(coal_city_id, link_id, player_id, brass_players, brass_track);
	}
	
	public void cancelDoubleCardSelection()
	{
		brass_players.cancelDoubleCardSelection();
	}
	
	public boolean isGameOver()
	{
		boolean phase_over = brass_players.isPhaseOver();
		if (!phase_over) return false;
		
		if (!brass_phase)
		{
			brass_phase = true;
			awardVictoryPoints();

			brass_board.changePhase();
			brass_players.changePhase();
			brass_deck.dealStartRailPhase();
			brass_players.dealStartingHand(brass_deck);
			return false;		
		}
		else
		{
			//awardVictoryPoints();  //keeps awarding victory points, move to change state
			return true;
		}
	}
	
	public void awardVictoryPoints()
	{
		for (int i = 1; i <= 4; i++)
		{
			int victory_points = brass_board.getVictoryPoints(i);
			brass_players.awardVictoryPoints(victory_points, i, brass_track);
		}
	}
	
	public boolean canTakeLoan(int player_id)
	{
		int income_level = brass_players.getIncome(player_id);
		boolean can_take_loan = (income_level > -10) && (!brass_phase || !brass_deck.isDeckExhausted());
		return can_take_loan;
	}
	
	public boolean getBrassPhase()
	{
		return brass_phase;
	}
	
	public boolean isFirstTurn()
   {
	   return brass_players.isFirstTurn();
   }
	
	public void techUpgrade(int token_id, int player_id)
	{
		int iron_city_id = brass_board.canMoveIron(player_id);
		if (iron_city_id == 0) iron_city_id = 20;  //can always use the demand track
		brass_board.moveIron(iron_city_id, player_id, brass_players, brass_track);
		brass_players.discardToken(token_id);
	}
	
	public boolean canTechUpgrade(int player_id)
	{
		//prefers the player's iron (a -1 indicates there is no iron on the board)
		int iron_city_id = brass_board.canMoveIron(player_id);
		//will never be -1 as can always buy from demand track
		if (iron_city_id > 0) return true;
		
		int iron_cost = brass_board.getCostToBuyFromIronDemandTrack();
		int player_money = brass_players.getMoney(player_id);
		return (player_money >= iron_cost);
	}
	
	public void sellCotton(int player_id)
	{
		brass_board.sellCotton(player_id, brass_players, brass_track);
	}

	public boolean canSellCotton(int player_id)
	{
		return brass_board.canSellCotton(player_id);
	}
	
	public int getSelectedToken(int x, int y)
	{
		return brass_players.getSelectedToken(x, y);
	}
	
	public int getSelectedCity(int x, int y)
	{
		return brass_board.getSelectedCity(x, y);
	}
	
	public int getSelectedLink(int x, int y)
	{
		return brass_board.getSelectedLink(x, y);
	}
	
	public int getActivePlayerID()
	{
		return brass_players.getActivePlayerID();
	}
	
	public int getSelectedPlayer(int x, int y)
	{
		return brass_players.getSelectedPlayer(x, y);
	}

	public void displayPlayer(int display_player_id)
	{
		brass_players.displayPlayer(display_player_id);
	}
	
	public void cancelCardSelection()
	{
		brass_players.cancelCardSelection();
	}
	
	public void playerActionCompleted()
	{
		brass_players.playerActionCompleted(brass_deck);
	}
	
	public void selectCard(int card_num)
	{
		brass_players.selectCard(card_num);
	}
	
	public void loanActionSelected(int loan_amount)
	{
		brass_players.executeLoanAction(loan_amount, brass_track);
	}
	
	public int getSelectedAction(int x, int y)
	{
		return brass_board.getSelectedAction(x, y);
	}
	
	public int getCardCityTechID(int brass_card_num)
	{
		return brass_players.getCardCityTechID(brass_card_num);
	}
	
	public int getSelectedCard(int x, int y)
	{
		return brass_players.getSelectedCard(x, y);
	}
	
	//0 indicates coal is not required
	//>0 indicates that coal is required and can be taken from the city_id returned
	//-1 indicates that coal is required but cannot be shipped to the current location
	//20 indicates that coal can be obtained from the coal demand track
	public int canMoveCoal(int city_id, int industry_id, int player_id)
	{
		//determine whether coal is required from the player
		boolean is_coal_required = brass_players.isCoalRequired(industry_id, player_id);
		if (!is_coal_required) return 0;
		
		boolean is_city_connected_to_constructed_port = brass_board.isCityConnectedToConstructedPort(city_id);

		//if so, call:
		int coal_city_id = brass_board.canMoveCoal(city_id, player_id);

		if (coal_city_id > 0)
		{
			return coal_city_id;
		}
		
		else if (is_city_connected_to_constructed_port)
		{
			 return 20;
		}
		
		return -1;
	}
	
	//0 indicates iron is not required
	//>0 indicates that iron is required and can be taken from the city_id returned
	//20 indicates that iron can be obtained from the iron demand track
	public int canMoveIron(int industry_id, int player_id)
	{
		//determine whether iron is required from the player
		boolean is_iron_required = brass_players.isIronRequired(industry_id, player_id);
		if (!is_iron_required) return 0;
		
		//if so, call:
		int iron_city_id = brass_board.canMoveIron(player_id);
		if (iron_city_id > 0)
		{
			return iron_city_id;
		}

		return 20; 
	}
   
   public BrassGame(int num_players)
   {
	   this.num_players = num_players;
	   num_computer_players = 4 - num_players;
	   brass_computer_players = new BrassComputerPlayer[num_computer_players];
	   for (int i = 1; i <= num_computer_players; i++)
	   {
		   brass_computer_players[i - 1] = new BrassSellCottonComputerPlayer(this);
	   }

	   BrassXML brass_xml = new BrassXML("resources/brass_pixels.xml");
	   
	   select_card_state = new BrassSelectCardState(this);
	   select_action_state = new BrassSelectActionState(this);
	   link_action_state = new BrassLinkActionState(this);
	   build_action_state = new BrassBuildActionState(this);
	   tech_upgrade_action_state = new BrassTechUpgradeActionState(this);
	   double_card_action_state = new BrassDoubleCardActionState(this);
	   double_rail_action_state = new BrassDoubleRailActionState(this);
	   replace_action_state = new BrassReplaceActionState(this);
	   
	   game_over_state = new BrassGameOverState(this);
	   current_state = select_card_state;
	   
	   brass_phase = false;  //canal_phase
	   
		gui.ImageLoader il = gui.ImageLoader.getImageLoader();
		
		brass_deck = new BrassDeck(il, brass_xml);
		brass_deck.dealStartCanalPhase();
		
		brass_track = new BrassTrack(brass_xml);
		
		brass_players = new BrassPlayers(brass_deck, brass_xml, brass_track);
		brass_board = new BrassBoard(brass_xml);
		
		//create the gui last
		PixelDimension gui_dimension = brass_xml.getPixelDimension("gui");
		brass_gui = new BasicGUI(gui_dimension.getWidth(), gui_dimension.getHeight(), "Brass", "images/brass_icon.png", this);
		brass_computer_players[3] = new BrassAICoalStrategy(this, brass_board);
   }
   
   public void draw(Graphics g, int width, int height)
   {
		brass_board.draw(g);
		brass_players.draw(g);
		brass_deck.draw(g);
   }
	
	public BrassState getSelectCardState()
	{
		return select_card_state;
	}
	
	public BrassState getSelectActionState()
	{
		return select_action_state;
	}
	
	public BrassState getBuildActionState()
	{
		return build_action_state;
	}
	
	public BrassState getLinkActionState()
	{
		return link_action_state;
	}
	
	public BrassState getTechUpgradeActionState()
	{
		return tech_upgrade_action_state;
	}
	
	public BrassState getDoubleCardActionState()
	{
		return double_card_action_state;
	}
	
	public BrassState getDoubleRailActionState()
	{
		return double_rail_action_state;
	}
	
	public BrassState getReplaceActionState()
	{
		return replace_action_state;
	}
	
	public BrassState getGameOverState()
	{
		return game_over_state;
	}
	
	public void changeState(BrassState brass_state)
	{
		current_state = brass_state;
		if (isGameOver())
		{
			awardVictoryPoints();
			current_state = game_over_state;
		}
	}
   
   public void keyPressed(char key) 
	{
		System.out.println("The " + key + " key was pressed.");
	}
	
	public int[] getScreenDimensions()
	{
		return brass_gui.getScreenDimensions();
	}
	
	public int getGUIWidth()
	{
		return brass_gui.getWidth();
	}
	
	public int getGUIHeight()
	{
		return brass_gui.getHeight();
	}
	
	public int getTopInset()
	{
		java.awt.Insets insets = brass_gui.getInsets();
		int top = insets.top;
		return top;
	}
	
	public int getLeftInset()
	{
		java.awt.Insets insets = brass_gui.getInsets();
		int left = insets.left;
		return left;
	}
}
