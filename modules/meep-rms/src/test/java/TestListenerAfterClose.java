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
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests adding and using listeners after close.
 *
 * @since 2025/04/23
 */
public class TestListenerAfterClose
	extends TestRunnable
	implements RecordListener
{
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@Override
	public void recordAdded(RecordStore __rs, int __id)
	{
		// This one should work
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@Override
	public void recordChanged(RecordStore __rs, int __id)
	{
		this.fail();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@Override
	public void recordDeleted(RecordStore __rs, int __id)
	{
		this.fail();
	}
	
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
			// Add listener, which should be retained
			store.addRecordListener(this);
			
			// Add new record
			id = store.addRecord(new byte[0], 0, 0);
		}
		
		// Open again
		try (RecordStore store = RecordStore.openRecordStore("test",
			false))
		{
			// Change then delete the record, no listeners should be called
			store.setRecord(id, new byte[0], 0, 0);
			store.deleteRecord(id);
		}
	}
}
