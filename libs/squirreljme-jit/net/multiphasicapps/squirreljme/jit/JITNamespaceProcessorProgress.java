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

/**
 * This interface is used to indicate progress for namespace processing.
 *
 * @since 2016/07/23
 */
public interface JITNamespaceProcessorProgress
{
	/**
	 * Reports that a class is being procesed.
	 *
	 * @param __cl The class being processed.
	 * @since 2016/07/23
	 */
	public abstract void progressClass(String __cl);
	
	/**
	 * Reports that a namespace is being processed.
	 *
	 * @param __ns The namespace being processed.
	 * @since 2016/07/23
	 */
	public abstract void progressNamespace(String __ns);
	
	/**
	 * Reports that a resource is being processed.
	 *
	 * @param __rs The resource being processed.
	 * @since 2016/07/23
	 */
	public abstract void progressResource(String __rs);
}

