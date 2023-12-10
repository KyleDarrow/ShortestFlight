package interview;

import java.util.*;

    /**
     You are working for a promising new â€œflight hackingâ€ startup â€œAlgoFlightsâ€.
     The startup aims to find ultra-cheap flights by combining multiple flights from various airlines and by taking portions of multi leg flights.

     A database of flights is loaded into the AlgoFlight algorithm every morning with all offered flights.
     Flights will have:
     Source, destination, price

     After initializing the system, customers can enter a source and destination city and get the cheapest flight (You will only need to return the price of the flight).

     If no flight path reaches the destination from the source, return -1

     You must solve this problem using a hand made graph data structure and Dijkstraâ€™s algorithm.

     You are given the flight class which will be used to pass the list of flights.

     You will implement two methods:
     initializeFlightGraph(List of flight objects)
     getCheapestFlight(source, destination)

     You must provide a running time analysis of the getCheapestFlight function

     Code Author: <Kyle Darrow>

     Running Time Analysis of getCheapestFlight
     --------------------
     <The running time of this program is O(n log(n)). Adding flights to a graph has a running time of
     O(n) where n is the number of flights. In the while loop, when removing elements from the queue,
     the operation takes O(log(n)) time and we iterate over each city for O(n) time. When we calculate
     the overall time complexity we multiply the time outside the while loop with the worst case time in
     the while loop, getting a running time of O(n log (n)).>
     */
    public class AlgoJet {
        public static class Flight {
            private final String source;
            private final String destination;
            private final int price;
            public Flight(String source, String destination, int price) {
                this.source = source;
                this.destination = destination;
                this.price = price;
            }

            public String getSource() {
                return source;
            }

            public String getDestination() {
                return destination;
            }

            public int getPrice() {
                return price;
            }
        }

        /**
         * This is my recommended (and optional) way to represent the graph which will me an adjacency List of:
         * Source to a map of all reachable destinations with the cheapest price to get to that destination from the source
         */
        private Map<String, Map<String, Integer>> graph;

        public AlgoJet() {
            this.graph = new HashMap<>();
        }

        public void initializeFlightGraph(List<Flight> flights) {
            // check for each flight from the flights and store their information
            for (Flight flight : flights) {
                String sourceCity = flight.getSource();
                String destinationCity = flight.getDestination();
                int price = flight.getPrice();
                // if the source does not exist, create a new entry of the source in the graph
                if (!graph.containsKey(sourceCity)) {
                    graph.put(sourceCity, new HashMap<>());
                }
                // fill in destination and pricing information for source city
                graph.get(sourceCity).put(destinationCity, price);
            }
        }

        public int getCheapestFlight(String source, String destination) {
            // My recommended way to store the results for Dijkstra's algorithm
            Map<String, Integer> distances = new HashMap<>();
            // Initialize Dijkstra's results
            for (String city : graph.keySet()) {
                distances.put(city, Integer.MAX_VALUE);
            }
            distances.put(source, 0);

            // Initialize priority Queue
            PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Comparator.comparingInt(entry -> entry.getValue()));
            pq.add(new AbstractMap.SimpleEntry<>(source, 0));

            // run if the queue is not empty
            while (!pq.isEmpty()) {
                // look at the head of the queue
                Map.Entry<String, Integer> entry = pq.peek();
                // check if there is an entry
                if (entry != null){
                    // remove that entry
                    pq.remove(entry);
                }
                // get the key of that entry, which is a city
                String currentCity = entry.getKey();
                // get the value of the entry, which is the price
                int currentPrice = entry.getValue();
                // check if our current city is the destination
                if (currentCity.equals(destination)) {
                    // return the price of that city
                    return currentPrice;
                }
                // ignore the path if a cheaper option has been found
                if (currentPrice > distances.get(currentCity)) {
                    continue;
                }
                // check if the current city in the graph
                if (graph.containsKey(currentCity))
                    // iterate over the neighboring cities
                    for (Map.Entry<String, Integer> neighbor : graph.get(currentCity).entrySet()) {
                    // get the key and value of each neighbor
                        String neighborCity = neighbor.getKey();
                        int priceToNeighbor = neighbor.getValue();
                        // calculate the total price of the trip
                        int totalPrice = currentPrice + priceToNeighbor;
                        /*
                        check if a neighboring city is not in the map or if the most recent total price
                        is less than a previous price. If either is true, we found a cheaper flight.
                         */
                        if (!distances.containsKey(neighborCity) || totalPrice < distances.get(neighborCity)) {
                            // when a cheaper path is found, update the map
                            distances.put(neighborCity, totalPrice);
                            // add the new entry neighboring city with the updated price
                            pq.add(new AbstractMap.SimpleEntry<>(neighborCity, totalPrice));
                    }
                }
            }
            // if there is no valid path
            return -1;
        }

        /*
        DO NOT EDIT BELOW THIS
        Below is the unit testing suite for this file.
        It provides all the tests that your code must pass to get full credit.
        */
        public static void runUnitTests() {
            testExample();
            testUnreachableDestination();
            testSameSourceAndDestination();
            testCycle();
            testMultipleFlights();
        }

        private static void printTestResult(String testName, boolean result) {
            String color = result ? "\033[92m" : "\033[91m";
            String reset = "\033[0m";
            System.out.println(color + "[" + result + "] " + testName + reset);
        }

        private static void testAnswer(String testName, int result, int expected) {
            if (result == expected) {
                printTestResult(testName, true);
            } else {
                printTestResult(testName, false);
                System.out.println("Expected: " + expected + "\nGot:      " + result);
            }
        }

        private static void testAnswer(String testName, int[] result, int[] expected) {
            if (Arrays.equals(result, expected)) {
                printTestResult(testName, true);
            } else {
                printTestResult(testName, false);
                System.out.println("Expected: " + Arrays.toString(expected) + "\nGot:      " + Arrays.toString(result));
            }
        }

        public static void testExample() {
            AlgoJet algoJet = new AlgoJet();
            List<Flight> flights = new ArrayList<>();
            flights.add(new Flight("A", "B", 100));
            flights.add(new Flight("A", "C", 150));
            flights.add(new Flight("B", "C", 40));
            flights.add(new Flight("B", "D", 200));
            flights.add(new Flight("C", "D", 100));
            flights.add(new Flight("C", "E", 120));
            flights.add(new Flight("D", "E", 80));
            algoJet.initializeFlightGraph(flights);
            int result1 = algoJet.getCheapestFlight("A", "E");
            int result2 = algoJet.getCheapestFlight("A", "D");
            int result3 = algoJet.getCheapestFlight("A", "C");
            int result4 = algoJet.getCheapestFlight("B", "E");

            testAnswer("testExample_1", result1, 260);
            testAnswer("testExample_2", result2, 240);
            testAnswer("testExample_3", result3, 140);
            testAnswer("testExample_4", result4, 160);
        }

        public static void testUnreachableDestination() {
            AlgoJet algoJet = new AlgoJet();
            List<Flight> flights = new ArrayList<>();
            flights.add(new Flight("A", "B", 100));
            flights.add(new Flight("B", "C", 150));
            flights.add(new Flight("D", "E", 100));
            algoJet.initializeFlightGraph(flights);
            int result = algoJet.getCheapestFlight("A", "E");
            testAnswer("testUnreachableDestination", result, -1);
        }

        public static void testSameSourceAndDestination() {
            AlgoJet algoJet = new AlgoJet();
            List<Flight> flights = new ArrayList<>();
            flights.add(new Flight("A", "B", 100));
            flights.add(new Flight("B", "C", 150));
            flights.add(new Flight("C", "D", 100));
            algoJet.initializeFlightGraph(flights);
            int result = algoJet.getCheapestFlight("A", "A");
            testAnswer("testSameSourceAndDestination", result, 0);
        }

        public static void testCycle() {
            AlgoJet algoJet = new AlgoJet();
            List<Flight> flights = new ArrayList<>();
            flights.add(new Flight("A", "B", 200));
            flights.add(new Flight("B", "C", 150));
            flights.add(new Flight("C", "D", 120));
            flights.add(new Flight("C", "A", 180));
            flights.add(new Flight("C", "B", 100));
            flights.add(new Flight("B", "E", 300));
            algoJet.initializeFlightGraph(flights);
            int result = algoJet.getCheapestFlight("A", "E");
            testAnswer("testCycle", result, 500);
        }

        public static void testMultipleFlights() {
            AlgoJet algoJet = new AlgoJet();
            List<Flight> flights = new ArrayList<>();
            flights.add(new Flight("A", "B", 200));
            flights.add(new Flight("B", "C", 150));
            flights.add(new Flight("A", "C", 140));
            flights.add(new Flight("A", "C", 180));
            flights.add(new Flight("A", "B", 100));
            flights.add(new Flight("B", "E", 300));
            flights.add(new Flight("B", "E", 250));
            flights.add(new Flight("C", "E", 220));
            algoJet.initializeFlightGraph(flights);
            int result = algoJet.getCheapestFlight("A", "E");
            testAnswer("testMultipleFlights", result, 350);
        }

        public static void main(String[] args) {
            runUnitTests();
        }
    }

