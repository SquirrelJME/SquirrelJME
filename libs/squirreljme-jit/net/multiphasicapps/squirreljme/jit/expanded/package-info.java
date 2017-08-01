// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * This package contains the expanded byte code which has the purpose of
 * representing a more simple operation of the byte code (think RISC instead
 * of CISC, where the JVM is CISC). The main purpose of this is to have it so
 * that translation engines do not need to implement support for the various
 * Java instructions over and over which can be error prone. The translators
 * will get the expanded and simplified instruction set before they generate
 * any machine code.
 *
 * @since 2017/08/01
 */

package net.multiphasicapps.squirreljme.jit.expanded;

