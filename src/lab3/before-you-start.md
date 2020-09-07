## Before you start

For this assignment a `docker-compose.yml` file is provided to build and run
the required services in Docker containers.
[Docker Compose](https://docs.docker.com/compose/) is a tool for defining and
running multi-container Docker applications. Please
[install](https://docs.docker.com/compose/install/) the latest version for
this assignment. Also take a look at the
[Compose file reference](https://docs.docker.com/compose/compose-file/).

For this assignment the following containers are defined in the
`docker-compose.yml` file:

- `zookeeper-server`: A Zookeeper server instance.
- `kafka-server`: A single Kafka server instance.
- `kafka-producer`: A Kafka producer running the GDELTProducer application.
- `kafka-stream`: A Kafka stream processor running the GDELTStream application.
- `kafka-consumer`: A Kafka consumer running the GDELTConsumer application.
- `kafka-console-consumer`: A Kafka consumer subscribed to the
  `gdel-histogram` topic, writing records to the console.
- `visualizer`: Simple webserver serving the GDELTVisualizer web application.
  The application can be accessed when the service is running by navigating to
  [localhost:1234](http://localhost:1234).

To start the containers, navigate to the `lab3` directory and run
`docker-compose up`. This will build and start all the containers defined in
the compose file. To start over, stop and remove everything with
`docker-compose down`.

The GDELTStream application, which you will develop for this assignment,
is built and run in the `kafka-stream` service container. To start an
interactive `sbt` shell use `docker-compose exec kafka-stream sbt`. Make sure
the other services are up before starting your streams application. You can
stop your application before running it again after changing the source using
`CTRL+C`.

You can use the following `docker-compose` commands to interact with your
running containers:

- `docker-compose up`: Create and start containers
- `docker-compose up -d`: Create and start containers in the background
- `docker-compose down`: Stop and remove containers, networks, images, and
  volumes
- `docker-compose start`: Start services
- `docker-compose restart`: Restart services
- `docker-compose stop`: Stop services
- `docker-compose rm`: Remove stopped containers
- `docker-compose logs --follow <SERVICE>`: View and follow log output from
  containers

For a full list of available commands please refer to the
[CLI Reference](https://docs.docker.com/compose/reference/overview/).
