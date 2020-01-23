# Assignment 3
The project represnts a game with 24 stages in which you need to collect the most points(represted by fruits) in a weighted directed graph using
given robots.

which can be solved an automatic algorhim that we implemnted, or manualy using mouse clicks.

we created a GUI (with the help of StdDraw) to represnt visually above game. in it you can choose a stage and in what mode to solve it 
(manual/automatic).


![](https://user-images.githubusercontent.com/37403170/72686488-1a907380-3afe-11ea-968d-7d659edfe8fa.png)


## Modes
- Manual : using double mouse click you can choose a location for your given robts, 
and again by double mouse clicking on a target node next to a deired robot(only on neighboring nodes) 

- Automatic : we choose the loaction of each robot and where will it go we attmpted a greedy aproach which will hopefully will be optimal.
and will collect the most fruits.

furthermore there is an option to save a run manual or automatic to kml format whitch can be displayed in google earth animation, 
all the stages are based on ariel university.

## main classes
### data structes:
- DGraph
- Robot
- Fruit
- Arena
### gui :
Graph_GUI
MyGameGUI
### algorhtims:
Graph_Algo
Game_Algo

# Assigment 4 addtions
## resultFetcher
responsible for DB data fetching

added better game algorthims for gameAlgo

NOTE ~! game might look laggy at times but it is intentional in order to minimize moves usage we update the server move call only when nesscery.
