package net.whydah.sso.commands.adminapi.user;

import java.net.URI;

import com.github.kevinsawicki.http.HttpRequest;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.whydah.sso.commands.baseclasses.BaseHttpPostHystrixCommand;
import net.whydah.sso.commands.baseclasses.HttpSender;

/**
 * Command for sending SMS via Target365/Strex gateway
 * Documentation: https://github.com/Target365/REST
 */
public class CommandSendSMSToUserTarget365 extends BaseHttpPostHystrixCommand<String> {

    private String recipientPhoneNumber;
    private String messageContent;
    private String sender;
    private String apiKey;
    private String tag;
    
    public static int DEFAULT_TIMEOUT = 10000;
    private static final String TARGET_PATH = "/api/out-messages";

    /**
     * @param serviceURL Base URL for Target365 API (e.g., https://shared.target365.io)
     * @param apiKey API key for authentication
     * @param sender Originator/sender name or number
     * @param recipientPhoneNumber Recipient phone number (e.g., +4791905054)
     * @param messageContent SMS message content
     * @param tag Tag for message categorization (e.g., "sso", "pincode")
     */
    public CommandSendSMSToUserTarget365(String serviceURL, String apiKey, String sender, 
                                         String recipientPhoneNumber, String messageContent, String tag) {
        super(URI.create(serviceURL), "", "", "CommandSendSMSToUserTarget365", DEFAULT_TIMEOUT);
        
        this.apiKey = apiKey;
        this.sender = sender;
        this.recipientPhoneNumber = normalizePhoneNumber(recipientPhoneNumber);
        this.messageContent = messageContent;
        this.tag = tag != null ? tag : "sso";
        
        if (this.messageContent == null || this.recipientPhoneNumber == null || 
            serviceURL == null || this.apiKey == null) {
            log.error(TAG + " initialized with null-values - will fail - messageContent:{}, recipientPhoneNumber:{}, serviceUrl:{}, apiKey present:{}", 
                     messageContent, recipientPhoneNumber, serviceURL, (apiKey != null));
        }
    }

    /**
     * Constructor with custom timeout
     */
    public CommandSendSMSToUserTarget365(String serviceURL, String apiKey, String sender,
                                         String recipientPhoneNumber, String messageContent, 
                                         String tag, int timeout) {
        super(URI.create(serviceURL), "", "", "CommandSendSMSToUserTarget365", timeout);
        
        this.apiKey = apiKey;
        this.sender = sender;
        this.recipientPhoneNumber = normalizePhoneNumber(recipientPhoneNumber);
        this.messageContent = messageContent;
        this.tag = tag != null ? tag : "sso";
        
        if (this.messageContent == null || this.recipientPhoneNumber == null || 
            serviceURL == null || this.apiKey == null) {
            log.error(TAG + " initialized with null-values - will fail - messageContent:{}, recipientPhoneNumber:{}, serviceUrl:{}, apiKey present:{}", 
                     messageContent, recipientPhoneNumber, serviceURL, (apiKey != null));
        }
    }

    /**
     * Normalize phone number to international format with +
     */
    private String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        
        // Remove spaces and common separators
        phoneNumber = phoneNumber.replaceAll("[\\s\\-\\.]", "");
        
        // Add + prefix if not present and starts with country code
        if (!phoneNumber.startsWith("+")) {
            // Assume Norwegian number if 8 digits
            if (phoneNumber.length() == 8) {
                phoneNumber = "+47" + phoneNumber;
            } else if (phoneNumber.startsWith("47") && phoneNumber.length() == 10) {
                phoneNumber = "+" + phoneNumber;
            } else if (!phoneNumber.startsWith("00")) {
                phoneNumber = "+" + phoneNumber;
            }
        }
        
        return phoneNumber;
    }

    @Override
    protected HttpRequest dealWithRequestBeforeSend(HttpRequest request) {
        // Build JSON payload according to Target365 API specification
        JSONObject payload = new JSONObject();
        payload.put("recipient", recipientPhoneNumber);
        payload.put("content", messageContent);
        
        if (sender != null && !sender.isEmpty()) {
            payload.put("sender", sender);
        }
        
        if (tag != null && !tag.isEmpty()) {
            JSONArray tags = new JSONArray();
            tags.add(tag);
            payload.put("tags", tags);
        }
        
        // Set headers
        request.contentType("application/json");
        request.header("X-ApiKey", apiKey);
        
        // Send JSON body
        request.send(payload.toJSONString());
        
        log.debug(TAG + " - Sending SMS to {} with tag {}", recipientPhoneNumber, tag);
        
        return request;
    }

    @Override
    protected String getTargetPath() {
        return TARGET_PATH;
    }

    /**
     * Override the doPostCommand to handle HTTP 201 Created as success
     */
    @Override
    protected String doPostCommand() {
        try {
            String uriString = whydahServiceUri.toString();
            if (getTargetPath() != null) {
                uriString += getTargetPath();
            }

            log.trace(TAG + " - whydahServiceUri={} myAppTokenId={}", uriString, myAppTokenId);

            if (getQueryParameters() != null && getQueryParameters().length != 0) {
                request = HttpRequest.post(uriString, true, getQueryParameters());
            } else {
                request = HttpRequest.post(uriString);
            }
            request.trustAllCerts();
            request.trustAllHosts();

            if (getFormParameters() != null && !getFormParameters().isEmpty()) {
                request.contentType(HttpSender.APPLICATION_FORM_URLENCODED);
                request.form(getFormParameters());
            }

            request = dealWithRequestBeforeSend(request);

            byte[] responseBody = request.bytes();
            byte[] responseBodyCopy = responseBody.clone();
            int statusCode = request.code();
            
            String responseAsText = net.whydah.sso.util.StringConv.UTF8(responseBodyCopy);
            if (responseBodyCopy.length > 0) {
                log.trace("responseBody: {}", responseAsText);
            }
            
            // Handle both 200 OK and 201 Created as success
            switch (statusCode) {
                case java.net.HttpURLConnection.HTTP_OK:
                case java.net.HttpURLConnection.HTTP_CREATED:
                    onCompleted(responseAsText);
                    return dealWithResponse(responseAsText);
                default:
                    onFailed(responseAsText, statusCode);
                    return dealWithFailedResponse(responseAsText, statusCode);
            }
        } catch (Exception ex) {
            log.error(TAG + " - Exception occurred while sending SMS", ex);
            throw new RuntimeException(TAG + " - Failed to send SMS", ex);
        }
    }

    @Override
    protected String dealWithResponse(String response) {
        log.info(TAG + " - SMS sent successfully to {}. Response: {}", recipientPhoneNumber, response);
        return response;
    }

    @Override
    protected String dealWithFailedResponse(String responseBody, int statusCode) {
        log.error(TAG + " - Failed to send SMS to {}. Status code: {}, Response: {}", 
                 recipientPhoneNumber, statusCode, responseBody);
        return null;
    }
    
    @Override
    protected void onCompleted(String responseBody) {
        log.info(TAG + " - SMS sent successfully to {} with tag '{}'", recipientPhoneNumber, tag);
    }
    
    @Override
    protected void onFailed(String responseBody, int statusCode) {
        log.error(TAG + " - Failed to send SMS. Status code: {}, Response: {}", statusCode, responseBody);
    }
}