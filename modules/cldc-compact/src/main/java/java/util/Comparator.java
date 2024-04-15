// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface Comparator<T>
{
	@Api
	int compare(T __a, T __b);
	
	/**
	 * Generally this should return {@code true} if a given comparison results
	 * in a value of {@code 0}.
	 *
	 * {@inheritDoc}
	 * @since 2016/04/12
	 */
	@Override
	boolean equals(Object __a);
}

