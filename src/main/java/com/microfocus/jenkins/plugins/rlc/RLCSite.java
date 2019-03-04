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
import com.microfocus.jenkins.plugins.rlc.exceptions.AuthenticationException;
import hudson.AbortException;
import hudson.model.Descriptor;
import hudson.util.Secret;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Representation of Release Control instance and defaults
 *
 * @author Kevin A. Lee
 */
public class RLCSite implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(Descriptor.class.getName());
    private String profileName;
    private String aeUrl;
    private String oeUrl;
    private String user;
    private String password;
    private Integer releaseTrainTableId;
    private Integer releasePackageTableId;

    public RLCSite() {
    }

    public RLCSite(String profileName, String aeUrl, String oeUrl, String user, String password,
                   Integer releaseTrainTableId, Integer releasePackageTableId) {
        this.profileName = profileName;
        this.aeUrl = aeUrl;
        this.oeUrl = oeUrl;
        this.user = user;
        this.password = password;
        this.releaseTrainTableId = releaseTrainTableId;
        this.releasePackageTableId = releasePackageTableId;
    }

    public static List<RLCSite> readSitesFromConfigurationXml(File file) {
        List<RLCSite> sites = new ArrayList<>();
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList nodeList = document.getElementsByTagName(RLCSite.class.getName());
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String profileName = element.getElementsByTagName("profileName").item(0).getTextContent();
                String aeUrl = element.getElementsByTagName("aeUrl").item(0).getTextContent();
                String oeUrl = element.getElementsByTagName("oeUrl").item(0).getTextContent();
                String user = element.getElementsByTagName("user").item(0).getTextContent();
                String password = element.getElementsByTagName("password").item(0).getTextContent();
                Integer releaseTrainTableId = Integer.valueOf(element.getElementsByTagName("releaseTrainTableId").item(0).getTextContent());
                Integer releasePackageTableId = Integer.valueOf(element.getElementsByTagName("releasePackageTableId").item(0).getTextContent());
                sites.add(new RLCSite(profileName, aeUrl, oeUrl, user, password, releaseTrainTableId, releasePackageTableId));
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to read configuration file: " + file.getName(), e);
        }
        return sites;
    }

    public String getDisplayName() {
        if (StringUtils.isEmpty(profileName)) {
            return aeUrl;
        } else {
            return profileName;
        }
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getAeUrl() {
        return aeUrl;
    }

    public void setAeUrl(String aeUrl) {
        this.aeUrl = aeUrl;
        if (this.aeUrl != null) {
            this.aeUrl = this.aeUrl.replaceAll("\\\\", "/");
        }
        while (this.aeUrl != null && this.aeUrl.endsWith("/")) {
            this.aeUrl = this.aeUrl.substring(0, this.aeUrl.length() - 1);
        }
    }

    public String getOeUrl() {
        return oeUrl;
    }

    public void setOeUrl(String oeUrl) {
        this.oeUrl = oeUrl;
        if (this.oeUrl != null) {
            this.oeUrl = this.oeUrl.replaceAll("\\\\", "/");
        }
        while (this.oeUrl != null && this.oeUrl.endsWith("/")) {
            this.oeUrl = this.oeUrl.substring(0, this.oeUrl.length() - 1);
        }
    }

    public String verifyConnection() throws AuthenticationException {
        String token = null;
        // check connection to Micro Focus DA
        RLCClient rlcClient = new RLCClient(
                aeUrl,
                oeUrl,
                user,
                Secret.fromString(password));
        try {
            token = rlcClient.verifyConnection();
        } catch (Exception ex) {
           throw new AuthenticationException("Unable to connect to Micro Focus Release Control: " + ex.toString());
        }
        return token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getReleaseTrainTableId() {
        return releaseTrainTableId;
    }

    public void setReleaseTrainTableId(Integer releaseTrainTableId) {
        this.releaseTrainTableId = releaseTrainTableId;

    }

    public Integer getReleasePackageTableId() {
        return releasePackageTableId;
    }

    public void setReleasePackageTableId(Integer releasePackageTableId) {
        this.releasePackageTableId = releasePackageTableId;

    }
}
