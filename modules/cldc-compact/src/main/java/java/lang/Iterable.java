// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.util.Iterator;

/**
 * This represents a class which can be iterated through giving one or more
 * values.
 *
 * @param <T> The type this returns.
 * @since 2018/12/08
 */
@Api
public interface Iterable<T>
{
	/**
	 * Returns the iterator over the object.
	 *
	 * @return The object iterator.
	 * @since 2018/12/08
	 */
	@Api
	Iterator<T> iterator();
}

