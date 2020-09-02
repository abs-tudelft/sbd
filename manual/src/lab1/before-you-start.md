## Before you start

We recommend you read all the relevant sections on Scala and Spark in the
guide. Make sure you have Docker up-and-running and that you have built the
required images for Spark and SBT, as per the instructions. You can verify your
set-up by going through the steps of the Spark tutorial.

Download the template project from [lab's GitHub repository] (contained in
the `templates/lab1/` folder), either by forking or cloning the repository, or
downloading a zip file. You should execute all Docker commands from this
project folder. Rename the Scala file and the class it contains to something
meaningful. Update the project name in `build.sbt`. The project folder also
contains a `data/` directory, which will contain the data you download for
testing, as explained in the following. The `data/` folder is automatically
mounted in the working directory of relevant containers.

[lab's github repository]: https://github.com/abs-tudelft/sbd
