import java.util.*;

public class Graphics {
	
	Intersection[][] intersections;
	String[] line;
	String[] outputLine;
	String[] light;
	String[][][] incV; //incoming vehicles
	String[][][] outV; //incoming vehicles
	String[][] interV;
	String[][]interAll;
	/*
	 * array list of positions vehicle goes to depenidng on turn signal
	 */
	ArrayList<Instructions> leftTurns; // first 1 is top 
	ArrayList<Instructions> forwardsOne;
	ArrayList<Instructions> forwardsTwo;
	ArrayList<Instructions> rightTurns;
	
	public Graphics(int height, int width)
	{
		intersections = new Intersection[height][width];
		line = new String[30]; //display size
		outputLine = new String[8];
		light = new String[5];
		incV = new String [4][2][6]; //road;
		outV = new String [4][2][6]; //road; lane; position(not used)
		interV = new String [4][2]; //road; lane
		interAll = new String [8][9]; //x and y
		leftTurns = new ArrayList<Instructions>();
		rightTurns = new ArrayList<Instructions>();
		forwardsOne = new ArrayList<Instructions>();
		forwardsTwo = new ArrayList<Instructions>();
		for (int i = 0; i < 30; i++)
			line[i] = "";
		for (int i = 0; i < 8; i++)
			outputLine[i] = "";
		for (int i = 0; i < 5; i++)
			light[i] = "";
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				for (int k = 0; k < 6; k++)
				{
					incV[i][j][k] = "   ";
					outV[i][j][k] = "   ";
				}
				interV[i][j] = "   ";
			}
		}
		for (int i = 0; i < 8; i++) //transition intersections 
		{
			for (int j = 0; j < 9; j++)
			{
				interAll[i][j] = "   ";
			}
		}
		assignIntersectionInstructions();
	}
	public void assignIntersectionInstructions()
	{
		leftTurns.add(new Instructions("22222345671234566666"));
		leftTurns.add(new Instructions("65544322222222224568"));
		leftTurns.add(new Instructions("55555543217654322222"));
		leftTurns.add(new Instructions("12345555556666543210"));
		
		rightTurns.add(new Instructions("00000000000000000000"));
		rightTurns.add(new Instructions("77777777770000000000"));
		rightTurns.add(new Instructions("77777777778888888888"));
		rightTurns.add(new Instructions("00000000008888888888"));
		
		forwardsOne.add(new Instructions("22222222221234456678"));
		forwardsOne.add(new Instructions("66544322102222222222"));
		forwardsOne.add(new Instructions("55555555557665433210"));
		forwardsOne.add(new Instructions("12234556776666666666"));
		
		forwardsTwo.add(new Instructions("00000000001234456678"));
		forwardsTwo.add(new Instructions("66544322100000000000"));
		forwardsTwo.add(new Instructions("77777777777665433210"));
		forwardsTwo.add(new Instructions("12234556778888888888"));
	}
	public void addIntersection(Intersection newIntersection, int y, int x) //adds an intersection at specified location
	{
		intersections[y][x] = newIntersection;
		
	}
	public String attachSignal(String vehicle, LaneType turnSignal, int direction)
	{
		switch (direction)
		{
			case 0:
				switch (turnSignal)
				{
					case LEFT:
						return vehicle + ">";
					case FORWARD:
						return "v" + vehicle;
					case RIGHT:
						return "<" + vehicle;
					default:
						return "   ";
				}
			case 1:
				switch (turnSignal)
				{
					case LEFT:
						return "v" + vehicle;
					case FORWARD:
						return "<" + vehicle;
					case RIGHT:
						return "^" + vehicle;
					default:
						return "   ";
				}
			case 2:
				switch (turnSignal)
				{
					case LEFT:
						return "<" + vehicle;
					case FORWARD:
						return vehicle + "^";
					case RIGHT:
						return vehicle + ">";
					default:
						return "   ";
				}
			case 3:
				switch (turnSignal)
				{
					case LEFT:
						return vehicle + "^";
					case FORWARD:
						return vehicle + ">";
					case RIGHT:
						return vehicle +"v";
					default:
						return "   ";
				}
			default:
				return "    ";
		}
	}
	public String displayLight(Light light)
	{
		switch (light)
		{
		case LEFTGREEN:
			return "LeftGreen";
		case RED:
			return "   Red   ";
		case GREEN:
			return "  Green  ";
		default:
			return "         ";
		}
	}

	public void assignOutputDrawings(int y, int x)
	{
		for (int i = 0; i < 8; i++)
		{
			if (intersections[y][x].output[i] !=null) //check if lane is empty in intersection, then set it to the itersection if hasnt moved else set it to transition squares.
			{
				outputLine[i] = attachSignal(intersections[y][x].output[i].toString(),
						LaneType.FORWARD, (i / 2 + 2)%4);
			}
			else
				outputLine[i] ="   ";
			
		}
	}
	public void drawIntersection()
	{
		assignInputDrawings(0, 0);
		assignOutputDrawings(0,0);
		drawLights(0,0);
		drawIncVehicles(0,0,0);
		drawIncVehicles(0,0,1);
		drawIncVehicles(0,0,2);
		drawIncVehicles(0,0,3);
		wipeIntersection();
		drawInterVehicles(0,0,0);
		drawInterVehicles(0,0,1);
		drawInterVehicles(0,0,2);
		drawInterVehicles(0,0,3);
		//Top Lane 16, 2 Top intersection,1 right inter 6 right lane, 1 light arr, 1 right inter, 6 right lane, 1 light arr
		//1 arr + light, 1 light arr, 6 left lane, 1 left inter, 1 light arr, 6 left lane, 1 left inter, 2 bot inter, 16 bot lane
		System.out.println();
		System.out.printf("Total Cycles:%d     \r\n",Main.simCycle);
		System.out.printf("                         |  %s  |  %s  |xxx|  %s  |  %s  |\r\n",incV[0][1][4],incV[0][0][4],outV[2][0][4],outV[2][1][4]);
		System.out.printf("                         |               |xxx|               |\r\n");
		System.out.printf("       %s                 |  %s  |  %s  |xxx|  %s  |  %s  |\r\n",light[0],incV[0][1][3],incV[0][0][3],outV[2][0][3],outV[2][1][3]);
		System.out.printf("       %s                 |               |xxx|               |\r\n",light[1]);
		System.out.printf(" %s           |  %s  |  %s  |xxx|  %s  |  %s  |\r\n"            ,light[2],incV[0][1][2],incV[0][0][2],outV[2][0][2],outV[2][1][2]);
		System.out.printf("       %s                 |               |xxx|               |\r\n",light[3]);
		System.out.printf("       %s                 |  %s  |  %s  |xxx|  %s  |  %s  |\r\n",light[4],incV[0][1][1],incV[0][0][1],outV[2][0][1],outV[2][1][1]);
		System.out.printf("                         |               |xxx|               |\r\n");
		System.out.printf("                         |  %s  |  %s  |xxx|  %s  |  %s  |\r\n",incV[0][1][0],incV[0][0][0],outputLine[0],outputLine[1]);
		System.out.printf("--------------------------                                   -----------------------\r\n");
		System.out.printf("   %s %s %s %s %s %s  %s %s %s %s %s %s %s  %s   %s %s %s %s %s %s\r\n",
				outV[3][0][5],outV[3][0][4],outV[3][0][3],outV[3][0][2],outV[3][0][1],outputLine[7],
				interV[0][1], interAll[1][0], interV[0][0],
				interAll[3][0], interAll[4][0], interAll[5][0], interAll[6][0],
				interV[1][1],
				incV[1][1][0],incV[1][1][1],incV[1][1][2],incV[1][1][3],incV[1][1][4],incV[1][1][5]);
		System.out.printf("--  --  --  --  --  --  --  %s     %s         %s     %s   --  --  --  --  --  --\r\n",interAll[0][1], interAll[2][1],interAll[5][1], interAll[7][1] );
		System.out.printf("   %s %s %s %s %s %s  %s %s %s %s %s %s %s  %s   %s %s %s %s %s %s\r\n",
				outV[3][1][5],outV[3][1][4],outV[3][1][3],outV[3][1][2],outV[3][1][1],outputLine[6],
				interAll[0][2], interAll[1][2], interAll[2][2], interAll[3][2], interAll[4][2], interAll[5][2], interAll[6][2],
				interV[1][0],
				incV[1][0][0],incV[1][0][1],incV[1][0][2],incV[1][0][3],incV[1][0][4],incV[1][0][5]);
		System.out.printf("--------------------------  %s     %s         %s     %s  -----------------------\r\n" ,interAll[0][3], interAll[2][3],interAll[5][3], interAll[7][3]);
		System.out.printf("xxxxxxxxxxxxxxxxxxxxxxxxx   %s     %s         %s     %s   xxxxxxxxxxxxxxxxxxxxxxx\r\n",interAll[0][4], interAll[2][4],interAll[5][4], interAll[7][4]);
		System.out.printf("--------------------------  %s     %s         %s     %s  -----------------------\r\n" ,interAll[0][5], interAll[2][5],interAll[5][5], interAll[7][5]);
		System.out.printf("   %s %s %s %s %s %s  %s %s %s %s %s %s %s %s   %s %s %s %s %s %s\r\n",
				incV[3][0][5],incV[3][0][4],incV[3][0][3],incV[3][0][2],incV[3][0][1],incV[3][0][0], //left
				interV[3][0], interAll[1][6], interAll[2][6], interAll[3][6], interAll[4][6], interAll[5][6], interAll[6][6], interAll[7][6], //middle
				outputLine[2],outV[1][1][1],outV[1][1][2],outV[1][1][3],outV[1][1][4],outV[1][1][5]); //right
		System.out.printf("--  --  --  --  --  --  --  %s     %s         %s     %s   --  --  --  --  --  --\r\n" ,interAll[0][7], interAll[2][7],interAll[5][7], interAll[7][7]);
		System.out.printf("   %s %s %s %s %s %s  %s %s %s %s %s %s %s  %s  %s %s %s %s %s %s\r\n",
				incV[3][1][5],incV[3][1][4],incV[3][1][3],incV[3][1][2],incV[3][1][1],incV[3][1][0],
				interV[3][1], interAll[1][8], interAll[2][8], interAll[3][8], interAll[4][8],
				interV[2][0], interAll[6][8], interV[2][1],
				outputLine[3],outV[1][0][1],outV[1][0][2],outV[1][0][3],outV[1][0][4],outV[1][0][5]);
		System.out.printf("--------------------------                                   -----------------------\r\n");
		System.out.printf("                         |  %s  |  %s  |xxx|  %s  |  %s  |\r\n",outputLine[5],outputLine[4],incV[2][0][0],incV[2][1][0]);
		System.out.printf("                         |               |xxx|               |\r\n");
		System.out.printf("                         |  %s  |  %s  |xxx|  %s  |  %s  |\r\n",outV[0][1][1],outV[0][0][1],incV[2][0][1],incV[2][1][1]);
		System.out.printf("                         |               |xxx|               |\r\n");
		System.out.printf("                         |  %s  |  %s  |xxx|  %s  |  %s  |\r\n",outV[0][1][2],outV[0][0][2],incV[2][0][2],incV[2][1][2]);
		System.out.printf("                         |               |xxx|               |\r\n");
		System.out.printf("                         |  %s  |  %s  |xxx|  %s  |  %s  |\r\n",outV[0][1][3],outV[0][0][3],incV[2][0][3],incV[2][1][3]);
		System.out.printf("                         |               |xxx|               |\r\n");
		System.out.printf("                         |  %s  |  %s  |xxx|  %s  |  %s  |\r\n",outV[0][1][4],outV[0][0][4],incV[2][0][4],incV[2][1][4]);
		System.out.printf("                         |               |xxx|               |\r\n");
/*		System.out.printf("\nTotal Cycles: %10d %s|xxx|               |\r\n", Main.simCycle, line[9]); //9
		System.out.printf("Current Traffic Cycle:%2d %s|xxx|       |       |\r\n", Main.simCycle % intersections[0][0].totalTrafficCycle, line[8]);//8
		System.out.printf( "                         %s|xxx|               |\r\n"//7
						+ "                         %s|xxx|       |       |\r\n"//6
						+ "                         %s|xxx|               |\r\n"//5
						+ "                         %s|xxx|  %s  |  %s  |\r\n"//4
						+ "                         %s|xxx|               |\r\n"//3
						+ "                         %s|xxx|       |       |\r\n"//2
						+ "                         %s|xxx|               |\r\n"//1
						+ "                         %s|xxx|       |       |\r\n"//0
						+ "--------------------------                                   ----------------------\r\n"
						+ "            %s             %s\r\n"//10
						+ "--  --  --  --  --  -- --                                    --  --  --  --  --  --\r\n"
						+ "            %s                            %s\r\n"//11
						+ "--------------------------                 %s                 ----------------------\r\n" //12
						+ "xxxxxxxxxxxxxxxxxxxxxxxxx            %s           xxxxxxxxxxxxxxxxxxxxxx\r\n"//13
						+ "--------------------------                 %s                 ----------------------\r\n"//14
						+ "   %s                         %s\r\n"//15
						+ "--  --  --  --  --  --  --                                   --  --  --  --  --  --\r\n"
						+ "   %s     %s\r\n"//16
						+ "--------------------------                                   ----------------------\r\n"
						+ "                         |       |       |xxx%s|\r\n"//17
						+ "                         |               |xxx%s|\r\n"//18
						+ "                         |       |       |xxx%s|\r\n"//19
						+ "                         |               |xxx%s|\r\n"//20
						+ "                         |  %s  |  %s  |xxx%s|\r\n"//21
						+ "                         |               |xxx%s|\r\n"//22
						+ "                         |       |       |xxx%s|\r\n"//23
						+ "                         |               |xxx%s|\r\n"//24
						+ "                         |       |       |xxx%s|\r\n"//25
						+ "                         |               |xxx%s|\r\n"//26
						,line[7],line[6],line[5],line[4],outputLine[0],outputLine[1],line[3],line[2],line[1],line[0],outputLine[7],line[10],outputLine[6],line[11]
						,line[12],line[13],line[14],line[15],outputLine[2],line[16],outputLine[3],line[17],line[18],line[19],line[20],outputLine[5],outputLine[4],line[21],
						line[22],line[23],line[24],line[25],line[26]); */
	}
	/*
	 *                                              |  22   |  21   |xxx|       |       |
		                                            |  20      19   |xxx|               |
		 				  "                         |  18   |  17   |xxx|       |       |
						+ "                         |  16      15   |xxx|               |
						+ "                         |  14   |  13   |xxx|       |       |
						+ "                         |  12      11   |xxx|               |
						+ "                         |  10   |  09   |xxx|       |       |
						+ "                         |  08      07   |xxx|               |
						+ "                         |  06   |  05   |xxx|       |       |
						+ "                         |  04      03   |xxx|               |
						+ "--------------------------                                   ----------------------
						+ "                            02      01                  02   04  06  08  10  12  14
						+ "--  --  --  --  --  -- --                                    --  --  --  --  --  --\r\n"
						+ "                                           t2           01   03  05  07  09  11  13\r\n"
						+ "--------------------------                 t1                ----------------------\r\n"
						+ "xxxxxxxxxxxxxxxxxxxxxxxxx              Light                 xxxxxxxxxxxxxxxxxxxxxx\r\n"
						+ "--------------------------                 b1                ----------------------\r\n"
						+ "   %s  %s  %s  %s  %s  %s    %s            b2                                      \r\n"
						+ "--  --  --  --  --  --  --                                   --  --  --  --  --  --\r\n"
						+ "   %s  %s  %s  %s  %s  %s    %s                 %s      %s                         \r\n"
						+ "--------------------------                                   ----------------------\r\n"
						+ "                         |       |       |xxx|  %s   |  %s   |\r\n"
						+ "                         |               |xxx|  %s      %s   |\r\n"
						+ "                         |       |       |xxx|  %s   |  %s   |\r\n"
						+ "                         |               |xxx|  %s      %s   |\r\n"
						+ "                         |       |       |xxx|  %s   |  %s   |\r\n"
						+ "                         |               |xxx|  %s      %s   |\r\n"
						+ "                         |       |       |xxx|  %s   |  %s   |\r\n"
						+ "                         |               |xxx|  %s      %s   |\r\n");
	 */
	public void drawInterVehicles(int y, int x, int road)
	{
		Vehicle tempVehicle;
		for (int i = 0; i < 2; i++)
		{
			tempVehicle= intersections[y][x].getIntersectionSpace(road, i);
			if(tempVehicle != null)
			{
				double complete = tempVehicle.completetion;
				int test = (int) (complete * 10);
				if (complete < 0.1)
					interV[road][i] = attachSignal(tempVehicle.toString(),tempVehicle.turnSignal,road);
				else
				{
					switch (tempVehicle.turnSignal)
					{
					case LEFT:
						interAll[leftTurns.get(road).x[(int) ((test-1)*0.8)]][leftTurns.get(road).y[(int) ((test-1)*0.8)]] = attachSignal(tempVehicle.toString(),tempVehicle.turnSignal,road);
						break;
					case RIGHT:
						interV[road][i] = attachSignal(tempVehicle.toString(),tempVehicle.turnSignal,road);
						break;
					case FORWARD:
						if (i ==0)
							interAll[forwardsOne.get(road).x[(int) ((test-1)*0.8)]][forwardsOne.get(road).y[(int) ((test-1)*0.8)]] = attachSignal(tempVehicle.toString(),tempVehicle.turnSignal,road);
						else
							interAll[forwardsTwo.get(road).x[(int) ((test-1)*0.8)]][forwardsTwo.get(road).y[(int) ((test-1)*0.8)]] = attachSignal(tempVehicle.toString(),tempVehicle.turnSignal,road);
						break;
					}
					
				}
			}
		}
	}
	public void wipeIntersection() //clear the intersection so it can redraw
	{
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				interV[i][j] = "   ";
			}
		}
		for (int i = 0;i < 8; i++)
		{
			for (int j=0;j<9;j++)
			{
				interAll[i][j] = "   ";
			}
		}
	}
	public void drawIncVehicles(int y, int x, int road)
	{
		Queue<Vehicle> temp;
		String currentVehicle;
		for (int k = 0; k < 2; k++)
		{
			temp = new LinkedList<Vehicle>(intersections[y][x].getLane(road,k)); 
			int maxRows;
			if(road % 2 == 0)
				maxRows = 5;
			else
				maxRows = 6;
			for (int i = 0; i < maxRows; i++)
			{
				if (temp.peek() != null)
				{
					currentVehicle = attachSignal(temp.peek().toString(),temp.peek().turnSignal,road);
				}
				else
					currentVehicle = "   ";
				temp.poll();
				incV[road][k][i] =currentVehicle;
			}
		}
	}
	
	public void drawLights(int y,int x)
	{
		if (intersections[y][x].light0)
		{
			String roadLight = displayLight(intersections[y][x].light[0].light);
			light[0] = "^";
			light[1] = "|";
			light[2] = "  " + roadLight + "  ";
			light[3] = "|";
			light[4] = "v";
		}
		else
		{
			String roadLight = displayLight(intersections[y][x].light[1].light);
			light[0]= " ";
			light[1] = " ";
			light[2] = "<-" + roadLight + "->";
			light[3] = " ";
			light[4] = " ";
		}
	}
	public void assignInputDrawings(int y,int x)
	{
		/*
		 * lights
		 */
		if (intersections[y][x].light0)
		{
			String roadLight = displayLight(intersections[y][x].light[0].light);
			light[0] = "^";
			light[1] = "|";
			light[2] = "  " + roadLight + "  ";
			light[3] = "|";
			light[4] = "v";
		}
		else
		{
			String roadLight = displayLight(intersections[y][x].light[1].light);
			light[0]= " ";
			light[1] = " ";
			light[2] = "<-" + roadLight + "->";
			light[3] = " ";
			light[4] = " ";
		}
		//maybe i should do it by row?
		/*
		 * Top Lanes 10 cars
		 */
		for (int i = 0; i < 10; i++)
			line[i] = "|";
		Queue<Vehicle> temp;
		Vehicle tempVehicle;
		String currentVehicle = "   ";
		for (int i = 1; i >= 0; i--) //right most lane first
		{
			temp = new LinkedList<Vehicle>(intersections[y][x].getLane(0,i)); 
			int maxRows = intersections[y][x].getLaneSize(0, i);
			
			for (int laneRow = 0; laneRow < 10; laneRow++)
			{
				if (temp.peek() != null && laneRow % 2 == 0)
				{
					currentVehicle = attachSignal(temp.peek().toString(),temp.peek().turnSignal,0);
				}
				else
					currentVehicle = "   ";
				temp.poll();
				line[laneRow] += ("  " + currentVehicle + "  ");
			}
			if (i == 1)
			{
				for (int j = 0; j < 10; j++)
				{
					if (j%2 == 1)
						line[j] += " ";
					else
						line[j] += "|";
				}
			}
		} 
		/*
		 * right lane + intersection
		 */
		line[10] = ""; //clean line
		for (int i = 1; i >=0; i--)
		{
			tempVehicle= intersections[y][x].getIntersectionSpace(0, i);
			if(tempVehicle != null)
				line[10] += attachSignal(tempVehicle.toString(),tempVehicle.turnSignal,0);
			else
				line[10] += "   ";
			line[10] +="     ";
		}
		line[10] += "            ";
		tempVehicle = intersections[y][x].getIntersectionSpace(1,1);
		if(tempVehicle != null)
			line[10] += attachSignal(tempVehicle.toString(),tempVehicle.turnSignal,1) + "  ";
		else
			line[10] += "     ";
		temp = new LinkedList<Vehicle>(intersections[y][x].getLane(1,1));
		for (int i = 0; i < 6; i++)
		{
			if (temp.peek() != null)
			{
				currentVehicle = attachSignal(temp.peek().toString(),temp.peek().turnSignal,1);
			}
			else
				currentVehicle = "   ";
			temp.poll();
			line[10] += currentVehicle + " ";
		}
		/*
		 * second lane of right road and intersection as well as light
		 */
		
		tempVehicle = intersections[y][x].getIntersectionSpace(1,0);
		if(tempVehicle != null)
			line[11] += attachSignal(tempVehicle.toString(),tempVehicle.turnSignal,1) + "  ";
		else
			line[11] += "     ";
		temp = new LinkedList<Vehicle>(intersections[y][x].getLane(1,0));
		for (int i = 0; i < 6; i++)
		{
			if (temp.peek() != null)
			{
				currentVehicle = attachSignal(temp.peek().toString(),temp.peek().turnSignal,1);
			}
			else
				currentVehicle = "   ";
			temp.poll();
			line[11] += currentVehicle + " ";
		}
		/*
		 * lane left of left road
		 */
		line[15] ="";
		temp = new LinkedList<Vehicle>(intersections[y][x].getLane(3,0));
		for (int i = 0; i < 6; i++)
		{
			if (temp.peek() != null)
			{
				currentVehicle = attachSignal(temp.peek().toString(),temp.peek().turnSignal,3);
			}
			else
				currentVehicle = "   ";
			temp.poll();
			line[15] = currentVehicle + " " + line[15];
		}
		line[15] += " ";
		tempVehicle = intersections[y][x].getIntersectionSpace(3,0);
		if(tempVehicle != null)
			line[15] += attachSignal(tempVehicle.toString(),tempVehicle.turnSignal,3) + "            ";
		else
			line[15] += "               ";
		if (intersections[y][x].light0)
			line[15] += "v";
		else
			line[15] += " ";
		/*
		 * right lane of left road
		 */
		line[16] ="";
		temp = new LinkedList<Vehicle>(intersections[y][x].getLane(3,1));
		for (int i = 0; i < 6; i++)
		{
			if (temp.peek() != null)
			{
				currentVehicle = attachSignal(temp.peek().toString(),temp.peek().turnSignal,3);
			}
			else
				currentVehicle = "   ";
			temp.poll();
			line[16] = currentVehicle + " " + line[16];
		}
		line[16] += " ";
		tempVehicle = intersections[y][x].getIntersectionSpace(3,1);
		if(tempVehicle != null)
			line[16] += attachSignal(tempVehicle.toString(),tempVehicle.turnSignal,3) + "                 ";
		else
			line[16] += "                    ";
		/*
		 * bottom intersection
		 */
		for (int i = 0; i < 2; i++)
		{
			tempVehicle= intersections[y][x].getIntersectionSpace(2, i);
			if(tempVehicle != null)
				line[16] += attachSignal(tempVehicle.toString(),tempVehicle.turnSignal,2);
			else
				line[16] += "   ";
			line[16] +="     ";
		}
		/*
		 * bottom lane
		 */
		for (int i = 17; i < 27; i++)
			line[i] = "|";
		for (int i = 0; i < 2; i++) //right most lane first
		{
			temp = new LinkedList<Vehicle>(intersections[y][x].getLane(2,i)); 
			int maxRows = intersections[y][x].getLaneSize(2, i);
			
			for (int laneRow = 17; laneRow < 27; laneRow++)
			{
				if (temp.peek() != null && laneRow % 2 == 1)
				{
					currentVehicle = attachSignal(temp.peek().toString(),temp.peek().turnSignal,2);
				}
				else
					currentVehicle = "   ";
				temp.poll();
				line[laneRow] += ("  " + currentVehicle + "  ");
			}
			if (i == 0)
			{
				for (int j = 17; j < 27; j++)
				{
					if (j%2 == 0)
						line[j] += " ";
					else
						line[j] += "|";
				}
			}
		} 
		
	}
	public void drawTransition(int road, int lane, int completion, LaneType direction, String output) //1/9 2/9 /39
	{
		
	}
}

