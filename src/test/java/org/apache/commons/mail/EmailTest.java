package org.apache.commons.mail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EmailTest {
    private static String[] TEST_EMAILS = {"ab@bc.com", "a.b@c.org", "testEmail@gmail.com"};
    private static String[] TEST_EMPTY_EMAILS = {};
    private static String TEST_EMAIL = "testEmail@gmail.com";
    private static String TEST_HOST_NAME = "TestHostName";
    private String[] TEST_VALID_CHARS = {" ", "a", "A", "\uc5ec", "0123456789", "01234567890123456789"};
    private Map<String, String> TEST_HEADERS = new HashMap<String, String>() {{
        put("key1", "value1");
        put("key2", "value2");
    }};

    private EmailConcrete email;
    private Object testObject;

    @Before
    public void setUpEmailTests() throws Exception {
        email = new EmailConcrete();
        testObject = new Object();
    }

    // addBcc Tests
    @Test
    public void testAddBcc() throws Exception {
        email.addBcc(TEST_EMAILS);
        assertEquals(3, email.getBccAddresses().size());
    }

    @Test
    public void testAddBcc_EmptyEmails() throws Exception {
        try {
            email.addBcc(TEST_EMPTY_EMAILS);
        }
        catch(Exception e) {
            assertEquals(0, email.getBccAddresses().size());
        }
    }

    // addCc Tests
    @Test
    public void testAddCC() throws Exception {
        email.addCc(TEST_EMAIL);
        assertEquals(1, email.getCcAddresses().size());
    }

    // addHeader Tests
    @Test
    public void testAddHeader() throws Exception {
        email.addHeader("validName", "validValue");
        assertEquals(1, email.getHeaders().size());
    }

    @Test
    public void testAddHeader_InvalidName() throws Exception {
        try {
            email.addHeader(null, "validValue");
        }
        catch(Exception e) {
            assertEquals(0, email.getHeaders().size());
        }
    }

    @Test
    public void testAddHeader_InvalidValue() throws Exception {
        try {
            email.addHeader("validName", null);
        }
        catch(Exception e) {
            assertEquals(0, email.getHeaders().size());
        }
    }

    // addReplyTo tests
    @Test
    public void testAddReplyTo() throws Exception {
        email.addReplyTo(TEST_EMAIL, "validName");
        assertEquals(1, email.replyList.size());
    }

    // buildMimeMessage Tests
    @Test
    public void testBuildMimeMessage() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addCc(TEST_EMAILS);

        // Act
        email.buildMimeMessage();

        // Assert
        assertNotNull(email.getMimeMessage());
    }

    @Test
    public void testBuildMimeMessage_MessageAlreadyBuilt() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addCc(TEST_EMAILS);

        // Act
        email.buildMimeMessage();

        try {
            email.buildMimeMessage();
        }
        catch (Exception e) {
            // Assert
            assertNotNull(email.getMimeMessage());
        }
    }

    @Test
    public void testBuildMimeMessage_SubjectExists() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addCc(TEST_EMAILS);
        email.setSubject("TestSubject");

        // Act
        email.buildMimeMessage();

        // Assert
        assertNotNull(email.getMimeMessage());
    }

    @Test
    public void testBuildMimeMessage_SubjectAndCharsetExists() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addCc(TEST_EMAILS);
        email.setSubject("TestSubject");
        email.setCharset("UTF-8");

        // Act
        email.buildMimeMessage();

        // Assert
        assertNotNull(email.getMimeMessage());
    }

    @Test
    public void testBuildMimeMessage_InvalidContentTypeExists() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addCc(TEST_EMAILS);
        email.setContent(testObject, "TestContentType");

        // Act
        email.buildMimeMessage();

        // Assert
        assertNotNull(email.getMimeMessage());
    }

    @Test
    public void testBuildMimeMessage_ValidContentTypeExists() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addCc(TEST_EMAILS);
        email.setContent("", "text/plain");

        // Act
        email.buildMimeMessage();

        // Assert
        assertNotNull(email.getMimeMessage());
    }

    @Test
    public void testBuildMimeMessage_WithEmailBody() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addCc(TEST_EMAILS);
        email.emailBody = new MimeMultipart();

        // Act
        email.buildMimeMessage();

        // Assert
        assertNotNull(email.getMimeMessage());
    }

    @Test
    public void testBuildMimeMessage_WithEmailBodyAndContentType() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addCc(TEST_EMAILS);
        email.emailBody = new MimeMultipart();
        email.contentType = "TestContentType";

        // Act
        email.buildMimeMessage();

        // Assert
        assertNotNull(email.getMimeMessage());
    }

    @Test
    public void testBuildMimeMessage_NoFromAddress() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.addCc(TEST_EMAILS);

        try {
            // Act
            email.buildMimeMessage();
        }
        catch (Exception e) {
            // Assert
            assertNotNull(email.getMimeMessage());
        }
    }

    @Test
    public void testBuildMimeMessage_NoReceivers() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);

        try {
            // Act
            email.buildMimeMessage();
        }
        catch (Exception e) {
            // Assert
            assertNotNull(email.getMimeMessage());
        }
    }

    @Test
    public void testBuildMimeMessage_WithToList() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addTo(TEST_EMAILS);

        // Act
        email.buildMimeMessage();

        // Assert
        assertNotNull(email.getMimeMessage());
    }

    @Test
    public void testBuildMimeMessage_WithCcList() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addCc(TEST_EMAILS);

        // Act
        email.buildMimeMessage();

        // Assert
        assertNotNull(email.getMimeMessage());
    }

    @Test
    public void testBuildMimeMessage_WithBccList() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addBcc(TEST_EMAILS);

        // Act
        email.buildMimeMessage();

        // Assert
        assertNotNull(email.getMimeMessage());
    }

    @Test
    public void testBuildMimeMessage_WithReplyList() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addCc(TEST_EMAILS);
        email.addReplyTo(TEST_EMAIL);

        // Act
        email.buildMimeMessage();

        // Assert
        assertNotNull(email.getMimeMessage());
    }

    @Test
    public void testBuildMimeMessage_WithHeaders() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addCc(TEST_EMAILS);
        email.addHeader("validName", "validValue");

        // Act
        email.buildMimeMessage();

        // Assert
        assertNotNull(email.getMimeMessage());
    }

    @Test
    public void testBuildMimeMessage_PopBeforeSmtp() throws Exception {
        // Arrange
        email.setHostName(TEST_HOST_NAME);
        email.setFrom(TEST_EMAIL);
        email.addCc(TEST_EMAILS);

        try {
            email.setPopBeforeSmtp(true, "", "", "");

            // Act
            email.buildMimeMessage();
        }
        catch (Exception e) {
            // Assert
            assertNotNull(email.getMimeMessage());
        }
    }

    // getHostName Tests
    @Test
    public void testGetHostName_NoSessionOrHost() throws Exception {
        assertNull(email.getHostName());
    }

    @Test
    public void testGetHostName_WithSession() throws Exception {
        email.setMailSession(Session.getInstance(new Properties()));
        assertNull(email.getHostName());
    }

    @Test
    public void testGetHostName_WithHostName() throws Exception {
        email.setHostName(TEST_HOST_NAME);
        assertNotNull(email.getHostName());
    }

    // getMailSession Tests
    @Test
    public void testGetMailSession() throws Exception {
        email.setHostName(TEST_HOST_NAME);
        assertNotNull(email.getMailSession());
    }

    @Test
    public void testGetMailSession_NoHostName() throws Exception {
        try {
            assertNotNull(email.getMailSession());
        }
        catch (Exception e) {
            assertEquals("Cannot find valid hostname for mail session", e.getMessage());
        }
    }

    @Test
    public void testGetMailSession_WithAuthenticator() throws Exception {
        email.setHostName(TEST_HOST_NAME);
        email.setAuthenticator(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return super.getPasswordAuthentication();
            }
        });

        assertNotNull(email.getMailSession());
    }

    @Test
    public void testGetMailSession_WithSSL() throws Exception {
        email.setHostName(TEST_HOST_NAME);
        email.setSSLOnConnect(true);
        assertNotNull(email.getMailSession());
    }

    @Test
    public void testGetMailSession_WithTLSAndSSLCheck() throws Exception {
        email.setHostName(TEST_HOST_NAME);
        email.setStartTLSEnabled(true);
        email.setSSLCheckServerIdentity(true);
        assertNotNull(email.getMailSession());
    }

    @Test
    public void testGetMailSession_WithBounceAddress() throws Exception {
        email.setHostName(TEST_HOST_NAME);
        email.setBounceAddress(TEST_EMAIL);
        assertNotNull(email.getMailSession());
    }

    // getSentDate tests
    @Test
    public void testGetSentDate() throws Exception {
        assertNotNull(email.getSentDate());
    }

    @Test
    public void testGetSentDate_SentDateExists() throws Exception {
        email.setSentDate(new Date());
        assertNotNull(email.getSentDate());
    }

    // getSocketConnectionTimeout Tests
    @Test
    public void testGetSocketConnectionTimeout() throws Exception {
        assertEquals(60000, email.getSocketConnectionTimeout());
    }

    // setFrom Tests
    @Test
    public void testSetFrom() throws Exception {
        assertNotNull(email.setFrom(TEST_EMAIL));
    }

    // Misc. Tests to reach 70% Test Coverage
    @Test
    public void testSetStartTLSRequired() throws Exception {
        assertNotNull(email.setStartTLSRequired(true));
    }

    @Test
    public void testSetHeaders() throws Exception {
        email.setHeaders(TEST_HEADERS);
        assertEquals(2, email.getHeaders().size());
    }

    @Test
    public void testSetSocketConnectionTimeout() throws Exception {
        email.setSocketConnectionTimeout(1000);
        assertEquals(1000, email.getSocketConnectionTimeout());
    }

    @Test
    public void testSetSocketTimeout() throws Exception {
        email.setSocketTimeout(30000);
        assertEquals(30000, email.getSocketTimeout());
    }

    @Test
    public void testUpdateContentType()  throws Exception {
        email.updateContentType("; charset=TEST_CHARSET");
        assertEquals("; charset=TEST_CHARSET", email.getContentType());
    }

    @Test
    public void testSetSmtpPort() throws Exception {
        email.setSmtpPort(3000);
        assertEquals("3000", email.getSmtpPort());
    }

    // Teardown
    @After
    public void tearDownEmailTest() throws Exception {
        email = null;
        testObject = null;
    }
}
