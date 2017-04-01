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
 * Each card has a face which may be set to cards dynamically, each face
 * should represent raw machine code representations. The faces are calculated
 * when the output is about to be generated.
 *
 * Implementations of this interface are not expected to be thread safe and it
 * is not required to be.
 *
 * @see Deck
 * @see Card
 * @since 2017/04/01
 */
public interface Face
{
}

