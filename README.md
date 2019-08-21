# twilio_java_call_tracking

- In order to make this application work, a basic understanding of the following is assumed:
  - Java 11
  - Spring boot
  - Twilio API.

- Open and change application.yml
```yml
twilio:
  account_sid: # your account sid
  auth_token: # your auth token
  twilio_number: # your trial number
  url: # your url for your backend. The twilio application needs an accessible url that can be either on a host server or with the usage of the ngrok service.
```

- You will need to adapt the \<NUMBER OF DEALERSHIP\> variables in CallTrackingService.java with real or Twilio numbers.
