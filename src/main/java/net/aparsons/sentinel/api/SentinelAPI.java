package net.aparsons.sentinel.api;

public class SentinelAPI {
    
    private static final String BASE_URL = "https://sentinel.whitehatsec.com/api";
    
    private final String key;
    
    public SentinelAPI(String key) {
        this.key = key;
    }

    public String getSiteDetailsString(int siteId) {
        return BASE_URL + "/site/" + siteId + "?key=" + key;
    }
    
    public String getSiteListString() {
        return BASE_URL + "/site" + "?key=" + key;
    }
    
    public String getSiteVulnDetailsString(int siteId) {
        return getAllVulnDetailsString() + "&query_site=" + siteId;
    }
    
    public String getAllVulnDetailsString() {
        return getVulnDetailsString(true, true, true, true, true, true, true, true, true, true);
    }
    
    public String getVulnDetailsString(boolean displayAbbr, boolean displayAttackVectors, boolean displayBody, boolean displayDescription, boolean displayHeaders, boolean displayParam, boolean displayQA, boolean displayRequest, boolean displayResponse, boolean displaySolution) {
        // Adjusting for dependancies
        if (displayAbbr || displayBody || displayHeaders || displayParam || displayRequest || displayResponse) {
            displayAttackVectors = true;
            if (displayAbbr) {
                displayBody = true;
            }
        }
        
        StringBuilder builder = new StringBuilder();
        builder.append(BASE_URL);
        builder.append("/vuln");
        
        int count = 0;
        
        if (displayAbbr) {
            builder.append(count == 0 ? "?" : "&");
            builder.append("display_abbr=1");            
            count++;
        }
        
        if (displayAttackVectors) {
            builder.append(count == 0 ? "?" : "&");
            builder.append("display_attack_vectors=1");            
            count++;
        }
        
        if (displayBody) {
            builder.append(count == 0 ? "?" : "&");
            builder.append("display_body=1");            
            count++;
        }
        
        if (displayDescription) {
            builder.append(count == 0 ? "?" : "&");
            builder.append("display_description=1");            
            count++;
        }
        
        if (displayHeaders) {
            builder.append(count == 0 ? "?" : "&");
            builder.append("display_headers=1");            
            count++;
        }
        
        if (displayParam) {
            builder.append(count == 0 ? "?" : "&");
            builder.append("display_param=1");            
            count++;
        }
        
        if (displayQA) {
            builder.append(count == 0 ? "?" : "&");
            builder.append("display_qanda=1");            
            count++;
        }
        
        if (displayRequest) {
            builder.append(count == 0 ? "?" : "&");
            builder.append("display_request=1");            
            count++;
        }
        
        if (displayResponse) {
            builder.append(count == 0 ? "?" : "&");
            builder.append("display_response=1");            
            count++;
        }
        
        if (displaySolution) {
            builder.append(count == 0 ? "?" : "&");
            builder.append("display_solution=1");            
            count++;
        }
        
        builder.append(count == 0 ? "?" : "&");
        builder.append("key=");
        builder.append(key);
        
        return builder.toString();
    }
    
}
