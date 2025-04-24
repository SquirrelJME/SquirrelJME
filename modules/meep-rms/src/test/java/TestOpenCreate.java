// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that nothing is done on the record.
 *
 * @since 2018/12/13
 */
public class TestOpenCreate
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/13
	 */
	@Override
	public void test()
		throws Throwable
	{
		// Open new database, creating it for testing
		try (RecordStore ignore = RecordStore.openRecordStore("test",
			true))
		{
			// Do nothing!
		}
	}
}

