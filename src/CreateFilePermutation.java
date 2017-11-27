
public class CreateFilePermutation
{
	public static void main(String[] args)
	{
		util.Keyboard key = util.Keyboard.getKeyboard();
		int high = key.readInt("Enter the upper limit integer value (lower limit assumed to be 1): ");
		String file_name = key.readString("Enter the file where the permutation will be stored: ");
		
		util.RandomPermutation rp = new util.RandomPermutation(high, high);
		
		util.WriteTextFile wf = new util.WriteTextFile(file_name);
		while(rp.hasNext())
		{
			int num = rp.next();
			wf.writeLine(num + "");
		}
		wf.close();
		
		util.ReadTextFile rf = new util.ReadTextFile(file_name);
		String line = rf.readLine();
		int[] integers = new int[high - 1 + 1];
		int count = 0;
		while(!rf.EOF())
		{
			int num = Integer.parseInt(line);
			integers[count] = num;
			count++;
			line = rf.readLine();
		}
		rf.close();
		
		//make sure all of the requested integers are present in the permutation
		java.util.Arrays.sort(integers);
		int size = integers.length;
		for (int i = 0; i < size; i++)
		{
			System.out.println(integers[i]);
		}
	}
}
