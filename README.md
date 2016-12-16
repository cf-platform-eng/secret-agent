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
1. check out and build the [multi](https://github.com/cf-platform-eng/secret-agent/tree/multi) branch of the secret agent. It is [configured](https://github.com/cf-platform-eng/secret-agent/blob/multi/agent/src/main/resources/application.properties) so that the agent is not listening on the "usual ports and IP" (so it will not conflict with the passthrough and can't be reached fromoutside the container).
1. Use the [just-agent](https://github.com/cf-platform-eng/basic-boot-buildpack/tree/just-agent) branch of the custom buildpack created above: it is set to run just the agent.
1. copy the passthrough jar file and the [multi-buildpack.yml](https://github.com/cf-platform-eng/secret-agent/blob/multi/passthrough/multi-buildpack.yml) file to a new, empty temp directory
1. from the new temp directory do a cf push

  ```bash
  cf push passthrough
  ```
  
1. Validate that the passthrough is able to cummunicate with the agent

  ```bash
  curl -i http://passthrough.your.domain/hello
  
  HTTP/1.1 200 OK
  Content-Type: application/json;charset=UTF-8
  Date: Fri, 16 Dec 2016 14:44:13 GMT
  X-Vcap-Request-Id: 255cdcd7-cd29-4d17-4313-ca2bfe074f4a
  Content-Length: 37

  {"hello from 10.254.0.50":2147483647}
  ```
  The "hello" response is coming from the agent, proxied by the passthrough.

####Pros
 * Multi-builpack is supposed to be the officially supported way to do this going forward

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
