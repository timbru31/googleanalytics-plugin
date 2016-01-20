package hudson.plugins.google.analytics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.JenkinsRule.WebClient;
import org.jvnet.hudson.test.recipes.LocalData;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHead;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import hudson.ExtensionList;
import hudson.model.PageDecorator;

public class HeaderWebTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();
    private HtmlPage page;
    private WebClient webClient = j.createWebClient();

    @Before
    public void setUp() throws IOException, SAXException {
        webClient.setJavaScriptEnabled(false);
        page = webClient.goTo("configure");
    }

    /**
     * Asserts that the analytics script is in the head element.
     */
    @LocalData
    @Test
    public void testHeadElementContainsScript() {
        WebAssert.assertInputContainsValue(page, "_.profileId", "AProfileId");
        HtmlHead item = (HtmlHead) page.getElementsByTagName(HtmlHead.TAG_NAME).item(0);
        assertTrue("The page text did not contain the google analytics script", item.asXml().contains("window,document,'script','//www.google-analytics.com/analytics.js'"));
    }

    /**
     * Asserts that the header contains the profile within quotes.
     */
    @LocalData
    @Test
    public void testScriptContainsProfileWithinQuotation() {
        WebAssert.assertInputContainsValue(page, "_.profileId", "AProfileId");
        assertTrue("The page text did not contain the profile", page.asXml().contains("ga('create', 'AProfileId', 'auto');"));
    }

    /**
     * Asserts that the header does not contain the google analytics script.
     */
    @Test
    public void testEmptyHeaderIfEmptyProfileId() {
        WebAssert.assertInputContainsValue(page, "_.profileId", "");
        assertFalse("The page text contained the profile", page.asXml().contains("ga('create', 'AProfileId', 'auto');"));
    }

    /**
     * Asserts that the profile id for decorator is updated when submitted
     */
    @Test
    public void testSubmittingConfigurationUpdatesProfileId() throws Exception {
        WebClient webClient = j.createWebClient();
        webClient.setThrowExceptionOnScriptError(false);
        HtmlForm form = webClient.goTo("configure").getFormByName("config");
        form.getInputByName("_.profileId").setValueAttribute("NewProfile");
        form.submit((HtmlButton) j.last(form.getHtmlElementsByTagName("button")));
        ExtensionList<PageDecorator> all = PageDecorator.all();
        boolean match = false;
        for (PageDecorator decorator : all) {
            if (decorator instanceof GoogleAnalyticsPageDecorator) {
                GoogleAnalyticsPageDecorator googleAnalyticsPageDecorator = (GoogleAnalyticsPageDecorator) decorator;
                assertEquals("The new profile id was not correct", "NewProfile", googleAnalyticsPageDecorator.getProfileId());
                match = true;
            }
        }
        assertTrue("Expected to find at least on Google Analytics Page Decorator", match);
    }
}
