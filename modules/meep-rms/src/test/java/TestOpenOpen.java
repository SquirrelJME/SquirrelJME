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
 * Tests opening and then opening again the same record store.
 *
 * @since 2025/04/23
 */
public class TestOpenOpen
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
		try (RecordStore ignore = RecordStore.openRecordStore("test",
			true))
		{
			// Do nothing!
		}
		
		// It should be able to be opened again
		try (RecordStore ignore = RecordStore.openRecordStore("test",
			false))
		{
			// Do nothing!
		}
		
		// Requesting it be created should not actually do anything
		try (RecordStore ignore = RecordStore.openRecordStore("test",
			true))
		{
			// Do nothing!
		}
	}
}
