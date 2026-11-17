# Algorithm Explorer

**CSC2107: Design and Analysis of Algorithms**  
**Uganda Christian University**  
**Advent Semester 2026**

Algorithm Explorer is an interactive Java Swing application for learning recursion, fundamental data structures, sorting algorithms, and experimental time-complexity analysis. Users provide their own input, run the selected algorithm or operation, and view the result, intermediate steps, operation counts, and execution time.

## Project Objectives

The application was developed to:

- demonstrate recursive algorithms and compare naive recursion with more efficient approaches;
- implement stacks, linear and circular queues, and singly and doubly linked lists from first principles;
- implement and visualize five required sorting algorithms;
- include first-element yellow/green Quick Sort and an additional randomized-pivot option;
- measure comparisons, swaps, moves, calls, iterations, and execution time;
- compare experimental measurements with theoretical Big-O complexity;
- provide one integrated and usable graphical application.

## Technologies Used

- **Language:** Java 21
- **Interface:** Java Swing
- **Build tool:** Apache Maven
- **Testing:** JUnit 5
- **Version control:** Git and GitHub
- **Development environment:** Visual Studio Code

## Main Modules

### Module A: Recursion Lab

#### Fibonacci

Computes the nth Fibonacci number using:

- naive recursion;
- memoized recursion;
- bottom-up dynamic programming.

For each method, the application displays the result, function calls or loop iterations, and execution time. Naive recursion is limited to a safe input size.

#### Factorial

Computes factorial using:

- recursive factorial;
- iterative factorial.

The implementation supports `0! = 1`, rejects negative input, and displays calls or iterations and execution time.

#### Tower of Hanoi

- recursively generates every move;
- supports 1 to 8 disks;
- allows source, auxiliary, and destination peg selection;
- animates disk movement;
- includes Start, Pause, Step, Reset, and speed controls;
- displays a running move counter;
- confirms that the total number of moves equals `2^n - 1`.

### Module B: Data Structures Lab

All data structures are implemented without ready-made stack, queue, or linked-list classes.

#### Array-Based Stack

Supports:

- push;
- pop;
- peek;
- size;
- is empty;
- is full;
- display;
- clear;
- editable fixed capacity.

Overflow and underflow produce clear messages.

#### Balanced-Bracket Checker

Uses the custom stack to check expressions containing `()`, `[]`, and `{}`. The stack state and explanatory action are displayed after each symbol.

#### Linear Queue

Supports enqueue, dequeue, front, size, is empty, is full, display, clear, and editable capacity. The interface demonstrates the false-full problem by showing unused positions before the front index that cannot be reused.

#### Circular Queue

Supports the same queue operations while allowing the rear index to wrap around and reuse freed array positions. Front and rear indices are displayed in the interface.

#### Singly Linked List

Supports insertion and deletion at the beginning and end, before and after a specified element, deletion of a specified element, search, size, and forward display.

#### Doubly Linked List

Supports all required insertion, deletion, search, size, forward-display, and backward-display operations using custom nodes with `next` and `previous` references.

### Module C: Sorting Lab

The Sorting Lab implements:

- Bubble Sort;
- Selection Sort;
- Insertion Sort;
- Merge Sort;
- First-Pivot Quick Sort using the class yellow/green partition;
- Randomized-Pivot Quick Sort as an additional option.

The interface provides:

- typed or randomly generated input;
- input validation;
- array state after every pass, merge, or partition;
- highlighted active elements;
- Step and Auto-play controls;
- Pause, Reset, and adjustable playback speed;
- comparisons, swaps, moves, and execution time;
- a Compare All table for the five required sorting algorithms;
- best-, average-, and worst-case complexity;
- extra-space complexity;
- stability information.

### Module D: Time Complexity Analysis

Module D uses dedicated benchmark implementations that avoid visualization snapshots so large-input measurements are not distorted by display overhead.

Experiments use:

- random input;
- already sorted input;
- reverse-sorted input;
- at least three repetitions per configuration.

Standard sizes:

`100, 500, 1,000, 2,000, 5,000, 10,000`

Additional Merge Sort and Quick Sort sizes:

`50,000, 100,000`

The module displays:

- average, minimum, and maximum execution time;
- average comparisons, swaps, and moves;
- completed and skipped configurations;
- progress and cancellation controls;
- a chart for the quadratic sorting algorithms;
- a chart for Merge Sort and Quick Sort;
- a discussion guide for relating results to theoretical complexity.

Large ordered first-pivot Quick Sort configurations are skipped by default because they perform quadratic work. They can be enabled explicitly for extended worst-case experiments.

## System Architecture

Algorithm Explorer uses a layered design:

```text
Swing User Interface
        |
        v
Controllers and Input Validation
        |
        v
Algorithms, Data Structures, and Benchmark Logic
        |
        v
Result, Step, Progress, and Report Models
```

The algorithm and data-structure logic is kept separate from the Swing interface so the backend can be tested independently.

## Project Structure

```text
AlgorithmExplorer/
├── docs/
│   └── verification/
├── src/
│   ├── main/
│   │   └── java/ug/ucu/algorithmexplorer/
│   │       ├── algorithms/
│   │       │   ├── recursion/
│   │       │   └── sorting/
│   │       ├── analysis/
│   │       │   └── benchmark/
│   │       ├── controllers/
│   │       ├── datastructures/
│   │       ├── models/
│   │       │   ├── analysis/
│   │       │   └── sorting/
│   │       ├── ui/
│   │       └── Main.java
│   └── test/java/ug/ucu/algorithmexplorer/
│       ├── algorithms/
│       ├── analysis/
│       ├── controllers/
│       └── datastructures/
├── .gitignore
├── pom.xml
└── README.md
```

## Prerequisites

Install:

- JDK 21 or later;
- Apache Maven;
- Git;
- Visual Studio Code or another Java-compatible IDE.

Verify the installations:

```powershell
java -version
javac -version
mvn -version
git --version
```

## Installation

Clone the public repository:

```powershell
git clone https://github.com/howardlaboy/AlgorithmExplorer.git
cd AlgorithmExplorer
```

Alternatively, extract the submitted source ZIP and open the extracted project folder.

No additional third-party runtime libraries are required beyond the dependencies declared in `pom.xml`.

## Build and Test

Run the complete automated test suite:

```powershell
mvn clean test
```

A successful run should end with:

```text
BUILD SUCCESS
```

## Run the Application

Compile the project:

```powershell
mvn clean compile
```

Run the main class:

```powershell
java -cp target\classes ug.ucu.algorithmexplorer.Main
```

The main window provides access to all four modules.

## Package the Application

Run:

```powershell
mvn clean package
```

Maven creates the packaged JAR in the `target` directory.

## Testing

JUnit 5 tests cover:

- recursion results and edge cases;
- stack overflow and underflow;
- balanced-bracket processing;
- Linear Queue false-full behavior;
- Circular Queue wraparound;
- linked-list insertion, deletion, search, and traversal;
- sorting correctness and operation counting;
- immutable sorting snapshots;
- controller input validation;
- benchmark input generation;
- benchmark sorting correctness;
- experiment-plan creation;
- progress reporting, cancellation, and safety rules.

## Contributors

- Chemonges Emmanuel
- Lakica Johnson Howard
- Sean Ntege
- Loono Jethro
- Arinda Elvis
- Mutaawe Simon Peter   
