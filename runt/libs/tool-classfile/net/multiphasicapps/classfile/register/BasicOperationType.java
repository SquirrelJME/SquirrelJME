// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

/**
 * These represent basic operations which contain a description and a form
 * that will be easily transcoded to C.
 *
 * The following variables exist:
 * {@code sjmeclass_t* rp} - Reference queue stack, the first entry is always
 *     {@code -1} and represents the top of the stack.
 *
 * The following registers are used in a specific way:
 * {@code 0} - Zero register, this is always zero.
 *
 * @since 2019/04/06
 */
public interface BasicOperationType
{
}

