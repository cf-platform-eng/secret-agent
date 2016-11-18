#secret-agent
a POC to try out various approaches for embedding a java agent in a continer alongside a pushed app.

##goals
 * language agnostic, the pushed app can be in any supported language
 * agent should be private to the container, not accessible from the outside
 * no special work needed by the pushed app to make use of the agent
 * minimally invasive to cf, the pushed app, existing procedures, etc.
 
##approaches
###[multi-buildpack](https://github.com/cloudfoundry-incubator/multi-buildpack)
Recommended by the Buildpacks team in NY, will be fully supported in the future.

####Process
Fork the java buildpack (https://github.com/JaredGordon/java-buildpack)
Do what's needed to get the agent in there and running (update the 3 big scripts, add the jar inside there)
Add any needed certs (later, as needed)
Publish the buildpack
Use the buildpack as per the instructions on the multi-buildpacks site.

####Pros
Might be the supported way to do this going forward
Maintained by the official buildpacks team

####Cons
Not supported yet
Requires forking the java buildpack and keeping up with any updates to it.
Is not "the usual way to push apps"

###[meta-buildpack](https://github.com/cf-platform-eng/meta-buildpack)
A more lightweight approach?

###Process
Clone the meta-buildpack project
Write a decorator
Run the scripts

####Pros
More lightweight than the multi-buildpack approach
Would not need to clone the java buildpack

####Cons
Need to install multi-buildpack and place it at the top of the buildpack stack
An experimental approach, may or may not be mainstreamed in the future
Pushing a decorator means that all apps will be decorated? Or just certain apps? How to control?
