package hudson.plugins.google.analytics;

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

    @DataBoundConstructor
    public GoogleAnalyticsPageDecorator(String profileId, boolean anonymizeIp, boolean displayFeatures) {
        this();
        this.profileId = profileId;
        this.anonymizeIp = anonymizeIp;
        this.displayFeatures = displayFeatures;
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
}
