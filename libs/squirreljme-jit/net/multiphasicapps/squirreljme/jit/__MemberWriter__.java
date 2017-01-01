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

import net.multiphasicapps.squirreljme.classformat.MemberDescriptionStream;

/**
 * This bridges the member description stream to member writing.
 *
 * @since 2016/09/10
 */
abstract class __MemberWriter__
	implements MemberDescriptionStream
{
	/** The namespace writer used. */
	protected final JITNamespaceWriter namespace;
	
	/** The owning class writer. */
	final __ClassWriter__ classwriter;
	
	/**
	 * Initializes the base member writer.
	 *
	 * @param __nsw The namespace writer.
	 * @param __cw The owning class writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	__MemberWriter__(JITNamespaceWriter __nsw, __ClassWriter__ __cw)
		throws NullPointerException
	{
		// Check
		if (__nsw == null || __cw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.namespace = __nsw;
		this.classwriter = __cw;
	}
}

