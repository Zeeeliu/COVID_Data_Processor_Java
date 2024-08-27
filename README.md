Executive Summary 

This project report supplements the source code of CIT 594 Group Project to provide an overview of the custom feature, explanation of data structure utilized and lessons learned from team collaboration on the project. 

Custom Feature Overview:
Our team has designed a feature called "showFullyVaxRateToHouseValueCorrelation'' which calculates and displays the correlation between the full COVID-19 vaccination rate and the average market value of properties in a region. This computation integrates data from three sources for all the zip codes in the region: COVID-19 vaccination data (including the number of fully vaccinated individuals), population figures, and property market values. The feature first computes the total number of fully vaccinated individuals and the total population to determine the fully vaccination rate of the region. It then calculates the average market value of all properties in the region. These metrics are presented together to illustrate the relationship between vaccination status (full vaccination rate) and economic status (property market value) of the entire city of Philadelphia. 

To ensure the accuracy of the feature, we have performed the following validation methods: 
Independent manual calculation checks: we have  the vaccination rates and average property values using excel, and compared the expected result with our feature output for accuracy.
Code Peer Review: we have conducted peer code reviews in Github to identify logical errors and ensure requirements are met with correct implementation.
Junit Testing: we have leveraged test cases to validate the feature's reliability across various data sets and intermediate methods.
Data Structure Analysis: Population 

Our team decided to use data structure List to store “Population” when it is used to support Feature #2 “Show the total population for all ZIP Codes”. This feature was implemented in the showTotalPopulation() method of the DataProcessor class for calculating the total population.

We decided to choose the List data structure because of its simplicity while not comprising the efficiency of our program. The feature requires sequentially accessing each Population object to calculate the total. The List data structure offers O(n) complexity for iterating over the entire list, which is necessary for summing up the population.

Alternatively, we have considered using a Map, with ZIP codes as keys and population figures as values. This provides an efficient way to store and access population data by ZIP codes and also offers the same performance of O(n) for iteration. For our specific use case of aggregating the total population, the additional overhead of managing key-value pairs in a Map is unnecessary. Furthermore, a List is more memory-efficient in this context as it does not require the extra storage space for keys. Hence, we decided to use the List data structure. 
Data Structure Analysis: Vaccination

Our team decided to use the Map<String, Map<String, Double>> data structure to support Feature #3 - "Show Total Vaccinations Per Capita for Each ZIP Code on a Given Date." This was utilized within the VaccinationDataProcessor class and executed in the calculateVaccinationsPerCapita method.

The rationale behind selecting a nested Map structure stems from its capability to efficiently organize and access data based on two keys - the date and the ZIP code. The outer Map is keyed on the date, and its values are another Map, where ZIP codes are the keys and the calculated vaccinations per capita are the values. This structure allows for O(1) complexity for retrieval, which allows quick data access based on specific keys.

Alternatively, we considered using a List or ArrayList of Vaccination objects containing date and ZIP code fields. However, this approach would have led to O(n) complexity for lookups, as iterating through the list to find a specific date and ZIP code combination would be necessary. This will be highly inefficient in performance. Hence, we decided to use the Map data structure. 
Data Structure Analysis: Property

Our team elected to use a Map<String, Double> data structure in the PropertyDataProcessor classIn implementing Feature #4 - "Show the Average Market Value for Properties in a Specified ZIP Code," This decision was demonstrated in the methods showAveragePropertyMetric and getOrCalculateAveragePropertyMetric.

The decision to utilize a Map was driven by its ability to efficiently associate ZIP codes with their corresponding average property metrics. This key-value pairing facilitates O(1) complexity for both storing and retrieving data. This is an essential feature when managing extensive datasets from the property source file. The Map’s direct access to previously calculated averages based on ZIP code enhances the speed and efficiency of the application, especially when lookups are required in a large dataset.

We considered alternatives such as ArrayLists to store pairs of ZIP codes and metrics. Although simpler, this would have O(n) complexity for data retrieval, as it would require searching through the list for a specific ZIP code. 
