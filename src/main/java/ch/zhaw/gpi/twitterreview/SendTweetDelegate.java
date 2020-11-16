package ch.zhaw.gpi.twitterreview;

import java.util.Date;

import javax.inject.Named;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Named("sendTweetAdapter")
public class SendTweetDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println(execution.getProcessInstanceId() + " - " + new Date().toString());
        Thread.sleep(20000);
        System.out.println(execution.getProcessInstanceId() + " - hat das lang gedauert");
    }

}
