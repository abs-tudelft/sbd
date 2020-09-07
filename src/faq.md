# Frequently asked questions (FAQ)

## Java 11
Help! I'm getting:
```java.lang.IllegalArgumentException: Unsupported class file major version 55```

Spark `2.4.6` only runs on Java 8.

If you're on Ubuntu 20.04, this version is not installed by default.
You could install it and enable it using:

```
sudo apt install openjdk-8-jdk
sudo update-java-alternatives --jre-headless --jre --set java-1.8.0-openjdk-amd6
```

However, it's more convenient to [use the Docker container] for this course.

[use the Docker container]: guide/docker.md