package brass;

public class BrassDriver
{
	public static void main(String[] args)
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
	 
	 System.out.println(num_players);
		BrassGame brass_game = new BrassGame(num_players);
   }
}
