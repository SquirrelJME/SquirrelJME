// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ssjit;

/**
 * This is a function which is bound to a code producer which enables the
 * code generator to have a patchable set of native code gneration. This is the
 * base class for any provided functionality.
 *
 * If a producer is not given a function interface that implements a specific
 * set of functions, then it may provide virtual functions to handle these
 * situations.
 *
 * @since 2016/06/27
 */
public interface SSJITFunction
{
	/**
	 * Binds a given producer to this function so that if this function
	 * requires any other code generation or register management, it may
	 * perform such action.
	 *
	 * @param __p The producer to bind to this function.
	 * @throws IllegalStateException If a producer is already bound.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/27
	 */
	public abstract void bind(SSJITProducer __p)
		throws IllegalStateException, NullPointerException;
}

