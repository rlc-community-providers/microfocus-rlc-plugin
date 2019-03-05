# Micro Focus Release Control plugin for Jenkins 2.x

This plugin allows [Micro Focus Release Control](https://www.microfocus.com/products/release-control/) (RLC) to be used in and invoked from [Jenkins Pipelines](https://jenkins.io/solutions/pipeline/). It is a community supported plugin and provides the following ad-hoc build steps:

- [x] **Create Release Package** - create a new Release Package from Jenkins
- [x] **Get Release Train State** - get the state of a Release Train
- [x] **Get Release Package State** - get the state of a Release Package
- [x] **Send ALF Event** - Send an ALF event that could be used to carry out additional automation from RLC (using the SBM platform Orchestration Engine)

Micro Focus Release Control can be used across the development lifecycle but is typically used to automate releases into controlled environments (Staging/Pre-Prod/Production) and has additional enterprise capabilities above and beyond Jenkins to support this. 

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
## Usage Instructions

* Download the latest hpi from the [release](https://github.com/rlc-community-providers/microfocus-rlc-plugin/tree/master/release) directory.

* Install into Jenkins using `Manage Jenkins -> Manage Plugins -> Advanced - Upload Plugin` and browse to hpi downloaded above.

* Restart Jenkins for the plugin to be available.

* Create a new profile for `RLC Server` from the `Manage Jenkins -> Configure System` page. In a new RLC instance the Release Package and Release Train Table Id's should be correct but would need to updated in an existing SBM instance.
  Note: This site is referred to in the steps above as "localhost - admin".
  
* Create or update a Jenkins Pipeline and reference the RLC steps. You can create a parameterized build so that the step parameters are used consistently - see this [Example Pipeline](https://raw.githubusercontent.com/rlc-community-providers/microfocus-rlc-plugin/master/doc/pipeline.txt) 

## Build Instructions

* Clone the repository from github.

* Run the plugin in test Jenkins instance:

```
mvn hpi:run -Djetty.port=8090
```

* Browse to **http://localhost:8090/jenkins** to test the plugin. 

* Create a new profile as above.

Note: you will have to install the [Jenkins Pipeline](https://wiki.jenkins-ci.org/display/JENKINS/Pipeline+Plugin) plugins to be able to create pipelines.

## Release Notes

##### 0.1.2

 - Removed ability to infinitely check for status - thus blocking Jenkins
 - Refactored dependencies
 
##### 0.1.1

 - Allowed checking for multiple States in `rlcGetReleasePackageState` and `rlcGetReleaseTrainState`
