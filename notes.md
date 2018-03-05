# Notes

Summary of important ideas covered each week

# Week 1
## Lecture 1: Union-Find


## Lecture 2: Analysis of Algorithm

#### Observation

- Measure time with Stopwatch()
- Doubling Analysis and Log-log Plot 
    - Measure running time for every x2 increase in input size N
    - Do a log-log plot
    - T(N) = aN^b
    
#### Mathematical Models
- String concact takes constant * length of string
- Simplifying Calculations
    - Use the most expensive basic operation as a guide
        ```java
        int count = 0;
        for (int i = 0; i < N; i++)
         for (int j = i+1; j < N; j++)
         if (a[i] + a[j] == 0) count++;
        ```
        Here, array access will be used(a[i] and a[j] happens N(N-1) times)
    - Tilde(~) notation
        Ignore the lower order. So (1/6)N^3 + 20N + 16 -> ~(1/6)N^3
- Discrete sum can become Calculus to approximate

#### Order of Growth Classifications
- Exponential - combinatorial search, or exhaustive search. Finding all subsets of an array
would be a type of exponential problem
- "Need linear or linearithmic alg to keep pace with Moore's law."

#### Theory of Algorithms
- Best Case = Lower bound = Ω
    - Kind of a thought experiment where **at least** this has to be done. 
    - ex. Running time of the optimal algorithm for solving 3-SUM is Ω(N).
    - explanation: In order to solve 3-SUM in whatever genius way, all the elements have to be touched
    for some sort of comparison. Therefore we know that it must at least be Ω(N). 
    
- Worst Case = Upper bound = O
    - Specific Algorithm will **at least** be the worst case, since it works. 
    - ex. Running time of the optimal algorithm for 3-SUM is O(N2 logN ).
    - explanation: It works. But we don't know if it's optimal.   
    
- Optimal Algorithm = Lower bound ~= Upper bound = Θ 
    - Ex. Brute-force algorithm for 1-SUM is optimal: its running time is Θ(N)
    - With regards to 3-SUM, we don't really know. 

- ! O is not approximate model
    - Do not use O as approximate since it means Upper bound. Use ~ for approximate.
    
#### Memory
|type|bytes|
| ---------- |:----------:|
|boolean|1|
|byte|1|
|char|2|
|int|4|
|float|4|
|long|8|
|double|8|
|char[]|2N + 24|
|int[]|4N + 24|
|double[]| 8N + 24|
|Object| (sum of private variables) + (optional)padding to next multiple of 8 + (optional) Reference to inner class(including array) 8 bytes + 16(overhead)|


# Week 2
## Lecture 3: Stacks and Queues


## Lecture 4: Elementary Sort


# Week 3
## Lecture 5: MergeSort


## Lecture 6: QuickSort