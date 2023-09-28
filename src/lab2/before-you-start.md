## Before you start

### AWS Accounts

Each member of your group as signed up in Brightspace has received an e-mail
with a username (group-XX), a password, and an AWS console login link. This
account can be used to perform the lab.

Due to some issues with AWS Educate, we will not use individual AWS Educate
accounts, but multiple accounts linked to a single root account. This root
account has **a shared and limited pool of credits for all students**. Each
group can absorb about 50 USD from the pool.

Because we are using a shared and limited resource, be aware of the following
rules:

### Terminate your cluster after use.
- You pay for cluster per commissioned minute. After you are done working
  with a cluster, you ***MUST terminate*** the cluster, to avoid unnecessary
  costs.
### Run the cluster in the correct region.
- Please run clusters in regions europe (like `eu-west-1` or `eu-central-1`). If you run into spot instance limits, it might be beneficial to change the region you are using. Note: this is different than what was here before, that was for previous years. 
### After changing your code, take small steps before spawning a large cluster.
- First, make sure your application runs correctly for a tiny data set on
  your laptop (e.g. in the Docker containers of Lab 1). Check the result and
  continue if it's as expected.
- Then, you can move to AWS and use a tiny cluster on a slightly larger data
  set. Check the result and continue if it's as expected.
- Then, you can increase the cluster resources and try again on a larger
  data set. Check the result and continue if it's as expected.
- Continue in this way until your goal.
### Your cluster must be called after your group, as follows: `group-xx`
- Failure to name your cluster after your group could result in random 
  terminations of your cluster.
### Do not interfere with clusters of other groups:
- Because the setup this year is a work-around, it is not a watertight setup.
  It may be that you discover ways to interfere with other groups. All activity 
  for your AWS account is logged. If you interfere with other groups, you will 
  immediately fail this course. Be careful what actions you perform, and only
  perform them if you are absolutely sure it will not disturb the general 
  progress of the lab for others.

Make sure you have read the introduction on Amazon Web services in the guide
chapter before starting the assignment.
