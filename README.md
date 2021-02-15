# Primer Backend Challenge

Simple application which allows a token to be created, and then used to make payments in the sandbox environment of either Braintree or Stripe. The token creation endpoint will return only the token ID, while the payments endpoint will return the outcome and some metadata from the payment.

## Using the API

For demonstrative purposes the application is being hosted on AWS (via Fargate), so the following requests can be run as-is.

#### Token Creation: 
A token can be created with the curl command below, all of the fields are mandatory: 

`curl --header "Content-Type: application/json" \--data '{"cardHolderName":"Some Name", "cardNumber":"4444333322221111", "expiryDate":{"month":"01", "year":"2023"}}' http://ec2co-ecsel-i2c9s1u61xw-1639109703.us-east-2.elb.amazonaws.com:8080/tokens`

For Stripe, there are only certain card numbers which are allowed in the sandbox env. One of these is below, the rest are documented on the [Stripe website](https://stripe.com/docs/testing)

`curl --header "Content-Type: application/json" --data '{"cardHolderName":"Some Name", "cardNumber":"4242424242424242", "expiryDate":{"month":"01", "year":"2023"}}' http://ec2co-ecsel-i2c9s1u61xw-1639109703.us-east-2.elb.amazonaws.com:8080/tokens`

This will produce a response with the token ID:

`{"tokenId":"6ae7f1e7-8618-48ee-955b-715c9bb04d96"}`

#### Making a payment
The API for making a payment consists only of an amount and a token ID. The currency for all payments is EUR.

`curl --header "Content-Type: application/json"
--data '{"amount":1001, "tokenId":"6ae7f1e7-8618-48ee-955b-715c9bb04d96"}'
http://ec2co-ecsel-i2c9s1u61xw-1639109703.us-east-2.elb.amazonaws.com:8080/payments`

And this will produce a response similar to:
`{"outcome":"Sent for Settlement",
"transactionValue":{"amount":1001,"currency":"EUR"},"cardDetails":{"last4":"1111","scheme":"visa"}}`

By default, Braintree is used. To make a request to Stripe it is required to add a query parameter to the request URL (`?processor=stripe`)

`curl --header "Content-Type: application/json" --data '{"amount":1001, "tokenId":"365a0c21-a3b5-4e83-a4dd-ec10cad43c47"}' http://ec2co-ecsel-i2c9s1u61xw-1639109703.us-east-2.elb.amazonaws.com:8080/payments?processor=stripe`


## Running the application
The only pre-requisite is Java 15. Once installed, run `./gradlew bootRun` from the root directory. The above requests can then be ran with localhost:8080 replacing the AWS doamin.

To run the tests run `./gradlew test`


## Notes on implementation
* The application is written in Java with Spring Boot. This choice was made because Java is the most common language that I have used recently, while Spring Boot makes creating a small web app quick & simple.
* The application consists of two endpoints - one to create a token, one to make a payment. The API for both is very minimal, so there is no way of entering e.g. a billing address on a token, or a CVC on a payment.
* The payment gateways in use are Braintree and Stripe. If none is specified then the default is Braintree. 
* Stripe will error if the card is not a pre-defined test card.
* The error handling relies heavily on the default Spring handling. This is not ideal and the errors could be modified to improve the dev experience by implementing full card validation prior to making a payment, and by reducing the amount of internal details printed in the error message for PCI reasons. 
* Developed with TDD
