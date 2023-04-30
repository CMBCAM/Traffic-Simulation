
public class Instructions {
	int[] x;
	int[] y;
	
	public Instructions()
	{
		x = new int[10]; //the x coord at that completion
		y = new int[10]; //the y coord at that completion
	}
	public Instructions(String coords) //20 characters
	{
		x = new int[10]; //the x coord at that completion
		y = new int[10]; //the y coord at that completion
		if (coords.length() == 20)
		{
			for (int i = 0; i < 10; i++)
			{
				x[i] = Integer.parseInt(coords.substring(i, i+1));
				y[i] = Integer.parseInt(coords.substring(i+10, i+11));
			}
		}
	}
	public void setCoord(int step, int nx, int ny)
	{
		x[step] = nx;
		y[step] = ny;
	}
}
