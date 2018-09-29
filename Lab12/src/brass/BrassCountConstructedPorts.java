package brass;

public class BrassCountConstructedPorts implements util.CountCommand<BrassIndustry>
{
	private int constructed_port_count;
	
	public BrassCountConstructedPorts()
	{
		constructed_port_count = 0;
	}
	
	public int getCount()
	{
		return constructed_port_count;
	}
	
   public void execute(BrassIndustry brass_industry)
   {
		if (brass_industry.isConstructedPort())
		{
			constructed_port_count++;
		}
   }
}
