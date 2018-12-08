# The HiRiQ Board Problem

Given any random starting board configuration and two possible moves for each game piece, generate the set of moves required to 
take the starting board state to the final solved board configuration. The final board configuration is denoted by a board 
where all pieces except the center most piece are black, with the central piece being white. 

For more information: [Project Descripton](http://crypto.cs.mcgill.ca/~crepeau/COMP250/HW4.pdf)

# Implementation 

The implementation found in `HiRiQSolver.java` uses the A* path-finding algorithm. In this algorithm, we first scan the given board
state and generate all possible moves that can be taken. These moves are then passed into a priority queue and sorted in 
ascending order (lowest distance at the head of the queue) based on how close
they move the board to the final solved board state. Each board state has a unique integer value which indicates which pegs on the
board are currently white. The final board state has the value 2x<sup>15</sup> = 32768. 

The distance metric used to to sort the potential moves is dist = abs(d(x) - 32768) 
where d(x) denotes the total integer value of the current board state after applying move x.

At each iteration, the most optimal move is applied to the current board state, a new set of moves is generated and 
sorted using the distance function above. This 
is repeated until the solution state is found or the algorithm encounters a dead end. In the case of a dead end, the algorithm tries the 
second most optimal move from the previous iteration and continues until all the states have been explored. 
