// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import javax.microedition.rms.RecordStore;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Blank records should be valid.
 *
 * @since 2025/04/24
 */
public class TestBlank
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2025/04/24
	 */
	@Override
	public void test()
		throws Throwable
	{
		// Open new database, creating it for testing
		try (RecordStore store = RecordStore.openRecordStore("test",
			true))
		{
			// Add empty record
			int id = store.addRecord(new byte[0], 0, 0);
			
			// Should return a blank array
			this.secondary("blank", store.getRecord(id));
		}
	}
}

