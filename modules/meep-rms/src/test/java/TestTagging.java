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
 * Tests tagging of records.
 *
 * @since 2025/04/23
 */
public class TestTagging
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
		// Create new database
		try (RecordStore store = RecordStore.openRecordStore("test",
			true))
		{
			// Set record tag
			int id = store.addRecord(new byte[0], 0, 0, 7);
			
			// Should be able to be obtained
			this.secondary("first", store.getTag(id));
			
			// Set new tag
			store.setRecord(id, new byte[0], 0, 0, 9);
			
			// Should be the new value
			this.secondary("second", store.getTag(id));
		}
	}
}
