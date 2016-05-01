// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

/**
 * This class contains helper methods for methods that exist in the desktop
 * {@link java.util.Collections} but ones for which are missing in Java ME.
 *
 * @since 2016/02/28
 */
public class MissingCollections
{
	
	
	
	/**
	 * Not initialized.
	 *
	 * @since 2016/02/28
	 */
	private MissingCollections()
	{
	}
	
	/**
	 * Makes a boxed wrapped around a given list.
	 *
	 * @param __v The list to wrap boxed values for.
	 * @return The wrapped array as boxed values or the empty list if the
	 * input array is {@code null} or is empty.
	 * @since 2016/04/11
	 */
	public static List<Integer> boxedList(int... __v)
	{
		if (__v == null || __v.length <= 0)
			return MissingCollections.<Integer>emptyList();
		return new __BoxedIntegerList__(__v);
	}
}

