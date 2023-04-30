# Traffic-Simulation
Simulate Traffic at an Intersection
Create an intersection object that itself holds 4 roads objects, those roads hold x lanes objects for outgoing and x lanes objects for incoming for the intersection. 
First lane for each type is defaulted to the left lane, last lane is defaulted to right The lane class holds a queue that holds vehicles. Vehicles have a destination if they will turn or not, which is randomized.  
Traffic lights are also included in an intersection and rotate on a 30 second red, 15 second green left 15 second full green. 	 
The lanes also have an intersection vehicle which determines how other vehicles act

Part 2
Vehicles now have speeds at which they can cross through an intersection
Speeds are in terms of how many vehicles can cross an intersection
Base speed is 0 acceleration is default 0.1, max 0.4 per tick takes 1.0 to make a full cross, 
Speed increases each tick by 0.1
Vehicle speeds reset once a vehicle does not move through the intersection
Cycles
The main program will run on cycles, each cycle being 1 second . Each cycles actions can be divided into phases although some advance on each tick, which is 1/10 of a second:
Update Lights: (cycles)
The lights update based on the current cycle.Lights will pause if there are no vehicles in the lane which is turning green. Once a vehicle is in the lane a 5 second delay occurs before swapping cycles
Intersection Check: (ticks)
During each cycle, each lane will update their vehicles (refer to traffic light rules)
Exit Outgoing: (ticks)
Vehicles exit outgoing lanes
Exit Intersection: (ticks)
Vehicles inside the intersection will update position in intersection or exit  into outgoing lanes. A vehicle will not move if the space in front of them is occupied
Enter Intersection: (ticks)
If the conditions are fulfilled for each intersection check vehicle, that vehicle will enter the intersection adjacent to their lane, otherwise they will stay in their lane.
Spawn New Vehicles: (cycles)
Each lane will follow its spawn rules, potentially adding a new vehicle to its lane. (refer to vehicle spawning for more information)

Traffic Light Rules
Red Lights
-	Cannot turn left
-	Cannot move forward
-	Can turn right if intersection in front of vehicle is not occupied
Left Green Lights
-	Can turn left
-	Cannot move forward
-	Can turn right if intersection in front of vehicle is not occupied (case for single lane roads) 
Green Lights
-	Can turn left if both opposite intersection lanes do not have a vehicle going forward
-	Can move forward
-	Can turn right

Vehicle Spawning
During each vehicle spawn phase, every lane will be given a chance to spawn a new vehicle in the incoming lanes. This chance will be based on both the lane spawn chance, as well as a multiplier for the intersection itself to simulate how busy that road is in addition to the global multiplier for different times of day. Each lane will default to the normal road spawn chance, but it can also be set manually per lane. The basic formula is as follows: 

spawnChance x intersectionMultiplier x globalMultiplier

Once a lane has decided to spawn a vehicle, it will determine its destination based on its lane (50/50 for now). See LaneType for more information.

Main 
-	global multiplier
-	Defaults variables for vehicle
-	Global ID (0 - 99)
-	updateIntersections
-	This will call the phases starting for each intersection.
Intersection 
-	Intersection multiplier
-	Array of roads
-	Start 0 from north and going clockwise increase  >
-	2 traffic lights
-	Will determine its max cycle length its left turn end point and green end point
-	First light is for even roads (0, 2) second light is for odd roads (1,3) (blue/orange)
-	Total traffic cycle
-	Takes sum of both traffic lights
-	updateLights(int cycle)
-	Takes cycle mod the totalTrafficCycle determines which light it goes to by taking first lights max cycle and checking if its greater than
-	If it is, send the result - first max cycle to second traffic light (which changes based on that input
-	Else, it sends  the result to first light
-	intersectionCheck
-	Checks each roads incoming lanes front vehicles direction and compares it to current intersections vehicles to set up if that vehicle can enter
-	Checks current light
-	Updates each vehicle at front of lane to if they can enter intersection or not
-	Loops through each road and their lanes. 
-	Get the vehicle at the front of lane’s queue and determine if they can enter that lane’s “intersection”
-	/*
-	 * types of checks:
-	 * Left
-	 * 	green left
-	 * 		true
-	* 	green
-	 * 		check opposite lane
-	 * 			false if any opposite are forward
 * 			true otherwise
-	 * 	red
-	 * 		false automatically
-	* Forward
-	 * 	green
 * 		true
-	 * 	red
-	 * 		false
-	 * Right
-	 * 	green / left green
-	 * 		true
-	 * 	red
-	 * 		check adjacent lane
 * 		false if forward
-	 * 		true otherwise
-	 * In conclusion it doesnt matter if turnsignal or light is checked first
-	*/
-	For checking left lanes, it checks all road + 2 mod 4 (opposite lanes) to see if any vehicle is going forward in that intersection. If so, it cannot enter
-	For right turn on red lights check the most right lane incoming on the intersection to its lane (aka the last index in the lane array) this road is (road - 1) % 4
-	If the front vehicle cannot go, it sets all vehicles speeds to 0 in the lane to simulate stopping
-	exitIntersection
-	Removes the vehicles from the intersection (will add to other intersections in the future) 
-	enterIntersection 
-	Removes vehicles that can enter intersection from their lane and adds them to the intersection vehicle
-	
-	accelerateIntersections
-	Increases the speed of all vehicles in lane
-	spawnVehicles - see vehicle spawning 
Road
-	Holds incoming lanes list, default 2 with one left lane and one right lane
-	Can assign lane type manually, all lance by default can go forward as well (can remove this for right run / left turn only lanes)
-	Road spawn rate 
-	TYPES OF LANES
-	Left only
-	Left
-	Forward only
-	Right
-	Right only
Lane
-	Holds queue based number of vehicle
-	Only display x amount on screen, but can hold infinite atm
-	spawn rate which inherits road spawn rate 
-	Lane intersection vehicle as well
Vehicle
-	Direction destination (right left forward)
-	When spawned, will determine direction based on current lane spawned in
-	Unique ID
-	Boolean to check if it can enter intersection
-	Checks for if the intersection vehicle allows for this vehicle to proceed with its action
-	Speed
-	Accelerate
-	Increases speed by acceleration
-	Acceleration (default 0.1)
-	Completion
-	Double 
-	How close it is to exiting intersection

Traffic light
-	tracks cycles
-	Integer for when left green ends
-	Integer for when light turns red
-	Method to check which light it should be based on current cycle
-	Does not switch if there are no vehicles in opposite lane
-	Delayed by 5 seconds if its the first vehicle to the intersection
LaneType (enum)
-	Determines lane as well as turn signals (left and right will be used for double lanes like this sim)
-	LEFTONLY
-	Can only turn left
-	LEFT
-	Can only turn left and forward
-	FORWARD
-	Can only go forward
-	RIGHT
-	Can only turn right and forward
-	RIGHTONLY
-	Can only go right
Light (enum)
-	Types of lights
-	LEFTGREEN
-	Only turn left (technically right too)
-	GREEN
-	Can go
-	RED
-	Cannot go
Graphics
-	2d array of intersections
-	Draws each intersection and inserts all data into correct locations
-	Generate lane vehicles (queue<vehicle>, road)
-	Polls the first 5-6 vehicles adds prefix/postfixes to them for their turn signal and returns a string array with them
-	Generate intersection vehicle (vehicle, road)
-	Takes that vehicle and adds prefixes/postfixes 
-	Returns string array with all intersections
-	Most of the graphics have been redesigned, each specific location a vehicle can appear on the board is now its one variable in a string array which uses sets of loops and logic to determine where each vehicle will appear. The board wipes its self before adding new vehicles to create animation
Instructions
-	Contains each set of coordinates for each vehicles destination animation
-	10 xs then 10 ys for string input to auto fill input
-	 
-	leftTurns.add(new Instructions("22222234561234566666"));
-	leftTurns.add(new Instructions(“65544322222222224568”));
-	leftTurns.add(new Instructions("55555543217654322222"));

4/20/2022
-	Finished sim cycle to update every 1000ms 
-	Finished traffic light cycle which rotates between lights based on current cycle
4/25/2022 - 10:50am
-	Finished vehicle spawns with correct turnsignal in lanes as well as right road
-	Have not added modifiers yet
-	 default is 10% chance each lane
-	50/50 for direction depending on lane
-	Left off on turn signal and intersection logic for enter intersection RIGHT
-	See intersection, road, and lane
4/25/2022 - 8:00pm 
-	Finished intersection logic - all vehicles now correctly know if they can enter to lane or not based on the current intersection status as well as the current traffic light
-	Finished exit intersection, making lanes exit vehicles in their intersection reference, as well as giving a potential if needed
-	Finished adding vehicles into the intersection if they can

-	Assigned each vehicle an ID upon creation, currently set a maximum to 100 ids, and will loop once its reached
-	All spawn rates now reference a static variable created in main, multipliers now correctly apply to vehicle spawning
-	Created a simple info page showing all cars exiting and entering
-	Started working on graphics, the roads have been created as well as cycle counter and traffic cycle counter
-	Need to display vehicles, their turnsignal in relation to current road as well as ID spaces on road as well as current light and which light.

4/27/2022 - 10:30am
-	Half way through graphics
-	Also thinking of adding output when intersection is exited so that can display the location the vehicle reaches
4/27/2022 - 12:20pm
-	Redesigning whole graphics class to make more consistent and legible
-	With new design, might be able to animate cars traveling to output
-	
-	
May 2nd 2022
-	The Problem
-	Old system does not allow for interval based updates on the vehicles
-	Hard coded
-	Proposal:
-	Add a coordinate system to the roads, perhaps even remove queues?
-	Vehicle lanes will be determined via coordinates I.E destination could be coord based or lane based with coord and have pathfinding to the coords thus create an array list, and ID can be tied to array?
-	No more vehicle queues, array list for vehicles is tied to intersection perhaps? References to vehicles with array list in lanes and roads for keeping track
-	Changing this to making vehicle intersections to be arraylists (kept the same)which vehicles having percentage or coordinates of completion. Allows for acceleration to determine speed thus allowing animation. 
-	*new
-	All vehicles in the lane will accelerate if the front vehicle has not been determined to be unable to move,
-	Why
-	The coordinate system will allow for seamless acceleration and speed for vehicles
-	Allows implementation of own vehicle if you want to control lol
-	Graphics implementation
-	Each ascii location can be tied to a segment of coords, mod 20 or something so that it doesn't move so fast across screen
May 12, 2022
-	For lights switching only when occupied, have a check before swapping lights which delays the change until then, need to change how traffic cycle sim increments to be separate. 
-	Perhaps change trafifcs into phases instead of a hardcoded mod for better visibility
May 15 2022
-	Vehicles now have acceleration and speed
-	Acceleration method to increase speed
-	Speed gets reset if the front vehicle in lane cannot go
-	All vehicles accelerate in a lane
-	The vehicles updating and graphics are off the sim cycle and now update more frequently while still keeping the sim working.
-	TODO
-	Work on graphics, rehaul cause it sucks
-	
May 16 2022
-	Animate!
-	Write the strings for each case 
-	Removed the temporary characters spread throughout the road
-	TODO
-	Finish instructions for all cases
-	Implement instructions into intersection drawings
!
