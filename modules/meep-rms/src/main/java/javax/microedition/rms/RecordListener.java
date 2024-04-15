// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is used to be notified of changes which occur in a record store.
 *
 * @since 2017/02/26
 */
@Api
public interface RecordListener
{
	/**
	 * This is called when a record was added to the store.
	 *
	 * @param __rs The record store the change was made in.
	 * @param __id The ID of the added record.
	 * @since 2017/02/26
	 */
	@Api
	void recordAdded(RecordStore __rs, int __id);
	
	/**
	 * This is called when a record was changed in a store.
	 *
	 * @param __rs The record store the change was made in.
	 * @param __id The ID of the changed record.
	 * @since 2017/02/26
	 */
	@Api
	void recordChanged(RecordStore __rs, int __id);
	
	/**
	 * This is called when a record was deleted from a store.
	 *
	 * @param __rs The record store the change was made in.
	 * @param __id The ID of the deleted record, an attempt to use the record
	 * after it has been used is illegal will throw an
	 * {@link InvalidRecordIDException}.
	 * @since 2017/02/26
	 */
	@Api
	void recordDeleted(RecordStore __rs, int __id);
}

