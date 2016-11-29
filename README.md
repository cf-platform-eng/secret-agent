#secret-agent
a POC to try out various approaches for embedding a java agent in a continer alongside a pushed app.

##goals
 * language agnostic, the pushed app can be in any supported language
 * agent should be private to the container, not accessible from the outside
 * no special work needed by the pushed app to make use of the agent
 * minimally invasive to cf, the pushed app, existing procedures, etc.
 
##Approaches
###Custom buildpack
####Process
1. Create a [buildpack](https://github.com/cf-platform-eng/basic-boot-buildpack) that will run the agent alongside an app in the same container.
1. Put the agent out where we can get to it via wget
1. Push a [boot app](https://github.com/cf-platform-eng/secret-agent/tree/master/passthrough) using this buildpack, such as with the manifest below:
```yaml
applications:
- name: passthrough
  memory: 1G
  instances: 1
  path: target/passthrough-1.0.0.jar
  buildpack: https://github.com/cf-platform-eng/basic-boot-buildpack

```

####Pros
 * It's simple
 * It works
 * We can control the jre used (to install a custom cert for instance, or use an alternate jre)

####Cons
 * Need a custom buildpack, and need to keep it up to date with CVEs
 * Assumes that the app being pushed is a boot app

###[multi-buildpack](https://github.com/cloudfoundry-incubator/multi-buildpack)
Recommended by the Buildpacks team in NY, might be fully supported in the future.

####Process
1. Modify the custom buildpack created above to just run the agent
1. Publish the buildpack
1. Use the custom buildpack as per the instructions on the multi-buildpacks site.

####Pros
 * Might be the supported way to do this going forward
 * Maintained by the official buildpacks team

####Cons
 * Not supported yet
 * Need a custom buildpack, and need to keep it up to date with CVEs
 * Is not "the usual way to push apps"

###[meta-buildpack](https://github.com/cf-platform-eng/meta-buildpack)
A more lightweight approach?

###Process
1. Clone the meta-buildpack project
1. Modify our custom buildpack to make it a decorator
1. Run the scripts

####Pros
 * More lightweight than the multi-buildpack approach
 * Decorator might be easier to deal with than a full-blown custom buildpack

####Cons
 * Need to install multi-buildpack and place it at the top of the buildpack stack
 * An experimental approach, may or may not be mainstreamed in the future
 * Pushing a decorator means that all apps will be decorated? Or just certain apps? How to control?