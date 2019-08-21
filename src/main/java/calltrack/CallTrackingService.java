package calltrack;

import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.voice.Number;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Service
public class CallTrackingService {

    private final HashMap<String, String> customerAdvertisementMapper = new HashMap<>();
    private final HashMap<String, Map.Entry<String, String>> advertisementMapper = new HashMap<>();

    private final TwilioConfiguration twilioConfiguration;

    @Autowired
    public CallTrackingService(final TwilioConfiguration twilioConfiguration) {
        this.twilioConfiguration = twilioConfiguration;
        this.advertisementMapper.put("abc", new AbstractMap.SimpleEntry<>("<NUMBER OF DEALERSHIP>", "Bugatti Dealership"));
        this.advertisementMapper.put("abcd", new AbstractMap.SimpleEntry<>("<NUMBER OF DEALERSHIP>", "Fiat Dealership"));
        this.advertisementMapper.put("ghi", new AbstractMap.SimpleEntry<>("<NUMBER OF DEALERSHIP>", "Toyota Dealership"));
    }

    void init(final String caller, final String adId) throws URISyntaxException {
        //TODO Add phone number validator

        final String url = String.format("%s/calltrack/call?adId=%s", this.twilioConfiguration.getUrl(), adId);

        final Call call = Call.creator(
                new PhoneNumber(caller), new PhoneNumber(this.twilioConfiguration.getTwilioNumber()), new URI(url))
                .create();
    }

    void getNumberAndPlaceCall(final String adId, final HttpServletRequest request, final HttpServletResponse response) throws IOException {

        VoiceResponse voiceResponse;
        if (this.advertisementMapper.containsKey(adId)) {
            final Number number = new Number.Builder(this.advertisementMapper.get(adId).getKey()).build();
            final Say say = new Say.Builder("You will now be connected to " + this.advertisementMapper.get(adId).getValue()).build();
            voiceResponse = new VoiceResponse.Builder().say(say).dial(new Dial.Builder().number(number).build()).build();
            this.mapCustomerWithDealer(request.getParameter("Called"), adId);
            System.out.println("Success!");
        } else {
            System.out.println("Error sent!");
            final Say say = new Say.Builder("The advertisement that you selected is no longer available.").build();
            voiceResponse = new VoiceResponse.Builder().say(say).build();
        }

        response.setContentType("application/xml");
        response.getWriter().print(voiceResponse.toXml());
    }

    private void mapCustomerWithDealer(final String customer, final String advertisementId) {
        customerAdvertisementMapper.put(customer, advertisementId);
    }

    void proxyCall(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String customerNumber = request.getParameter("From");
        VoiceResponse voiceResponse;
        if (this.customerAdvertisementMapper.containsKey(customerNumber)) {
            String advertisementId = this.customerAdvertisementMapper.get(customerNumber);

            if (this.advertisementMapper.containsKey(advertisementId)) {
                final Number number = new Number.Builder(this.advertisementMapper.get(advertisementId).getKey()).build();
                final Say say = new Say.Builder("You will now be connected to " + this.advertisementMapper.get(advertisementId).getValue()).build();
                voiceResponse = new VoiceResponse.Builder().say(say).dial(new Dial.Builder().number(number).build()).build();
            } else {
                final Say say = new Say.Builder("Unfortunately the car that you are interested in is no longer available.").build();
                voiceResponse = new VoiceResponse.Builder().say(say).build();
            }
        } else {
            final Say say = new Say.Builder("Please check the website for a cool car").build();
            voiceResponse = new VoiceResponse.Builder().say(say).build();
        }
        response.setContentType("application/xml");
        response.getWriter().print(voiceResponse.toXml());
    }
}
