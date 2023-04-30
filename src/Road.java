import java.util.*;
public class Road {
	
	ArrayList<Lane> road;
	double spawnRate = Main.defaultSpawnRate;
	public Road() //lowest default left, highest default right
	{
		road = new ArrayList<Lane>();
		road.add(new Lane(LaneType.LEFT, spawnRate));
		road.add(new Lane(LaneType.RIGHT, spawnRate));
	}
	
	public void addLane(int index, LaneType newLaneType)
	{
		road.add(index, new Lane(newLaneType, spawnRate));
	}
	
	public void addLane(int index, LaneType newLaneType, double newSpawnRate)
	{
		road.add(index, new Lane(newLaneType, newSpawnRate));
	}
	
	public void changeLaneSpawnRate(int index, double newSpawnRate)
	{
		road.get(index).spawnRate = newSpawnRate;
	}
	
	public void changeAllSpawnRate(double newSpawnRate)
	{
		for (int i = 0; i < road.size(); i++)
		{
			road.get(i).spawnRate = newSpawnRate;
		}
	}
	public Queue<LaneType> getIntersectionTurnSignals() //grabs all lane intersection vehicle turnsignals
	{
		Queue<LaneType> intersectionVehicles = new LinkedList<LaneType>();
		Vehicle currentVehicle;
		for (int i = 0; i < road.size(); i++)
		{
			currentVehicle = road.get(i).getIntersectionVehicle();
			if (currentVehicle != null)
				intersectionVehicles.add(currentVehicle.turnSignal);
		}
		return intersectionVehicles;
	}
	
	public void checkIntersection(Queue<LaneType> turnSignals, int laneIndex)
	{
		road.get(laneIndex).lane.peek().canEnter();
		for (int i = 0; i < turnSignals.size(); i++)
		{
			//checks lane first vehicle to see if oncoming vehicles are forward
			//the reason its in vehicle class is if I want to add other situations or change the current one
			if (road.get(laneIndex).lane.peek().checkOncomingVehicle(turnSignals.poll()) == false) 
			{
				road.get(laneIndex).lane.peek().cantEnter(); //sets to false if any lane gets flagged
			}
		}
	}
	
	public Vehicle exitIntersection(int laneIndex)
	{
			return road.get(laneIndex).exitIntersection();
			//System.out.println(road.get(i).exitIntersection() + " exits!");
	}
	public void enterIntersection()
	{
		for (int i = 0; i < road.size(); i++)
		{
			road.get(i).enterIntersection();
		}
	}
	public void spawnVehicles(double gMultiplier, double iMultiplier)
	{
		for (int i = 0; i < road.size(); i++)
			road.get(i).spawnVehicle(gMultiplier, iMultiplier);
	}
	
	public boolean checkLane(int index)
	{
		if (road.get(index).lane.size() == 0)
			return false;
		else
			return true;
	}
	public boolean inIntersection(int laneIndex)
	{
		if (road.get(laneIndex).intersection ==null)
		{
			return false;
		}
		return true;
	}
	public String toString()
	{
		String output ="";
		for (int i = 0; i < road.size(); i ++)
		{
			output += ("\n " + road.get(i).laneType.toString() + " Intersection " + road.get(i).intersection + road.get(i));
		}
		String size = ("Size=" + road.size());
		return (output);
	}
}
