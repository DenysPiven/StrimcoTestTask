
# Football Team Selector

This program automatically selects the best football team of 11 players with the highest total rating from a dataset, considering constraints such as the maximum number of players per club and nationality. It uses a backtracking algorithm to optimize the team selection process and includes a timeout feature to return the best result found within 10 seconds.

## Features
- **CSV Parsing**: Reads player data from a CSV file, including ratings for various positions.
- **Team Optimization**: Forms a team with the highest possible total rating, fulfilling all positional and constraint requirements.
- **Timeout Handling**: Stops the algorithm after 10 seconds and returns the best solution found so far.
- **Custom Position Sorting**: Outputs the selected team in a predefined role order.

## How It Works
1. The program reads a dataset of players, including their ratings for various positions.
2. It filters candidates for each role, limiting to the top 100 for efficiency.
3. Using backtracking, it evaluates possible team combinations while respecting constraints.
4. Outputs the best team and their total rating.

## Usage
1. Place your dataset file as `Data.csv` in the resources folder.
2. Run the `Main` class to generate the best team.
3. View the selected team and total rating in the console output.

## Example Output
```
Selected team:
Position: GK | Player: Courtois, Thibaut | Nation: Бельгия | Club: R. Madrid | Positions: [GK] | Rating: 92.79
Position: DL | Player: Hernandez, Lucas | Nation: Франция | Club: Paris SG | Positions: [DL, DC] | Rating: 87.62
Position: DC | Player: van Dijk, Virgil | Nation: Голландия | Club: Liverpool | Positions: [DC] | Rating: 90.67
Position: DC | Player: Dias, Ruben | Nation: Португалия | Club: Man City | Positions: [DC] | Rating: 89.87
Position: DR | Player: Danilo | Nation: Бразилия | Club: Juventus | Positions: [DR, DL, DC] | Rating: 85.91
Position: MC | Player: Barella, Nicolo | Nation: Италия | Club: Inter | Positions: [MC] | Rating: 87.5
Position: MC | Player: Koke | Nation: Испания | Club: A. Madrid | Positions: [DM, MR, ML, MC] | Rating: 84.01
Position: AML | Player: Kvaratskhelia, Khvicha | Nation: Грузия | Club: Parthenope | Positions: [AML] | Rating: 89.24
Position: AMC | Player: Messi, Lionel | Nation: Аргентина | Club: Inter Miami | Positions: [AMR, AMC, FC] | Rating: 94.58
Position: AMR | Player: Saka, Bukayo | Nation: Англия | Club: Arsenal | Positions: [AMR, AML] | Rating: 91.36
Position: ST | Player: Son, Heung-Min | Nation: Южная Корея | Club: Tottenham | Positions: [AML, ST] | Rating: 91.52
Total Team Rating: 985.07
```
