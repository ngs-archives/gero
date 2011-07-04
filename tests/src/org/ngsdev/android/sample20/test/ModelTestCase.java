package org.ngsdev.android.sample20.test;

import android.test.AndroidTestCase;

import java.io.IOException;
import java.util.List;

import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.DatabaseHelper;
import org.ngsdev.android.model.BookmarkedUri;
import org.ngsdev.android.model.HistoryUri;
import org.ngsdev.android.model.ManagedUri;
import org.ngsdev.android.model.UriBookmarkManager;
import org.ngsdev.android.model.UriHistoryManager;
import org.ngsdev.android.model.UriManager;
import org.ngsdev.android.sample20.test.model.BookmarkedTicket;
import org.ngsdev.android.sample20.test.model.HistoryTicket;
import org.ngsdev.android.sample20.test.model.ManagedTicket;
import org.ngsdev.android.sample20.test.model.TicketBookmarkManager;
import org.ngsdev.android.sample20.test.model.TicketHistoryManager;
import org.ngsdev.android.util.FileUtil;
import org.ngsdev.android.util.Log20;

import android.content.Context;
import android.net.Uri;

public class ModelTestCase extends AndroidTestCase {

	public static final String DB_NAME = "managedUri.db";

	@SuppressWarnings("unchecked")
	protected void setUp() throws Exception {
		super.setUp();
		UriManager.buildDB(DB_NAME, 1, BookmarkedUri.class, HistoryUri.class,
				BookmarkedTicket.class, HistoryTicket.class);
		Log20.enable = true;
	}

	protected void tearDown() throws Exception {
		super.tearDown();

		DatabaseHelper.dropDatabase(this.getContext(), DB_NAME);
	}

	public void testBasic() throws ActiveRecordException {

		ManagedUri uri = null;
		Context c = this.getContext();
		UriHistoryManager m1 = new UriHistoryManager(c);
		UriBookmarkManager m2 = new UriBookmarkManager(c);

		m1.setMaxStorable(20);
		assertEquals(m1.getMaxstorable(), 20);
		m1.setMaxStorable(10);
		assertEquals(m1.getMaxstorable(), 10);
		m2.setMaxStorable(20);
		assertEquals(m2.getMaxstorable(), 0);

		m1.delete();
		m2.delete();

		m1.add(Uri.parse("s20://sample20.app/path/to/content1"));
		m1.add(Uri.parse("s20://sample20.app/path/to/content2"));
		m1.add(Uri.parse("s20://sample20.app/path/to/content3"));
		m1.add(Uri.parse("s20://sample20.app/path/to/content4"));

		m2.add(Uri.parse("s20://sample20.app/path/to/content1"));
		m2.add(Uri.parse("s20://sample20.app/path/to/content5"));

		assertEquals(m1.size(), 4);
		assertEquals(m2.size(), 2);

		List<ManagedUri> all = null;
		all = m1.all();
		assertEquals(all.get(0).getUri().getPath(), "/path/to/content1");
		assertEquals(all.get(1).getUri().getPath(), "/path/to/content2");
		assertEquals(all.get(2).getUri().getPath(), "/path/to/content3");
		assertEquals(all.get(3).getUri().getPath(), "/path/to/content4");

		all = m2.all();
		assertEquals(all.get(0).getUri().getPath(), "/path/to/content1");

		uri = m1.findUri(Uri.parse("s20://sample20.app/path/to/content1"));
		assertEquals(uri.getUri().getPath(), "/path/to/content1");

		uri = m2.findUri(Uri.parse("s20://sample20.app/path/to/content1"));
		assertEquals(uri.getUri().getPath(), "/path/to/content1");

		m1.delete();
		assertEquals(m1.size(), 0);
		m2.delete();
		assertEquals(m2.size(), 0);

		m1.close();
		m2.close();
	}

	public void testMax() throws ActiveRecordException {
		Context c = this.getContext();
		UriHistoryManager m = new UriHistoryManager(c);
		m.delete();

		m.setMaxStorable(10);

		for (int i = 1; i <= 15; i++) {
			m.add(Uri.parse(String.format(
					"s20://sample20.app/path/to/content%d", i)));
		}

		assertEquals(m.size(), 10);

		Log20.enable = true;

		List<ManagedUri> all = null;
		all = m.all();
		assertEquals(all.get(0).getUri().getPath(), "/path/to/content6");
		assertEquals(all.get(1).getUri().getPath(), "/path/to/content7");
		assertEquals(all.get(2).getUri().getPath(), "/path/to/content8");
		assertEquals(all.get(3).getUri().getPath(), "/path/to/content9");
		assertEquals(all.get(4).getUri().getPath(), "/path/to/content10");
		assertEquals(all.get(5).getUri().getPath(), "/path/to/content11");
		assertEquals(all.get(6).getUri().getPath(), "/path/to/content12");
		assertEquals(all.get(7).getUri().getPath(), "/path/to/content13");
		assertEquals(all.get(8).getUri().getPath(), "/path/to/content14");
		assertEquals(all.get(9).getUri().getPath(), "/path/to/content15");

		m.setMaxStorable(5);
		assertEquals(m.size(), 5);
		all = m.all();
		assertEquals(all.get(0).getUri().getPath(), "/path/to/content11");
		assertEquals(all.get(1).getUri().getPath(), "/path/to/content12");
		assertEquals(all.get(2).getUri().getPath(), "/path/to/content13");
		assertEquals(all.get(3).getUri().getPath(), "/path/to/content14");
		assertEquals(all.get(4).getUri().getPath(), "/path/to/content15");

		m.delete();
		assertEquals(m.size(), 0);

		m.close();
	}

	public void testTicket() throws ActiveRecordException, IOException {

		ManagedTicket ticket = null;
		Context c = this.getContext();
		TicketHistoryManager m1 = new TicketHistoryManager(c);
		TicketBookmarkManager m2 = new TicketBookmarkManager(c);
		
		String strJson = new String(FileUtil.getBytesFromResourceId(c, org.ngsdev.android.sample20.R.raw.test_ticket1));
		
		Log20.d(strJson);
		
		int i;
		
		for(i=1;i<=20;i++) {
			ticket = (ManagedTicket) m1.add(new HistoryTicket());
			assertNotNull(ticket);
		}

		for(i=1;i<=20;i++) {
			ticket = (ManagedTicket) m2.add(new BookmarkedTicket());
			assertNotNull(ticket);
		}

	}

}
