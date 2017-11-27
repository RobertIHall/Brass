package brass;

import java.util.List;
import java.util.ArrayList;

import java.awt.Graphics;
import java.awt.Image;

import gui.ImageLoader;
import gui.PixelDimension;
import gui.PixelPoint;

import util.FilePermutation;
import util.Permutation;

class BrassDeck
{
	private List<BrassCard> brass_deck;  		//keep the original deck intact as there are two phases
	private List<BrassCard> shuffled_deck;
	
	private BrassCard brass_card_back;
	
	public void draw(Graphics g)
	{
		if (shuffled_deck != null && shuffled_deck.size() > 0)
		{
			brass_card_back.draw(g);
		}
	}
	
	public boolean isDeckExhausted()
	{
		return (shuffled_deck.size() == 0);
	}
	
	public BrassCard deal()
	{
		BrassCard brass_card = null;
		if (shuffled_deck.size() > 0)
		{
			brass_card = shuffled_deck.remove(0);
		}
		return brass_card;
	}

	public void dealStartCanalPhase()
	{
		//discard 6 cards at the beginning of the canal phase
		shuffled_deck = shuffle();
		discard(6);
	}
	
	public void dealStartRailPhase()
	{
		// discard 2 at the beginning of the rail phase
		shuffled_deck = shuffle();
		discard(2);
	}
	
	private void discard(int num_to_discard)
	{
		for (int i = 1; i <= num_to_discard; i++)
		{
			BrassCard card = shuffled_deck.remove(0);
		}
	}
	
	private List<BrassCard> shuffle()
	{
		//the below line will eventually be changed to obtain new random numbers for every game
		util.Permutation p = util.PermutationFactory.getPermutation("resources/brass_deck_shuffle.txt", 66, 66);
		List<BrassCard> shuffle = new ArrayList<BrassCard>();
		while(p.hasNext())
		{
			int index = p.next();
			BrassCard bc = brass_deck.get(index -1);
			shuffle.add(bc);
		}
		
		return shuffle;
	}
	
	public BrassDeck(ImageLoader il, BrassXML brass_xml)
	{
		brass_deck = new ArrayList<BrassCard>();
		
		PixelDimension big_dim = brass_xml.getPixelDimension("big_cards");
		PixelDimension small_dim = brass_xml.getPixelDimension("small_cards");
		
		int big_width = big_dim.getWidth();  
		int big_height = big_dim.getHeight();
		int small_width = small_dim.getWidth();
		int small_height = small_dim.getHeight();
		
		brass_card_back = new BrassCard(0, 0, "Back", null, big_dim.getWidth(), big_dim.getHeight(), il.getImage("images/deck/small/card_back_small.jpg"), small_dim.getWidth(), small_dim.getHeight());
		PixelPoint small_loc_0 = brass_xml.getPixelCenter("small_cards");
		brass_card_back.showSmallCard(small_loc_0.getX(), small_loc_0.getY());
		
		Image barrow_big = il.getImage("images/deck/big/barrow_city_card_big.jpg");
		Image barrow_small = il.getImage("images/deck/small/barrow_city_card_small.jpg");
		//2 barrow cards
		BrassCard barrow = new BrassCard(1, 1, "Barrow", barrow_big, big_width, big_height, barrow_small, small_width, small_height);
		brass_deck.add(barrow);
		barrow = new BrassCard(2, 1, "Barrow", barrow_big, big_width, big_height, barrow_small, small_width, small_height);
		brass_deck.add(barrow);
		
		Image birkenhead_big = il.getImage("images/deck/big/birkenhead_city_card_big.jpg");
		Image birkenhead_small = il.getImage("images/deck/small/birkenhead_city_card_small.jpg");
		//2 birkenhead cards
		BrassCard birkenhead = new BrassCard(3, 2, "Birkenhead", birkenhead_big, big_width, big_height, birkenhead_small, small_width, small_height);
		brass_deck.add(birkenhead);
		birkenhead = new BrassCard(4, 2, "Birkenhead", birkenhead_big, big_width, big_height, birkenhead_small, small_width, small_height);
		brass_deck.add(birkenhead);
		
		Image blackburn_big = il.getImage("images/deck/big/blackburn_city_card_big.jpg");
		Image blackburn_small = il.getImage("images/deck/small/blackburn_city_card_small.jpg");
		//2 blackburn cards
		BrassCard blackburn = new BrassCard(5, 3, "Blackburn", blackburn_big, big_width, big_height, blackburn_small, small_width, small_height);
		brass_deck.add(blackburn);
		blackburn = new BrassCard(6, 3, "Blackburn", blackburn_big, big_width, big_height, blackburn_small, small_width, small_height);
		brass_deck.add(blackburn);
		
		Image bolton_big = il.getImage("images/deck/big/bolton_city_card_big.jpg");
		Image bolton_small = il.getImage("images/deck/small/bolton_city_card_small.jpg");
		//2 bolton cards
		BrassCard bolton = new BrassCard(7, 4, "Bolton", bolton_big, big_width, big_height, bolton_small, small_width, small_height);
		brass_deck.add(bolton);
		bolton = new BrassCard(8, 4, "Bolton", bolton_big, big_width, big_height, bolton_small, small_width, small_height);
		brass_deck.add(bolton);
		
		Image burnley_big = il.getImage("images/deck/big/burnley_city_card_big.jpg");
		Image burnley_small = il.getImage("images/deck/small/burnley_city_card_small.jpg");
		//2 burnley cards
		BrassCard burnley = new BrassCard(9, 5, "Burnley", burnley_big, big_width, big_height, burnley_small, small_width, small_height);
		brass_deck.add(burnley);
		burnley = new BrassCard(10, 5, "Burnley", burnley_big, big_width, big_height, burnley_small, small_width, small_height);
		brass_deck.add(burnley);
		
		//1 bury card
		BrassCard bury = new BrassCard(11, 6, "Bury", il.getImage("images/deck/big/bury_city_card_big.jpg"), big_width, big_height, il.getImage("images/deck/small/bury_city_card_small.jpg"), small_width, small_height);
		brass_deck.add(bury);
		
		Image colne_big = il.getImage("images/deck/big/colne_city_card_big.jpg");
		Image colne_small = il.getImage("images/deck/small/colne_city_card_small.jpg");
		//2 colne cards
		BrassCard colne = new BrassCard(12, 7, "Colne", colne_big, big_width, big_height, colne_small, small_width, small_height);
		brass_deck.add(colne);
		colne = new BrassCard(13, 7, "Colne", colne_big, big_width, big_height, colne_small, small_width, small_height);
		brass_deck.add(colne);
		
		//1 ellesmere card
		BrassCard ellesmere = new BrassCard(14, 8, "Ellesmere", il.getImage("images/deck/big/ellesmere_city_card_big.jpg"), big_width, big_height, il.getImage("images/deck/small/ellesmere_city_card_small.jpg"), small_width, small_height);
		brass_deck.add(ellesmere);
		
		//1 fleetwood card
		BrassCard fleetwood = new BrassCard(15, 9, "Fleetwood", il.getImage("images/deck/big/fleetwood_city_card_big.jpg"), big_width, big_height, il.getImage("images/deck/small/fleetwood_city_card_small.jpg"), small_width, small_height);
		brass_deck.add(fleetwood);
		
		Image lancaster_big = il.getImage("images/deck/big/lancaster_city_card_big.jpg");
		Image lancaster_small = il.getImage("images/deck/small/lancaster_city_card_small.jpg");
		//3 lancaster cards
		BrassCard lancaster = new BrassCard(16, 10, "Lancaster", lancaster_big, big_width, big_height, lancaster_small, small_width, small_height);
		brass_deck.add(lancaster);
		lancaster = new BrassCard(17, 10, "Lancaster", lancaster_big, big_width, big_height, lancaster_small, small_width, small_height);
		brass_deck.add(lancaster);
		lancaster = new BrassCard(18, 10, "Lancaster", lancaster_big, big_width, big_height, lancaster_small, small_width, small_height);
		brass_deck.add(lancaster);
		
		Image liverpool_big = il.getImage("images/deck/big/liverpool_city_card_big.jpg");
		Image liverpool_small = il.getImage("images/deck/small/liverpool_city_card_small.jpg");
		//4 liverpool cards
		BrassCard liverpool = new BrassCard(19, 11, "Liverpool", liverpool_big, big_width, big_height, liverpool_small, small_width, small_height);
		brass_deck.add(liverpool);
		liverpool = new BrassCard(20, 11, "Liverpool", liverpool_big, big_width, big_height, liverpool_small, small_width, small_height);
		brass_deck.add(liverpool);
		liverpool = new BrassCard(21, 11, "Liverpool", liverpool_big, big_width, big_height, liverpool_small, small_width, small_height);
		brass_deck.add(liverpool);
		liverpool = new BrassCard(22, 11, "Liverpool", liverpool_big, big_width, big_height, liverpool_small, small_width, small_height);
		brass_deck.add(liverpool);
		
		Image macclesfield_big = il.getImage("images/deck/big/macclesfield_city_card_big.jpg");
		Image macclesfield_small = il.getImage("images/deck/small/macclesfield_city_card_small.jpg");
		//2 macclesfield cards
		BrassCard macclesfield = new BrassCard(23, 12, "Macclesfield", macclesfield_big, big_width, big_height, macclesfield_small, small_width, small_height);
		brass_deck.add(macclesfield);
		macclesfield = new BrassCard(24, 12, "Macclesfield", macclesfield_big, big_width, big_height, macclesfield_small, small_width, small_height);
		brass_deck.add(macclesfield);
		
		Image manchester_big = il.getImage("images/deck/big/manchester_city_card_big.jpg");
		Image manchester_small = il.getImage("images/deck/small/manchester_city_card_small.jpg");
		//4 manchester cards
		BrassCard manchester = new BrassCard(25, 13, "Manchester", manchester_big, big_width, big_height, manchester_small, small_width, small_height);
		brass_deck.add(manchester);
		manchester = new BrassCard(26, 13, "Manchester", manchester_big, big_width, big_height, manchester_small, small_width, small_height);
		brass_deck.add(manchester);
		manchester = new BrassCard(27, 13, "Manchester", manchester_big, big_width, big_height, manchester_small, small_width, small_height);
		brass_deck.add(manchester);
		manchester = new BrassCard(28, 13, "Manchester", manchester_big, big_width, big_height, manchester_small, small_width, small_height);
		brass_deck.add(manchester);
		
		Image oldham_big = il.getImage("images/deck/big/oldham_city_card_big.jpg");
		Image oldham_small = il.getImage("images/deck/small/oldham_city_card_small.jpg");
		//2 oldham	cards
		BrassCard oldham = new BrassCard(29, 14, "Oldham", oldham_big, big_width, big_height, oldham_small, small_width, small_height);
		brass_deck.add(oldham);
		oldham = new BrassCard(30, 14, "Oldham", oldham_big, big_width, big_height, oldham_small, small_width, small_height);
		brass_deck.add(oldham);
		
		Image preston_big = il.getImage("images/deck/big/preston_city_card_big.jpg");
		Image preston_small = il.getImage("images/deck/small/preston_city_card_small.jpg");
		//3 preston cards
		BrassCard preston = new BrassCard(31, 15, "Preston", preston_big, big_width, big_height, preston_small, small_width, small_height);
		brass_deck.add(preston);
		preston = new BrassCard(32, 15, "Preston", preston_big, big_width, big_height, preston_small, small_width, small_height);
		brass_deck.add(preston);
		preston = new BrassCard(33, 15, "Preston", preston_big, big_width, big_height, preston_small, small_width, small_height);
		brass_deck.add(preston);
		
		Image rochdale_big = il.getImage("images/deck/big/rochdale_city_card_big.jpg");
		Image rochdale_small = il.getImage("images/deck/small/rochdale_city_card_small.jpg");
		//2 rochdale cards
		BrassCard rochdale = new BrassCard(34, 16, "Rochdale", rochdale_big, big_width, big_height, rochdale_small, small_width, small_height);
		brass_deck.add(rochdale);
		rochdale = new BrassCard(35, 16, "Rochdale", rochdale_big, big_width, big_height, rochdale_small, small_width, small_height);
		brass_deck.add(rochdale);
		
		Image stockport_big = il.getImage("images/deck/big/stockport_city_card_big.jpg");
		Image stockport_small = il.getImage("images/deck/small/stockport_city_card_small.jpg");
		//2 stockport cards
		BrassCard stockport = new BrassCard(36, 17, "Stockport", stockport_big, big_width, big_height, stockport_small, small_width, small_height);
		brass_deck.add(stockport);
		stockport = new BrassCard(37, 17, "Stockport", stockport_big, big_width, big_height, stockport_small, small_width, small_height);
		brass_deck.add(stockport);
		
		Image warrington_big = il.getImage("images/deck/big/warrington_city_card_big.jpg");
		Image warrington_small = il.getImage("images/deck/small/warrington_city_card_small.jpg");
		//2 warrington cards
		BrassCard warrington = new BrassCard(38, 18, "Warrington", warrington_big, big_width, big_height, warrington_small, small_width, small_height);
		brass_deck.add(warrington);
		warrington = new BrassCard(39, 18, "Warrington", warrington_big, big_width, big_height, warrington_small, small_width, small_height);
		brass_deck.add(warrington);
		
		Image wigan_big = il.getImage("images/deck/big/wigan_city_card_big.jpg");
		Image wigan_small = il.getImage("images/deck/small/wigan_city_card_small.jpg");
		//2 wigan cards
		BrassCard wigan = new BrassCard(40, 19, "Wigan", wigan_big, big_width, big_height, wigan_small, small_width, small_height);
		brass_deck.add(wigan);
		wigan = new BrassCard(41, 19, "Wigan", wigan_big, big_width, big_height, wigan_small, small_width, small_height);
		brass_deck.add(wigan);
		
		Image coal_big = il.getImage("images/deck/big/coal_industry_card_big.jpg");
		Image coal_small = il.getImage("images/deck/small/coal_industry_card_small.jpg");
		//5 coal cards
		BrassCard coal = new BrassCard(42, 20, "Coal", coal_big, big_width, big_height, coal_small, small_width, small_height);
		brass_deck.add(coal);
		coal = new BrassCard(43, 20, "Coal", coal_big, big_width, big_height, coal_small, small_width, small_height);
		brass_deck.add(coal);
		coal = new BrassCard(44, 20, "Coal", coal_big, big_width, big_height, coal_small, small_width, small_height);
		brass_deck.add(coal);
		coal = new BrassCard(45, 20, "Coal", coal_big, big_width, big_height, coal_small, small_width, small_height);
		brass_deck.add(coal);
		coal = new BrassCard(46, 20, "Coal", coal_big, big_width, big_height, coal_small, small_width, small_height);
		brass_deck.add(coal);
		
		Image cotton_big = il.getImage("images/deck/big/cotton_industry_card_big.jpg");
		Image cotton_small = il.getImage("images/deck/small/cotton_industry_card_small.jpg");
		BrassCard cotton = new BrassCard(47, 21, "Cotton", cotton_big, big_width, big_height, cotton_small, small_width, small_height);
		//8 cotton cards
		brass_deck.add(cotton);
		cotton = new BrassCard(48, 21, "Cotton", cotton_big, big_width, big_height, cotton_small, small_width, small_height);
		brass_deck.add(cotton);
		cotton = new BrassCard(49, 21, "Cotton", cotton_big, big_width, big_height, cotton_small, small_width, small_height);
		brass_deck.add(cotton);
		cotton = new BrassCard(50, 21, "Cotton", cotton_big, big_width, big_height, cotton_small, small_width, small_height);
		brass_deck.add(cotton);
		cotton = new BrassCard(51, 21, "Cotton", cotton_big, big_width, big_height, cotton_small, small_width, small_height);
		brass_deck.add(cotton);
		cotton = new BrassCard(52, 21, "Cotton", cotton_big, big_width, big_height, cotton_small, small_width, small_height);
		brass_deck.add(cotton);
		cotton = new BrassCard(53, 21, "Cotton", cotton_big, big_width, big_height, cotton_small, small_width, small_height);
		brass_deck.add(cotton);
		cotton = new BrassCard(54, 21, "Cotton", cotton_big, big_width, big_height, cotton_small, small_width, small_height);
		brass_deck.add(cotton);
		
		Image iron_big = il.getImage("images/deck/big/iron_industry_card_big.jpg");
		Image iron_small = il.getImage("images/deck/small/iron_industry_card_small.jpg");
		//3 iron cards
		BrassCard iron = new BrassCard(55, 22, "Iron", iron_big, big_width, big_height, iron_small, small_width, small_height);
		brass_deck.add(iron);
		iron = new BrassCard(56, 22, "Iron", iron_big, big_width, big_height, iron_small, small_width, small_height);
		brass_deck.add(iron);
		iron = new BrassCard(57, 22, "Iron", iron_big, big_width, big_height, iron_small, small_width, small_height);
		brass_deck.add(iron);
		
		Image port_big = il.getImage("images/deck/big/port_industry_card_big.jpg");
		Image port_small = il.getImage("images/deck/small/port_industry_card_small.jpg");
		//6 port cards
		BrassCard port = new BrassCard(58, 23, "Port", port_big, big_width, big_height, port_small, small_width, small_height);
		brass_deck.add(port);
		port = new BrassCard(59, 23, "Port", port_big, big_width, big_height, port_small, small_width, small_height);
		brass_deck.add(port);
		port = new BrassCard(60, 23, "Port", port_big, big_width, big_height, port_small, small_width, small_height);
		brass_deck.add(port);
		port = new BrassCard(61, 23, "Port", port_big, big_width, big_height, port_small, small_width, small_height);
		brass_deck.add(port);
		port = new BrassCard(62, 23, "Port", port_big, big_width, big_height, port_small, small_width, small_height);
		brass_deck.add(port);
		port = new BrassCard(63, 23, "Port", port_big, big_width, big_height, port_small, small_width, small_height);
		brass_deck.add(port);
		
		Image shipyard_big = il.getImage("images/deck/big/shipyard_industry_card_big.jpg");
		Image shipyard_small = il.getImage("images/deck/small/shipyard_industry_card_small.jpg");
		//3 shipyard cards
		BrassCard shipyard = new BrassCard(64, 24, "Shipyard", shipyard_big, big_width, big_height, shipyard_small, small_width, small_height);
		brass_deck.add(shipyard);
		shipyard = new BrassCard(65, 24, "Shipyard", shipyard_big, big_width, big_height, shipyard_small, small_width, small_height);
		brass_deck.add(shipyard);
		shipyard = new BrassCard(66, 24, "Shipyard", shipyard_big, big_width, big_height, shipyard_small, small_width, small_height);
		brass_deck.add(shipyard);
	}
	
}
