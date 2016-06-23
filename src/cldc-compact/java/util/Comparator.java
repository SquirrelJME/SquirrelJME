// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.util;

public interface Comparator<T>
{
	public abstract int compare(T __a, T __b);
	
	/**
	 * Generally this should return {@code true} if a given comparison results
	 * in a value of {@code 0}.
	 *
	 * {@inheritDoc}
	 * @since 2016/04/12
	 */
	@Override
	public abstract boolean equals(Object __a);
}

