// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

/**
 * This is used to be notified of changes which occur in a record store.
 *
 * @since 2017/02/26
 */
public interface RecordListener
{
	/**
	 * This is called when a record was added to the store.
	 *
	 * @param __rs The record store the change was made in.
	 * @param __id The ID of the added record.
	 * @since 2017/02/26
	 */
	public abstract void recordAdded(RecordStore __rs, int __id);
	
	/**
	 * This is called when a record was changed in a store.
	 *
	 * @param __rs The record store the change was made in.
	 * @param __id The ID of the changed record.
	 * @since 2017/02/26
	 */
	public abstract void recordChanged(RecordStore __rs, int __id);
	
	/**
	 * This is called when a record was deleted from a store.
	 *
	 * @param __rs The record store the change was made in.
	 * @param __id The ID of the deleted record, an attempt to use the record
	 * after it has been used is illegal will throw an
	 * {@link InvalidRecordIDException}.
	 * @since 2017/02/26
	 */
	public abstract void recordDeleted(RecordStore __rs, int __id);
}

