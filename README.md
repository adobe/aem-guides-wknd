# WKND Sites Project

This is the code for the WKND Reference site: [https://www.wknd.site/](https://www.wknd.site/)

There is also a corresponding tutorial where you can learn how to implement a website using the latest standards and technologies in Adobe Experience Manager (AEM): 

1. [WKND Tutorial Overview](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/overview.html)
2. [Project Setup](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/project-setup.html)
3. [Component Basics](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/component-basics.html)
4. [Pages and Templates](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/pages-templates.html)
5. [Client-Side Libraries](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/client-side-libraries.html)
6. [Style a Component](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/style-system.html)
7. [Custom Component](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/custom-component.html)
8. [Unit Testing](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-wknd-tutorial-develop/unit-testing.html)

## Modules

The main parts of the project are:

* **core**: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* **ui.apps**: contains the /apps (and /etc) parts of the project, ie JS & CSS clientlibs, components, templates, runmode specific configs as well as Hobbes-tests
* **ui.content**: contains mutable content (not /apps) that is integral to the running of the WKND site. This include template types, templates, policies and base-line organization page and asset structures.
* **ui.content.sample**: WKND is often used as a pre-built reference site for demos and training; making it useful to have a full sample site with content and assets. HOWEVER the storage of authored content (pages, assets) in git is rare and not recommended for real-world implementations.
* **ui.tests**: Java bundle containing JUnit tests that are executed server-side. This bundle is not to be deployed onto production.
* **ui.launcher**: contains glue code that deploys the ui.tests bundle (and dependent bundles) to the server and triggers the remote JUnit execution
* **dispatcher**: contains dispatcher configurations for AEM as a Cloud Service
* **repository-structure**:  Empty package that defines the structure of the Adobe Experience Manager repository the Code packages in this project deploy into.
* **all**: An empty module that embeds the above sub-modules and any vendor dependencies into a single deployable package.

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

To build all the modules and deploy the `all` package to a local instance of AEM, run in the project root directory the following command:

    mvn clean install -PautoInstallSinglePackage

Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallSinglePackagePublish

Or alternatively

    mvn clean install -PautoInstallSinglePackage -Daem.port=4503

Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle

Or to deploy only a single content package, run in the sub-module directory (i.e `ui.apps`)

    mvn clean install -PautoInstallPackage

### Building for AEM 6.x.x

The project has been designed for **AEM as a Cloud Service**. The project is also backward compatible with AEM **6.4.8** and **6.5.5** by adding the `classic` profile when executing a build, i.e:

    mvn clean install -PautoInstallSinglePackage -Pclassic

#### When using an IDE

When using an IDE like IntelliJ, please make sure to check `classic` in your Maven Profile tab.

Example screenshot:

![maven profile tab with classic option checked](https://experienceleague.adobe.com/docs/experience-manager-learn/assets/intelliJMavenProfiles.png)

### WKND Sample content

By default, sample content from `ui.content.sample` will be deployed and installed along with the WKND code base. The WKND reference site is used for demo and training purposes and having a pre-built, fully authored site is useful. However, the behavior of including a full reference site (pages, images, etc...) in source control is *unusual* and is **not** recommended for a real-world implementation.

Including `ui.content.sample` will **overwrite** any authored content during each build. If you wish to disable this behavior modify the [filter.xml](ui.content.sample/src/main/content/META-INF/vault/filter.xml) file and update the `mode=merge` attribute to avoid overwriting the paths.

```diff
- <filter root="/content/wknd" />
+ <filter root="/content/wknd" mode="merge"/>
```

### Upgrading versions

If upgrading to a new version of WKND, it is recommended up modify the filters in `ui.content.sample` to remove the `mode="merge"` attribute prior to deploying.

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
