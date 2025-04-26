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
import javax.microedition.rms.RecordListener;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests listening to data changes.
 *
 * @since 2025/04/23
 */
public class TestListener
	extends TestRunnable
	implements RecordListener
{
	/** First set of data. */
	private static final byte[] _DATA_A =
		new byte[]{1, 3, 5, 9, 11, 13, 18, 21};
	
	/** Second set of data. */
	private static final byte[] _DATA_B =
		new byte[]{12, 81, 31, 11, 9, 5, 3, 1};
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@Override
	public void recordAdded(RecordStore __rs, int __id)
	{
		try
		{
			// Should exist with A's data
			this.secondary("added",
				Arrays.equals(TestListener._DATA_A, __rs.getRecord(__id)));
		}
		catch (RecordStoreException __e)
		{
			throw new RuntimeException(__e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@Override
	public void recordChanged(RecordStore __rs, int __id)
	{
		try
		{
			// Should exist with B's data
			this.secondary("changed",
				Arrays.equals(TestListener._DATA_B, __rs.getRecord(__id)));
		}
		catch (RecordStoreException __e)
		{
			throw new RuntimeException(__e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@Override
	public void recordDeleted(RecordStore __rs, int __id)
	{
		try
		{
			// Should have no data
			this.secondary("deleted", __rs.getRecord(__id));
		}
		catch (RecordStoreException __e)
		{
			throw new RuntimeException(__e);
		}
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
			id = store.addRecord(TestListener._DATA_A, 0,
				TestListener._DATA_A.length);
			
			// It should be able to be opened again, with the listener intact
			try (RecordStore sub = RecordStore.openRecordStore("test",
				false))
			{
				// Should refer to the same store
				this.secondary("same", store == sub);
				
				// Set new data
				sub.setRecord(id, TestListener._DATA_B, 0,
					TestListener._DATA_B.length);
			}
			
			// It should be able to be opened again, with the listener intact
			try (RecordStore sub = RecordStore.openRecordStore("test",
				false))
			{
				// Delete the data
				sub.deleteRecord(id);
			}
		}
	}
}
