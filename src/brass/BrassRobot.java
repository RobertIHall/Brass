package brass;

import java.awt.Robot;
import java.awt.event.InputEvent;

public class BrassRobot
{
	public static void main(String[] args)  throws java.awt.AWTException
	{
	   int num_players;
	   
	   if (args.length == 0)
	   {
		   num_players = 4;
	   }
	   else
	   {
		   try
		   {
				num_players = Integer.parseInt(args[0]);
		   }
		   catch (NumberFormatException nfe)
		   {
				num_players = 4;
		   }
	   }
	   
		Robot robot = null;
		
		robot = new Robot();
		robot.setAutoDelay(20);
		BrassRobot brass_robot = new BrassRobot(robot, num_players);
		brass_robot.runRobotGame();
	}
   
	private Robot robot;
	private BrassGame brass_game;
	private int x_left;
	private int y_top;
	private int left_button;

	public BrassRobot(Robot r, int num_players)
	{
		robot = r;
		brass_game = new BrassGame(num_players);
		
		//a bunch of fiddling in order to get the mouse click positions relative to the board
		//and not the entire screen
		int[] screen_dimensions = brass_game.getScreenDimensions();
		int[] gui_dimensions = new int[2];
		gui_dimensions[0] = brass_game.getGUIWidth();
		gui_dimensions[1] = brass_game.getGUIHeight();
		
		int x_left_screen = screen_dimensions[0]/2 - gui_dimensions[0]/2;
		int y_top_screen = screen_dimensions[1]/2 - gui_dimensions[1]/2;
		
		int left_button_id = InputEvent.BUTTON1_MASK;
		
		//adjust the top left starting point to include the gui window and title bar
		int x_left_screen_adjustment = brass_game.getLeftInset();
		int y_top_screen_adjustment = brass_game.getTopInset();
		x_left_screen = x_left_screen + x_left_screen_adjustment;
		y_top_screen = y_top_screen + y_top_screen_adjustment;
		
		x_left = x_left_screen;
		y_top = y_top_screen;
		left_button = left_button_id;
	}
   
	public void runRobotGame()
	{	
		//canal phase
		
		//yellow
		clickCard1();
		clickBuildAction();
		clickStockport();
		
		//red
		clickCard6();
		clickTechUpgradeAction();
		clickCoalToken();
		clickIronToken();
		
		//green
		clickCard2();
		clickBuildAction();
		clickCottonMillToken();
		
		//purple
		clickCard2();
		clickBuildAction();
		clickCoalToken();
		
		//red
		clickCard8();
		clickBuildAction();
		clickPortToken();
		
		clickCard2();
		clickLinkAction();
		clickLancasterPrestonCanal();
		
		//purple
		clickCard8();
		clickLinkAction();
		clickBoltonManchesterCanal();
		
		clickCard2();
		clickBuildAction();
		clickIronToken();
		
		//yellow
		clickCard4();
		clickLinkAction();
		clickMacclesfieldStockportCanal();
		
		clickCard8();
		clickLinkAction();
		clickMacclesfieldMidlandsCanal();
		
		//green
		clickCard2();
		clickLinkAction();
		clickColneYorkshireCanal();
		
		clickCard6();
		clickTechUpgradeAction();
		clickCottonMillToken();
		clickCoalToken();
		
		//green
		clickCard1();
		clickLoan30Action();
		
		clickCard2();
		clickSellCottonAction();
		
		//yellow
		clickCard1();
		clickBuildAction();
		clickCottonMillToken();
		
		clickCard7();
		clickSellCottonAction();
			
		//purple
		clickCard3();
		clickLinkAction();
		clickManchesterWarringtonCanal();
		
		clickCard2();
		clickBuildAction();
		clickWarrington();
		
		//red
		clickCard5();
		clickBuildAction();
		clickIronToken();
		
		clickCard6();
		clickTechUpgradeAction();
		clickCottonMillToken();
		clickCottonMillToken();
		
		//green
		clickCard6();
		clickLinkAction();
		clickBurnleyColneCanal();
		
		clickCard2();
		clickLinkAction();
		clickBlackburnBurnleyCanal();
		
		//red
		clickCard8();
		clickLoan30Action();
		
		clickCard4();
		clickTechUpgradeAction();
		clickCottonMillToken();
		clickPortToken();
		
		//purple
		clickCard1();
		clickLinkAction();
		clickManchesterOldhamCanal();
		
		clickCard6();
		clickLinkAction();
		clickOldhamRochdaleCanal();
	
		//yellow
		clickCard3();
		clickLoan30Action();
		
		clickCard7();
		clickBuildAction();
		clickCottonMillToken();
		
		//red
		clickCard6();
		clickBuildAction();
		clickIronToken();
		
		clickCard1();
		clickBuildAction();
		clickCoalToken();
		
		//green
		clickCard1();
		clickBuildAction();
		clickCottonMillToken();
		
		clickCard5();
		clickBuildAction();
		clickCottonMillToken();
		
		//purple
		clickCard4();
		clickTechUpgradeAction();
		clickCottonMillToken();
		clickCottonMillToken();
		
		clickCard5();
		clickLinkAction();
		clickWarringtonWiganCanal();
		
		//yellow
		clickCard1();
		clickBuildAction();
		clickCottonMillToken();
		
		clickCard7();
		clickSellCottonAction();
		
		//purple
		clickCard3();
		clickLinkAction();
		clickEllesmereWarringtonCanal();
		
		clickCard5();
		clickBuildAction();
		clickEllesmere();
		
		//yellow
		clickCard4();
		clickTechUpgradeAction();
		clickCoalToken();
		clickPortToken();
		
		clickCard1();
		clickBuildAction();
		clickIronToken();
		
		//red
		clickCard1();
		clickBuildAction();
		clickCottonMillToken();
		
		clickCard6();
		clickSellCottonAction();
		
		//green
		clickCard4();
		clickLinkAction();
		clickBlackburnWiganCanal();
		
		clickCard5();
		clickLinkAction();
		clickLiverpoolWiganCanal();
		
		//yellow
		clickCard1();
		clickTechUpgradeAction();
		clickPortToken();
		clickCottonMillToken();
		
		clickCard4();
		clickBuildAction();
		clickCoalToken();
		
		//green
		clickCard1();
		clickBuildAction();
		clickPortToken();
		
		clickCard2();
		clickSellCottonAction();
		
		//purple
		clickCard4();
		clickBuildAction();
		clickPortToken();
		
		clickCard2();
		clickLinkAction();
		clickRochdaleYorkshireCanal();
		
		//red
		clickCard3();
		clickLinkAction();
		clickBuryManchesterCanal();
		
		clickCard1();
		clickBuildAction();
		clickBury();
		
		//green
		clickCard2();
		clickBuildAction();
		clickWigan();
		
		clickCard1();
		clickSellCottonAction();
		
		//yellow
		clickCard2();
		clickTechUpgradeAction();
		clickCottonMillToken();
		clickCottonMillToken();
		
		clickCard1();
		clickBuildAction();
		clickCottonMillToken();
		
		//purple
		clickCard1();
		clickBuildAction();
		clickCoalToken();
		
		clickCard2();
		clickLoan20Action();
		
		//red
		clickCard2();
		clickLoan10Action();
		
		clickCard1();
		clickLinkAction();
		clickManchesterStockportCanal();
		
		//rail phase
		
		//red
		clickCard4();
		clickBuildAction();
		clickPortToken();
		
		clickCard6();
		clickLinkAction();
		clickManchesterWarringtonRail();
		
		//green
		clickCard3();
		clickTechUpgradeAction();
		clickIronToken();
		clickPortToken();
	
		clickCard1();
		clickLinkAction();
		clickBoltonWiganRail();
		
		//purple
		clickCard6();
		clickLinkAction();
		clickManchesterOldhamRail();
		
		clickCard5();
		clickBuildAction();
		clickManchester();
		
		//yellow
		clickCard6();
		clickLinkAction();
		clickColneYorkshireRail();
		
		clickCard1();
		clickSellCottonAction();
		
		//yellow
		clickCard2();
		clickBuildAction();
		clickCottonMillToken();
		
		clickCard4();
		clickLinkAction();
		clickRochdaleYorkshireRail();
		
		//green
		clickCard2();
		clickLinkAction();
		clickBlackburnWiganRail();
		
		clickCard1();
		clickBuildAction();
		clickCoalToken();
		
		//purple
		clickCard1();
		clickBuildAction();
		clickPortToken();
		
		clickCard3();
		clickLinkAction();
		clickPrestonWiganRail();
		
		//red
		clickCard8();
		clickLinkAction();
		clickBuryManchesterRail();
		
		clickCard3();
		clickTechUpgradeAction();
		clickPortToken();
		clickPortToken();
		
		//red
		clickCard2();
		clickBuildAction();
		clickManchester();
		
		clickCard5();
		clickSellCottonAction();
		
		//green
		clickCard6();
		clickBuildAction();
		clickBolton();
		
		clickCard1();
		clickBuildAction();
		clickBolton();
		
		//purple
		clickCard6();
		clickBuildAction();
		clickPortToken();
		
		clickCard2();
		clickLinkAction();
		clickLancasterPrestonRail();
		
		//yellow
		clickCard3();
		clickBuildAction();
		clickCoalToken();
		
		clickCard6();
		clickLinkAction();
		clickManchesterStockportRail();
		
		//yellow
		clickCard6();
		clickBuildAction();
		clickStockport();
		
		clickCard3();
		clickSellCottonAction();
		
		//purple
		clickCard7();
		clickBuildAction();
		clickIronToken();
		
		clickCard2();
		clickLinkAction();
		clickBarrowLancasterRail();
		
		//red
		clickCard7();
		clickBuildAction();
		clickCoalToken();
		
		clickCard5();
		clickBuildAction();
		clickPreston();
		
		//green
		clickCard7();
		clickBuildAction();
		clickPortToken();
		
		clickCard2();
		clickSellCottonAction();
		
		//green
		clickCard5();
		clickBuildAction();
		clickIronToken();
		
		clickCard3();
		clickBuildAction();
		clickPortToken();
		
		//red
		clickCard1();
		clickBuildAction();
		clickPortToken();
		
		clickCard8();
		clickTechUpgradeAction();
		clickCottonMillToken();
		clickCottonMillToken();
		
		//purple
		clickCard8();
		clickTechUpgradeAction();
		clickShipyardToken();
		clickShipyardToken();
		
		clickCard3();
		clickTechUpgradeAction();
		clickShipyardToken();
		clickShipyardToken();
		
		//yellow
		clickCard4();
		clickLinkAction();
		clickWarringtonWiganRail();
		
		clickCard2();
		clickSellCottonAction();
	
		//purple
		clickCard5();
		clickBuildAction();
		clickCoalToken();
		
		clickCard6();
		clickLinkAction();
		clickLiverpoolWarringtonRail();
		
		//yellow
		clickCard4();
		clickBuildAction();
		clickStockport();
		
		clickCard1();
		clickSellCottonAction();
		
		//red
		clickCard2();
		clickLinkAction();
		clickFleetwoodPrestonRail();
		
		clickCard6();
		clickBuildAction();
		clickFleetwood();
		
		//green
		clickCard1();
		clickLinkAction();
		clickBlackburnBurnleyRail();
		
		clickCard4();
		clickBuildAction();
		clickBurnley();
		
		//purple
		clickCard1();
		clickBuildAction();
		clickCoalToken();
		
		clickCard4();
		clickLinkAction();
		clickEllesmereWarringtonRail();
		
		//red
		clickCard3();
		clickLinkAction();
		clickOldhamRochdaleRail();
		
		clickCard1();
		clickDiscardAction();
		
		//yellow
		clickCard2();
		clickLinkAction();
		clickBurnleyColneRail();
		
		clickCard3();
		clickBuildAction();
		clickPortToken();
		
		//green
		clickCard1();
		clickLinkAction();
		clickBlackburnPrestonRail();
		
		clickCard2();
		clickLinkAction();
		clickBoltonManchesterRail();
		
		//red
		clickCard1();
		clickBuildAction();
		clickCottonMillToken();
		
		clickCard2();
		clickSellCottonAction();
		
		//yellow
		clickCard1();
		clickLinkAction();
		clickBirkenheadEllesmereRail();
		
		clickCard2();
		clickLinkAction();
		clickLiverpoolWiganRail();
		
		//purple
		clickCard2();
		clickBuildAction();
		clickBirkenhead();  //using virtual link
		
		clickCard1();
		clickDiscardAction();
		
		//green
		clickCard1();
		clickLinkAction();
		clickBlackburnBoltonRail();
		
		clickCard2();
		clickLinkAction();
		clickBoltonBuryRail();
		
	}
	
	public void clickBarrowLancasterRail()
	{
		robot.mouseMove(200 + x_left, 59 + y_top);
		robotMouseClick();
	}
	
	public void clickBirkenheadEllesmereRail()
	{
		robot.mouseMove(185 + x_left, 483 + y_top);
		robotMouseClick();
	}
	
	public void clickBlackburnBoltonRail()
	{
		robot.mouseMove(337 + x_left, 309 + y_top);
		robotMouseClick();
	}
	
	public void clickBlackburnBurnleyRail()
	{
		robot.mouseMove(396 + x_left, 265 + y_top);
		robotMouseClick();
	}
	
	public void clickBlackburnPrestonRail()
	{
		robot.mouseMove(302 + x_left, 278 + y_top);
		robotMouseClick();
	}
	
	public void clickBlackburnWiganRail()
	{
		robot.mouseMove(298 + x_left, 319 + y_top);
		robotMouseClick();
	}
	
	public void clickBlackpoolPrestonRail()
	{
		robot.mouseMove(205 + x_left, 258 + y_top);
		robotMouseClick();
	}
	
	public void clickBoltonBuryRail()
	{
		robot.mouseMove(377 + x_left, 333 + y_top);
		robotMouseClick();
	}
	
	public void clickBoltonManchesterRail()
	{
		robot.mouseMove(366 + x_left, 380 + y_top);
		robotMouseClick();
	}
	
	public void clickBoltonWiganRail()
	{
		robot.mouseMove(300 + x_left, 362 + y_top);
		robotMouseClick();
	}
	
	public void clickBurnleyBuryRail()
	{
		robot.mouseMove(414 + x_left, 280 + y_top);
		robotMouseClick();
	}
	
	public void clickBurnleyColneRail()
	{
		robot.mouseMove(433 + x_left, 207 + y_top);
		robotMouseClick();
	}
	
	public void clickBuryManchesterRail()
	{
		robot.mouseMove(423 + x_left, 364 + y_top);
		robotMouseClick();
	}
	
	public void clickBuryRochdaleRail()
	{
		robot.mouseMove(451 + x_left, 307 + y_top);
		robotMouseClick();
	}
	
	public void clickColneYorkshireRail()
	{
		robot.mouseMove(517 + x_left, 213 + y_top);
		robotMouseClick();
	}
	
	public void clickEllesmereNorthwichRail()
	{
		robot.mouseMove(285 + x_left, 525 + y_top);
		robotMouseClick();
	}
	
	public void clickEllesmereWarringtonRail()
	{
		robot.mouseMove(284 + x_left, 486 + y_top);
		robotMouseClick();
	}
	
	public void clickFleetwoodPrestonRail()
	{
		robot.mouseMove(204 + x_left, 228 + y_top);
		robotMouseClick();
	}
	
	public void clickLancasterPrestonRail()
	{
		robot.mouseMove(247 + x_left, 172 + y_top);
		robotMouseClick();
	}
	
	public void clickLancasterScotlandRail()
	{
		robot.mouseMove(275 + x_left, 68 + y_top);
		robotMouseClick();
	}
	
	public void clickLiverpoolSouthportRail()
	{
		robot.mouseMove(180 + x_left, 336 + y_top);
		robotMouseClick();
	}
	
	public void clickLiverpoolWarringtonRail()
	{
		robot.mouseMove(244 + x_left, 411 + y_top);
		robotMouseClick();
	}
	
	public void clickLiverpoolWiganRail()
	{
		robot.mouseMove(225 + x_left, 385 + y_top);
		robotMouseClick();
	}
	
	public void clickMacclesfieldMidlandsRail()
	{
		robot.mouseMove(431 + x_left, 604 + y_top);
		robotMouseClick();
	}
	
	public void clickMacclesfieldStockportRail()
	{
		robot.mouseMove(448 + x_left, 538 + y_top);
		robotMouseClick();
	}
	
	public void clickManchesterOldhamRail()
	{
		robot.mouseMove(468 + x_left, 406 + y_top);
		robotMouseClick();
	}
	
	public void clickManchesterStockportRail()
	{
		robot.mouseMove(437 + x_left, 473 + y_top);
		robotMouseClick();
	}
	
	public void clickManchesterWarringtonRail()
	{
		robot.mouseMove(345 + x_left, 420 + y_top);
		robotMouseClick();
	}
	
	public void clickMidlandsNorthwichRail()
	{
		robot.mouseMove(358 + x_left, 586 + y_top);
		robotMouseClick();
	}
	
	public void clickOldhamRochdaleRail()
	{
		robot.mouseMove(506 + x_left, 342 + y_top);
		robotMouseClick();
	}
	
	public void clickPrestonSouthportRail()
	{
		robot.mouseMove(235 + x_left, 297 + y_top);
		robotMouseClick();
	}
	
	public void clickPrestonWiganRail()
	{
		robot.mouseMove(263 + x_left, 310 + y_top);
		robotMouseClick();
	}
	
	public void clickRochdaleYorkshireRail()
	{
		robot.mouseMove(510 + x_left, 276 + y_top);
		robotMouseClick();
	}
	
	public void clickSouthportWiganRail()
	{
		robot.mouseMove(218 + x_left, 336 + y_top);
		robotMouseClick();
	}
	
	public void clickWarringtonWiganRail()
	{
		robot.mouseMove(270 + x_left, 391 + y_top);
		robotMouseClick();
	}
	
	public void clickBlackburnBurnleyCanal()
	{
		robot.mouseMove(387 + x_left, 258 + y_top);
		robotMouseClick();
	}
	
	public void clickBlackburnWiganCanal()
	{
		robot.mouseMove(292 + x_left, 315 + y_top);
		robotMouseClick();
	}
	
	public void clickBurnleyColneCanal()
	{
		robot.mouseMove(426 + x_left, 201 + y_top);
		robotMouseClick();
	}
	
	public void clickBoltonBuryCanal()
	{
		robot.mouseMove(377 + x_left, 338 + y_top);
		robotMouseClick();
	}
	
	public void clickBoltonManchesterCanal()
	{
		robot.mouseMove(367 + x_left, 392 + y_top);
		robotMouseClick();
	}
	
	public void clickBuryManchesterCanal()
	{
		robot.mouseMove(418 + x_left, 363 + y_top);
		robotMouseClick();
	}
	
	public void clickLancasterPrestonCanal()
	{
		robot.mouseMove(247 + x_left, 172+ y_top);
		robotMouseClick();
	}
	
	public void clickFleetwoodPrestonCanal()
	{
		robot.mouseMove(212 + x_left, 223 + y_top);
		robotMouseClick();
	}
	
	public void clickPrestonWiganCanal()
	{
		robot.mouseMove(255 + x_left, 311 + y_top);
		robotMouseClick();
	}
	
	public void clickManchesterOldhamCanal()
	{
		robot.mouseMove(460 + x_left, 403 + y_top);
		robotMouseClick();
	}
	
	public void clickManchesterStockportCanal()
	{
		robot.mouseMove(432 + x_left, 473 + y_top);
		robotMouseClick();
	}
	
	public void clickManchesterWarringtonCanal()
	{
		robot.mouseMove(345 + x_left, 442 + y_top);
		robotMouseClick();
	}
	
	public void clickLiverpoolWiganCanal()
	{
		robot.mouseMove(234 + x_left, 391 + y_top);
		robotMouseClick();
	}
	
	public void clickLiverpoolEllesmereCanal()
	{
		robot.mouseMove(198 + x_left, 450 + y_top);
		robotMouseClick();
	}
	
	public void clickEllesmereWarringtonCanal()
	{
		robot.mouseMove(262 + x_left, 469 + y_top);
		robotMouseClick();
	}
	
	public void clickOldhamRochdaleCanal()
	{
		robot.mouseMove(498 + x_left, 341 + y_top);
		robotMouseClick();
	}
	
	public void clickWarringtonWiganCanal()
	{
		robot.mouseMove(279 + x_left, 391 + y_top);
		robotMouseClick();
	}
	
	public void clickMacclesfieldStockportCanal()
	{
		robot.mouseMove(440 + x_left, 538 + y_top);
		robotMouseClick();
	}
	
	public void clickColneYorkshireCanal()
	{
		robot.mouseMove(517 + x_left, 202 + y_top);
		robotMouseClick();
	}
	
	public void clickRochdaleYorkshireCanal()
	{
		robot.mouseMove(503 + x_left, 268 + y_top);
		robotMouseClick();
	}
	
	public void clickEllesmereNorthwichCanal()
	{
		robot.mouseMove(283 + x_left, 533 + y_top);
		robotMouseClick();
	}
	
	public void clickNorthwichMidlandsCanal()
	{
		robot.mouseMove(352 + x_left, 591 + y_top);
		robotMouseClick();
	}
	
	public void clickMacclesfieldMidlandsCanal()
	{
		robot.mouseMove(418 + x_left, 604 + y_top);
		robotMouseClick();
	}
	
	public void clickBarrow()
	{
		robot.mouseMove(133 + x_left, 92 + y_top);
		robotMouseClick();
	}
	
	public void clickBirkenhead()
	{
		robot.mouseMove(163 + x_left, 430 + y_top);
		robotMouseClick();
	}
	
	public void clickBlackburn()
	{
		robot.mouseMove(346 + x_left, 256 + y_top);
		robotMouseClick();
	}
	
	public void clickBolton()
	{
		robot.mouseMove(351 + x_left, 336 + y_top);
		robotMouseClick();
	}
	
	public void clickBurnley()
	{
		robot.mouseMove(422 + x_left, 228 + y_top);
		robotMouseClick();
	}
	
	public void clickBury()
	{
		robot.mouseMove(407 + x_left, 335 + y_top);
		robotMouseClick();
	}
	
	public void clickColne()
	{
		robot.mouseMove(458 + x_left, 188 + y_top);
		robotMouseClick();
	}
	
	public void clickEllesmere()
	{
		robot.mouseMove(243 + x_left, 498 + y_top);
		robotMouseClick();
	}
	
	public void clickFleetwood()
	{
		robot.mouseMove(181 + x_left, 195 + y_top);
		robotMouseClick();
	}
	
	public void clickLancaster()
	{
		robot.mouseMove(242 + x_left, 116 + y_top);
		robotMouseClick();
	}
	
	public void clickLiverpool()
	{
		robot.mouseMove(196 + x_left, 390 + y_top);
		robotMouseClick();
	}
	
	public void clickMacclesfield()
	{
		robot.mouseMove(422 + x_left, 570 + y_top);
		robotMouseClick();
	}
	
	public void clickManchester()
	{
		robot.mouseMove(416 + x_left, 447 + y_top);
		robotMouseClick();
	}
	
	public void clickOldham()
	{
		robot.mouseMove(496 + x_left, 374 + y_top);
		robotMouseClick();
	}
	
	public void clickPreston()
	{
		robot.mouseMove(258 + x_left, 228 + y_top);
		robotMouseClick();
	}
	
	public void clickRochdale()
	{
		robot.mouseMove(519 + x_left, 306 + y_top);
		robotMouseClick();
	}
	
	public void clickStockport()
	{
		robot.mouseMove(425 + x_left, 501 + y_top);
		robotMouseClick();
	}
	
	public void clickWarrington()
	{
		robot.mouseMove(294 + x_left, 445 + y_top);
		robotMouseClick();
	}
	
	public void clickWigan()
	{
		robot.mouseMove(236 + x_left, 367 + y_top);
		robotMouseClick();
	}
	
	public void clickDiscardAction()
	{
		robot.mouseMove(784 + x_left, 213 + y_top);
		robotMouseClick();
	}
	
	public void clickTechUpgradeAction()
	{
		robot.mouseMove(826 + x_left, 102 + y_top);
		robotMouseClick();
	}
	
	public void clickSellCottonAction()
	{
		robot.mouseMove(626 + x_left, 102 + y_top);
		robotMouseClick();
	}
	
	public void clickLinkAction()
	{
		robot.mouseMove(629 + x_left, 169 + y_top);
		robotMouseClick();
	}
	
	public void clickBuildAction()
	{
		robot.mouseMove(729 + x_left, 129 + y_top);
		robotMouseClick();
	}
	
	public void clickLoan10Action()
	{
		robot.mouseMove(602 + x_left, 221 + y_top);
		robotMouseClick();
	}
	
	public void clickLoan20Action()
	{
		robot.mouseMove(719 + x_left, 219 + y_top);
		robotMouseClick();
	}
	
	public void clickLoan30Action()
	{
		robot.mouseMove(842 + x_left, 218 + y_top);
		robotMouseClick();
	}
	
	public void clickCoalToken()
	{
		robot.mouseMove(602 + x_left, 28 + y_top);
		robotMouseClick();
	}
	
	public void clickCottonMillToken()
	{
		robot.mouseMove(669 + x_left, 31 + y_top);
		robotMouseClick();
	}
	
	public void clickIronToken()
	{
		robot.mouseMove(731 + x_left, 29 + y_top);
		robotMouseClick();
	}
	
	public void clickPortToken()
	{
		robot.mouseMove(791 + x_left, 26 + y_top);
		robotMouseClick();
	}
	
	public void clickShipyardToken()
	{
		robot.mouseMove(846 + x_left, 30 + y_top);
		robotMouseClick();
	}
	
	public void clickCard1()
	{
		robot.mouseMove(613 + x_left, 490 + y_top);
		robotMouseClick();
	}
	
	public void clickCard2()
	{
		robot.mouseMove(686 + x_left, 486 + y_top);
		robotMouseClick();
	}
	
	public void clickCard3()
	{
		robot.mouseMove(763 + x_left, 484 + y_top);
		robotMouseClick();
	}
	
	public void clickCard4()
	{
		robot.mouseMove(836 + x_left, 485 + y_top);
		robotMouseClick();
	}
	
	public void clickCard5()
	{
		robot.mouseMove(608 + x_left, 591 + y_top);
		robotMouseClick();
	}
	
	public void clickCard6()
	{
		robot.mouseMove(683 + x_left, 591 + y_top);
		robotMouseClick();
	}
	
	public void clickCard7()
	{
		robot.mouseMove(758 + x_left, 597 + y_top);
		robotMouseClick();
	}
	
	public void clickCard8()
	{
		robot.mouseMove(837 + x_left, 597 + y_top);
		robotMouseClick();
	}
	
	public void robotMouseClick()
	{
		robot.mousePress(left_button);
		robot.mouseRelease(left_button);
	}
	
	public void robotMouseMove(int x, int y)
	{
		robot.mouseMove(x + x_left, y + y_top);
		robotMouseClick();
	}
}
