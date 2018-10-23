/* ===========================================================================
 *  Copyright (c) 2018 Micro Focus. All rights reserved.
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

import com.microfocus.jenkins.plugins.rlc.client.RLCClient;
import com.microfocus.jenkins.plugins.rlc.utils.RLCUtils;
import hudson.AbortException;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.FormValidation;
import hudson.util.Secret;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.Nonnull;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Collections;
import java.util.Set;

/**
 * Step to create a new Release Package in Micro Focus Release Control
 *
 * @author Kevin A. Lee
 */
public class CreateReleasePackageStep extends AbstractRLCStep {

    @Extension
    public static final CreateReleasePackageDescriptor DESCRIPTOR = new CreateReleasePackageDescriptor();

    private String projectName;
    private String title;
    private String description;
    private Integer releaseTypeId;
    private Integer deploymentPathId;
    private Integer applicationId;
    private Integer releaseTrainId;
    private String messageLog;

    @DataBoundConstructor
    public CreateReleasePackageStep(String siteName) {
        super(siteName);
    }

    public String getProjectName() {
        return this.projectName;
    }

    @DataBoundSetter
    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

    public String getTitle() {
        return this.title;
    }

    @DataBoundSetter
    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    @DataBoundSetter
    public void setDescription(final String description) {
        this.description = description;
    }

    public Integer getReleaseTypeId() {
        return this.releaseTypeId;
    }

    @DataBoundSetter
    public void setReleaseTypeId(final Integer releaseTypeId) {
        this.releaseTypeId = releaseTypeId;
    }

    public Integer getDeploymentPathId() {
        return this.deploymentPathId;
    }

    @DataBoundSetter
    public void setDeploymentPathId(final Integer deploymentPathId) {
        this.deploymentPathId = deploymentPathId;
    }

    public Integer getApplicationId() {
        return this.applicationId;
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

    public String getMessageLog() {
        return this.messageLog;
    }

    @DataBoundSetter
    public void setMessageLog(final String messageLog) {
        this.messageLog = messageLog;
    }

    @Override
    public StepExecution start(StepContext context) {
        return new Execution(context, this);
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
    @Extension
    public static class CreateReleasePackageDescriptor extends AbstractRLCDescriptorImpl {

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

        @Override
        public String getDisplayName() {
            return "Micro Focus RLC Create Release Package";
        }

        @Override
        public String getFunctionName() {
            return "rlcCreateReleasePackage";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return Collections.singleton(Run.class);
        }
    }

    public static final class Execution extends AbstractItemProviderExecution<String> {

        private static final long serialVersionUID = 1L;
        private transient CreateReleasePackageStep step;

        Execution(@Nonnull StepContext context, @Nonnull CreateReleasePackageStep step) {
            super(context);
            this.step = step;
        }

        @Override
        protected String run() throws Exception {

            TaskListener listener = this.getContext().get(TaskListener.class);

            // set listener in Abstract class for logging
            step.setListener(listener);

            String siteName = step.getSiteName();
            RLCSite site = DESCRIPTOR.getSiteByName(siteName);

            String resolvedProjectName = this.getEnvironment(listener).expand(step.getProjectName());
            String resolvedTitle = this.getEnvironment(listener).expand(step.getTitle());
            String resolvedDescription = this.getEnvironment(listener).expand(step.getDescription());
            String resolvedMessageLog = this.getEnvironment(listener).expand(step.getMessageLog());

            // check connection to Micro Focus DA
            RLCClient rlcClient = new RLCClient(
                    site.getAeUrl(),
                    site.getOeUrl(),
                    site.getUser(),
                    Secret.fromString(site.getPassword()));
            try {
                rlcClient.verifyConnection();
            } catch (Exception ex) {
                throw new AbortException("Unable to connect to Micro Focus Release Control: " + ex.toString());
            }

            String itemId = "";
            String itemUrl = "";
            Integer recordId = 0;
            try {
                step.log("Creating new Release Package \" " + resolvedTitle + "\"");

                UriBuilder uriBuilder = UriBuilder.fromPath(site.getAeUrl()).path("jsonapi").path("submittoproject").path(resolvedProjectName);
                URI uri = uriBuilder.build();
                step.log("Submitting to: " + uri.toString());

                //{"transition":
                // {"TITLE":"Release of App 1",
                // "RLM_RELEASE_TYPE":2,
                // "DESCRIPTION":"Release of App 1 Description",
                // "RLM_DEPLOYMENT_PATH":1,
                // "RLM_MESSAGE_LOG":"some more comments",
                // "RLM_RELEASE_TRAIN":1,
                // "RLM_APPLICATION": 1},
                // "fixedFields": false, "fields": [ {"dbname":"ISSUEID"}]}

                //{"transition":
                // {"TITLE":"Release 27",
                // "RLM_RELEASE_TYPE":1,
                // "RLM_DEPLOYMENT_PATH":1,
                // "RLM_APPLICATION":1,
                // "RLM_RELEASE_TRAIN":2,
                // "DESCRIPTION":"Release of 27"},
                // "fixedFields": false, "fields": [ {"dbname":"ISSUEID"}]}
                String postBody = "{\"transition\": {" +
                        "\"TITLE\":\"" + resolvedTitle + "\"," +
                        "\"RLM_RELEASE_TYPE\":" + step.getReleaseTypeId().toString() + "," +
                        "\"RLM_DEPLOYMENT_PATH\":" + step.getDeploymentPathId().toString() + "," +
                        (step.getApplicationId() == null || step.getApplicationId() == 0 ? "" : "\"RLM_APPLICATION\":" + step.getApplicationId().toString() + ",") +
                        (step.getReleaseTrainId() == null || step.getReleaseTrainId() == 0 ? "" : "\"RLM_RELEASE_TRAIN\":" + step.getReleaseTrainId().toString() + ",") +
                        (resolvedMessageLog.equalsIgnoreCase("") ? "" : "\"RLM_MESSAGE_LOG\":\"" + resolvedMessageLog + "\",") +
                        "\"DESCRIPTION\":\"" + resolvedDescription + "\"" +
                        "}," +
                        "\"fixedFields\": false, \"fields\": [ {\"dbname\":\"ISSUEID\"}]}";
                //step.log(postBody);
                String jsonOut = rlcClient.executeJSONPost(uri, postBody);
                JSONObject itemObj = new JSONObject(jsonOut).optJSONObject("item");
                if (itemObj != null) {
                    JSONObject idObj = itemObj.getJSONObject("id");
                    recordId = (Integer) idObj.get("id");
                    itemId = (String) idObj.get("itemId");
                    itemUrl = (String) idObj.get("url");
                    step.log("Created new Release Package " + itemId + " with record id: " + recordId);
                    step.log("See: " + RLCUtils.setWorkcenterUrl(itemUrl));
                } else {
                    step.log("Unable to Create Release Package: " + jsonOut);
                }
            } catch (Exception ex) {
                throw new AbortException("Unable to Create Release Package: " + ex.toString());
            }

            return recordId.toString();
        }
    }
}

