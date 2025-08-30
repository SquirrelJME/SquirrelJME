// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests enumerating records.
 *
 * @since 2025/04/23
 */
public class TestEnumerateSimple
	extends BaseEnumerate
{
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@Override
	public void test(RecordStore __store, int __a, int __b, int __c)
		throws Throwable
	{
		// Enumerate, no filters or otherwise
		RecordEnumeration iterator = __store.enumerateRecords(
			null, null, false);
		
		// Go through each
		while (iterator.hasNextElement())
		{
			// Get the ID of the next record
			int id = iterator.nextRecordId();
			
			// Matched one?
			if (id == __a)
				this.secondary("founda", true);
			else if (id == __b)
				this.secondary("foundb", true);
			else if (id == __c)
				this.secondary("foundc", true);
		}
	}
}
