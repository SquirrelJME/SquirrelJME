// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This interface represents anything that has accessible flags.
 *
 * @since 2018/09/09
 */
public interface HasAccessibleFlags
{
	/**
	 * Returns accessible flags.
	 *
	 * @return The accessible flags.
	 * @since 2018/09/09
	 */
	AccessibleFlags flags();
}

