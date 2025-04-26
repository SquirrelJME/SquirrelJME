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
 * Tests opening, adding, and recalling data.
 *
 * @since 2025/04/23
 */
public class TestStoreRecall
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
			id = store.addRecord(TestStoreRecall._DATA, 0,
				TestStoreRecall._DATA.length);
		}
		
		// It should be able to be opened again
		try (RecordStore store = RecordStore.openRecordStore("test",
			false))
		{
			// Recall the record
			byte[] recalled = store.getRecord(id);
			
			// Make sure it matches
			this.secondary("matches", true);
		}
	}
}
