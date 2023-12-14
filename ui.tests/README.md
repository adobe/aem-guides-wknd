
UI Testing module for WKND.stie
===

This folder contains a set of sample cypress UI tests that following good practices for AEMCS based on the tests 
provided in the [archetype](https://github.com/adobe/aem-project-archetype/tree/develop/src/main/archetype/ui.tests.cypress).

Note: this set of tests are intended to be used on AEMCS and have only been verified using a Cloud instance

## Structure

- `Dockerfile` commands to assemble the image
- `pom.xml` defines project dependencies and build configuration which will be used by Cloud Manager to build the test module image
- `/test-module` The test project (add your tests there)

### Dockerfile

Sample dockerfile is based on the `cypress/included` [image](https://hub.docker.com/r/cypress/included), which
provides all the dependencies and the binaries to run cypress tests.

### xvfb setup

>When running several Cypress instances in parallel, the spawning of multiple X11 servers at once can cause
> problems for some of them. In this case, you can separately start a single X11 server and pass the server's
> address to each Cypress instance using the DISPLAY environment variable.

The setup described in [the documentation](https://docs.cypress.io/guides/continuous-integration/introduction#In-Docker)
is implemented in `run.sh`,  used as the _entrypoint_ to the container.

## Execute test module

### Locally (standalone)

Refer to [test-module/README.md](test-module/README.md).

### Using the test image

In the Cloud Manager pipeline the test image will be build and executed, this behaviour can also be recreated.

#### Requirements

* Maven
* Docker
* Latest version of Chrome installed locally in default location
* Sample application deployed on your AEMCS environment (Author + Publish)

#### Build and run the test image

The image built from the Dockerfile can be used to execute tests locally against an AEM environment. The `ui-tests-docker-execution`
maven profile will start the docker-compose setup starting Cypress and the test module, executing the tests against
the AEM instance defined via environment variables. The test results will be stored in the `./target/reports` directory.

The following environment variables (AEM UI test convention) can be passed

| envvar | default |
| --- | --- |
| AEM_AUTHOR_URL | https://author-p***-e***.adobeaemcloud.com |
| AEM_AUTHOR_USERNAME | `admin` |
| AEM_AUTHOR_PASSWORD | `admin` |
| AEM_PUBLISH_URL | https://publish-p***-e***.adobeaemcloud.com|
| AEM_PUBLISH_USERNAME | `admin` |
| AEM_PUBLISH_PASSWORD | `admin` |
| REPORTS_PATH | `cypress/results` |


1. Build the Docker UI test image with below command
   ```
   mvn clean package -Pui-tests-docker-build
   ```

2. Run the test (set environment variables accordingly)
   ```
   mvn verify -Pui-tests-docker-execution \
   -DAEM_AUTHOR_URL=${AEM_AUTHOR_URL} \
   -DAEM_AUTHOR_USERNAME=${AEM_AUTHOR_USERNAME} \
   -DAEM_AUTHOR_PASSWORD=${AEM_AUTHOR_PASSWORD} \
   -DAEM_PUBLISH_URL=${AEM_PUBLISH_URL} \
   -DAEM_PUBLISH_USERNAME=${AEM_PUBLISH_USERNAME} \
   -DAEM_PUBLISH_PASSWORD=${AEM_PUBLISH_PASSWORD}   
   ```
