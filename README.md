Whydah-Admin-SDK
===============

![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/Cantara/Whydah-Admin-SDK) ![Build Status](https://jenkins.quadim.ai/buildStatus/icon?job=Whydah-Admin-SDK) ![GitHub commit activity](https://img.shields.io/github/commit-activity/y/Cantara/Whydah-Admin-SDK) [![Project Status: Active – The project has reached a stable, usable state and is being actively developed.](http://www.repostatus.org/badges/latest/active.svg)](http://www.repostatus.org/#active)  -
[![Known Vulnerabilities](https://snyk.io/test/github/Cantara/Whydah-Admin-SDK/badge.svg)](https://snyk.io/test/github/Cantara/Whydah-Admin-SDK)



A client library which aimed to make Whydah admin integration more easy and more resilient

 * XML and JSON parsing of Whydah datastructures sent over the wire.
 * Util library for all the frequent used API calls
 * SessionHandler for ApplicationSessions and User Sessions
 * Client logic for using Whydah Web SSO - SSOLoginWebapp (SSOLWA).
   * The Java SDK is in a really early stage, and is currently used to experiment with a new remoting approach to increase system resilliance
* Client logic for using administrative API in UserAdminService (UAS) for applications with appropriate rights
    * Used as a TEST driver for the new UAS admin API's


## Binaries

Binaries and dependency information for Maven, Ivy, Gradle and others can be found at [http://mvnrepo.cantara.no](http://mvnrepo.cantara.no/index.html#nexus-search;classname~Whydah).

Example for Maven:

```xml
        <dependency>
            <groupId>net.whydah.sso</groupId>
            <artifactId>Whydah-Admin-SDK</artifactId>
            <version>x.y.z</version>
        </dependency>
```


