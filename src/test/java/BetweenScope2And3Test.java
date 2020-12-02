import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.zhaw.gpi.twitterreview.CamundaProcessApplication;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CamundaProcessApplication.class)
public class BetweenScope2And3Test {

  @Test
  public void testHappyPath() {
    ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("Process_TwitterReview", withVariables(
        "tweetContent", "Ich bin ein zulässiger und ausreichend langer Tweet", "anfrageStellenderBenutzer", "a"));

    assertThat(processInstance).isStarted();

    complete(task(), withVariables("checkResult", "accepted"));

    assertThat(processInstance).isWaitingAt("TweetSendenTask");

    complete(externalTask());

    assertThat(processInstance).hasPassed("TweetGepostedEndEvent").isEnded();
  }

  @Test
  public void testAutomaticTweetRejectionPath() {
    ProcessInstance processInstance = runtimeService().createProcessInstanceByKey("Process_TwitterReview")
        .startBeforeActivity("AufVerboteneWorterPrufenTask").setVariables(withVariables("tweetContent",
            "Ich bin zu kurz", "email", "max.muster@a.ch", "userFullName", "Max Muster"))
        .execute();

    assertThat(processInstance).hasPassed("TweetAbgelehntEndEvent1").isEnded();
  }

}