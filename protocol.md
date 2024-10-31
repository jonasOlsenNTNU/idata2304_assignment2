# Protocol

Multiple clients can connect to one server.
A response is always returned to the client after a request.
When the state "Current channel" is changed, it is broadcast to all connected clients.
There can be several requests/responses during one connection.

The following requests could be sent from client to server:

* "on" turn on the TV
* "off" turn off the TV
* "?" to check if the TV is on
* "count" get the number of channels
* "current" get current channel
* "up" to increase channel by 1
* "down" to decrease channel by 1
* "exit" to disconnect from the Tv

Server can respond with:

* "The TV is off." - when trying to send a command to TV while it is off.
* "The TV is turned on" - after turning on the TV.
* "The TV is turned off" - after turning off the TV.
* "Number of available channels: {Count}" - after requesting the number of channels.
* "Current channel: {Channel}" - after requesting current channel.
* "Channel changed." - after changing the channel
* "Channel is set to: {Channel}" - broadcast after the channel has been changed.