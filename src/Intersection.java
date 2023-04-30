import java.util.*;
import java.math.*;
public class Intersection {
	
	double intersectionMuliplier = Main.defaultIntersectionMultiplier;
	Road[] road;
	Vehicle[] output;
	TrafficLight[] light;
	int totalTrafficCycle;
	int currentTrafficCycle;
	boolean light0;
	
	public Intersection() //Default constructor
	{
		light = new TrafficLight[2];
		road = new Road[4];
		output = new Vehicle[8];
		initLights(new TrafficLight(), new TrafficLight());
		initRoads(new Road(), new Road(), new Road(), new Road());
	}
	public Intersection(TrafficLight light1, TrafficLight light2) //custom lights
	{
		light = new TrafficLight[2];
		road = new Road[4];
		output = new Vehicle[8];
		initLights(light1, light2);
		initRoads(new Road(), new Road(), new Road(), new Road());
	}
	
	public void changeMultiplier(double newMultiplier)
	{
		intersectionMuliplier = newMultiplier;
	}
	public void initLights(TrafficLight light1, TrafficLight light2)
	{
		light[0] = light1;
		light[1] = light2;
		light[0].light = Light.LEFTGREEN;
		light[1].light = Light.RED;
		totalTrafficCycle = light[0].maxCycleLength + light[1].maxCycleLength;
		currentTrafficCycle =0;
	}
	
	public void initRoads(Road r1, Road r2, Road r3, Road r4)
	{
		road[0] = r1;
		road[1] = r2;
		road[2] = r3;
		road[3] = r4;
	}
	
	public void updateLights(int cycle)
	{
		//the current traffic cycle
		currentTrafficCycle ++;
		if(currentTrafficCycle > totalTrafficCycle)
			currentTrafficCycle = 0;
		if (currentTrafficCycle < light[0].maxCycleLength)
		{
			if (light0 == false && checkRoadEmpty(0))
			{
				currentTrafficCycle = totalTrafficCycle - 5;
				light[0].setLight(Light.RED);
				light[1].updateLight(currentTrafficCycle - light[0].maxCycleLength);
				light0 = false;
			}
			else
			{
				light[0].updateLight(currentTrafficCycle);
				light[1].setLight(Light.RED);
				light0 = true;
			}
		}
		else
		{
			if (light0 == true && checkRoadEmpty(1))
			{
				currentTrafficCycle -=5;
				light[0].updateLight(currentTrafficCycle);
				light[1].setLight(Light.RED);
				light0 = true;
			}
			else
			{
				light[0].setLight(Light.RED);
				light[1].updateLight(currentTrafficCycle - light[0].maxCycleLength);
				light0 = false;
			}
		}
	}
	
	public boolean checkRoadEmpty(int odd)
	{
		for (int k = 0; k < 2; k+=2)
			for (int i = 0; i < 2; i++)
			{
				if (road[k+odd].checkLane(i) == true)
					return false;
			}
		return true;
	}
	/*
	 * intersectionCheck
	 * Checks each roads incoming lanes front 
	 * vehicles direction and compares it to 
	 * current intersections vehicles to set 
	 * up if that vehicle can enter
	 */
	public void intersectionCheck()
	{
		//check each roads lane
		for (int i = 0; i < 4; i++) 
		{
			Light currentLight = light[i % 2].light; //gets the current light
			for (int j = 0; j < road[i].road.size(); j++)
			{
				if(road[i].checkLane(j)) //check if lane is empty
				{
					switch (road[i].road.get(j).lane.peek().turnSignal)
					{
						case LEFT:
							switch (currentLight)
							{
								case LEFTGREEN: //auto success
									road[i].road.get(j).lane.peek().canEnter();
									break;
								case GREEN:
									road[i].road.get(j).checkIntersection(
											road[(i + 2) % 4].getIntersectionTurnSignals()); //opposite road
									break;
								default:
									road[i].road.get(j).lane.peek().cantEnter();
									break;
							}
							break;
						case FORWARD:
								if (currentLight == Light.GREEN)
									road[i].road.get(j).lane.peek().canEnter();
								else
									road[i].road.get(j).lane.peek().cantEnter();
							break;
						case RIGHT:
							switch (currentLight)
							{
								case RED:
										//check if adjacent lane is not forward
									if (road[((i + 1) % 4)].road.get(road[((i + 1) % 4)].road.size() - 1).lane.size() != 0) //check if that lane is empty
									{
										if (road[((i + 1) % 4)].road.get(road[((i + 1) % 4)].road.size() - 1) //the right most lane to the left of this intersection
												.lane.peek().turnSignal != LaneType.FORWARD) //the adjacent lane
										{
											road[i].road.get(j).lane.peek().cantEnter();
										}
									}
								default:
									road[i].road.get(j).lane.peek().canEnter();
									break;
							}
							break;
						default:
							break;
					}
					if(road[i].road.get(j).lane.peek().canEnter == false) //checks if accelerate or not
					{
						road[i].road.get(j).stopAcceleration();
					}
				}
			}
		}
			
		
	}
	/*
	 * update all vehicles speeds and accelerations inside the intersection
	 */
	public void exitIntersection() //empties out current vehicles in intersection and sends them to output Vehicle
	{
		accelerateIntersections();
		for (int i = 0; i < 8; i++)
		{
			int r = i / 2; //the correct road
			int l = i % 2; //the correct lane
			if(road[r].inIntersection(l))
			{
				if ( road[r].road.get(l).intersection.completetion >= 1)
				{
					Vehicle temp = road[r].exitIntersection(l);
					int location = assignOutput(temp.turnSignal, r, l);
					output[location] = temp;
				}
				else
				{
					road[r].road.get(l).intersection.move();
				}
			}
		}
	}
	public void accelerateIntersections()
	{
		for (int i = 0; i < 8; i++)
		{
			int r = i / 2; //the correct road
			int l = i % 2; //the correct lane
			if(road[r].inIntersection(l))
			{
				road[r].road.get(l).intersection.accelerate();
				road[r].road.get(l).accelerateLane();
			}
		}
	}
	public int assignOutput(LaneType turnSignal, int road, int lane)
	{
		int currentRoad = (road*2 + lane);
		switch (turnSignal)
		{
		case LEFT:
			return (currentRoad + 2) % 8;
		case FORWARD:
			return (currentRoad + (4)) % 8;
		case RIGHT:
			currentRoad -= 2;
			return (Math.floorMod(currentRoad, 8));
		default:
			return 0;
		}
	}
	public void enterIntersection()
	{
		for (int i = 0; i < 4; i++)
			road[i].enterIntersection();
	}
	public Vehicle receiveVehicle(int outputIndex) //vehicle output
	{
		Vehicle temp = output[outputIndex];
		output[outputIndex] = null;
		return temp;
	}
	public void spawnVehicles(double gMultiplier) //in all 4 roads
	{
		for (int i = 0; i < 4; i++)
			road[i].spawnVehicles(gMultiplier, intersectionMuliplier);
	}
	public String toString()
	{
		String roadString;
		//road
		roadString = ("\nRoad 0: " + road[0] + "\nRoad 1: " + road[1] + "\nRoad 2: " + road[2] + 
				"\nRoad 3: " + road[3]);
		//lights
		String lightString = ("Light 1: " + light[0] + "\nLight 2: " + light[1]);
		return (lightString + roadString);
	}
	public Queue<Vehicle> getLane (int roadIndex, int laneIndex)
	{
		return road[roadIndex].road.get(laneIndex).lane;
	}
	public int getLaneSize (int roadIndex, int laneIndex)
	{
		return road[roadIndex].road.get(laneIndex).lane.size();
	}
	public int getRoadSize (int roadIndex)
	{
		return road[roadIndex].road.size();
	}
	public Vehicle getIntersectionSpace (int roadIndex, int laneIndex)
	{
		return road[roadIndex].road.get(laneIndex).intersection;
	}
}
