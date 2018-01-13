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
 * This package contains the basic virtual machine which is used as a base to
 * implement other virtual machines. Since the verifier, the interpreter, and
 * the translator from the byte code to more optimized code will read the same
 * set of instructions and handle them as such.
 *
 * @since 2017/10/15
 */

package cc.squirreljme.jit.program;

