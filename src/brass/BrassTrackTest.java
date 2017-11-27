package brass;

import org.junit.*;
import static org.junit.Assert.*;

public class BrassTrackTest
{
	private BrassTrack bt;
	private BrassCottonDemandTrack bcd;
	
	private int allowed_error;
	
	@Before 
	public void setUp() 
	{
		BrassXML bxml = new BrassXML("resources/brass_pixels.xml");
		
		bt = new BrassTrack(bxml);
		
		BrassDemandTrackFunction cotton_tile_function = new BrassDemandTrackCottonTileFunction();
		BrassDemandTrackFunction cotton_extra_income_function = new BrassDemandTrackCottonExtraIncomeFunction();
		bcd = new BrassCottonDemandTrack(bxml, cotton_tile_function, cotton_extra_income_function);
		allowed_error = 2;  //+- one pixel is the allowed error
    }
	
	@Test
	public void brassCottonDemand()
	{
		int cotton_demand_extra_income = bcd.cottonTrackIncome();
		int cotton_demand_index = bcd.getCottonDemandIndex();
		
		assertEquals("Cotton Demand Index 1", 2, cotton_demand_index);
		assertEquals("Cotton Demand Extra Income 1", 2, cotton_demand_extra_income);
		
		cotton_demand_extra_income = bcd.cottonTrackIncome();
		cotton_demand_index = bcd.getCottonDemandIndex();
		
		assertEquals("Cotton Demand Index 2", 3, cotton_demand_index);
		assertEquals("Cotton Demand Extra Income 2", 2, cotton_demand_extra_income);
		
		cotton_demand_extra_income = bcd.cottonTrackIncome();
		cotton_demand_index = bcd.getCottonDemandIndex();
		
		assertEquals("Cotton Demand Index 3", 6, cotton_demand_index);
		assertEquals("Cotton Demand Extra Income 3", 0, cotton_demand_extra_income);
		
		cotton_demand_extra_income = bcd.cottonTrackIncome();
		cotton_demand_index = bcd.getCottonDemandIndex();
		
		assertEquals("Cotton Demand Index 4", 8, cotton_demand_index);
		assertEquals("Cotton Demand Extra Income 4", -1, cotton_demand_extra_income);
		
		cotton_demand_extra_income = bcd.cottonTrackIncome();
		cotton_demand_index = bcd.getCottonDemandIndex();
		
		assertEquals("Cotton Demand Index 5", 8, cotton_demand_index);
		assertEquals("Cotton Demand Extra Income 5", -1, cotton_demand_extra_income);
	}
	
	@Test
	public void brassLocTrackTest0()
	{
		int x_pos = bt.getXPixel(0);
		int y_pos = bt.getYPixel(0);
		assertTrue("Brass Track Loc X 0 <=", 322 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 0 >=", 322 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 0 <=", 642 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 0 >=", 642 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest1()
	{
		int x_pos = bt.getXPixel(1);
		System.out.println(x_pos);
		int y_pos = bt.getYPixel(1);
		System.out.println(y_pos);
		assertTrue("Brass Track Loc X 1 <=", 302 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 1 >=", 302 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 1 <=", 642 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 1 >=", 642 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest5()
	{
		int x_pos = bt.getXPixel(5);
		int y_pos = bt.getYPixel(5);
		assertTrue("Brass Track Loc X 5 <=", 226 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 5 >=", 226 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 5 <=", 642 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 5 >=", 642 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest10()
	{
		int x_pos = bt.getXPixel(10);
		int y_pos = bt.getYPixel(10);
		assertTrue("Brass Track Loc X 10 <=", 132 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 10 >=", 132 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 10 <=", 642 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 10 >=", 642 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest16()
	{
		int x_pos = bt.getXPixel(16);
		System.out.println(x_pos);
		int y_pos = bt.getYPixel(16);
		System.out.println(y_pos);
		assertTrue("Brass Track Loc X 16 <=", 20 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 16 >=", 20 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 16 <=", 642 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 16 >=", 642 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest17()
	{
		int x_pos = bt.getXPixel(17);
		int y_pos = bt.getYPixel(17);
		assertTrue("Brass Track Loc X 17 <=", 20 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 17 >=", 20 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 17 <=", 623 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 17 >=", 623 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest18()
	{
		int x_pos = bt.getXPixel(18);
		int y_pos = bt.getYPixel(18);
		assertTrue("Brass Track Loc X 18 <=", 20 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 18 >=", 20 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 18 <=", 605 <= (y_pos + allowed_error)); 
		assertTrue("Brass Track Loc Y 18 >=", 605 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest32()
	{
		int x_pos = bt.getXPixel(32);
		int y_pos = bt.getYPixel(32);
		assertTrue("Brass Track Loc X 32 <=", 245 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 32 >=", 245 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 32 <=", 568 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 32 >=", 568 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest46()
	{
		int x_pos = bt.getXPixel(46);
		int y_pos = bt.getYPixel(46);
		assertTrue("Brass Track Loc X 46 <=", 19 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 46 >=", 19 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 46 <=", 532 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 46 >=", 532 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest55()
	{
		int x_pos = bt.getXPixel(55);
		int y_pos = bt.getYPixel(55);
		assertTrue("Brass Track Loc X 55 <=", 151 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 55 >=", 151 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 55 <=", 494 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 55 >=", 494 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest64()
	{
		int x_pos = bt.getXPixel(64);
		int y_pos = bt.getYPixel(64);
		assertTrue("Brass Track Loc X 64 <=", 19 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 64 >=", 19 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 64 <=", 458 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 64 >=", 458 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest70()
	{
		int x_pos = bt.getXPixel(70);
		int y_pos = bt.getYPixel(70);
		assertTrue("Brass Track Loc X 70 <=", 94 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 70 >=", 94 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 70 <=", 421 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 70 >=", 421 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest76()
	{
		int x_pos = bt.getXPixel(76);
		int y_pos = bt.getYPixel(76);
		assertTrue("Brass Track Loc X 76 <=", 19 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 76 >=", 19 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 76 <=", 386 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 76 >=", 386 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest82()
	{
		int x_pos = bt.getXPixel(82);
		int y_pos = bt.getYPixel(82);
		assertTrue("Brass Track Loc X 82 <=", 94 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 82 >=", 94 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 82 <=", 349 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 82 >=", 349 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest88()
	{
		int x_pos = bt.getXPixel(88);
		int y_pos = bt.getYPixel(88);
		assertTrue("Brass Track Loc X 88 <=", 18 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 88 >=", 18 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 88 <=", 311 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 88 >=", 311 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest93()
	{
		int x_pos = bt.getXPixel(93);
		int y_pos = bt.getYPixel(93);
		assertTrue("Brass Track Loc X 93 <=", 74 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 93 >=", 74 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 93 <=", 275 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 93 >=", 275 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassLocTrackTest98()
	{
		int x_pos = bt.getXPixel(98);
		System.out.println(x_pos);
		int y_pos = bt.getYPixel(98);
		System.out.println(y_pos);
		assertTrue("Brass Track Loc X 98 <=", 18 <= (x_pos + allowed_error));
		assertTrue("Brass Track Loc X 98 >=", 18 >= (x_pos - allowed_error));
		assertTrue("Brass Track Loc Y 98 <=", 238 <= (y_pos + allowed_error));
		assertTrue("Brass Track Loc Y 98 >=", 238 >= (y_pos - allowed_error));
	}
	
	@Test
	public void brassIncomeTrackTest26()
	{	
		int income_amt = BrassTrack.getIncomeAmount(26);
		assertEquals("Brass Track Income 26", 8, income_amt);
	}
		
	@Test
	public void brassIncomeTrackTest72()
	{	
		int income_amt = BrassTrack.getIncomeAmount(72);
		assertEquals("Brass Track Income 72", 23, income_amt);
	}
	
	@Test
	public void brassIncomeTrackTest82()
	{	
		int income_amt = BrassTrack.getIncomeAmount(82);
		assertEquals("Brass Track Income 82", 26, income_amt);
	}
	
	@Test
	public void brassIncomeTrackTest40()
	{	
		int income_amt = BrassTrack.getIncomeAmount(40);
		assertEquals("Brass Track Income 40", 14, income_amt);
	}
	
	@Test
	public void brassIncomeTrackTest39()
	{	
		int income_amt = BrassTrack.getIncomeAmount(39);
		assertEquals("Brass Track Income 39", 13, income_amt);
	}
	
	@Test
	public void brassIncomeTrackTest16()
	{	
		int income_amt = BrassTrack.getIncomeAmount(16);
		assertEquals("Brass Track Income 17", 3, income_amt);
	}
	
	@Test
	public void brassIncomeTrackTest17()
	{	
		int income_amt = BrassTrack.getIncomeAmount(17);
		assertEquals("Brass Track Income 17", 4, income_amt);
	}
	
	@Test
	public void brassIncomeTrackTest18()
	{	
		int income_amt = BrassTrack.getIncomeAmount(18);
		assertEquals("Brass Track Income 18", 4, income_amt);
	}
	
	@Test
	public void brassIncomeTrackTest19()
	{	
		int income_amt = BrassTrack.getIncomeAmount(19);
		assertEquals("Brass Track Income 19", 5, income_amt);
	}
	
	@Test
	public void brassIncomeTrackTest10()
	{	
		int income_amt = BrassTrack.getIncomeAmount(10);
		assertEquals("Brass Track Income 10", 0, income_amt);
	}
	
	@Test
	public void brassIncomeTrackTest5()
	{	
		int income_amt = BrassTrack.getIncomeAmount(5);
		assertEquals("Brass Track Income 5", -5, income_amt);
	}

	@Test
	public void brassLoanTrackTest87()
	{
		int income_index = BrassTrack.takeLoan(87, 30);
		assertEquals("Brass Track Loan 87, 30", 76, income_index);
	}
	
	@Test
	public void brassLoanTrackTest63()
	{
		int income_index = BrassTrack.takeLoan(63, 20);
		assertEquals("Brass Track Loan 63, 20", 57, income_index);
	}
	
	@Test
	public void brassLoanTrackTest16()
	{
		int income_index = BrassTrack.takeLoan(16, 10);
		assertEquals("Brass Track Loan 16, 10", 14, income_index);
	}
	
	@Test
	public void brassLoanTrackTest10()
	{
		int income_index = BrassTrack.takeLoan(10, 20);
		assertEquals("Brass Track Loan 10, 20", 8, income_index);
	}
}
