# Release Process

## Before you release

Make sure that all issues assigned to the current milestone have been closed and all necessary pull requests have been merged and closed out.  Don't proceed with the release until you know what you're releasing.

## Prerequisites

* You must have commit rights on this repository.
* You must have deploy rights to [Sonatype's SS Repository Hosting (OSSRH)](https://central.sonatype.org/publish/publish-guide/)

## Release via GitHub Action

### Run Release to OSSRH GitHub Action

Release for this project is performed via GitHub Actions. The source file for the release is stored at: `.github/workflows/maven-release.yml` and dictates the workflow **Release to OSSRH**.

1. Navigate to the repo in GitHub > [Actions](https://github.com/adobe/aem-guides-wknd/actions).
1. Under **Workflows** click **Release to OSSRH**
1. Choose the branch `main`. The workflow only runs against `main`.
1. Choose a release version. An odd/even version patterning is used. So for a minor release, if the current version is `1.1.0-SNAPSHOT` enter `1.2.0`.
1. Set **Dry Run** to **true** (default) and run the workflow. Ensure that the dry-run release executes successfully.
1. Repeat the above steps, but set **Dry Run** to **false**. This performs the actual release and deploys the release artifacts to a staging repository in https://oss.sonatype.org/

### Release Staging repository

Next release the artifacts in the staging repository to be deployed to Maven central.

1. Navigate to https://oss.sonatype.org/ and Log in.
1. Navigate to **Staging Repositories** and find the WKND Guides stage repo.
1. Click **Release**. Releasing the staging repo will push the artifacts to Maven Central after a small delay (four hours for all mirrors to catch up).
1. Download the artifact `aem-guides-wknd.all-X.X.X.zip`.

## Release via Local Maven Release Plugin Commands

### Setup 

In your Maven settings.xml file (`~/.m2/settings.xml`), add a server entry with the id `ossrh`. The username is your [OSSRH](https://issues.sonatype.org/projects/OSSRH/issues) username/Sonatype JIRA ID.

The password must be [stored encrypted](https://maven.apache.org/guides/mini/guide-encryption.html#How_to_encrypt_server_passwords):

    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                          https://maven.apache.org/xsd/settings-1.0.0.xsd">
        ...
        <servers>
            <server>
                <id>ossrh</id>
                <username>jirausername</username>
                <password>*****</password>
            </server>
        </servers>
        ...
    </settings>

These credentials are used to deploy to [OSSRH][https://central.sonatype.org/pages/ossrh-guide.html].

In addition, you need to setup [GPG](https://central.sonatype.org/publish/requirements/gpg/) to create OpenPGP signatures. After, installing https://www.gnupg.org/download/ you need to create key pair (if you don't have one yet) and make sure that the public key is distributed via hkp://pool.sks-keyservers.net.

It is recommended that your private key is protected with a passphrase. You can persist the passphrase in the settings.xml as well

     <server>
         <!-- has the passphrase for the gpg signing in encrypted format: http://maven.apache.org/plugins/maven-gpg-plugin/sign-mojo.html#passphraseServerId -->
         <id>gpg.passphrase</id>
         <!-- passphrase for your private key -->
         <password>****</password>
    </server>
    
### Release Process

1. Make sure that the issues and pull requests are associated with the proper milestone -- anything open for the current release should be moved to the next release, either minor or patch depending on the nature of the issue.

2. Run the release: `mvn release:prepare` followed by `mvn release:perform`. You may need to pass `-Dgpg.passphrase=****` if your passphrase is not persisted in your `settings.xml`.

> Note* for macOS it may be necessary to include the following in your `~/.bash_profile` if you get an error like "Inappropriate ioctl for device macO"

```
GPG_TTY=$(tty)
export GPG_TTY
```

3. Go to https://github.com/adobe/aem-guides-wknd/releases and edit the release tag and update the release text. Add compiled AEM Packages for AEM as a Cloud Service (default build) and special classic build for 6.x.x.

4. Log into https://oss.sonatype.org/ and close the staging repository. Closing the staging repo will automatically push the artifacts to Maven Central after a small delay (four hours for all mirrors to catch up)

5. Add a release announcement (and any other docs) to the documentation site.

## Publish the Release

After you release **AEM WKND Sites Project** (aka aem-guides-wknd) using any one of the above methods, follow below steps to publish it.

1. Go to https://github.com/adobe/aem-guides-wknd/releases and edit the release tag and update the release text. Add compiled AEM Packages from the previous step.
1. Close any issues fixed in the release and create the next milestone.
1. Add a release announcement (and any other docs) to the documentation site.
