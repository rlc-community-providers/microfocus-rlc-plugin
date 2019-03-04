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

package com.microfocus.jenkins.plugins.rlc.client;

import com.microfocus.jenkins.plugins.rlc.exceptions.AuthenticationException;
import com.microfocus.jenkins.plugins.rlc.model.ALFEvent;
import com.urbancode.commons.util.https.OpenSSLProtocolSocketFactory;
import hudson.util.Secret;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.core.UriBuilder;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.io.Serializable;
import java.net.URI;

/**
 * Provides a generic client and access methods for Micro Focus Release Control (RLC)
 *
 * @author Kevin A. Lee
 */
public class RLCClient implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The Application Engine URL.
     */
    private String aeUrl;

    /**
     * The Orchestration Engine url.
     */
    private String oeUrl;

    /**
     * The username.
     */
    private String user;

    /**
     * The password.
     */
    private Secret password;

    public RLCClient() {

    }

    /**
     * Instantiates a new Micro Focus RLC Client.
     *
     * @param aeUrl    the Application Engine URL of the RLC instance
     * @param oeUrl    the Orchestration Engine URL of the RLC instance
     * @param user     the username
     * @param password the password
     */
    public RLCClient(String aeUrl, String oeUrl, String user, Secret password) {
        this.aeUrl = aeUrl;
        this.oeUrl = oeUrl;
        this.user = user;
        this.password = password;
    }

    private static SOAPMessage createSOAPRequest(ALFEvent alfEvent) throws Exception {

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        SOAPPart soapPart = soapMessage.getSOAPPart();

        // create SOAP envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(alfEvent.getNamespace(), alfEvent.getNameSpaceURI());

        // create SOAP body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyEventNotice = soapBody.addChildElement("ALFEventNoticeDoc", alfEvent.getNamespace());
        soapBodyEventNotice.addAttribute(new QName("", "version"), "1.0");
        SOAPElement soapBodyEventBase = soapBodyEventNotice.addChildElement("Base", alfEvent.getNamespace());
        soapBodyEventBase.addChildElement("EventId", alfEvent.getNamespace()).setValue(alfEvent.getEventId());
        soapBodyEventBase.addChildElement("Timestamp", alfEvent.getNamespace()).setValue(alfEvent.getTimestamp());
        soapBodyEventBase.addChildElement("EventType", alfEvent.getNamespace()).setValue(alfEvent.getEventType());
        soapBodyEventBase.addChildElement("ObjectType", alfEvent.getNamespace()).setValue(alfEvent.getObjectType());
        soapBodyEventBase.addChildElement("ObjectId", alfEvent.getNamespace()).setValue(alfEvent.getObjectId());
        SOAPElement soapEventSource = soapBodyEventBase.addChildElement("Source", alfEvent.getNamespace());
        soapEventSource.addChildElement("Product", alfEvent.getNamespace()).setValue(alfEvent.getProductName());
        soapEventSource.addChildElement("ProductVersion", alfEvent.getNamespace()).setValue(alfEvent.getProductVersion());
        soapEventSource.addChildElement("ProductInstance", alfEvent.getNamespace()).setValue(alfEvent.getProductInstance());
        SOAPElement soapEventUser = soapBodyEventBase.addChildElement("User", alfEvent.getNamespace());
        SOAPElement soapEventALFSecurity = soapEventUser.addChildElement("ALFSecurity", alfEvent.getNamespace());
        SOAPElement soapEventUsernameToken = soapEventALFSecurity.addChildElement("UsernameToken", alfEvent.getNamespace());
        soapEventUsernameToken.addChildElement("Username", alfEvent.getNamespace()).setValue(alfEvent.getUsername());
        soapEventUsernameToken.addChildElement("Password", alfEvent.getNamespace()).setValue(alfEvent.getPassword());

        // extended data
        SOAPElement soapBodyExtension = soapBody.addChildElement("Extension", alfEvent.getNamespace());

        // TODO: must be a better way with reflection?
        /*
        for (Field field : alfEvent.getJenkinsEnvironment().getClass().getDeclaredFields()) {
            field.setAccessible(true); // if you want to modify private fields
            System.out.println(field.getName()
                    + " - " + field.getType()
                    + " - " + field.get(alfEvent.getJenkinsEnvironment()));
            String val = field.get(alfEvent.getJenkinsEnvironment()).toString();
            //soapBodyExtension.addChildElement(field.getName(),
            //        alfEvent.getNamespace()).setValue(val != null ? val : "");
        }*/

        soapBodyExtension.addChildElement("branchName", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getBranchName());
        soapBodyExtension.addChildElement("changeId", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getChangeId());
        soapBodyExtension.addChildElement("changeTitle", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getChangeTitle());
        soapBodyExtension.addChildElement("changeAuthor", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getChangeAuthor());
        soapBodyExtension.addChildElement("changeAuthorDisplayName", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getChangeAuthorDisplayName());
        soapBodyExtension.addChildElement("changeAuthorEmail", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getChangeAuthorEmail());
        soapBodyExtension.addChildElement("changeTarget", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getChangeTarget());
        soapBodyExtension.addChildElement("buildNumber", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getBuildNumber());
        soapBodyExtension.addChildElement("buildId", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getBuildId());
        soapBodyExtension.addChildElement("buildDisplayName", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getBuildDisplayName());
        soapBodyExtension.addChildElement("jobName", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getJobName());
        soapBodyExtension.addChildElement("jobBaseName", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getJobBaseName());
        soapBodyExtension.addChildElement("buildTag", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getBuildTag());
        soapBodyExtension.addChildElement("executionNumber", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getExecutionNumber());
        soapBodyExtension.addChildElement("nodeName", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getNodeName());
        soapBodyExtension.addChildElement("nodeLabels", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getNodeLabels());
        soapBodyExtension.addChildElement("workspace", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getWorkspace());
        soapBodyExtension.addChildElement("jenkinsHome", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getJenkinsHome());
        soapBodyExtension.addChildElement("jenkinsUrl", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getJenkinsUrl());
        soapBodyExtension.addChildElement("buildUrl", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getBuildUrl());
        soapBodyExtension.addChildElement("jobUrl", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getJobUrl());
        soapBodyExtension.addChildElement("javaHome", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getJavaHome());
        soapBodyExtension.addChildElement("svnRevision", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getSvnRevision());
        soapBodyExtension.addChildElement("cvsBranch", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getCvsBranch());
        soapBodyExtension.addChildElement("gitCommit", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getGitCommit());
        soapBodyExtension.addChildElement("gitUrl", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getGitUrl());
        soapBodyExtension.addChildElement("gitBranch", alfEvent.getNamespace()).setValue(alfEvent.getJenkinsEnvironment().getGitBranch());

        MimeHeaders headers = soapMessage.getMimeHeaders();
        //headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();

        /* Print the request message, just for debugging purposes */
        //System.out.println("Request SOAP Message:");
        //soapMessage.writeTo(System.out);
        //System.out.println("\n");

        return soapMessage;
    }

    /**
     * Gets the Application Engine URL.
     *
     * @return the aeUrl
     */
    public String getAeUrl() {
        return aeUrl;
    }

    /**
     * Sets the Application Enginer url.
     *
     * @param url the url
     */
    public void setAeUrl(String url) {
        this.aeUrl = url;
        if (this.aeUrl != null) {
            this.aeUrl = this.aeUrl.replaceAll("\\\\", "/");
        }
        while (this.aeUrl != null && this.aeUrl.endsWith("/")) {
            this.aeUrl = this.aeUrl.substring(0, this.aeUrl.length() - 1);
        }
    }

    /**
     * Gets the Orchestration Engine URL.
     *
     * @return the oeUrl
     */
    public String getOeUrl() {
        return oeUrl;
    }

    /**
     * Sets the Orchestration Engine url.
     *
     * @param url the url
     */
    public void setOeUrl(String url) {
        this.oeUrl = url;
        if (this.oeUrl != null) {
            this.oeUrl = this.oeUrl.replaceAll("\\\\", "/");
        }
        while (this.oeUrl != null && this.oeUrl.endsWith("/")) {
            this.oeUrl = this.oeUrl.substring(0, this.oeUrl.length() - 1);
        }
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the username.
     *
     * @param user the new username
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public Secret getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(Secret password) {
        this.password = password;
    }

    //
    // generic HTTP REST get,post,put methods
    //

    public String verifyConnection() throws Exception {
        // just see if we can get SSO Token for now
        return this.getSSOToken();
    }

    public String executeJSONGet(URI uri) throws Exception {
        String result = null;
        HttpClient httpClient = new HttpClient();

        if ("https".equalsIgnoreCase(uri.getScheme())) {
            ProtocolSocketFactory socketFactory = new OpenSSLProtocolSocketFactory();
            Protocol https = new Protocol("https", socketFactory, 443);
            Protocol.registerProtocol("https", https);
        }

        GetMethod method = new GetMethod(uri.toString());
        setDirectSsoInteractionHeader(method);
        method.setRequestHeader("Content-Type", "application/json");
        method.setRequestHeader("charset", "utf-8");
        method.setRequestHeader("ALFSSOAuthNToken", getSSOToken());
        try {
            HttpClientParams params = httpClient.getParams();
            params.setAuthenticationPreemptive(true);

            UsernamePasswordCredentials clientCredentials = new UsernamePasswordCredentials(user, password.getPlainText());
            httpClient.getState().setCredentials(AuthScope.ANY, clientCredentials);

            int responseCode = httpClient.executeMethod(method);
            //if (responseCode < 200 || responseCode < 300) {
            if (responseCode == 401) {
                throw new AuthenticationException("Error connecting to Micro Focus RLC: Invalid user and/or password");
            } else if (responseCode != 200) {
                throw new Exception("Error connecting to Micro Focus RLC: " + responseCode);
            } else {
                result = method.getResponseBodyAsString();
            }
        } finally {
            method.releaseConnection();
        }

        return result;
    }

    public String executeJSONPut(URI uri, String putContents) throws Exception {
        String result = null;
        HttpClient httpClient = new HttpClient();

        if ("https".equalsIgnoreCase(uri.getScheme())) {
            ProtocolSocketFactory socketFactory = new OpenSSLProtocolSocketFactory();
            Protocol https = new Protocol("https", socketFactory, 443);
            Protocol.registerProtocol("https", https);
        }

        PutMethod method = new PutMethod(uri.toString());
        setDirectSsoInteractionHeader(method);
        if (putContents != null)
            method.setRequestBody(putContents);
        method.setRequestHeader("Content-Type", "application/json");
        method.setRequestHeader("charset", "utf-8");
        method.setRequestHeader("ALFSSOAuthNToken", getSSOToken());

        try {
            HttpClientParams params = httpClient.getParams();
            params.setAuthenticationPreemptive(true);

            UsernamePasswordCredentials clientCredentials = new UsernamePasswordCredentials(user, password.getPlainText());
            httpClient.getState().setCredentials(AuthScope.ANY, clientCredentials);

            int responseCode = httpClient.executeMethod(method);

            //if (responseCode < 200 || responseCode < 300) {
            if (responseCode == 401) {
                throw new AuthenticationException("Error connecting to Micro Focus RLC: Invalid user and/or password");
            } else if (responseCode != 200 && responseCode != 204) {
                throw new Exception("Micro Focus RLC returned error code: " + responseCode);
            } else {
                result = method.getResponseBodyAsString();
            }
        } catch (Exception ex) {
            throw new Exception("Error connecting to Micro Focus RLC: " + ex.getMessage());
        } finally {
            method.releaseConnection();
        }

        return result;
    }

    public String executeJSONPost(URI uri, String postContents) throws Exception {
        String result = null;
        HttpClient httpClient = new HttpClient();

        if ("https".equalsIgnoreCase(uri.getScheme())) {
            ProtocolSocketFactory socketFactory = new OpenSSLProtocolSocketFactory();
            Protocol https = new Protocol("https", socketFactory, 443);
            Protocol.registerProtocol("https", https);
        }

        PostMethod method = new PostMethod(uri.toString());
        //setDirectSsoInteractionHeader(method);
        if (postContents != null)
            method.setRequestBody(postContents);
        method.setRequestHeader("Content-Type", "application/json");
        method.setRequestHeader("charset", "utf-8");
        String ssoToken = getSSOToken();
        method.setRequestHeader("ALFSSOAuthNToken", ssoToken);
        try {
            HttpClientParams params = httpClient.getParams();
            //params.setAuthenticationPreemptive(true);

            //UsernamePasswordCredentials clientCredentials = new UsernamePasswordCredentials(user, password.getPlainText());
            //httpClient.getState().setCredentials(AuthScope.ANY, clientCredentials);

            int responseCode = httpClient.executeMethod(method);
            if (responseCode == 401) {
                throw new AuthenticationException("Error connecting to Micro Focus RLC: Invalid user and/or password");
            } else if (responseCode != 200) {
                throw new Exception("Micro Focus RLC returned error code: " + responseCode);
            } else {
                result = method.getResponseBodyAsString();
                //System.out.println(result);
            }
        } catch (Exception e) {
            throw new Exception("Error connecting to Micro Focus RLC: " + e.getMessage());
        } finally {
            method.releaseConnection();
        }

        return result;
    }

    //
    // private methods
    //

    public void generateALFEvent(ALFEvent alfEvent) {

        String soapEndpointUrl = getOeUrl() + "/eventmanager/services/ALFEventManagerDocLit";

        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(alfEvent), soapEndpointUrl);

            // Print the SOAP Response
            //System.out.println("Response SOAP Message:");
            //soapResponse.writeTo(System.out);
            //System.out.println();

            soapConnection.close();
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
    }

    private String getSSOToken() throws Exception {
        String token = null;
        HttpClient httpClient = new HttpClient();
        URI uri = UriBuilder.fromPath(getOeUrl()).path("idp").path("services").path("rest").path("tokenservice").build();
        String jsonBody = "{   \"credentials\": {\"username\": \"" + user +
                "\", \"password\": \"" + password + "\"} }";
        if ("https".equalsIgnoreCase(uri.getScheme())) {
            ProtocolSocketFactory socketFactory = new OpenSSLProtocolSocketFactory();
            Protocol https = new Protocol("https", socketFactory, 443);
            Protocol.registerProtocol("https", https);
        }

        PostMethod method = new PostMethod(uri.toString());
        method.setRequestBody(jsonBody);
        method.setRequestHeader("Content-Type", "application/json");
        method.setRequestHeader("Accept", "application/json");
        method.setRequestHeader("charset", "utf-8");
        try {
            HttpClientParams params = httpClient.getParams();
            int responseCode = httpClient.executeMethod(method);

            if (responseCode == 401) {
                throw new AuthenticationException("Error connecting to Micro Focus RLC: Invalid user and/or password");
            } else if (responseCode != 200) {
                throw new Exception("Micro Focus RLC returned error code: " + responseCode);
            } else if (method.getResponseBodyAsString().isEmpty()) {
                throw new Exception("No response from Micro Focus RLC: please check URLs and credentials");
            } else {
                JSONObject jsonObj = new JSONObject(method.getResponseBodyAsString());
                JSONObject tokenObj = jsonObj.getJSONObject("token");
                token = tokenObj.getString("value");
            }
        } catch (Exception e) {
            throw new Exception("Error connecting to Micro Focus RLC: " + e.getMessage());
        } finally {
            method.releaseConnection();
        }
        return token;
    }

    private String encodePath(String path) {
        String result;
        URI uri;
        try {
            uri = new URI(null, null, path, null);
            result = uri.toASCIIString();
        } catch (Exception e) {
            result = path;
        }
        return result;
    }

    private void setDirectSsoInteractionHeader(HttpMethodBase method) {
        method.setRequestHeader("DirectSsoInteraction", "true");
    }

}
