# Frequently asked questions (FAQ)
(Thanks to everyone who spent the time to post their fixes to common problems on the Brightspace Q&A)

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

Another issue might be the standard firewall rules for the EC2 clusters. If you are having this problem, you can try the following steps:

  1. Go to your cluster overview page and scroll down to "Network and Security"
  2. Click the link below the "Primary Node - EMR managed security group"
  3. Click "Actions" > "Edit inbound rules" (top right)
  4. Find the rule for SSH (port 22) and click the X on the IP address that is currently there.
  5. Click in the search box and select "0.0.0.0/0". (or enter a more restrictive IP range if you want)
  6. Click "Save rules"

### Do I need to keep the github repo of lab 1 up to date with the changes I made for lab 2?

No, this is not necessary. If you have specific changes that you discuss in lab 1, make sure to specify that the code for that is in the lab 2 repository.
