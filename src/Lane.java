import java.util.*;
import java.math.*;

public class Lane {
	
	Queue<Vehicle> lane;
	Vehicle intersection = null;
	LaneType laneType;
	double spawnRate = Main.defaultSpawnRate;
	double effectiveSpawnRate;
	
	public Lane()
	{
		laneType = LaneType.FORWARD;
		effectiveSpawnRate = spawnRate;
		lane = new LinkedList<Vehicle>();
	}
	public Lane(LaneType newLaneType, double newSpawnRate) //this lane creation is technically default, the other one is if I don't specify anything
	{
		laneType = newLaneType;
		spawnRate = newSpawnRate;
		lane = new LinkedList<Vehicle>();
	}
	
	public Vehicle getIntersectionVehicle()
	{
		return intersection;
	}
	public void spawnVehicle(double gMultiplier, double iMultiplier)
	{
		effectiveSpawnRate = spawnRate * iMultiplier * gMultiplier; //the adjusted spawn rate after modifiers
		if (Math.random() < effectiveSpawnRate)
		{
			lane.add(new Vehicle(laneType));
			Main.vehicleID++;
			//System.out.println("Spawn!");
		}
		//else
			//System.out.println("Did not spawn!");
	}
	
	public Vehicle exitIntersection()
	{
		Vehicle temp = intersection;
		intersection = null;
		return temp;
	}
	public void enterIntersection()
	{
		if (intersection == null)
		{
			if (lane.size () != 0)
			{
				if(lane.peek().canEnter)
					intersection = lane.poll();
			}
		}
	}
	public void checkIntersection(Queue<LaneType> turnSignals)
	{
		lane.peek().canEnter();
		for (int i = 0; i < turnSignals.size(); i++)
		{
			//checks lane first vehicle to see if oncoming vehicles are forward
			//the reason its in vehicle class is if I want to add other situations or change the current one
			if (lane.peek().checkOncomingVehicle(turnSignals.poll()) == false) 
			{
				lane.peek().cantEnter(); //sets to false if any lane gets flagged
				break;
			}
		}
	}
	public void stopAcceleration()
	{
		Queue<Vehicle> temp = new LinkedList<Vehicle>(lane);
		//return Integer.toString(lane.size()); //the size
		for (int i = 0; i < lane.size(); i++)
		{
			Vehicle tempVehicle = temp.poll();
			tempVehicle.speed = 0;
			lane.add(tempVehicle);
			lane.poll();
		}
	}
	public void accelerateLane()
	{
		Queue<Vehicle> temp = new LinkedList<Vehicle>(lane);
		//return Integer.toString(lane.size()); //the size
		for (int i = 0; i < lane.size(); i++)
		{
			Vehicle tempVehicle = temp.poll();
			tempVehicle.accelerate();
			lane.add(tempVehicle);
			lane.poll();
		}
	}
	public String toString()
	{
		String output = "";
		Queue<Vehicle> temp = new LinkedList<Vehicle>(lane);
		//return Integer.toString(lane.size()); //the size
		for (int i = 0; i < lane.size(); i++)
		{
			output += temp.poll() + "; "; 
		}
		return "\n   " +output;
	}
}
