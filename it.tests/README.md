# Integration Tests

This module contains integration tests. These tests will be run in an AEM as a Cloud Service environment as part of the CI/CD pipeline when moving from Stage to Production.

## Run Tests locally

GraphQLEndpointIT test relying on running publish instance with persisted query deployed, to deploy WKND content there run the following command:

```
mvn clean install -PautoInstallSinglePackagePublish
```

To execute the integration tests locally use the following command:

```
mvn clean verify -Plocal
```

The following values are used for the local testing:

* Author: localhost:4502
* Publish: localhost:4503
