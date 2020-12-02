import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.managementService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;
import static org.junit.Assert.assertNotNull;

import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.spring.boot.starter.test.helper.StandaloneInMemoryTestConfiguration;
import org.junit.Rule;
import org.junit.Test;

import ch.zhaw.gpi.twitterreview.GetUserInformationDelegate;

public class ScopeOneTest {

  @Rule
  public final ProcessEngineRule processEngineRule = new StandaloneInMemoryTestConfiguration().rule();

  @Test
  @Deployment(resources = {"TweetRequestApprovalProcess.bpmn"}) 
  public void testHappyPath() {
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("Process_TwitterReview", withVariables(
        "tweetContent", "Ich bin ein zulässiger und ausreichend langer Tweet", "anfrageStellenderBenutzer", "a"));


    assertThat(processInstance).isStarted();

    Mocks.register("getUserInformationAdapter", new GetUserInformationDelegate());

    Job job = managementService().createJobQuery().singleResult();
    assertNotNull(job);

    managementService().executeJob(job.getId());

    // Nun findet er das (nicht) deployte DMN-Modell nicht...
    // => man müsste auch dieses wieder mocken, usw.
  }

}