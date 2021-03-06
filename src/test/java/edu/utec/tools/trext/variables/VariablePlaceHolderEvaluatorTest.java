package edu.utec.tools.trext.variables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import org.junit.runner.RunWith;
import edu.utec.test.junit.DefaultOrderedRunner;
import edu.utec.test.junit.ExplicitOrder;

@RunWith(DefaultOrderedRunner.class)
@ExplicitOrder({"replaceVariablesInString", "replaceJockersInString"})
public class VariablePlaceHolderEvaluatorTest {

  @Test
  public void replaceVariablesInString() throws Exception {

    VariablePlaceHolderEvaluator evaluator = new VariablePlaceHolderEvaluator();

    HashMap<String, Object> variables = new HashMap<String, Object>();
    variables.put("code", "201545457");
    variables.put("id", 666);
    variables.put("baseUrl", "http://localhost:5000");
    variables.put("secret", "changeme");

    assertEquals("http://acme.com/201545457",
        evaluator.replaceVariablesAndJockersInString("http://acme.com/${code}", variables));
    assertEquals("http://acme.com/201545457/201545457",
        evaluator.replaceVariablesAndJockersInString("http://acme.com/${code}/${code}", variables));
    assertEquals("http://acme.com/666",
        evaluator.replaceVariablesAndJockersInString("http://acme.com/${id}", variables));
    assertEquals("http://localhost:5000/client?clientId=666&clientSecret=changeme",
        evaluator.replaceVariablesAndJockersInString(
            "${baseUrl}/client?clientId=${id}&clientSecret=${secret}", variables));
  }

  @Test
  public void replaceJockersInString() throws Exception {

    VariablePlaceHolderEvaluator evaluator = new VariablePlaceHolderEvaluator();
    HashMap<String, Object> variables = new HashMap<String, Object>();

    Pattern p = Pattern.compile("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})");
    String value =
        evaluator.replaceVariablesAndJockersInString("http://acme.com/${srand}", variables);
    Matcher m = p.matcher(value);
    assertTrue("value must contain a string uuid. Current:" + value, m.find());

    p = Pattern.compile("/\\d+");
    value = evaluator.replaceVariablesAndJockersInString("http://acme.com/${irand}", variables);
    m = p.matcher(value);
    assertTrue("value must contain a random intenger.Curent:" + value, m.find());

    p = Pattern.compile("/\\d+.\\d+");
    value = evaluator.replaceVariablesAndJockersInString("http://acme.com/${drand}", variables);
    m = p.matcher(value);
    assertTrue("value must contain a random double.Curent:" + value, m.find());
  }
}
