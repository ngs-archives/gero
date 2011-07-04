package org.ngsdev.android.sample20.test;

import java.io.File;
import java.io.IOException;

import org.ngsdev.android.util.*;

import android.test.AndroidTestCase;

public class UtilTestCase extends AndroidTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testFile() throws IOException {
		File dir = new File("/data/data/org.ngsdev.android.sample20/testFile");
		dir.mkdirs();
		FileUtil.emptyDir(dir);
		File file = null;
		File[] files = null;

		file = new File(dir, "1.txt");
		file.createNewFile();
		file = new File(dir, "2.txt");
		file.createNewFile();
		file = new File(dir, "3.txt");
		file.createNewFile();
		files = dir.listFiles();
		assertEquals(files.length, 3);
		FileUtil.emptyDir(dir);
		files = dir.listFiles();
		assertEquals(files.length, 0);

		file = new File(dir, "4.txt");
		FileUtil.writeBytesToFile(file, new String("abcde").getBytes());

		assertEquals(new String(FileUtil.getBytesFromFile(file)), "abcde");

		FileUtil.emptyDir(dir);
		dir.delete();

		assertEquals(
				new String(FileUtil.getBytesFromResourceId(this.getContext(),
						org.ngsdev.android.sample20.R.raw.sample1)),
				"This is a sample text for unit testing.");

	}

}
