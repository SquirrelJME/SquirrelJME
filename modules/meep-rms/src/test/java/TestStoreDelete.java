// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import javax.microedition.rms.RecordStore;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests opening, adding, and deleting the data.
 *
 * @since 2025/04/23
 */
public class TestStoreDelete
	extends TestRunnable
{
	/** Data to store into the record and to test against. */
	private static final byte[] _DATA =
		new byte[]{1, 3, 5, 9, 11, 13, 18, 21};
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@Override
	public void test()
		throws Throwable
	{
		// Create new database
		int id;
		try (RecordStore store = RecordStore.openRecordStore("test",
			true))
		{
			// Store into it
			id = store.addRecord(TestStoreDelete._DATA, 0,
				TestStoreDelete._DATA.length);
		}
		
		// It should be able to be opened again
		try (RecordStore store = RecordStore.openRecordStore("test",
			false))
		{
			// Delete it
			store.deleteRecord(id);
			
			// Should be missing
			this.secondary("data", store.getRecord(id));
		}
	}
}
