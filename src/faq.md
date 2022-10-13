# Frequently asked questions (FAQ)

### How do I use the H3 library?

You can import it in your scala file like:
```
import com.uber.h3core.H3Core
import com.uber.h3core.util.GeoCoord
```
And then add to library dependencies in your build.sbt:
```
"com.uber" % "h3" % "3.7.0"
```
(If this does not work for you, let us know!)

### I cannot connect to my cluster with SSH

If you have trouble connecting via SSH to your cluster, the issue might be that you are using the private subnet.

During cluster creation, in step 2 under Networking, select one of the public EC2 subnets.
