import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

/** Testklass.
 * @author jaanus
 */
public class GraphTaskTest {

   @Test (timeout=20000)
   public void test_normal_graph() {
      GraphTask.main (10, 10);
   }

   @Test (timeout=20000)
   public void test_small_graph() {
      GraphTask test = new GraphTask();

      List<GraphTask.Arc> result = test.run_test1();

      assertNotNull(result);
      assertEquals(1, result.size());
      assertEquals("av1_v2", result.get(0).toString());

      System.out.println(result);
   }

   @Test (timeout=20000)
   public void test_null_graph() {
      GraphTask test = new GraphTask();

      List<GraphTask.Arc> result = test.run_test2();

      assertNull(result);

      System.out.println(result);
   }


   @Test (timeout=20000)
   public void test5() {
      GraphTask.main (2000, 4000);
   }

}
