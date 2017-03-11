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
 * This interface is used to associate classes (in most cases an enum) to a
 * specific register.
 *
 * Registers are opaque in that they do not have a specified size or data type
 * due to the potential for architectures which have variable register sizes.
 *
 * All implementations of this class must support {@link #equals(Object)} and
 * {@link #hashCode()}.
 *
 * @since 2017/03/10
 */
public interface Register
{
}

