## Before you start

For this assignment a `docker-compose.yml` file is provided to easily build and
run all required services in Docker containers.
[Docker Compose](https://docs.docker.com/compose/) is a tool for defining and
running multi-container Docker applications. Please
[install](https://docs.docker.com/compose/install/) the latest version for
this assignment. Also take a look at the
[Compose file reference](https://docs.docker.com/compose/compose-file/).

For this assignment the following containers are defined in the
`docker-compose.yml` file:

| Container          | Description                                             |
|--------------------|---------------------------------------------------------|
| `zookeeper-server` | A Zookeeper server instance. Kafka requires Zookeeper to run. |
| `kafka-server`     | A single Kafka server instance. |
| `producer`         | A Kafka producer running the Producer application. |
| `transformer`      | A Kafka stream processor running the Transformer application. |
| `consumer`         | A Kafka consumer running the Consumer application. |
| `visualizer`       | Simple webserver serving the Visualizer web application. The application can be accessed when the service is running by navigating to [localhost:1234](http://localhost:1234). |
| `events`           | A Kafka consumer subscribed to the `events` topic, writing records to the console. Useful for debugging. | 
| `updates`          | A Kafka consumer subscribed to the `updates` topic, writing records to the console. Useful for debugging. |

To start the containers, navigate to the repository root directory and run
`docker-compose up`. This will build and start all the containers defined in
the compose file. To start over, stop and remove everything with
`docker-compose down`.

The Transformer application, which you will develop for this assignment, is
built and run in the `transformer` service container. To start an interactive
`sbt` shell use `docker-compose exec transformer sbt` from the repository root.
From there, you can `compile` and `run` the Transformer application. Make sure
the other services are up before starting your streams application. You can stop
your application before running it again after changing the source using
`CTRL+C`.

You can use the following `docker-compose` commands to interact with your
running containers:

| Command                                  | Description                       |
|------------------------------------------|-----------------------------------|
| `docker-compose up`                      |  Create and start containers. |
| `docker-compose up -d`                   |  Create and start containers in the background. |
| `docker-compose down`                    |  Stop and remove containers, networks, images, and  volumes. |
| `docker-compose start`                   |  Start services. |
| `docker-compose restart`                 |  Restart services. |
| `docker-compose stop`                    |  Stop services. |
| `docker-compose rm`                      |  Remove stopped containers. |
| `docker-compose logs --follow <SERVICE>` |  View and follow log output from  containers. |

For a full list of available commands please refer to the
[CLI Reference](https://docs.docker.com/compose/reference/overview/).
