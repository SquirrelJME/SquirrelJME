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
import net.multiphasicapps.tac.TestBoolean;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests next record ID remains valid.
 *
 * @since 2025/04/23
 */
public class TestNextId
	extends TestRunnable
{
	/** The number of bulk records. */
	public static final int BULK =
		8;
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@Override
	public void test()
		throws Throwable
	{
		// Open new database, creating it for testing
		try (RecordStore store = RecordStore.openRecordStore("test",
			true))
		{
			// Create a bunch of IDs
			int[] ids = new int[TestNextId.BULK];
			boolean[] matches = new boolean[TestNextId.BULK];
			for (int i = 0; i < TestNextId.BULK; i++)
			{
				// Get the next ID that would be created
				int nextId = store.getNextRecordID();
				
				// Creating a new record should use that ID!
				ids[i] = store.addRecord(new byte[0], 0, 0);
				matches[i] = (ids[i] == nextId);
			}
			
			// First run result
			this.secondary("first", matches);
			
			// Delete odd records
			for (int i = 1; i < TestNextId.BULK; i += 2)
				store.deleteRecord(ids[i]);
			
			// Create them again
			for (int i = 1; i < TestNextId.BULK; i += 2)
			{
				// Get the next ID that would be created
				int nextId = store.getNextRecordID();
				
				// Creating a new record should use that ID!
				int id = store.addRecord(new byte[0], 0, 0);
				matches[i] = (id == nextId);
			}
			
			// Second run result
			this.secondary("second", matches);
		}
	}
}
