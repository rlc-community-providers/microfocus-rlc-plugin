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
import com.microfocus.jenkins.plugins.rlc.model.RLCStep;
import com.microfocus.jenkins.plugins.rlc.utils.RLCUtils;
import hudson.*;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.FormValidation;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

/**
 * Create a new Release Package in Micro FOcus Release Control (via SBM)
 *
 * @author Kevin A. Lee
 */
public class CreateReleasePackageStep extends RLCStep {

    private String projectName;
    private String title;
    private String description;
    private Integer releaseTypeId;
    private Integer deploymentPathId;
    private Integer applicationId;
    private Integer releaseTrainId;
    private String messageLog;

    @DataBoundSetter
    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return this.projectName;
    }

    @DataBoundSetter
    public void setTitle(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    @DataBoundSetter
    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    @DataBoundSetter
    public void setReleaseTypeId(final Integer releaseTypeId) {
        this.releaseTypeId = releaseTypeId;
    }

    public Integer getReleaseTypeId() {
        return this.releaseTypeId;
    }

    @DataBoundSetter
    public void setDeploymentPathId(final Integer deploymentPathId) {
        this.deploymentPathId = deploymentPathId;
    }

    public Integer getDeploymentPathId() {
        return this.deploymentPathId;
    }

    @DataBoundSetter
    public void setApplicationId(final Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getReleaseTrainId() {
        return this.releaseTrainId;
    }

    @DataBoundSetter
    public void setReleaseTrainId(final Integer releaseTrainId) {
        this.releaseTrainId = releaseTrainId;
    }

    public Integer getApplicationId() {
        return this.applicationId;
    }

    @DataBoundSetter
    public void setMessageLog(final String messageLog) {
        this.messageLog = messageLog;
    }

    public String getMessageLog() {
        return this.messageLog;
    }

    @DataBoundConstructor
    public CreateReleasePackageStep(final String aeUrl, final String oeUrl) {
        this.setAeUrl(RLCUtils.rmSlashFromUrl(aeUrl));
        this.setOeUrl(RLCUtils.rmSlashFromUrl(oeUrl));
    }

    /**
     * Descriptor for {@link CreateReleasePackageStep}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See {@code src/main/resources/com/microfocus/jenkins/plugins/rlc/CreateReleasePackage/*.jelly}
     * for the actual HTML fragment for the configuration screen.
     * </p>
     */
    @Symbol("rlcCreateReleasePackage")
    @Extension
    public static class DescriptorImpl extends RLCDescriptorImpl {

        private FormValidation verifyProjectName(final String projectName) {
            if (StringUtils.isEmpty(projectName))
                return FormValidation.error("A Project Name is required");
            return FormValidation.ok();
        }

        private FormValidation verifyTitle(final String title) {
            if (StringUtils.isEmpty(title))
                return FormValidation.error("A Title is required");
            return FormValidation.ok();
        }

        private FormValidation verifyDescription(final String description) {
            if (StringUtils.isEmpty(description))
                return FormValidation.error("A Description is required");
            return FormValidation.ok();
        }

        private FormValidation verifyReleaseTypeId(final Integer releaseTypeId) {
            if (releaseTypeId == null || releaseTypeId == 0)
                return FormValidation.error("A Release Type Id is required");
            return FormValidation.ok();
        }

        private FormValidation verifyDeploymentPathId(final Integer deploymentPathId) {
            if (deploymentPathId == null || deploymentPathId == 0)
                return FormValidation.error("A Deployment Path Id is required");
            return FormValidation.ok();
        }

        // TODO: more validation of contents
        public FormValidation doCheckEventType(@QueryParameter final String value) {
            return FormValidation.ok();
        }

        @Override
        public String getDisplayName() {
            return "Micro Focus RLC Create Release";
        }
   }

    @Override
    public void perform(Run<?,?> run, FilePath workspace, Launcher launcher, TaskListener listener)
        throws InterruptedException, IOException {

        this.setLogger(listener.getLogger());

        //JenkinsEnvironment jenkinsEnvironment = new JenkinsEnvironment();
        //EnvVars envVars = run.getEnvironment(listener);
        //log(envVars.toString());

        //listener.hyperlink("http://localhost:8080", "http://localhost:8080");

        String resolvedProjectName = run.getEnvironment(listener).expand(getProjectName());
        String resolvedTitle = run.getEnvironment(listener).expand(getTitle());
        String resolvedDescription = run.getEnvironment(listener).expand(getDescription());
        String resolvedMessageLog = run.getEnvironment(listener).expand(getMessageLog());

        final UsernamePasswordCredentials usernamePasswordCredentials = RLCUtils.getUsernamePasswordCredentials(getCredentialsId());
        if (usernamePasswordCredentials == null) {
            throw new AbortException("Micro Focus RLC account credentials are not filled in.");
        }

        // check connection to Micro Focus DA
        RLCClient rlcClient = new RLCClient(
                this.getAeUrl(),
                this.getOeUrl(),
                usernamePasswordCredentials.getUsername(),
                usernamePasswordCredentials.getPassword());
        try {
            rlcClient.verifyConnection();
        } catch (Exception ex) {
            throw new AbortException("Unable to connect to Micro Focus Release Control: " + ex.toString());
        }

        try {
            log("Creating new release: " + title);;
            UriBuilder uriBuilder = UriBuilder.fromPath(getAeUrl()).path("jsonapi").path("submittoproject").path(resolvedProjectName);
            URI uri = uriBuilder.build();
            log("Submitting to: " + uri.toString());

            //{"transition":
            // {"TITLE":"Release of App 1","RLM_RELEASE_TYPE":2,
            // "DESCRIPTION":"Release of App 1 Description",
            // "RLM_DEPLOYMENT_PATH":1,
            // "RLM_MESSAGE_LOG":"some more comments",
            // "RLM_RELEASE_TRAIN":1,
            // "RLM_APPLICATION": 1},
            // "fixedFields": false, "fields": [ {"dbname":"ISSUEID"}]}
            String postBody = "{\"transition\": {" +
                    "\"TITLE\":\"" + resolvedTitle + "\"," +
                    "\"RLM_RELEASE_TYPE\":" + releaseTypeId.toString() + "," +
                    "\"RLM_DEPLOYMENT_PATH\":" + deploymentPathId.toString() + "," +
                    (applicationId == null || applicationId == 0 ? "" : "\"RLM_APPLICATION\":" + applicationId.toString() + ",") +
                    (releaseTrainId == null || releaseTrainId == 0 ? "" : "\"RLM_RELEASE_TRAIN\":" + releaseTrainId.toString() + ",") +
                    (resolvedMessageLog.equalsIgnoreCase("") ? "" : "\"RLM_MESSAGE\":\"" + resolvedMessageLog + "\",") +
                    "\"DESCRIPTION\":\"" + resolvedDescription + "\"" +
                "}," +
                "\"fixedFields\": false, \"fields\": [ {\"dbname\":\"ISSUEID\"}]}";
            //log(postBody);
            String jsonOut = rlcClient.executeJSONPost(uri, postBody);
            JSONObject itemObj = new JSONObject(jsonOut).optJSONObject("item");
            if (itemObj != null) {
                JSONObject idObj = itemObj.getJSONObject("id");
                String itemId = (String) idObj.get("itemId");
                String itemUrl = (String) idObj.get("url");
                log("Created new release package item with id: " + itemId);
                log("See: " + itemUrl + "?shell=swc");
            }
        } catch (Exception ex) {
            throw new AbortException("Unable to Create Release: " + ex.toString());
        }

    }
}

