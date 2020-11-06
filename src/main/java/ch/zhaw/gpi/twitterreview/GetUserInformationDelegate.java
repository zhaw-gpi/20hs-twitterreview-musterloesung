package ch.zhaw.gpi.twitterreview;

import javax.inject.Named;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



@Named("getUserInformationAdapter")
public class GetUserInformationDelegate implements JavaDelegate {


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String userName = (String) execution.getVariable("anfrageStellenderBenutzer");

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8070/api/users/{userName}", HttpMethod.GET, null, String.class, userName);

        String fullName;

        if(response.getStatusCode().equals(HttpStatus.OK)){
            JSONObject userAJsonObject = new JSONObject(response.getBody());
            fullName = userAJsonObject.getString("firstName") + " " + userAJsonObject.getString("officialName");
        } else {
            fullName = "Mr. X";
        }

        execution.setVariable("userFullName", fullName);

    }
    
}
