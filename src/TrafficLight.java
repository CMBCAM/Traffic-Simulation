
public class TrafficLight {
	
	int leftGreen, maxCycleLength;
	Light light;
	
	public TrafficLight()
	{
		leftGreen = Main.defaultLeftGreen;
		maxCycleLength = Main.defaultLightDuration;
	}
	public TrafficLight(int newLeftGreen, int newMax)
	{
		leftGreen = newLeftGreen;
		maxCycleLength = newMax;
	}
	
	public void updateLight(int currentCycle) 
	{
		if (currentCycle < leftGreen)
		{
			light = Light.LEFTGREEN;
		}
		else
		{
			light = Light.GREEN;
		}
	}
	public void setLight(Light setLight)
	{
		light = setLight;
	}
	public String toString()
	{
		return light.toString();
	}
}
