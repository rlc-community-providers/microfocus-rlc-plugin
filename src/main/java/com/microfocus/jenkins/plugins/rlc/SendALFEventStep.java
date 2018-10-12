/* ===========================================================================
 *  Copyright (c) 2017 Micro Focus. All rights reserved.
 *
 *  Use of the Sample Code provided by Micro Focus is governed by the following
 *  terms and conditions. By using the Sample Code, you agree to be bound by
 *  the terms contained herein. If you do not agree to the terms herein, do
 *  not install, copy, or use the Sample Code.
 *
 *  1.  GRANT OF LICENSE.  Subject to the terms and conditions herein, you
 *  shall have the nonexclusive, nontransferable right to use the Sample Code
 *  for the sole purpose of developing applications for use solely with the
 *  Micro Focus software product(s) that you have licensed separately from Micro Focus.
 *  Such applications shall be for your internal use only.  You further agree
 *  that you will not: (a) sell, market, or distribute any copies of the
 *  Sample Code or any derivatives or components thereof; (b) use the Sample
 *  Code or any derivatives thereof for any commercial purpose; or (c) assign
 *  or transfer rights to the Sample Code or any derivatives thereof.
 *
 *  2.  DISCLAIMER OF WARRANTIES.  TO THE MAXIMUM EXTENT PERMITTED BY
 *  APPLICABLE LAW, SERENA PROVIDES THE SAMPLE CODE AS IS AND WITH ALL
 *  FAULTS, AND HEREBY DISCLAIMS ALL WARRANTIES AND CONDITIONS, EITHER
 *  EXPRESSED, IMPLIED OR STATUTORY, INCLUDING, BUT NOT LIMITED TO, ANY
 *  IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY, OF FITNESS FOR A
 *  PARTICULAR PURPOSE, OF LACK OF VIRUSES, OF RESULTS, AND OF LACK OF
 *  NEGLIGENCE OR LACK OF WORKMANLIKE EFFORT, CONDITION OF TITLE, QUIET
 *  ENJOYMENT, OR NON-INFRINGEMENT.  THE ENTIRE RISK AS TO THE QUALITY OF
 *  OR ARISING OUT OF USE OR PERFORMANCE OF THE SAMPLE CODE, IF ANY,
 *  REMAINS WITH YOU.
 *
 *  3.  EXCLUSION OF DAMAGES.  TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE
 *  LAW, YOU AGREE THAT IN CONSIDERATION FOR RECEIVING THE SAMPLE CODE AT NO
 *  CHARGE TO YOU, SERENA SHALL NOT BE LIABLE FOR ANY DAMAGES WHATSOEVER,
 *  INCLUDING BUT NOT LIMITED TO DIRECT, SPECIAL, INCIDENTAL, INDIRECT, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, DAMAGES FOR LOSS OF
 *  PROFITS OR CONFIDENTIAL OR OTHER INFORMATION, FOR BUSINESS INTERRUPTION,
 *  FOR PERSONAL INJURY, FOR LOSS OF PRIVACY, FOR NEGLIGENCE, AND FOR ANY
 *  OTHER LOSS WHATSOEVER) ARISING OUT OF OR IN ANY WAY RELATED TO THE USE
 *  OF OR INABILITY TO USE THE SAMPLE CODE, EVEN IN THE EVENT OF THE FAULT,
 *  TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY, OR BREACH OF CONTRACT,
 *  EVEN IF SERENA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.  THE
 *  FOREGOING LIMITATIONS, EXCLUSIONS AND DISCLAIMERS SHALL APPLY TO THE
 *  MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW.  NOTWITHSTANDING THE ABOVE,
 *  IN NO EVENT SHALL SERENA'S LIABILITY UNDER THIS AGREEMENT OR WITH RESPECT
 *  TO YOUR USE OF THE SAMPLE CODE AND DERIVATIVES THEREOF EXCEED US$10.00.
 *
 *  4.  INDEMNIFICATION. You hereby agree to defend, indemnify and hold
 *  harmless Micro Focus from and against any and all liability, loss or claim
 *  arising from this agreement or from (i) your license of, use of or
 *  reliance upon the Sample Code or any related documentation or materials,
 *  or (ii) your development, use or reliance upon any eventId or
 *  derivative work created from the Sample Code.
 *
 *  5.  TERMINATION OF THE LICENSE.  This agreement and the underlying
 *  license granted hereby shall terminate if and when your license to the
 *  applicable Micro Focus software product terminates or if you breach any terms
 *  and conditions of this agreement.
 *
 *  6.  CONFIDENTIALITY.  The Sample Code and all information relating to the
 *  Sample Code (collectively "Confidential Information") are the
 *  confidential information of Micro Focus.  You agree to maintain the
 *  Confidential Information in strict confidence for Micro Focus.  You agree not
 *  to disclose or duplicate, nor allow to be disclosed or duplicated, any
 *  Confidential Information, in whole or in part, except as permitted in
 *  this Agreement.  You shall take all reasonable steps necessary to ensure
 *  that the Confidential Information is not made available or disclosed by
 *  you or by your employees to any other person, firm, or corporation.  You
 *  agree that all authorized persons having access to the Confidential
 *  Information shall observe and perform under this nondisclosure covenant.
 *  You agree to immediately notify Micro Focus of any unauthorized access to or
 *  possession of the Confidential Information.
 *
 *  7.  AFFILIATES.  Micro Focus as used herein shall refer to Micro Focus,
 *  and its affiliates.  An entity shall be considered to be an
 *  affiliate of Micro Focus if it is an entity that controls, is controlled by,
 *  or is under common control with Micro Focus.
 *
 *  8.  GENERAL.  Title and full ownership rights to the Sample Code,
 *  including any derivative works shall remain with Micro Focus.  If a court of
 *  competent jurisdiction holds any provision of this agreement illegal or
 *  otherwise unenforceable, that provision shall be severed and the
 *  remainder of the agreement shall remain in full force and effect.
 * ===========================================================================
 */

package com.microfocus.jenkins.plugins.rlc;

import com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials;
import com.microfocus.jenkins.plugins.rlc.client.RLCClient;
import com.microfocus.jenkins.plugins.rlc.model.ALFEvent;
import com.microfocus.jenkins.plugins.rlc.model.JenkinsEnvironment;
import com.microfocus.jenkins.plugins.rlc.model.RLCStep;
import com.microfocus.jenkins.plugins.rlc.utils.RLCUtils;
import hudson.*;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.FormValidation;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import java.io.IOException;

/**
 * Step to send an ALF Event to Release Control (via SBM)
 *
 * @author Kevin A. Lee
 */
public class SendALFEventStep extends RLCStep {

    private String eventId;
    private String eventType;
    private String objectId;
    private String objectType;

    @DataBoundSetter
    public void setEventId(final String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return this.eventId;
    }

    @DataBoundSetter
    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return this.eventType;
    }

    @DataBoundSetter
    public void setObjectId(final String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return this.objectId;
    }

    @DataBoundSetter
    public void setObjectType(final String objectType) {
        this.objectType = objectType;
    }

    public String getObjectType() {
        return this.objectType;
    }

    @DataBoundConstructor
    public SendALFEventStep(final String url) {
        this.setUrl(RLCUtils.rmSlashFromUrl(url));
    }


    /**
     * Descriptor for {@link SendALFEventStep}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See {@code src/main/resources/com/microfocus/jenkins/plugins/rlc/SendALFEvent/*.jelly}
     * for the actual HTML fragment for the configuration screen.
     * </p>
     */
    @Symbol("rlcSendALFEvent")
    @Extension
    public static class DescriptorImpl extends RLCDescriptorImpl {

        private FormValidation verifyEventId(final String eventId) {
            if (StringUtils.isEmpty(eventId))
                return FormValidation.error("An eventId is required");
            return FormValidation.ok();
        }

        private FormValidation verifyEventType(final String eventType) {
            if (StringUtils.isEmpty(eventType))
                return FormValidation.error("An eventType is required");
            return FormValidation.ok();
        }

        private FormValidation verifyObjectId(final String objectId) {
            if (StringUtils.isEmpty(objectId))
                return FormValidation.error("An objectId is required");
            return FormValidation.ok();
        }

        private FormValidation verifyObjectType(final String objectType) {
            if (StringUtils.isEmpty(objectType))
                return FormValidation.error("An objectType is required");
            return FormValidation.ok();
        }

        // TODO: more validation of contents
        public FormValidation doCheckEventType(@QueryParameter final String value) {
            return FormValidation.ok();
        }

        @Override
        public String getDisplayName() {
            return "Micro Focus RLC Send ALF Event";
        }
   }

    @Override
    public void perform(Run<?,?> run, FilePath workspace, Launcher launcher, TaskListener listener)
        throws InterruptedException, IOException {

        this.setLogger(listener.getLogger());

        JenkinsEnvironment jenkinsEnvironment = new JenkinsEnvironment();
        EnvVars envVars = run.getEnvironment(listener);
        //log(envVars.toString());

        //listener.hyperlink("http://localhost:8080", "http://localhost:8080");

        // TODO: must be a better way with reflection?
        jenkinsEnvironment.setBranchName(envVars.get("BRANCH_NAME", ""));
        jenkinsEnvironment.setChangeId(envVars.get("CHANGE_ID", ""));
        jenkinsEnvironment.setChangeTitle(envVars.get("CHANGE_TITLE", ""));
        jenkinsEnvironment.setChangeAuthor(envVars.get("CHANGE_AUTHOR", ""));
        jenkinsEnvironment.setChangeAuthorDisplayName(envVars.get("CHANGE_AUTHOR_DISPLAY_NAME", ""));
        jenkinsEnvironment.setChangeAuthorEmail(envVars.get("CHANGE_AUTHOR_EMAIL", ""));
        jenkinsEnvironment.setChangeTarget(envVars.get("CHANGE_TARGET", ""));
        jenkinsEnvironment.setBuildNumber(envVars.get("BUILD_NUMBER", ""));
        jenkinsEnvironment.setBuildId(envVars.get("BUILD_ID", ""));
        jenkinsEnvironment.setBuildDisplayName(envVars.get("BUILD_DISPLAY_NAME", ""));
        jenkinsEnvironment.setJobName(envVars.get("JOB_NAME", ""));
        jenkinsEnvironment.setJobBaseName(envVars.get("JOB_BASE_NAME", ""));
        jenkinsEnvironment.setBuildTag(envVars.get("BUILD_TAG", ""));
        jenkinsEnvironment.setExecutionNumber(envVars.get("EXECUTION_NUMBER", ""));
        jenkinsEnvironment.setNodeName(envVars.get("NODE_NAME", ""));
        jenkinsEnvironment.setNodeLabels(envVars.get("NODE_LABELS", ""));
        jenkinsEnvironment.setWorkspace(envVars.get("WORKSPACE", ""));
        jenkinsEnvironment.setJenkinsHome(envVars.get("JENKINS_HOME", ""));
        jenkinsEnvironment.setJenkinsUrl(envVars.get("JENKINS_URL", ""));
        jenkinsEnvironment.setBuildUrl(envVars.get("BUILD_URL", ""));
        jenkinsEnvironment.setJobUrl(envVars.get("JOB_URL", ""));
        jenkinsEnvironment.setJavaHome(envVars.get("JAVA_HOME", ""));
        jenkinsEnvironment.setSvnRevision(envVars.get("SVN_REVISION", ""));
        jenkinsEnvironment.setCvsBranch(envVars.get("CVS_BRANCH", ""));
        jenkinsEnvironment.setGitCommit(envVars.get("GIT_COMMIT", ""));
        jenkinsEnvironment.setGitUrl(envVars.get("GIT_URL", ""));
        jenkinsEnvironment.setGitBranch(envVars.get("GIT_BRANCH", ""));

        String resolvedEventId = run.getEnvironment(listener).expand(getEventId());
        String resolvedEventType = run.getEnvironment(listener).expand(getEventType());
        String resolvedObjectId = run.getEnvironment(listener).expand(getObjectId());
        String resolvedObjectType = run.getEnvironment(listener).expand(getObjectType());

        final UsernamePasswordCredentials usernamePasswordCredentials = RLCUtils.getUsernamePasswordCredentials(getCredentialsId());
        if (usernamePasswordCredentials == null) {
            throw new AbortException("Micro Focus RLC account credentials are not filled in.");
        }

        // check connection to Micro Focus DA
        RLCClient RLCClient = new RLCClient(this.getUrl(),
                usernamePasswordCredentials.getUsername(),
                usernamePasswordCredentials.getPassword());
        try {
            RLCClient.verifyConnection();
        } catch (Exception ex) {
            throw new AbortException("Unable to connect to Micro Focus Release Control: " + ex.toString());
        }

        try {
            ALFEvent alfEvent = new ALFEvent();
            if (!StringUtils.isEmpty(resolvedEventId)) alfEvent.setEventId(resolvedEventId);
            if (!StringUtils.isEmpty(resolvedEventType)) alfEvent.setEventType(resolvedEventType);
            if (!StringUtils.isEmpty(resolvedObjectId)) alfEvent.setObjectId(resolvedObjectId);
            if (!StringUtils.isEmpty(resolvedObjectType)) alfEvent.setObjectType(resolvedObjectType);
            alfEvent.setUsername(usernamePasswordCredentials.getUsername());
            alfEvent.setPassword(usernamePasswordCredentials.getPassword().getPlainText());
            alfEvent.setJenkinsEnvironment(jenkinsEnvironment);
            log("Sending: " + alfEvent.toString());
            RLCClient.generateALFEvent(alfEvent);
            log("Sent.");
        } catch (Exception ex) {
            throw new AbortException("Unable to send ALF Event: " + ex.toString());
        }

    }
}

