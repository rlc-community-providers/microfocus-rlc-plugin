# Micro Focus Release Control plugin for Jenkins 2.x

This plugin allows [Micro Focus Release Control](https://www.microfocus.com/products/release-control/) (RLC) to be 
used in and invoked from [Jenkins Pipelines](https://jenkins.io/solutions/pipeline/). It is a community supported
plugin and provides the following adhoc build steps:

- [x] Create Release Package - create a new Release Package from Jenkins
- [x] Get Release Train State - get the state of a Release Train
- [x] Get Release Package State - get the state of a Release Package
- [x] Send ALF Event - Send an ALF event that could be used to carry out additional automation from SBM/RLC

Micro Focus Release Control can be used across the development lifecycle but is typically used to automate releases into controlled environments (Staging/Pre-Prod/Production) and has additional enterprise capabilities above and beyond
Jenkins to support this. 

## Example pipeline code

Below is an example of some pipeline code that uses these steps.
```
def rlcSite = 'localhost - admin'
def rlcProjectName = 'TRM_RLM_TURNOVERS.TURNOVERS'
def appVersion = '1.0'

def rpId =  'com.microfocus.jenkins.plugins.rlc.CreateReleasePackageStep'(
       siteName: "${rlcSite}",
       title: "Application R${appVersion}.${BUILD_NUMBER}",
       description: "Release of Application - ${appVersion}.${BUILD_NUMBER}",
       projectName: "${rlcProjectName}",
       releaseTypeId: 1,
       applicationId: 1,
       deploymentPathId: 1,
       releaseTrainId: 1,
       messageLog: "Created automatically from Jenkins ${env.BUILD_URL}"
   )
println rpId
def rpState = 'com.microfocus.jenkins.plugins.rlc.GetReleasePackageStateStep'(
       siteName: "${rlcSite}",
       releasePackageId: "${rpId}",
       desiredState: 'Completed,Closed',
       waitForState: true,
       delayInterval: 5000,
       maxRetries: 5
   )
println rpState   
```
![Example Pipeline](https://raw.githubusercontent.com/rlc-community-providers/microfocus-rlc-plugin/master/doc/pipeline.txt)

## Usage Instructions

* Download the latest hpi from the ![release](https://raw.githubusercontent.com/rlc-community-providers/microfocus-rlc-plugin/master/release) directory.

* Install into Jenkins using Jenkins Configuration - Plugins - Upload Plugin

* Create a new Site Profile from the Jenkins Global Configuration page to connect to Micro Focus RLC, and that will be referred to in the steps - "localhost - admin" in the above example.


## Build Instructions

* Clone the repository from github.

* Run the plugin in test Jenkins instance:

```
mvn hpi:run -Djetty.port=8090
```

* Browse to **http://localhost:8090/jenkins** to test the plugin. 

* Create a new Site Profile from the Jenkins Global Configuration page to connect to Micro Focus RLC, and that will be referred to in the steps - "localhost - admin" in the above example.

Note: you will have to install the [Jenkins Pipeline](https://wiki.jenkins-ci.org/display/JENKINS/Pipeline+Plugin) plugins to be able to create pipelines.

##Release Notes

#####0.1.2

 - Removed ability to infinitely check for status - thus blocking Jenkins
 - Refactored dependencies
 
#####0.1.1

 - Allowed checking for multiple States in `rlcGetReleasePackageState` and `rlcGetReleaseTrainState`

#####0.1-SNAPSHOT
*The plugin has not yet been completed or validated and so is not yet available for installation directly from the Jenkins
plugin repository. To try the plugin, please build locally using the above instructions and install the generated ".hpi" file directory.*

