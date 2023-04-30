public class Vehicle {
	
	LaneType turnSignal, currentLane;
	double turnChance = Main.turnChance;
	boolean canEnter = false;
	int id;
	double speed = 0;
	double acceleration = Main.defaultAcceleration;
	double completetion = 0;
	//double speed;
	public Vehicle()
	{
		turnSignal = LaneType.FORWARD;
		id = Main.vehicleID % 100;
	}
	public Vehicle(LaneType newCurrentLane)
	{
		currentLane = newCurrentLane;
		randomLane();
		id = Main.vehicleID % 100;
	}
	public void accelerate()
	{
		if (speed < 0.4)
			speed += acceleration;
	}
	public void move()
	{
		completetion += speed;
	}
	public void randomLane()
	{
		switch (currentLane)
		{
			case LEFTONLY:
				turnSignal = LaneType.LEFT;
				break;
			case LEFT:
				if(Math.random() < turnChance)
					turnSignal = LaneType.LEFT;
				else
					turnSignal = LaneType.FORWARD;
				break;
			case FORWARD:
					turnSignal = LaneType.FORWARD;
					break;
			case RIGHT:
				if(Math.random() < turnChance)
					turnSignal = LaneType.RIGHT;
				else
					turnSignal = LaneType.FORWARD;
				break;
			case RIGHTONLY:
				turnSignal = LaneType.RIGHT;
				break;
		}
	}
	public void canEnter()
	{
		canEnter = true;
	}
	public void cantEnter()
	{
		canEnter = false;
	}
	//this will check for things such as left turn safety and right turn on red lights
	public boolean checkOncomingVehicle(LaneType oncomingVehicle)
	{
		if (oncomingVehicle == LaneType.FORWARD)
			return false;
		else
			return true;
	}
	
	public String toString()
	{
		String printID;
		if (Integer.toString(id).length() == 1)
			printID = "0" + id; //adds 0 if single digit
		else
			printID = "" + id;
		return printID;
	}
}
