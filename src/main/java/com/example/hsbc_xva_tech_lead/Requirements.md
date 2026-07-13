Problem Statement
You’re building a component that consumes a real-time stream of stock price updates. Each update contains:
a timestamp (integer)
a price (integer)
Updates may arrive out of chronological order. Sometimes an earlier update for a timestamp was incorrect. If multiple updates arrive with the same timestamp, the latest update replaces (corrects) the earlier one.
Design and implement a data structure that supports:
Update the stock price at a given timestamp (overwriting any previous price for that timestamp).
Return the current price, defined as the price at the latest (maximum) timestamp seen so far.
Return the maximum price among all recorded timestamps.
Return the minimum price among all recorded timestamps.

Requirements: Implement a class StockPrice with the following methods:
StockPrice()
— initialise with no price records. - Done
void update(int timestamp, int price)
— record/correct the price at timestamp. 
int current()
— return the latest price (price at the greatest timestamp).
int maximum()
— return the highest price currently recorded.
int minimum()
— return the lowest price currently recorded.

Example
Input (method calls):
["StockPrice", "update", "update", "current", "maximum", "update", "maximum", "update", "minimum"]
[[], [1, 10], [2, 5], [], [], [1, 3], [], [4, 2], []]
Output:
[null, null, null, 5, 10, null, 5, null, 2]
Explanation:
update(1, 10)
→ price at t=1 is 10
update(2, 5)
→ price at t=2 is 5
current()
→ latest timestamp is 2 → returns 5
maximum()
→ max is 10
update(1, 3)
→ correct timestamp 1 from 10 to 3
maximum()
→ now max is 5
update(4, 2)
→ price at t=4 is 2
minimum()
→ min is 2

Constraints
1 <= timestamp, price <= 10^9
Total number of calls to update, current, maximum, minimum is at most 10^5
current, maximum, minimum will only be called after at least one update