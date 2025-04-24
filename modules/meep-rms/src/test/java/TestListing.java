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
 * Tests listing owned record stores.
 *
 * @since 2025/04/23
 */
public class TestListing
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@SuppressWarnings("EmptyTryBlock")
	@Override
	public void test()
		throws Throwable
	{
		// Create a bunch of records
		for (String name : Arrays.asList("a", "b", "c"))
			try (RecordStore ignore = RecordStore.openRecordStore(name,
				true))
			{
				// Do nothing
			}
		
		// Locate all store names
		String[] names = RecordStore.listRecordStores();
		
		// Sort them as the order is undefined, then report
		Arrays.sort(names);
		this.secondary("names", names);
	}
}
