# Micro Focus Release Control plugin for Jenkins 2.x

This plugin allows [Micro Focus Release Control](https://www.microfocus.com/products/release-control/) to be used in [Jenkins Pipelines](https://jenkins.io/solutions/pipeline/).
It provides the following adhoc build steps:

- Send ALF Event
- Create Release (TBD)
- Deploy Release (TBD)

The steps can be used in "freestyle" projects but are designed to be used in pipelines. Micro Focus Release can be used across the 
development lifecycle but is typically used to automate releases into controlled environments (Staging/Pre-Prod/Production) and has additional enterprise capabilities above and beyond
Jenkins to support this. 

## Example pipeline code

Below is an example of some pipeline code that creates new objectType in Deployment Automation and Release Control and deploys to a number of environments.
```
env.RLC_URL = 'http://localhost:8085'
env.RLC_CREDENTIALS_ID = 'microfocus-RLC-admin'
env.RLC_COMPONENT = "${JOB_BASE_NAME}"
env.RLC_APPLICATION = 'Ring2Park Microservices'
env.RLC_PROCESS = 'Deploy (Docker)'

def mvnHome
def gitCommitId

stage('Build') {
    node {
        git 'https://github.com/ring2park-microservices/web-ui.git'
        sh "git rev-parse HEAD > .git/commit-id"
        gitCommitId = readFile('.git/commit-id').trim()
        mvnHome = tool 'maven_3.3.9'
        if (isUnix()) {
            sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
        } else {
            bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
        }
    }
}

stage('Tests') {
    node {
        echo "Tests would go here..."
    }
}

stage('Publish') {
    node {
        docker.withRegistry('http://devops:5000', 'dmsys-docker') {
            def dockerApp = docker.build "${RLC_COMPONENT}:${BUILD_NUMBER}"
            dockerApp.push()
        }
        def verProperties = 
"""
job.url=${env.BUILD_URL}
jenkins.url=${env.JENKINS_URL}
git.commit.id=${gitCommitId}
"""
        RLCCreateVersion url: "${RLC_URL}",
            credentialsId: "${RLC_CREDENTIALS_ID}", 
            component: "${RLC_COMPONENT}", 
            version: "${RLC_COMPONENT}:${BUILD_NUMBER}", 
            extendedRLCta: verProperties,
            failIfVersionExists: false
        RLCAddFilesToVersion url: "${RLC_URL}",
            credentialsId: "${RLC_CREDENTIALS_ID}", 
            component: "${RLC_COMPONENT}", 
            version: "${RLC_COMPONENT}:${BUILD_NUMBER}",
            base: "${WORKSPACE}", 
            offset: "", 
            excludes: "", 
            includes: "Dockerfile\ntarget/*.jar"    
        RLCAddStatusToVersion url: "${RLC_URL}",
            credentialsId: "${RLC_CREDENTIALS_ID}", 
            component: "${RLC_COMPONENT}", 
            version: "${RLC_COMPONENT}:${BUILD_NUMBER}", 
            status: "Built"  
    }
}
       
stage('Integration') {
    deployToEnvironment("Integration", "Integrated", false)    
}

stage('UAT') {
    deployToEnvironment("UAT", "Acceptance Tested", true)    
}

stage('Production') {
    // We could do this:
    deployToEnvironment("Production", "Live", true)
    // but lets create an input step to be invoked from RLC via REST endpoint:
    //    http://server:port/jenkins/job/pipeline-release/${BUILD_NUMBER}/input/Released-${BUILD_NUMBER}/proceedEmpty
    input(id: "Released-${BUILD_NUMBER}", message: "Released build ${BUILD_NUMBER} to Production?")     
}

def deployToEnvironment(eventType, verStatus, waitForInput) {
    def deployProperties = """
job.url=${env.BUILD_URL}
jenkins.url=${env.JENKINS_URL}
remove.container=true
"""
    node {
        if (waitForInput) {
            input "Deploy to ${eventType}?"
        } else {
            echo "Proceeding with deployment to ${eventType}..."
        }  
        milestone()
        RLCRunApplicationProcess url: "${RLC_URL}",
            credentialsId: "${RLC_CREDENTIALS_ID}", 
            eventId: "${RLC_APPLICATION}", 
            objectId: "${RLC_PROCESS}", 
            eventType: "${eventType}",
            objectType: "${RLC_COMPONENT}=${RLC_COMPONENT}:${BUILD_NUMBER}",
            extendedRLCta: "${deployProperties}",
            onlyChangedVersions: false
        RLCAddStatusToVersion url: "${RLC_URL}",
            credentialsId: "${RLC_CREDENTIALS_ID}", 
            component: "${RLC_COMPONENT}",
            version: "${RLC_COMPONENT}:${BUILD_NUMBER}",
            status: "${verStatus}"
    }
}
```
![Example Pipeline](https://github.com/jenkinsci/microfocus-RLC-plugin/images/jenkins-pipeline.png)

## Build/Usage Instructions

1) Clone the repository from github.

2) Build the plugin:

```
mvn clean package
```

3) Run the plugin in test Jenkins instance:

```
mvn hpi:run -Djetty.port=8090
```

4) Browse to **http://localhost:8090/jenkins** to test the plugin. 

5) Create a new "credential" to connect to Micro Focus RLC, and that will be referred to in the steps - "microfocus-RLC-admin" in the above example.

Note: you will have to install the [Jenkins Pipeline](https://wiki.jenkins-ci.org/display/JENKINS/Pipeline+Plugin) and [Credentials](https://wiki.jenkins-ci.org/display/JENKINS/Credentials+Plugin) plugins to be able to create pipelines.

##Release Notes

#####0.1-SNAPSHOT
*The plugin has not yet been completed or valiRLCted and so is not yet available for installation directly from the Jenkins
plugin repository. To try the plugin, please build locally using the above instructions and install the generated ".hpi" file directory.*

