// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.pipe;

/**
 * This is an expanded pipe which is given expanded byte code.
 *
 * It is {@link AutoCloseable} so that the compiler can instantly tell the
 * pipeline when it has been closed so that if delayed instruction generation
 * is required it can output such instructions at the final stage.
 *
 * @since 2017/08/13
 */
public interface ExpandedPipe
	extends AutoCloseable
{
}

