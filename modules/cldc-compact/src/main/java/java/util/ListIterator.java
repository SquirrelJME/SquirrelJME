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
public interface ListIterator<E>
	extends Iterator<E>
{
	@Api
	void add(E __v);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	boolean hasNext();
	
	@Api
	boolean hasPrevious();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	E next();
	
	@Api
	int nextIndex();
	
	@Api
	E previous();
	
	@Api
	int previousIndex();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	void remove();
	
	@Api
	void set(E __v);
}

