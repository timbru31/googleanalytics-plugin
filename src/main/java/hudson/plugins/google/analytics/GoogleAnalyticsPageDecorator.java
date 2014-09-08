package hudson.plugins.google.analytics;

import java.util.ArrayList;
import java.util.StringTokenizer;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.Util;
import hudson.model.PageDecorator;

@Extension
public class GoogleAnalyticsPageDecorator extends PageDecorator {
    private String profileId;
    private boolean anonymizeIp;
    private boolean displayFeatures;
    private ArrayList<String> customFeatures;

    @DataBoundConstructor
    public GoogleAnalyticsPageDecorator(String profileId, boolean anonymizeIp, boolean displayFeatures, ArrayList<String> customFeatures) {
        this();
        this.profileId = profileId;
        this.anonymizeIp = anonymizeIp;
        this.displayFeatures = displayFeatures;
        this.customFeatures = customFeatures;
    }
    
    public GoogleAnalyticsPageDecorator() {
        super(GoogleAnalyticsPageDecorator.class);
        load();
    }

    @Override
    public String getDisplayName() {
        return "Google Analytics";
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this, json);
        save();
        return true;
    }

    public String getProfileId() {
        return Util.fixEmpty(profileId);
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
   
    public boolean getAnonymizeIp() {
        return anonymizeIp;
    }
    
    public void setAnonymizeIp(boolean anonymizeIp) {
        this.anonymizeIp = anonymizeIp;
    }
    
    public boolean getDisplayFeatures() {
        return displayFeatures;
    }
    
    public void setDisplayFeatures(boolean displayFeatures) {
        this.displayFeatures = displayFeatures;
    }

    public String getCustomFeatures() {
        StringBuilder sb = new StringBuilder();
        for (String s : this.customFeatures) {
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

    public void setCustomFeatures(ArrayList<String> customFeatures) {
        this.customFeatures = customFeatures;
    }
    
    public void setCustomFeatures(String rawCustomFeatures) {
        this.customFeatures = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(rawCustomFeatures, "\t\n\r\f");
        while (tokenizer.hasMoreTokens()) {
            this.customFeatures.add(tokenizer.nextToken());
        }
    }
}
