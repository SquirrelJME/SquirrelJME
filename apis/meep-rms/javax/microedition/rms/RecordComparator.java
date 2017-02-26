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
 * This is an interface which defines a method for comparing records in an
 * implementation defined manner.
 *
 * As an example the comparator may be used by the {@link RecordEnumeration}
 * in the following manner:
 * {@code
 * RecordComparator comp = new CustomRecordComparator();
 * if (comp.compare(recordstore.getRecord(a), recordstore.getRecord(b)) ==
 *     RecordComparator.PRECEDES)
 *     return a;
 * }
 *
 * @see RecordEnumeration RecordFilter
 * @since 2017/02/26
 */
public interface RecordComparator
{
	/** This represents two equal records. */
	public static final int EQUIVALENT =
		0;
	
	/** This represents a record that follows another. */
	public static final int FOLLOWS =
		1;
	
	/** This represents a record that precedes another. */
	public static final int PRECEDES =
		-1;
	
	/**
	 * Compares the data in one record with the data in another record.
	 *
	 * @param __a The first record, the array must not be modified by the
	 * comparator.
	 * @param __b The second record, the array must not be modified by the
	 * comparator.
	 * @return The record comparison, one of {@link #EQUIVALENT},
	 * {@link #FOLLOWS}, or {@link #PRECEDES}.
	 * @since 2017/02/26
	 */
	public abstract int compare(byte[] __a, byte[] __b);
}

