package org.yaz4j;

import org.junit.BeforeClass;
import org.junit.Test;
import org.yaz4j.exception.ZoomException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class QueryTest {
  
  /**
   * YAZ library is loaded by Connection, but this test class never creates a connection object, so we must load the library explicitly
   *
   * @throws Exception if the loading failed
   */
  @BeforeClass
  public static void setUp() throws Exception {
    // on Linux   'yaz4j' maps to 'libyaz4j.so' (i.e. 'lib' prefix & '.so'  extension)
    // on Windows 'yaz4j' maps to 'yaz4j.dll'   (i.e.                '.dll' extension)
    LoadLib.load("yaz4j");
  }
  
  @Test
  public void PrefixQueryNull() throws ZoomException {
    String msg = null;
    try {
      new PrefixQuery(null);
    } catch (IllegalArgumentException e) {
      msg = e.getMessage();
    }
    assertEquals("query string cannot be null", msg);
  }

  @Test
  public void SortByNull() throws ZoomException {
    String msg = null;
    try {
      Query q = new PrefixQuery("x");
      q.sortBy(null, null);
    } catch (IllegalArgumentException e) {
      msg = e.getMessage();
    }
    assertEquals("strategy string cannot be null", msg);
  }

  @Test
  public void PrefixQueryInvalid() {
    String msg = null;
    try {
      new PrefixQuery("@attr 1=4");
    } catch (ZoomException e) {
      msg = e.getMessage();
    }
    assertEquals("invalid query @attr 1=4", msg);
  }

  @Test
  public void CQLQueryInvalid() {
    String msg = null;
    try {
      new CQLQuery("(");
    } catch (ZoomException e) {
      msg = e.getMessage();
    }
    // CQL are sent verbatim to server, so no error here
    assertNull(msg);
  }

  @Test
  public void QuerySortByFailed1() {
    String msg = null;
    try {

      Query q = new PrefixQuery("@attr 1=4 title");
      q.sortBy("x", "1=4");
    } catch (ZoomException e) {
      msg = e.getMessage();
    }
    assertEquals("invalid sort strategy x", msg);
  }

  @Test
  public void QuerySortByFailed2() {
    String msg = null;
    try {
      Query q = new PrefixQuery("@attr 1=4 title");
      q.sortBy("z3950", "1=");
    } catch (ZoomException e) {
      msg = e.getMessage();
    }
    assertEquals("invalid sort criteria 1=", msg);
  }

}
