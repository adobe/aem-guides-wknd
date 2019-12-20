# WKND Sites Project

This is the code companion to a multi-part series on Adobe Docs:

### [Getting Started with AEM Sites - WKND Tutorial](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/overview.html)

1. [Project Setup](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/project-setup.html)
2. [Component Basics](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/component-basics.html)
3. [Pages and Templates](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/pages-templates.html)
4. [Client-Side Libraries](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/client-side-libraries.html)
5. [Style a Component](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/style-system.html)
6. [Custom Component](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/custom-component.html)
7. [Unit Testing](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/unit-testing.html)

## Modules

The main parts of the project are:

* core: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* ui.apps: contains the /apps (and /etc) parts of the project, ie JS&CSS clientlibs, components, templates, runmode specific configs as well as Hobbes-tests
* ui.content: contains sample content using the components from the ui.apps
* ui.tests: Java bundle containing JUnit tests that are executed server-side. This bundle is not to be deployed onto production.
* ui.launcher: contains glue code that deploys the ui.tests bundle (and dependent bundles) to the server and triggers the remote JUnit execution

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

If you have a running AEM instance you can build and package the whole project and deploy into AEM with  

    mvn clean install -PautoInstallPackage

Depending on your maven configuration, you may find it helpful to force the resolution of the Adobe public repo with

    mvn clean install -PautoInstallPackage -Padobe-public
    
Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallPackagePublish
    
Or alternatively

    mvn clean install -PautoInstallPackage -Daem.port=4503

Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle

## Testing

There are three levels of testing contained in the project:

* unit test in core: this show-cases classic unit testing of the code contained in the bundle. To test, execute:

    ```
    mvn clean test
    ```

* server-side integration tests: this allows to run unit-like tests in the AEM-environment, ie on the AEM server. To test, execute:

    ```
    mvn clean verify -PintegrationTests
    ```

* client-side Hobbes.js tests: JavaScript-based browser-side tests that verify browser-side behavior. To test, go in the browser, open the page in 'Developer mode', open the left panel and switch to the 'Tests' tab and find the generated 'MyName Tests' and run them.


## Maven settings

The project comes with the auto-public repository configured. To setup the repository in your Maven settings, refer to:

    http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html
