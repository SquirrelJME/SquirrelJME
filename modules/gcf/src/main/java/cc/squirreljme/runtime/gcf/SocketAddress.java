// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

/**
 * This interface represents a generic address type.
 *
 * @since 2019/05/06
 */
public interface SocketAddress
{
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	int hashCode();
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	String toString();
}

