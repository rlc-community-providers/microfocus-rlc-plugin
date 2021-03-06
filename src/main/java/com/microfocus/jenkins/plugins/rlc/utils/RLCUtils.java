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

package com.microfocus.jenkins.plugins.rlc.utils;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import hudson.security.ACL;
import jenkins.model.Jenkins;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

public class RLCUtils {

    /**
     * Remove the trailing slash from url.
     *
     * @param url the URL
     * @return URL with the trailing slash removed, if it exists.
     */
    public static String rmSlashFromUrl(final String url) {
        if (!StringUtils.isEmpty(url)) {
            return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        } else {
            return url;
        }
    }

    /**
     * Check if a String is a valid URL
     *
     * @param url the URL
     * @return true if a valid URL, else false
     */
    public static boolean isUrl(final String url) {
        boolean valid = false;
        if (url != null && url.length() > 0) {
            try {
                new URL(url);
                valid = true;
            } catch (MalformedURLException e) {
                // malformed; ignore
            }
        }
        return valid;
    }

    /**
     * Get the token from the credentials identified by the given id.
     *
     * @param credentialsId The id for the credentials
     * @return Jenkins credentials
     */
    public static StringCredentials getTokenCredentials(final String credentialsId) {
        return getJenkinsCredentials(credentialsId, StringCredentials.class);
    }

    /**
     * Get the user/pass from the credentials identified by the given id.
     *
     * @param credentialsId The id for the credentials
     * @return Jenkins credentials
     */
    public static UsernamePasswordCredentials getUsernamePasswordCredentials(final String credentialsId) {
        return getJenkinsCredentials(credentialsId, UsernamePasswordCredentials.class);
    }

    /**
     * Get the credentials identified by the given id from the Jenkins credential store.
     *
     * @param credentialsId    The id for the credentials
     * @param credentialsClass The class of credentials to return
     * @return Jenkins credentials
     */
    public static <T extends Credentials> T getJenkinsCredentials(final String credentialsId, final Class<T> credentialsClass) {
        if (StringUtils.isEmpty(credentialsId))
            return null;
        return CredentialsMatchers.firstOrNull(
                CredentialsProvider.lookupCredentials(credentialsClass,
                        Jenkins.getInstance(), ACL.SYSTEM, Collections.<DomainRequirement>emptyList()),
                CredentialsMatchers.withId(credentialsId)
        );
    }

    public static String setWorkcenterUrl(String itemUrl) {
        return itemUrl.replace("/tmtrack/tmtrack.dll?", "/workcenter/tmtrack.dll?shell=swc&");
    }
}
