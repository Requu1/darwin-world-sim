# Animal World Simulation

A desktop simulation project written in **Java** with **JavaFX**, focused on modeling the life of animals in a dynamic environment. The application allows the user to configure simulation parameters, launch multiple simulations, observe the map in real time, and inspect both global and per-animal statistics.

## Project Overview

This project simulates an ecosystem in which animals move across a rectangular map, consume plants, reproduce, lose energy over time, and eventually die. The simulation includes a **season system**, which affects world temperature and introduces changing environmental conditions.

The application provides a graphical user interface built with JavaFX and supports live visualization of the map and selected statistics.

## Variant

**Seasons**

The simulation includes changing seasons, which influence the temperature of the world and make the environment more dynamic.

## Main Features

- **Configurable simulation parameters**
  - map width and height
  - starting number of plants
  - number of plants growing each day
  - starting number of animals
  - animal starting energy
  - daily energy loss
  - energy gained from eating plants
  - minimum energy required for reproduction
  - reproduction energy cost
  - genome length
  - minimum and maximum mutation count
  - season duration
  - minimum temperature
  - warm distance

- **Season-based simulation**
  - alternating summer and winter periods
  - changing world temperature over time
  - dynamic environmental conditions

- **Live map visualization**
  - animals displayed on a grid map
  - plants displayed in real time
  - multiple animals can occupy the same cell
  - selected animal can be highlighted and tracked

- **Animal tracking**
  - the user can select an animal and inspect its state
  - if multiple animals occupy one tile, clicking allows browsing them

- **Animal statistics panel**
  - genome
  - currently active gene
  - current energy
  - number of eaten plants
  - number of children
  - number of days lived
  - day of death

- **Global simulation statistics**
  - current simulation day
  - current world temperature
  - number of animals
  - number of plants
  - number of free positions
  - average animal energy
  - average lifespan
  - average number of children
  - most popular genome

- **Live chart window**
  - a separate statistics window for each simulation
  - live visualization of selected statistics during runtime

- **Multiple simulations at the same time**
  - each simulation opens in a separate window
  - each simulation also gets its own statistics window

- **Pause / resume support**
  - simulation can be paused and resumed during runtime

## Implemented Extensions

The project includes the following extensions:

- running multiple simulations simultaneously in separate windows
- live visualization of animal energy
- tracking detailed statistics of a selected animal
- live chart visualization of selected simulation statistics

## Technologies Used

- **Java 23**
- **JavaFX**
- **Gradle**
- **JUnit 5**

## Project Structure

```text
darwin-simulation/
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
└── src
    ├── main
    │   ├── java
    │   │   └── agh.ics.oop
    │   │       ├── animal
    │   │       ├── app
    │   │       ├── map
    │   │       ├── other
    │   │       ├── presenters
    │   │       ├── simulation
    │   │       └── util
    │   └── resources
    └── test
        └── java
```

## How to Run

### Run the simulation

From the `darwin-simulation` directory, run:

```bash
./gradlew run
```

On Windows:

```bash
gradlew.bat run
```

This starts the JavaFX application and opens the simulation starter window, where the user can configure parameters and launch simulations.

### Run the tests

From the `darwin-simulation` directory, run:

```bash
./gradlew test
```

On Windows:

```bash
gradlew.bat test
```

### Run from an IDE

You can also open the project in **IntelliJ IDEA** and:

- run the Gradle `run` task to start the application
- run the Gradle `test` task to execute tests
- or run the main application class directly if needed

Main application class:

```java
agh.ics.oop.app.WorldGUI
```

## Notes

- The simulation is launched in its own thread.
- Each started simulation opens:
  - one main simulation window
  - one statistics/chart window
- The GUI is built with JavaFX and uses FXML presenters.
- The application allows observing both the world state and detailed information about selected animals.

## Author

**Szymon Potępa**
