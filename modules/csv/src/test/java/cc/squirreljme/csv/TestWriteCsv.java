// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.csv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests writing CSVs.
 *
 * @since 2023/09/14
 */
public class TestWriteCsv
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2023/09/14
	 */
	@Override
	public void test()
		throws IOException
	{
		// Write the CSV output
		byte[] data;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream out = new PrintStream(baos, true,
				"utf-8");
			CsvWriter<String[]> writer = new CsvWriter<>(
				new __ExampleSerialization__(), out))
		{
			writer.writeAll(
				new String[]{"1234", "5678", "9012", "3456"},
				new String[]{"squirrel", "rat", "rodent", "mouse"},
				new String[]{"one"},
				new String[]{"two", "three"},
				new String[]{"Hello World", "  Squeak  ",
					"\"Hello\", \"Something\"", "CUTE!"});
			
			// Get the data!
			out.flush();
			data = baos.toByteArray();
		}
		
		// Is the same as the read operation now
		try (CsvReader<String[]> reader = new CsvReader<>(
			new __ExampleSerialization__(), new CsvReaderInputStream(data)))
		{
			List<String[]> result = reader.readAll();
			
			for (int i = 0, n = result.size(); i < n; i++)
				this.secondary("" + i, result.get(i));
		}
	}
}
