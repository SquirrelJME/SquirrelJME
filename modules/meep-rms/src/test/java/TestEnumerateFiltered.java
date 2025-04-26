// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.ByteIntegerArray;
import java.util.Arrays;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;

/**
 * Tests filtered enumerations.
 *
 * @since 2025/04/23
 */
public class TestEnumerateFiltered
	extends BaseEnumerate
	implements RecordFilter
{
	/**
	 * {@inheritDoc}
	 * @since 2025/04/25
	 */
	@Override
	public boolean matches(byte[] __b)
	{
		this.secondary("matches", true);
		
		// Debug
		Debugging.debugNote("Checking: %s against %s: %b",
			new ByteIntegerArray(__b).toString(),
			new ByteIntegerArray(BaseEnumerate.DATA_B).toString(),
			Arrays.equals(__b, BaseEnumerate.DATA_B));
		
		return Arrays.equals(__b, BaseEnumerate.DATA_B);
	}
	
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
			this, null, false);
		
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
