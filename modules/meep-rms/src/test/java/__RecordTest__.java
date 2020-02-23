// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreNotFoundException;
import net.multiphasicapps.tac.TestSupplier;

/**
 * Base class to wrap record store access, since it needs to be created and
 * deleted.
 *
 * @param <T> The return type.
 * @since 2018/12/13
 */
abstract class __RecordTest__<T>
	extends TestSupplier<T>
{
	/**
	 * Runs an RMS test on the given record store.
	 *
	 * @param __rs The input record store.
	 * @return The result of the test.
	 * @throws NullPointerException On null arguments.
	 * @throws Throwable On any exception.
	 * @since 2018/12/13
	 */
	public abstract T test(RecordStore __rs)
		throws NullPointerException, Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/13
	 */
	@Override
	public final T test()
		throws Throwable
	{
		// Try to delete a previously created record store
		try
		{
			RecordStore.deleteRecordStore("rms-test");
		}
		
		// Ignore this because it is expected
		catch (RecordStoreNotFoundException e)
		{
		}
		
		// Open the database y creating a new one
		try (RecordStore rs = RecordStore.openRecordStore("rms-test", true))
		{
			// Run test on it
			return this.test(rs);
		}
	}
}

