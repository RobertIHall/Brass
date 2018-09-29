package brass;

//this class is instantiated in BrassCities and requires knowledge
//of the BrassIndustry interface (isUnflippedIndustry method)
//we could simply write a method in BrassCity that does the counting
//and call it from BrassCities

//the reason why this design pattern is still useful is it allows us
//to add functionality to a class even if we can't or don't want to modify the class
//this idea is similar to Visitor Design Pattern
public class BrassCountUnflippedIndustry implements util.CountCommand<BrassIndustry>
{
	private int unflipped_industry_count;
	private int industry_id;
	
	public BrassCountUnflippedIndustry(int ind_id)
	{
		unflipped_industry_count = 0;
		industry_id = ind_id;
	}
	
	public int getCount()
	{
		return unflipped_industry_count;
	}
	
   public void execute(BrassIndustry brass_industry)
   {
		if (brass_industry.isUnflippedIndustry(industry_id))
		{
			unflipped_industry_count++;
		}
   }
}
