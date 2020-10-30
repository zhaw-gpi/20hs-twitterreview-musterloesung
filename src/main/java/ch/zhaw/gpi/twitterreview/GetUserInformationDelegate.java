package ch.zhaw.gpi.twitterreview;

import javax.inject.Named;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;


import ch.zhaw.gpi.twitterreview.ldap.LdapService;

@Named("getUserInformationAdapter")
public class GetUserInformationDelegate implements JavaDelegate {

    @Autowired
    private LdapService ldapService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String userName = (String) execution.getVariable("anfrageStellenderBenutzer");

        String userAsJsonString = ldapService.getUser(userName);

        JSONObject userAsJsonObject  = new JSONObject(userAsJsonString);

        execution.setVariable("userEmail", userAsJsonObject.getString("email"));

        String fullName = userAsJsonObject.getString("firstName") + " " + userAsJsonObject.getString("officialName");

        execution.setVariable("userFullName", fullName);

    }
    
}
