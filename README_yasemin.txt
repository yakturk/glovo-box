There is an OrderService which provides service to the API. The service returns orders for each courier according to conditions.
Conditions are defined according to WORDING.md file. 

 slot prioritise:
- All orders will be shown in order, according to their pickup distances. 
- But if there are a few orders inside of a slot (500m) and this slot is the closest to the Courier: Orders are prioritised via vip/food parameters.
For Example:
Order1 1 km far, vip
Order2 700 m far,vip
order3 400 m far, vip
order4 350 m far, food

after prioritise :
order3
order4
order2
order1

There is an OrderServiceTest class too. It consists tests which provide all conditions.
There is an ApiErrorHandler class which catch exceptions and returns json messages for api messages.
There is a CourierNotFoundException class too. If a wrong id is sent to the service, it throws the exception and print a message.



