// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.rms.RecordListener;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreNotFoundException;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests deleting an entire record store.
 *
 * @since 2025/04/23
 */
public class TestDeleteAll
	extends TestRunnable
{
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
			// Create a bunch of records
			for (int i = 0; i < 25; i++)
				store.addRecord(new byte[i], 0, i);
			
			// To mark created count
			this.secondary("created", store.getNumRecords());
		}
		
		// Delete the record store
		RecordStore.deleteRecordStore("test");
		
		// Creating the record store should fail
		try (RecordStore ignored = RecordStore.openRecordStore("test",
			false))
		{
			this.fail();
		}
		catch (RecordStoreNotFoundException ignored)
		{
			// This is good!
		}
		
		// Create it again, there should be zero records
		try (RecordStore store = RecordStore.openRecordStore("test",
			true))
		{
			this.secondary("count", store.getNumRecords());
		}
	}
}
