// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.csv;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests reading a CSV.
 *
 * @since 2023/09/14
 */
public class TestReadCsv
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2023/09/14
	 */
	@Override
	public void test()
		throws IOException
	{
		try (InputStream in = TestReadCsv.class.getResourceAsStream(
				"read.csv");
			CsvReader<String[]> reader = new CsvReader<>(
				new __ExampleSerialization__(), new CsvReaderInputStream(in)))
		{
			List<String[]> result = reader.readAll();
			
			for (int i = 0, n = result.size(); i < n; i++)
				this.secondary("" + i, result.get(i));
		}
	}
}
