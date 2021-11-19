Mazes, CS351L Project 3 by Marina Seheon, Ruby Ta, and Joseph Barela

* Joseph implemented maze generator algorithms. 
* Ruby worked on the Wall Follower and Lightning solving algorithms. 
* Marina worked on the Tremaux solving algorithm and version control. 
* We all met frequently to help each other with code.


Functionality:
* This program generates mazes in 3 different ways: Randomized Depth First Search, Randomized Kruskal’s, Randomized Prim’s. It also solves the mazes that were generated in 4 different ways: Random Mouse, Wall Follower (using single/multithreading), Tremaux, and Lightning.
* The program will ask you go to take a text file (input file) with the format:
900
10
kruskal
tremaux

Extra Credit: We added an extra solving algorithm and have an astetically pleasing program.

Notes: The program might be lagged if we make it generate/solve the maze too fast. We can adjust this rate by changing the waitTime value in src/MazeSolvers/BaseSolver.java

Known bugs: Wall solver sometimes cannot display the maze properly if you use multithreading.
