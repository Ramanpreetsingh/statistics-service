# statistics-service

A scalable service that provides statistics for transactions. It takes timestamp and amount of transaction as json payload eg:
```
POST /transactions
{
"amount": 3.5,
"timestamp": 12890212
}
```
A transaction can be deleted by :
```
DELETE /transactions
```
Statistics for the configured interval should be given by
```
GET /statistics
```
Output:
```
{
	sum: 9 //- Total sum of transaction value.
	avg: 4 //- Average amount of transaction value
	max: 5 //- Maximum transaction value
	min: 2 //- Minimum transaction value
	count: 5 // Total number of transactions
}
```
The service needs to handle concurrent requests and be thread safe. No persistance layer can be used i.e no database or in memory databases can be used.

The endpoints have to execute in constant time and memory (O(1))
