package calltrack;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("twilio")
public class TwilioConfiguration {

    private String accountSid;
    private String authToken;
    private String twilioNumber;
    private String url;

    public TwilioConfiguration() {

    }

    String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    String getTwilioNumber() {
        return twilioNumber;
    }

    public void setTwilioNumber(String twilioNumber) {
        this.twilioNumber = twilioNumber;
    }

    String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
