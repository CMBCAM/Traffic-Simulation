import java.time.*;
import java.io.*;
import java.lang.*;
public class Main {

	/*
	 * CONTROL SIM SPEED
	 */
	static int simSpeed = 100; //milliseconds tick rate !!WARNING!! Acceleration is implemented in that each tick is 1/10 of a cycle, changing may result in graphical issues.
	static int cycleSpeed = 1000 / simSpeed; // 1 second
	
	static int simCycle;
	static int prevCycle;
	static int currentTick = 0;
	static int vehicleID;
	static long previousCycleTime = System.currentTimeMillis();
	/*
	 * FORMULA FOR SPAWN CHANCE
	 * spawnChance * intersectionMultiplier * globalMultiplier
	 * spawnChance is defaulted to 0.1 for both the road and thus also the lanes it creates, although both can be manually modified
	 */
	static double globalMultiplier = 1; //the global multiplier, time of day etc.
	static double defaultIntersectionMultiplier = 1; //the standard intersection multiplier, can be modified
	static double defaultSpawnRate = 0.1; //every cycle each lane has this chance to spawn a vehicle
	static double turnChance = 0.5; //when spawning on a left or right lane, how likely it is to go left or right instead of forward
	static int defaultLeftGreen = 7; //the default left turn green on a light
	static int defaultLightDuration = 30; //the default green light duration;
	static double defaultAcceleration = 0.1;
	static double maxSpeed = 0.4;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * Create an intersection object that itself holds 4 roads objects, those roads hold x lanes objects for outgoing and x lanes objects for incoming for the intersection. 
		 * First lane for each type is defaulted to the left lane, last lane is defaulted to right The lane class holds a queue that holds vehicles. Vehicles have a destination if they will turn or not, which is randomized.  
		 * Traffic lights are also included in an intersection and rotate on a 30 second red, 15 second green left 15 second full green. 	
		 * The lanes also have an intersection vehicle which determines how other vehicles act
		 */
		TrafficLight light1 = new TrafficLight();
		TrafficLight light2 = new TrafficLight();
		
		Intersection intersection = new Intersection(light1, light2);
		Graphics graphic = new Graphics(1,1);
		graphic.addIntersection(intersection, 0, 0);
		
		while(true)
		{
			if (simCycle != prevCycle)
				updateLights(intersection);
			
			intersectionCheck(intersection);
			updateIntersections(intersection);
			if (simCycle != prevCycle)
				updateSpawns(intersection);
			//System.out.println("Total Cycles: " + simCycle + " \nCurrent Traffic Cycle: " + (simCycle % intersection.totalTrafficCycle));
			//System.out.println(intersection);
			clearConsole();
			
			graphic.drawIntersection();
			prevCycle = simCycle;
			updateTime();
			
		}
		
	}
	
	public static void updateTime()
	{
		while (System.currentTimeMillis() < previousCycleTime + simSpeed)
		{
		}
		currentTick++;
		if (currentTick % cycleSpeed ==0)
			simCycle++;
		previousCycleTime = System.currentTimeMillis();
	}
	
	public static void updateLights(Intersection intersection)
	{
		intersection.updateLights(simCycle);
	}
	
	public static void updateIntersections (Intersection intersection)
	{
		//if (simCycle != prevCycle)
		//{
			for (int i = 0; i < 8; i++)
			{
				intersection.receiveVehicle(i);
			}
		//}
			intersection.exitIntersection();
		//if (simCycle != prevCycle)
			intersection.enterIntersection();
	}
	public static void updateSpawns(Intersection intersection)
	{
		intersection.spawnVehicles(globalMultiplier);
	}
	
	public static void intersectionCheck(Intersection intersection)
	{
		intersection.intersectionCheck();
	}
	public static void clearConsole() {
		for (int i = 0; i < 30; i++)
			System.out.println();
	}

}
