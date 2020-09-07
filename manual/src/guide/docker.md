## Docker

According to the [Docker Documentation](https://docs.docker.com/get-started)

> Docker is a platform for developers and sysadmins to develop, deploy, and run
> applications with containers. The use of Linux containers to deploy
> applications is called containerization. Containers are not new, but their
> use for easily deploying applications is. Containerization is increasingly
> popular because containers are:
>
> Flexible
>
> - Even the most complex applications can be containerized.
>
> Lightweight
>
> - Containers leverage and share the host kernel.
>
> Interchangeable
>
> - You can deploy updates and upgrades on-the-fly.
>
> Portable
>
> - You can build locally, deploy to the cloud, and run anywhere.
>
> Scalable
>
> - You can increase and automatically distribute container replicas.
>
> Stackable
>
> - You can stack services vertically and on-the-fly.

For this course, we use Docker primarily to ensure every student is using the
exact same platform for their applications, and to avoid certain
platform-specific issues and peculiarities.

> You are **not** required to use Docker for this lab when you feel comfortable
> setting up the required tools on your own system.

A basic understanding of some [Docker](https://docs.docker.com/) concepts helps
in getting started with this course. [Part 1: Orientation and
setup](https://docs.docker.com/get-started/) of the [Get Started
Guide](https://docs.docker.com/get-started/) covers the basic
[Docker](https://docs.docker.com/) concepts used in this course.

Before trying the lab assignments and tutorials in the next sections, make sure
you [Install Docker
(stable)](https://docs.docker.com/install/#supported-platforms) and test your
installation by running the simple [Hello World
image](https://hub.docker.com/_/hello-world).

```bash
docker run hello-world
```

### Setting up Spark in Docker

In order to run Spark in a container, a `Dockerfile` is provided in the lab 1
repository, which can be used to build images for `spark-submit` to run your Spark
application, `spark-shell` to run a Spark interactive shell, and the Spark history
server to view event logs from application runs. You need to build these images before
you get started. The Dockerfiles we provide assume that you run Docker from the
folder at which they are located. Don't move them around! They will stop
working.

To build a docker image from the Dockerfile, we use `docker build`:

```bash
docker build --target <target> -t <tag> .
```

Here `<target>` selects the target from the Dockerfile, `<tag>` sets the tag
for the resulting image, and the `.` sets the build context to the current
working directory.

We use `docker build` to build the images we need to use Spark and SBT.

- `sbt`

  ```bash
  docker build \
  --build-arg BASE_IMAGE_TAG="8" \
  --build-arg SBT_VERSION="1.3.13" \
  --build-arg SCALA_VERSION="2.12.12" \
  -t sbt \
  github.com/hseeberger/scala-sbt.git#:debian
  ```

- `spark-shell`

  ```bash
  docker build --target spark-shell -t spark-shell .
  ```

- `spark-submit`

  ```bash
  docker build --target spark-submit -t spark-submit .
  ```

- `spark-history-server`

  ```bash
  docker build --target spark-history-server -t spark-history-server .
  ```

You can then run the following commands from the Spark application root
(the folder containing the `build.sbt` file). Please make sure to use the
provided template project.

- Run SBT to package or test your application (`sbt <command>`)

  ```bash
  docker run -it --rm -v "`pwd`":/root sbt sbt
  ```

- Start a Spark shell (`spark-shell`)

  ```bash
  docker run -it --rm -v "`pwd`":/io spark-shell
  ```

- Run your Spark application (`spark-submit`) (fill in the class name of your
  application and the name of your project!)

  ```bash
  docker run -it --rm -v "`pwd`":/io -v "`pwd`"/spark-events:/spark-events \
  spark-submit --class <YOUR_CLASSNAME> \
  target/scala-2.12/<YOUR_PROJECT_NAME>_2.12-1.0.jar
  ```

- Spawn the history server to view event logs, accessible at
  [localhost:18080](http://localhost:18080)

  ```bash
  docker run -it --rm -v "`pwd`"/spark-events:/spark-events \
  -p 18080:18080 spark-history-server
  ```

The rest of the manual will not generally mention these Docker commands again,
so know that if we mention e.g. `spark-shell`, you should run the corresponding
`docker run` command listed above. You can create scripts or aliases for your
favorite shell to avoid having to type a lot.
