# CSA09 – Programming in Java – Assignment 1
**Campus Smart-Parking System (Object-Oriented Model)**

- Name: Suvetha S
- Register number: 1921260208
- GitHub: Suvetha-1859
- Java version: OpenJDK 21 (`javac -version` → 21)

## How to compile and run

| Task | Folder | Compile | Run | Result |
|---|---|---|---|---|
| 1 | Task01 | `javac ObjectVsParallelArrays.java` | `java ObjectVsParallelArrays` | Object vs parallel-array search timing and space |
| 2 | Task02 | `javac DataRepresentation.java` | `java DataRepresentation` | Integer-division bug shown (Rs 30 instead of Rs 60) and fixed |
| 3 | Task03 | `javac *.java` | `java ParkingFacility` ; `java AllocationBenchmark` | Output matches expected; stack design benchmarked |
| 4 | Task04 | `javac TicketDemo.java` | `java TicketDemo` | Constructors, chaining, copy constructor |
| 5 | Task05 | `javac StaticDemo.java` | `java StaticDemo` | static counter, object passing, garbage collection |
| 6 | Task06 | `javac InheritanceDemo.java` | `java InheritanceDemo` | Constructor order Vehicle → Car → ElectricCar; UML in `uml_hierarchy.png` |
| 7 | Task07 | `javac OverrideDemo.java` | `java OverrideDemo` | Overriding, final, equals/hashCode |
| 8 | Task08 | `javac *.java` | `java FeeDemo` ; `java PolymorphismDemo` | FeeDemo output matches expected (30.0 / 90.0 / 210.0 / true) |
| 9 | Task09 | `javac AbstractionDemo.java` | `java AbstractionDemo` | Abstract class + two interfaces |
| 10 | Task10 | `javac -d out $(find src -name "*.java")` | `java -cp out parking.app.Main` | Packages, validated setters, wrapper pitfalls |
| 11 | Task11 | `javac -d out $(find src -name "*.java")` | `java -cp out parking.app.Main` ; `... parking.app.TestRunner` ; `... parking.app.Benchmark` | 25/25 tests pass, day total Rs 440 = hand calculation |
| 12 | Task12 | `javac -d out $(find src -name "*.java")` | `java -cp out parking.app.TestRunner12` ; `java -cp out parking.app.TestRunner` | 23/23 new tests pass; 25/25 Task 11 tests still pass unchanged |

.
