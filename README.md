# AEM WKND Sites Project

## Adobe Experience Manager compatibility

WKND versions are compatible with the following versions of Adobe Experience Manager:

| AEM version | WKND version |
|:------------|:------------------------------:|
| AEM as a Cloud Service | 3.x                 |
| 6.5 SP16    | 2.x, 3.x                       |

## Released artifacts

![Maven CI](https://github.com/adobe/aem-guides-wknd/actions/workflows/maven.yml/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.adobe.aem.guides/aem-guides-wknd/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.adobe.aem.guides/aem-guides-wknd)

This is a sample Adobe Experience Manager project for a full stack Sites implementation for a fictitious lifestyle brand, WKND.

![App screenshot](https://user-images.githubusercontent.com/8974514/119887685-489f7800-bee9-11eb-9db1-95c641e7c4ea.jpg)

## Live Demo

View the live demo at [https://www.wknd.site/](https://www.wknd.site/)

## Tutorial

A corresponding [tutorial is available](https://experienceleague.adobe.com/docs/experience-manager-learn/getting-started-wknd-tutorial-develop/overview.html) where you can learn how to implement a website using the latest standards and technologies in AEM Sites.

## How to use/install

### AEM as a Cloud Service

To deploy WKND to AEM as a Cloud Service, this project's source code must be deployed to AEM via Cloud Manager. 

1. Clone this Git repository
2. Push the `main` branch to your Cloud Manager's Git repository
3. Ensure a deployment pipline is configured to deploy the target branch/repo to the desired AEM as a Cloud Service env
4. Run the Cloud Manager pipeline
5. WKND will now be deployed to the target AEM as a Cloud Service environment

### Local development (AEM 6.5 or AEM SDK)

Pre-compiled AEM packages are available under the latest release for easy installation on local environments using [CRX Package Manager](http://localhost:4502/crx/packmgr/index.jsp)

* [`aem-guides-wknd.all-x.x.x.zip`](https://github.com/adobe/aem-guides-wknd/releases/latest): AEM as a Cloud Service, default build
* [`aem-guides-wknd.all-x.x.x-classic.zip`](https://github.com/adobe/aem-guides-wknd/releases/latest): AEM 6.5.x+

## How to build

For **AEM as a Cloud Service SDK**: 

```
$ cd aem-guides-wknd/
$ mvn clean install -PautoInstallSinglePackage
```

For **AEM 6.5.x**: 

```
$ cd aem-guides-wknd/
$ mvn clean install -PautoInstallSinglePackage -Pclassic
```

## System Requirements

WKND Version | AEM as a Cloud Service | AEM 6.5   | Java SE | Maven
-------------|------------------------|-----------|---------|---------
Latest (main branch)| Continual              | **6.5.13.0+** |  8, 11  | 3.3.9+
[v1.1.0](https://github.com/adobe/aem-guides-wknd/releases/tag/aem-guides-wknd-1.1.0) | Continual | 6.5.10+ | 8, 11 | 3.3.9+
[v1.0.0](https://github.com/adobe/aem-guides-wknd/releases/tag/aem-guides-wknd-1.0.0) | Continual | 6.5.4+ | 8, 11 | 3.3.9+

Setup your local development environment for [AEM as a Cloud Service SDK](https://experienceleague.adobe.com/docs/experience-manager-learn/cloud-service/local-development-environment-set-up/overview.html) or for [older versions of AEM](https://experienceleague.adobe.com/docs/experience-manager-learn/foundation/development/set-up-a-local-aem-development-environment.html).

## Notes

### WKND Sample Content

By default, sample content from `ui.content.sample` will be deployed and installed along with the WKND code base. The WKND reference site is used for demo and training purposes and having a pre-built, fully authored site is useful. However, the behavior of including a full reference site (pages, images, etc...) in source control is *unusual* and is **not** recommended for a real-world implementation.

Including `ui.content.sample` will **overwrite** any authored content during each build. If you wish to disable this behavior modify the [filter.xml](ui.content.sample/src/main/content/META-INF/vault/filter.xml) file and update the `mode=merge` attribute to avoid overwriting the paths.

```diff
- <filter root="/content/wknd" />
+ <filter root="/content/wknd" mode="merge"/>
```

### Powered by Adobe Stock

Many of the images in the WKND Reference website are from Adobe Stock and are Third Party Material as defined in the Demo Asset Additional Terms at https://www.adobe.com/legal/terms.html. If you want to use an Adobe Stock image for other purposes beyond viewing this demo website, such as featuring it on a website, or in marketing materials, you can purchase a license on Adobe Stock.

With Adobe Stock, you have access to more than 140 million high-quality, royalty-free images including photos, graphics, videos and templates to jumpstart your creative projects.

## Documentation

* This project was generated using the [AEM Project Archetype](https://experienceleague.adobe.com/docs/experience-manager-core-components/using/developing/archetype/overview.html).
* This project relies on [AEM Core Components](https://experienceleague.adobe.com/docs/experience-manager-core-components/using/introduction.html).
