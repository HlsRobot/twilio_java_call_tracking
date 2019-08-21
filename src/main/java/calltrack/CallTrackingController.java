package calltrack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("calltrack")
public class CallTrackingController {

    private final CallTrackingService callTrackingService;

    @Autowired
    public CallTrackingController(CallTrackingService callTrackingService) {
        this.callTrackingService = callTrackingService;
    }

    @GetMapping()
    public String init(@RequestParam("phone") final String caller, @RequestParam("adId") final String adId) throws URISyntaxException {
        this.callTrackingService.init(caller, adId);
        return "Thank you for calling!";
    }

    @PostMapping("call")
    public void forward(@RequestParam("adId") final String adId, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        this.callTrackingService.getNumberAndPlaceCall(adId, request, response);
    }

    @PostMapping("proxy_call")
    public void proxyCall(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        this.callTrackingService.proxyCall(request, response);
    }
}
