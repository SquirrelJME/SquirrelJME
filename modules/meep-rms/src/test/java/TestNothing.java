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

/**
 * Tests that nothing is done on the record.
 *
 * @since 2018/12/13
 */
public class TestNothing
	extends __RecordTest__<Object>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/13
	 */
	@Override
	public Object test(RecordStore __rs)
		throws RecordStoreException
	{
		try (RecordStore rs = RecordStore.openRecordStore("rmstest",
			true))
		{
		}
		
		return null;
	}
}

