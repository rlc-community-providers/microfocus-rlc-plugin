package com.microfocus.jenkins.plugins.rlc.model;

public class JenkinsEnvironment {

    private String branchName;
    private String changeId;
    private String changeUrl;
    private String changeTitle;
    private String changeAuthor;
    private String changeAuthorDisplayName;
    private String changeAuthorEmail;
    private String changeTarget;
    private String buildNumber;
    private String buildId;
    private String buildDisplayName;
    private String jobName;
    private String jobBaseName;
    private String buildTag;
    private String executionNumber;
    private String nodeName;
    private String nodeLabels;
    private String workspace;
    private String jenkinsHome;
    private String jenkinsUrl;
    private String buildUrl;
    private String jobUrl;
    private String javaHome;
    private String svnRevision;
    private String cvsBranch;
    private String gitCommit;
    private String gitUrl;
    private String gitBranch;

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getChangeUrl() {
        return changeUrl;
    }

    public void setChangeUrl(String changeUrl) {
        this.changeUrl = changeUrl;
    }

    public String getChangeTitle() {
        return changeTitle;
    }

    public void setChangeTitle(String changeTitle) {
        this.changeTitle = changeTitle;
    }

    public String getChangeAuthor() {
        return changeAuthor;
    }

    public void setChangeAuthor(String changeAuthor) {
        this.changeAuthor = changeAuthor;
    }

    public String getChangeAuthorDisplayName() {
        return changeAuthorDisplayName;
    }

    public void setChangeAuthorDisplayName(String changeAuthorDisplayName) {
        this.changeAuthorDisplayName = changeAuthorDisplayName;
    }

    public String getChangeAuthorEmail() {
        return changeAuthorEmail;
    }

    public void setChangeAuthorEmail(String changeAuthorEmail) {
        this.changeAuthorEmail = changeAuthorEmail;
    }

    public String getChangeTarget() {
        return changeTarget;
    }

    public void setChangeTarget(String changeTarget) {
        this.changeTarget = changeTarget;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getBuildDisplayName() {
        return buildDisplayName;
    }

    public void setBuildDisplayName(String buildDisplayName) {
        this.buildDisplayName = buildDisplayName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobBaseName() {
        return jobBaseName;
    }

    public void setJobBaseName(String jobBaseName) {
        this.jobBaseName = jobBaseName;
    }

    public String getBuildTag() {
        return buildTag;
    }

    public void setBuildTag(String buildTag) {
        this.buildTag = buildTag;
    }

    public String getExecutionNumber() {
        return executionNumber;
    }

    public void setExecutionNumber(String executionNumber) {
        this.executionNumber = executionNumber;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeLabels() {
        return nodeLabels;
    }

    public void setNodeLabels(String nodeLabels) {
        this.nodeLabels = nodeLabels;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getJenkinsHome() {
        return jenkinsHome;
    }

    public void setJenkinsHome(String jenkinsHome) {
        this.jenkinsHome = jenkinsHome;
    }

    public String getJenkinsUrl() {
        return jenkinsUrl;
    }

    public void setJenkinsUrl(String jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
    }

    public String getBuildUrl() {
        return buildUrl;
    }

    public void setBuildUrl(String buildUrl) {
        this.buildUrl = buildUrl;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public String getJavaHome() {
        return javaHome;
    }

    public void setJavaHome(String javaHome) {
        this.javaHome = javaHome;
    }

    public String getSvnRevision() {
        return svnRevision;
    }

    public void setSvnRevision(String svnRevision) {
        this.svnRevision = svnRevision;
    }

    public String getCvsBranch() {
        return cvsBranch;
    }

    public void setCvsBranch(String cvsBranch) {
        this.cvsBranch = cvsBranch;
    }

    public String getGitCommit() {
        return gitCommit;
    }

    public void setGitCommit(String gitCommit) {
        this.gitCommit = gitCommit;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getGitBranch() {
        return gitBranch;
    }

    public void setGitBranch(String gitBranch) {
        this.gitBranch = gitBranch;
    }

    public JenkinsEnvironment() {}

    @Override
    public String toString() {
        return "JenkinsEnvironment{" +
                "branchName='" + branchName + '\'' +
                ", changeId='" + changeId + '\'' +
                ", changeUrl='" + changeUrl + '\'' +
                ", changeTitle='" + changeTitle + '\'' +
                ", changeAuthor='" + changeAuthor + '\'' +
                ", changeAuthorDisplayName='" + changeAuthorDisplayName + '\'' +
                ", changeAuthorEmail='" + changeAuthorEmail + '\'' +
                ", changeTarget='" + changeTarget + '\'' +
                ", buildNumber='" + buildNumber + '\'' +
                ", buildId='" + buildId + '\'' +
                ", buildDisplayName='" + buildDisplayName + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jobBaseName='" + jobBaseName + '\'' +
                ", buildTag='" + buildTag + '\'' +
                ", executionNumber='" + executionNumber + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", nodeLabels='" + nodeLabels + '\'' +
                ", workspace='" + workspace + '\'' +
                ", jenkinsHome='" + jenkinsHome + '\'' +
                ", jenkinsUrl='" + jenkinsUrl + '\'' +
                ", buildUrl='" + buildUrl + '\'' +
                ", jobUrl='" + jobUrl + '\'' +
                ", javaHome='" + javaHome + '\'' +
                ", svnRevision='" + svnRevision + '\'' +
                ", cvsBranch='" + cvsBranch + '\'' +
                ", gitCommit='" + gitCommit + '\'' +
                ", gitUrl='" + gitUrl + '\'' +
                ", gitBranch='" + gitBranch + '\'' +
                '}';
    }
}
