// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.rms.RecordStore;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests counting up and down records.
 *
 * @since 2025/04/23
 */
public class TestCount
	extends TestRunnable
{
	/** Total to create, then delete. */
	public static final int TOTAL =
		24;
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@Override
	public void test()
		throws Throwable
	{
		try (RecordStore store = RecordStore.openRecordStore("test",
			true))
		{
			// Add
			int[] ids = new int[TestCount.TOTAL];
			for (int i = 0; i < TestCount.TOTAL; i++)
			{
				// Check count
				if (store.getNumRecords() != i)
					this.secondary("adda" + i,
						store.getNumRecords());
				
				// Create record
				ids[i] = store.addRecord(new byte[0], 0, 0);
				
				// Check count, again
				if (store.getNumRecords() != (i + 1))
					this.secondary("addb" + i,
						store.getNumRecords());
			}
			
			// Delete all records
			for (int i = 0; i < TestCount.TOTAL; i++)
			{
				// Check count
				if (store.getNumRecords() != (TestCount.TOTAL - i))
					this.secondary("dela" + i,
						store.getNumRecords());
				
				// Create record
				store.deleteRecord(ids[i]);
				
				// Check count, again
				if (store.getNumRecords() != (TestCount.TOTAL - (i + 1)))
					this.secondary("delb" + i,
						store.getNumRecords());
			}
		}
	}
}
