# RaodBumpPlotter

This application is a part of an Android assignment, and should fulfill the minimum requirements - Where as many standard
android features should be present, and two advanced features.

The applications in it's simpelst form is made to plot road bumps onto the google maps. The user stores all bumps that is met on 
the trip if the application is set to follow the user.

The application makes use of google maps, where the user is plotted as a bicycle and when the user starts following it's own
movement, the gps location is tracked and the movement is tracked with the accelerometer in the phone. For each spike in the 
accelerometer a entry in the database is created, an that entry will be retrieved when the user clicks on the button "See Bumps".
All bumps in the database is then plotted onto the google map.
