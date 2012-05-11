package org.sakaiproject.nakamura.solr;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.osgi.service.event.Event;
import org.sakaiproject.nakamura.api.lite.Session;
import org.sakaiproject.nakamura.api.lite.StoreListener;
import org.sakaiproject.nakamura.api.lite.accesscontrol.AccessControlManager;
import org.sakaiproject.nakamura.api.lite.content.Content;
import org.sakaiproject.nakamura.api.lite.content.ContentManager;
import org.sakaiproject.nakamura.api.solr.IndexingHandler;
import org.sakaiproject.nakamura.api.solr.QoSIndexHandler;
import org.sakaiproject.nakamura.api.solr.RepositorySession;
import org.sakaiproject.nakamura.api.solr.TopicIndexer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

/**
 * User: duffy
 * Date: 5/10/12
 * Time: 12:04 PM
 */
public class SparseIndexingServiceImplTest {

  SparseIndexingServiceImpl sisi = null;

  class TestTopicIndexer implements TopicIndexer {

    private HashSet<String> topics = new HashSet<String>();

    public void clear() {
      topics.clear();
    }

    @Override
    public void addHandler(String topic, IndexingHandler handler) {
      topics.add(topic);
    }

    @Override
    public void removeHandler(String topic, IndexingHandler handler) {
      topics.remove(topic);
    }

    public void assertContents(String correctTopics[]) throws Exception {
      if (correctTopics == null || correctTopics.length == 0) {
        assertTrue(topics.isEmpty());
        return;
      }

      for (String topic : correctTopics) {
        assertTrue(topics.contains(topic));
      }
      assertEquals(correctTopics.length, topics.size());
    }
  }

  class TestRepositorySession implements RepositorySession {
    Session sparseSession = null;

    TestRepositorySession (Session session) {
      sparseSession = session;
    }

    @SuppressWarnings("unchecked")
    public <T> T adaptTo(Class<T> c) {
      if (c.equals(org.sakaiproject.nakamura.api.lite.Session.class)) {
        return (T) sparseSession;
      }
      return null;
    }

    public void logout() {
      try {
        sparseSession.logout();
      } catch (Exception e) {
      }
    }
  }

  @Before
  public void setUp() throws Exception {
    sisi = new SparseIndexingServiceImpl();
  }

  private TestTopicIndexer activate (SparseIndexingServiceImpl sisi, Object topics) throws Exception {
    TestTopicIndexer testTopicIndexer = new TestTopicIndexer();

    sisi.contentIndexer = testTopicIndexer;

    HashMap<String, Object> props = new HashMap<String, Object>();

    if (topics != null) {
      props.put("resource.topics", topics);
    }

    sisi.activate(props);

    return testTopicIndexer;
  }

  /**
   * this works around SPARSE-198 by eliminating duplicates from DEFAULT_TOPICS
   * when SPARSE-198 is included in release this can simply be replaced with StoreListener.DEFAULT_TOPICS
   * @return
   */
  private final static String[] SPARSE_198_STOPGAP() {
    final HashSet<String> set = new HashSet<String>();

    for (final String s : StoreListener.DEFAULT_TOPICS) {
      set.add(s);
    }

    return set.toArray(new String[set.size()]);
  }

  @Test
  public void testTopicsAreRegisteredAndUnregistered() throws Exception {

    TestTopicIndexer tti = null;
    final String testTopic1 = "topic1";
    final String testTopic2 = "topic2";

    // try with null and expect default topics
    tti = activate(sisi, null);
    tti.assertContents(SPARSE_198_STOPGAP());
    sisi.deactivate(null);
    tti.assertContents(null);
    tti.clear();

    // try with empty string
    String emptyString = "";
    tti = activate(sisi, emptyString);
    tti.assertContents(SPARSE_198_STOPGAP());
    sisi.deactivate(null);
    tti.assertContents(null);
    tti.clear();

    // try with single topic string
    tti = activate(sisi, testTopic1);
    tti.assertContents(new String[] { testTopic1 });
    sisi.deactivate(null);
    tti.assertContents(null);
    tti.clear();

    // try with single delimited string
    String delimString = testTopic1 + "|" + testTopic2;
    tti = activate(sisi, delimString);
    tti.assertContents(new String[] { testTopic1, testTopic2 });
    sisi.deactivate(null);
    tti.assertContents(null);
    tti.clear();

    // try with empty Array
    String emptyArray[] = new String[0];
    tti = activate(sisi, emptyArray);
    tti.assertContents(null);
    sisi.deactivate(null);
    tti.assertContents(null);
    tti.clear();

    // try with two item Array
    String array[] = new String [] { testTopic1, testTopic2 };
    tti = activate(sisi, array);
    tti.assertContents(array);
    sisi.deactivate(null);
    tti.assertContents(null);
    tti.clear();

    // try with empty Collection
    ArrayList<String> arrayList = new ArrayList<String>();
    tti = activate(sisi, arrayList);
    tti.assertContents(arrayList.toArray(new String[0]));
    sisi.deactivate(null);
    tti.assertContents(null);
    tti.clear();

    // try with two item Collection
    arrayList.add(testTopic1);
    arrayList.add(testTopic2);
    tti = activate(sisi, arrayList);
    tti.assertContents(arrayList.toArray(new String[2]));
    sisi.deactivate(null);
    tti.assertContents(null);
    tti.clear();

    // try with unsupported type
    tti = activate(sisi, Boolean.TRUE);
    tti.assertContents(SPARSE_198_STOPGAP());
    sisi.deactivate(null);
    tti.assertContents(null);
    tti.clear();
  }

  class TestIndexingHandler implements IndexingHandler {

    Collection<SolrInputDocument> docs;
    Collection<String> deleteQueries;

    public TestIndexingHandler (Collection<SolrInputDocument> docs, Collection<String> deleteQueries) {
      this.docs = docs;
      this.deleteQueries = deleteQueries;
    }

    @Override
    public Collection<SolrInputDocument> getDocuments(RepositorySession repositorySession, Event event) {
      return docs;
    }

    @Override
    public Collection<String> getDeleteQueries(RepositorySession respositorySession, Event event) {
      return deleteQueries;
    }
  }

  private final Session prepSparseSession() throws Exception {
    Session session = mock(Session.class);
    ContentManager contentManager = mock(ContentManager.class);
    AccessControlManager accessControlManager = mock(AccessControlManager.class);

    when(session.getContentManager()).thenReturn(contentManager);
    when(session.getAccessControlManager()).thenReturn(accessControlManager);

    return session;
  }

  private final void addContent(Session session, Content content) throws Exception {
    final ContentManager contentManager = session.getContentManager();
    when(contentManager.get(eq(content.getPath()))).thenReturn(content);
  }

  @Test
  public void testDocsWithOnlySystemPropertiesFilteredOut() throws Exception {

    // prep sparse mocks
    Session session = prepSparseSession();
    Map<String, Object> contentProperties = new HashMap<String, Object>();

    contentProperties.put(SparseIndexingServiceImpl.SLING_RESOURCE_TYPE, "testType");
    Content content = new Content("testPath", contentProperties);

    addContent(session, content);

    AccessControlManager acm = session.getAccessControlManager();
    final String principals[] = new String[] { "testPrincipal1", "testPrincipal2" };

    when(acm.findPrincipals(anyString(), anyString(), anyInt(), eq(true))).thenReturn(principals);

    SolrInputDocument onlySystemProperties = new SolrInputDocument();
    onlySystemProperties.setField(IndexingHandler.FIELD_ID, "testID");

    SolrInputDocument withExtraProperty = new SolrInputDocument();
    withExtraProperty.setField(IndexingHandler.FIELD_ID, "testID");
    withExtraProperty.setField(IndexingHandler._DOC_SOURCE_OBJECT, content);
    withExtraProperty.setField("extraField", "extraFieldValue");

    HashSet<SolrInputDocument> docs = new HashSet<SolrInputDocument>();
    docs.add(onlySystemProperties);

    TestIndexingHandler tih = new TestIndexingHandler(docs, null);
    sisi.addHandler("testType", tih);

    TestRepositorySession repoSession = new TestRepositorySession(session);
    Dictionary props = new Hashtable();

    props.put("path", "testPath");
    props.put("resourceType", "testType");
    Event event = new Event(StoreListener.TOPIC_BASE + "authorizables/" + StoreListener.ADDED_TOPIC, props);

    assertTrue(sisi.getDocuments(repoSession, event).isEmpty());

    docs.add(withExtraProperty);

    Collection<SolrInputDocument> results = sisi.getDocuments(repoSession, event);

    assertEquals(1, results.size());

    SolrInputDocument doc = results.iterator().next();

    SolrInputField readerField = doc.getField(IndexingHandler.FIELD_READERS);
    assertNotNull(readerField);
    assertEquals(2, readerField.getValueCount());
    Collection<Object> readers = readerField.getValues();
    assertTrue(readers.contains("testPrincipal1"));
    assertTrue(readers.contains("testPrincipal2"));

    SolrInputField idField = doc.getField(IndexingHandler.FIELD_ID);
    assertEquals("testID", idField.getValue());

    SolrInputField pathField = doc.getField(IndexingHandler.FIELD_PATH);
    assertEquals("testPath", pathField.getValue());

    SolrInputField resourceTypeField = doc.getField(IndexingHandler.FIELD_RESOURCE_TYPE);
    assertEquals("testType", resourceTypeField.getValue());

    SolrInputField extraField = doc.getField("extraField");
    assertEquals("extraFieldValue", extraField.getValue());
  }

  class TestQoSIndexingHandler implements IndexingHandler, QoSIndexHandler {
    int ttl = Integer.MAX_VALUE;

    public TestQoSIndexingHandler (int ttl) {
      this.ttl = ttl;
    }

    @Override
    public Collection<SolrInputDocument> getDocuments(RepositorySession repositorySession, Event event) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<String> getDeleteQueries(RepositorySession respositorySession, Event event) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getTtl(Event event) {
      return ttl;
    }
  }

  @Test
  public void testTtlReturnsAppropriateValue() throws Exception {
    sisi.addHandler("testType", new TestQoSIndexingHandler(50));

    Dictionary props = new Hashtable();

    props.put("path", "testPath");
    props.put("resourceType", "testType");
    Event event = new Event(StoreListener.TOPIC_BASE + "authorizables/" + StoreListener.ADDED_TOPIC, props);

    assertEquals(50, sisi.getTtl(event));
  }

  @Test
  public void testGetDeleteQueries() throws Exception {
    sisi.addHandler ("testType", new IndexingHandler() {
      @Override
      public Collection<SolrInputDocument> getDocuments(RepositorySession repositorySession, Event event) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
      }

      @Override
      public Collection<String> getDeleteQueries(RepositorySession respositorySession, Event event) {
        return new ImmutableList.Builder<String>().add("testPath").build();
      }
    });

    Dictionary props = new Hashtable();

    props.put("path", "testPath");
    props.put("resourceType", "testType");
    Event event = new Event(StoreListener.TOPIC_BASE + "authorizables/" + StoreListener.DELETE_TOPIC, props);

    Collection<String> queries = sisi.getDeleteQueries(null, event);

    assertNotNull(queries);
    assertEquals(1, queries.size());
    assertTrue(queries.contains("testPath"));
  }
}
