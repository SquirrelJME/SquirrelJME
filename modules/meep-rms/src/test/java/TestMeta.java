// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Arrays;
import javax.microedition.rms.RecordStore;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests meta info such as size and dates.
 *
 * @since 2025/04/23
 */
public class TestMeta
	extends TestRunnable
{
	/** The number of records to create. */
	public static final int COUNT = 25;
	
	/** The size of each record. */
	public static final int SIZE = 10;
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@Override
	public void test()
		throws Throwable
	{
		// Create new database
		try (RecordStore store = RecordStore.openRecordStore("test",
			true))
		{
			this.secondary("available", 
				store.getSizeAvailable() > 0);
			
			// Create a bunch of record of a fixed size, it should total at
			// least all of them
			int id = -1;
			for (int i = 0; i < TestMeta.COUNT; i++)
				id = store.addRecord(new byte[TestMeta.SIZE],
					0, TestMeta.SIZE);
			this.secondary("size",
				store.getSize() >= (TestMeta.COUNT * TestMeta.SIZE));
			this.secondary("single", store.getRecordSize(id));
			
			// The name should be the same
			this.secondary("name", store.getName());
		}
	}
}
