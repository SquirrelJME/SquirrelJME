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
		try (RecordStore rs = RecordStore.openRecordStore("rmstest", true))
		{
		}
		
		return null;
	}
}

