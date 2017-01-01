// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.classformat.FieldDescriptionStream;

/**
 * The bridges the field description stream to the JIT file writer.
 *
 * @since 2016/09/10
 */
class __FieldWriter__
	extends __MemberWriter__
	implements FieldDescriptionStream
{
	/**
	 * Initializes the field writer.
	 *
	 * @param __nsw The namespace writer.
	 * @param __cw The owning class writer.
	 * @since 2016/09/10
	 */
	__FieldWriter__(JITNamespaceWriter __nsw, __ClassWriter__ __cw)
	{
		super(__nsw, __cw);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public void constantValue(Object __v)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public void endMember()
	{
		throw new Error("TODO");
	}
}

