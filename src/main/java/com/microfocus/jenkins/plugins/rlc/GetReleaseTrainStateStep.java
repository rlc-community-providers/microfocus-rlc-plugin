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
import com.microfocus.jenkins.plugins.rlc.configuration.RLCGlobalConfiguration;
import com.microfocus.jenkins.plugins.rlc.utils.RLCUtils;
import hudson.AbortException;
import hudson.Extension;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.FormValidation;
import hudson.util.Secret;
import jenkins.model.GlobalConfiguration;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Collections;
import java.util.Set;

/**
 * Step to get the state of a Release Train
 *
 * @author Kevin A. Lee
 */
public class GetReleaseTrainStateStep extends AbstractRLCStep {

    @Extension
    public static final GetReleaseTrainStateDescriptor DESCRIPTOR = new GetReleaseTrainStateDescriptor();

    private Integer releaseTrainId;
    private Boolean waitForState;
    private String desiredState;
    private Integer maxRetries;
    private Integer delayInterval;


    @DataBoundConstructor
    public GetReleaseTrainStateStep(String siteName) {
        super(siteName);
    }

    public Integer getReleaseTrainId() {
        return this.releaseTrainId;
    }

    @DataBoundSetter
    public void setReleaseTrainId(final Integer releaseTrainId) {
        this.releaseTrainId = releaseTrainId;
    }

    public Boolean getWaitForState() {
        return waitForState;
    }

    @DataBoundSetter
    public void setWaitForState(Boolean waitForState) {
        this.waitForState = waitForState;
    }

    public String getDesiredState() {
        return desiredState;
    }

    @DataBoundSetter
    public void setDesiredState(String desiredState) {
        this.desiredState = desiredState;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    @DataBoundSetter
    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Integer getDelayInterval() {
        return delayInterval;
    }

    @DataBoundSetter
    public void setDelayInterval(Integer delayInterval) {
        this.delayInterval = delayInterval;
    }

    @Override
    public StepExecution start(StepContext context) {
        return new Execution(context, this);
    }

    /**
     * Descriptor for {@link GetReleaseTrainStateStep}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See {@code src/main/resources/com/microfocus/jenkins/plugins/rlc/GetReleaseTrainStateStep/*.jelly}
     * for the actual HTML fragment for the configuration screen.
     * </p>
     */
    @Extension
    public static class GetReleaseTrainStateDescriptor extends StepDescriptor {

        private RLCGlobalConfiguration rlcGlobalConfiguration;

        public GetReleaseTrainStateDescriptor() {
            load();
            this.rlcGlobalConfiguration = GlobalConfiguration.all().get(RLCGlobalConfiguration.class);
        }

        public RLCSite getSiteByName(String siteName) {
            return rlcGlobalConfiguration.getSiteByName(siteName);
        }

        public RLCSite[] getSites() {
            return rlcGlobalConfiguration.getSites();
        }

        public RLCSite getSite(String siteName) {
            return getSiteByName(siteName);
        }

        private FormValidation doCheckReleaseTrainId(@QueryParameter Integer releaseTrainId) {
            if (releaseTrainId == null || releaseTrainId == 0)
                return FormValidation.error("A Release Train Id is required");
            return FormValidation.ok();
        }

        @Override
        public String getDisplayName() {
            return "Micro Focus RLC Release Train State";
        }

        @Override
        public String getFunctionName() {
            return "rlcGetReleaseTrainState";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return Collections.singleton(Run.class);
        }
    }

    public static final class Execution extends AbstractItemProviderExecution<String> {

        private static final long serialVersionUID = 1L;
        private transient GetReleaseTrainStateStep step;

        Execution(@Nonnull StepContext context, @Nonnull GetReleaseTrainStateStep step) {
            super(context);
            this.step = step;
        }

        @Override
        protected String run() throws Exception {

            // set listener in Abstract class for logging
            step.setListener(this.getContext().get(TaskListener.class));

            String siteName = step.getSiteName();
            RLCSite site = DESCRIPTOR.getSiteByName(siteName);

            step.log("Retrieving state of Release Train [" + site.getReleaseTrainTableId() + ":"
                    + step.getReleaseTrainId() + "]");
            if (step.getWaitForState()) {
                step.log("Waiting for desired state to be in: " + step.getDesiredState());
            }

            // check connection to Micro Focus RLC
            LOGGER.info("Creating RLC Client Connection:");
            LOGGER.info("AEURL="+site.getAeUrl()+"OEURL="+site.getOeUrl()+"User="+site.getUser());
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

            UriBuilder uriBuilder = UriBuilder.fromPath(site.getAeUrl()).path("jsonapi").path("GetItem")
                    .path(site.getReleaseTrainTableId().toString())
                    .path(step.getReleaseTrainId().toString());
            URI uri = uriBuilder.build();

            String titleVal = "";
            String stateVal = "";
            String itemUrl = "";
            int numTries = 0;
            int maxTries = step.getMaxRetries();
            if (maxTries < 0) {
                step.log("Max retries less than zerp is not supported; setting to \"1\"");
                maxTries = 1;
            }
            boolean finished = false;
            while (!finished) {
                try {
                    LOGGER.info("Executing GET: " + uri.toString());
                    String jsonOut = rlcClient.executeJSONGet(uri);
                    LOGGER.info("Response: " + jsonOut);
                    JSONObject itemObj = new JSONObject(jsonOut).optJSONObject("item");
                    if (itemObj != null) {
                        JSONObject fieldsObj = itemObj.getJSONObject("fields");
                        JSONObject idObj = itemObj.getJSONObject("id");
                        JSONObject stateObj = fieldsObj.getJSONObject("STATE");
                        JSONObject titleObj = fieldsObj.getJSONObject("TITLE");
                        itemUrl = (String) idObj.get("url");
                        stateVal = (String) stateObj.get("value");
                        titleVal = (String) titleObj.get("value");

                        if (step.getWaitForState()) {
                            if (StringUtils.containsIgnoreCase(step.getDesiredState(), stateVal)) {
                                this.getContext().setResult(Result.SUCCESS);
                                finished = true;
                            } else {
                                if (numTries >= maxTries) {
                                    step.log("Maximum number of retries reached ... aborting ...");
                                    finished = true;
                                    this.getContext().setResult(Result.ABORTED);
                                } else {
                                    step.log("Current State of Release Train \"" + titleVal + "\" is: " + stateVal);
                                    try {
                                        Thread.sleep(step.getDelayInterval());
                                    } catch (InterruptedException ex) {
                                        throw new AbortException("Unable to Get Release Train State: " + ex.toString());
                                    }
                                    numTries++;
                                }
                            }
                        } else {
                            this.getContext().setResult(Result.SUCCESS);
                            finished = true;
                        }

                    }
                } catch (Exception ex) {
                    throw new AbortException("Unable to Get Release Train State: " + ex.toString());
                }
            }

            step.log("State of Release Train \"" + titleVal + "\" is: " + stateVal);
            step.log("See: " + RLCUtils.setWorkcenterUrl(itemUrl));

            return stateVal;

        }

    }

}


