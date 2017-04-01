// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * This package contains the base card system for implementing output for raw
 * machine code instructions. Basically the output of a method is contained
 * within a deck where individual instructions are represented as cards in the
 * deck. When the machine code is to be processed the cards will be iterated
 * and it will be determined which actual instructions are placed to the output
 * along with potential references to other cards in the deck. Some cards are
 * special in that they are only for jump targets while others will be used to
 * generate instructions. The card system does not implement any machine code
 * output, it only contains the code needed to manage it. Packages which
 * provide instructions do not extend cards (which are final), they are only
 * pointed to by the cards as a generated instruction type.
 *
 * @since 2017/04/01
 */

package net.multiphasicapps.squirreljme.jit.cards;

