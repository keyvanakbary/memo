# Memo

A blogging platform built upon CQRS and Event Sourcing.

Basically a playground to explore and play with [Axon Framework](http://www.axonframework.org).

The code is divided in 4 modules:
* **Core**: Where the write model lives, it will eventually host the business logic for the blogging platform.
* **Core API**: Where the events and commands necessary to communicate with the domain live.
* **Query**: Where the read model lives.
* **Web**: A simple web app that pulls all together and exposes it to the world.
