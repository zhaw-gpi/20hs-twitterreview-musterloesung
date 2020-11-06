package ch.zhaw.gpi.twitterreview;

import javax.inject.Named;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;



@Named("getUserInformationAdapter")
public class GetUserInformationDelegate implements JavaDelegate {


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String userName = (String) execution.getVariable("anfrageStellenderBenutzer");

        

        execution.setVariable("userFullName", "Mr. X");

    }
    
}
