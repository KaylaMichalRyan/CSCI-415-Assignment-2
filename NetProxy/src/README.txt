README

How to run the HTTP Proxy Server
---------------------------------

1. Open the project in IDE (Netbeans)
2. Run the project
3. Open up a command prompt session
4. Type "telnet localhost 7000"
5. Enter a get command (GET http://www.kennedy-center.org/text/ HTTP/1.0)
6. The project will show simple data and say that it's writing the new site to disk (caching) while the telnet window will receive HTTP from the server
7. Hit CTRL + ] and then type "quit" and hit enter
8. Type "telnet localhost 7000"
9. Enter the same get command (GET http://www.kennedy-center.org/text/ HTTP/1.0)
10. The project command window will let you know it's pulling the information from cache while the telnet window fills up with the HTTP from said cache


Configuration
-----------------


--disable-logging			Logging can be turned off by including this flag.

--logging-location <filepath>		Sets the location for requests to be logged, defaults to current location.

--max-threads <number>			Set a cap on the number of clients that can be connected to the proxy at 						any one time. Default is 10.

--port-number <number>			You can specify any port you would like the server to listen on, default 						is 8080

Credits
-----------------

Alex Paulson
Brett Chastain
Joe Osbourne
Kyle Ryan