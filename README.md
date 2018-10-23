# Micro Focus Release Control plugin for Jenkins 2.x

This plugin allows [Micro Focus Release Control](https://www.microfocus.com/products/release-control/) to be used in [Jenkins Pipelines](https://jenkins.io/solutions/pipeline/).
It provides the following adhoc build steps:

- Create Release Package 
- Get Release Train State
- Ger Release Package State
- Send ALF Event

Micro Focus Release Control can be used across the development lifecycle but is typically used to automate releases into controlled environments (Staging/Pre-Prod/Production) and has additional enterprise capabilities above and beyond
Jenkins to support this. 

## Example pipeline code

Below is an example of some pipeline code that uses these steps.
```
env.RLC_SITE_NAME = 'localhost - admin'
env.RLC_RP_PROJECT_NAME = 'TRM_RLM_TURNOVERS.TURNOVERS'

def rpId = rlcCreateReleasePackage applicationId: 1, deploymentPathId: 1, 
    description: "Release of ${BUILD_NUMBER}", messageLog: "from ${JENKINS_URL}", 
    projectName: "${RLC_RP_PROJECT_NAME}", releaseTrainId: 2, releaseTypeId: 1, 
    siteName: "${RLC_SITE_NAME}", 
    title: "Release ${BUILD_NUMBER}"
println rpId
def packageState = rlcGetReleasePackageState delayInterval: 5000, desiredState: 'Completed', 
    maxRetries: -1, releasePackageId: ${rpId}, 
    siteName: "${RLC_SITE_NAME}", waitForState: true
println packageState
def rtState = rlcGetReleaseTrainState delayInterval: 5000, desiredState: 'QA', maxRetries: -1, 
    releaseTrainId: 1 siteName: "${RLC_SITE_NAME}", waitForState: true
println rtState
rlcSendALFEvent eventId: '', eventType: 'Pending', objectId: "${BUILD_NUMBER}", 
    objectType: 'Job', siteName: "${RLC_SITE_NAME}"
```
![Example Pipeline](https://github.com/jenkinsci/microfocus-RLC-plugin/images/jenkins-pipeline.png)

## Build/Usage Instructions

* Clone the repository from github.

* Run the plugin in test Jenkins instance:

```
mvn hpi:run -Djetty.port=8090
```

* Browse to **http://localhost:8090/jenkins** to test the plugin. 

* Create a new Site Profile from the Jenkins Global Configuration page to connect to Micro Focus RLC, and that will be referred to in the steps - "localhost - admin" in the above example.

Note: you will have to install the [Jenkins Pipeline](https://wiki.jenkins-ci.org/display/JENKINS/Pipeline+Plugin) plugins to be able to create pipelines.

##Release Notes

#####0.1-SNAPSHOT
*The plugin has not yet been completed or validated and so is not yet available for installation directly from the Jenkins
plugin repository. To try the plugin, please build locally using the above instructions and install the generated ".hpi" file directory.*

