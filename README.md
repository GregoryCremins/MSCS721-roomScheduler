# RoomScheduler
Simple console menu driven room scheduler framework.

The room scheduler is a piece of software to emulate a system which manages classrooms and meeting scheduling. It was intentionally created very buggy and poorly designed to demonstrate the process behind testing  and maintenance for code.

How to use:

Run the main function found in room scheduler.

Main menu options:

1) Add a room- this will add a room to the current list of rooms. The following prompts will appear
	a) The name of the room must be entered. Input a string for the name of the room.
	b) The capacity for the room must be entered. Enter a valid integer for a rooom capacity
2) Remove a room- this will remove a room from the current list of rooms. The following prompts will appear
	a) The room which you need to remove. Enter a valid string for room removal.
3) Schedule a room- This will schedule a meeting for a room for a given time The following prompts will appear
	a) The name of the room where you want the meeting to take place. Enter a valid name of a room.
	b) The start date of the meeting. Insert a valid date in the correct format: yyyy-dd-mm
	c) The start time of the meeting. Insert a valid time in the correct format: hh:mm
	d) The end date of the meeting. Insert a valid date in the correct format: yyyy-dd-mm
	e) The end time of the meeting. Insert a valid time in the correct format: hh:mm
4) List schedule- list the schedule of a room. The following prompts will appear
	a) The name of the room to query. Input the name of a valid room in string form.
5) List rooms- This will list all of the rooms in the current room list.
6) Export Rooms to a JSON file- this will export the current room list to a JSON file. The following prompts will appear
	a) The filename- It will ask you to specify the full filename of the file to export to
7) Import Rooms from a JSON file - this will import a roomlist from a file. THIS WILL OVERWRITE THE CURRENT ROOMLIST. The following prompts will appear:
	a) The filename- It will ask you to specify the full filename of the file to import from