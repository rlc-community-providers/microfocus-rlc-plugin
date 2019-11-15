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

import com.microfocus.jenkins.plugins.rlc.configuration.RLCGlobalConfiguration;
import hudson.model.Run;
import hudson.model.TaskListener;
import jenkins.model.GlobalConfiguration;
import org.apache.log4j.Logger;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.PrintStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractRLCStep extends Step {

    private String siteName;
    private PrintStream logger;
    private List<String> fileList = new ArrayList<String>();
    protected static final Logger LOGGER = Logger.getLogger("jenkins.RLCClient");
    private TaskListener listener;

    public AbstractRLCStep(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteName() {
        return siteName;
    }

    @DataBoundSetter
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public PrintStream getLogger() {
        return this.logger;
    }

    public void setLogger(PrintStream logger) {
        this.logger = logger;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public TaskListener getListener() {
        return this.listener;
    }

    public void setListener(TaskListener listener) {
        this.listener = listener;
    }

    /**
     * Write text to the build's console log (logger), prefixed with
     * "[Micro Focus RLC]".
     *
     * @param text message to log
     */
    public void log(final String text) {
        if (listener.getLogger() != null)
            listener.getLogger().println("[Micro Focus RLC] " + text);
    }

    /**
     * Resolve any Jenkins variables with their "real" values from the environment.
     *
     * @param input the string containing Jenkins variables
     * @return the "real" values from the environment
     */
    public String resolveVariables(Map<String, String> envMap, String input) {
        String result = input;
        if (input != null && input.trim().length() > 0) {
            Pattern pattern = Pattern.compile("\\$\\{[^}]*}");
            Matcher matcher = pattern.matcher(result);
            while (matcher.find()) {
                String key = result.substring(matcher.start() + 2, matcher.end() - 1);
                if (envMap.containsKey(key)) {
                    result = matcher.replaceFirst(Matcher.quoteReplacement(envMap.get(key)));
                    matcher.reset(result);
                }
            }
        }

        return result;
    }

    /*
     *
     * Extend this class as "MyClassNameDescriptor" and include additional fields/validation for the steps
     * - include the @Extension point (commented out below) and @Symbol annotation with the relevant
     * name you want the step to executed as.
     */
    //@Symbol("rlcStep")
    //@Extension // This indicates to Jenkins that this is an implementation of an extension point.
    /*public abstract static class AbstractRLCDescriptorImpl extends StepDescriptor {

        private RLCGlobalConfiguration rlcGlobalConfiguration;

        public AbstractRLCDescriptorImpl() {
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

        /*
         * Override this method with the steps display name
         *
        @Override
        public String getDisplayName() {
            return "Micro Focus RLC Step";
        }

        /*
         * Override this method with the steps function name
         *
        @Override
        public String getFunctionName() {
            return "rlcStep";
        }

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return Collections.singleton(Run.class);
        }
    }*/

}


