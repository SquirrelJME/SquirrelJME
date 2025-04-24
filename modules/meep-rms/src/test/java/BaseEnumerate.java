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
 * Tests enumerating records, base class for setup.
 *
 * @since 2025/04/23
 */
public abstract class BaseEnumerate
	extends TestRunnable
{
	/** First set of data. */
	public static final byte[] DATA_A =
		new byte[]{1, 3, 5, 9, 11, 13, 18, 21};
	
	/** Second set of data. */
	public static final byte[] DATA_B =
		new byte[]{12, 81, 31, 11, 9, 5, 3, 1};
	
	/** Third set of data. */
	public static final byte[] DATA_C =
		new byte[]{11, 9, 5, 3, 1, 12, 81, 31};
	
	/**
	 * Tests running an enumeration test.
	 *
	 * @param __store The store to access.
	 * @param __idA The first ID.
	 * @param __idB The second ID.
	 * @param __idC the third ID.
	 * @throws Throwable On any throwable.
	 * @since 2025/04/23
	 */
	public abstract void test(RecordStore __store,
		int __idA, int __idB, int __idC)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2025/04/23
	 */
	@Override
	public void test()
		throws Throwable
	{
		// Create new databases for later iteration
		int idA;
		int idB;
		int idC;
		try (RecordStore store = RecordStore.openRecordStore("test",
			true))
		{
			idA = store.addRecord(BaseEnumerate.DATA_A, 0,
				BaseEnumerate.DATA_A.length);
			idB = store.addRecord(BaseEnumerate.DATA_B, 0,
				BaseEnumerate.DATA_B.length);
			idC = store.addRecord(BaseEnumerate.DATA_C, 0,
				BaseEnumerate.DATA_C.length);
		}
		
		// It should be able to be opened again
		try (RecordStore store = RecordStore.openRecordStore("test",
			false))
		{
			// Use subtest
			this.test(store, idA, idB, idC);
		}
	}
}
