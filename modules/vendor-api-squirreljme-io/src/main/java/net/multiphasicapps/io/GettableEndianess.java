// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

/**
 * This interface is used for classes which can get their endianess obtained.
 *
 * @since 2016/08/11
 */
public interface GettableEndianess
{
	/**
	 * Obtains the current default endianess of the data.
	 *
	 * @return The current endianess.
	 * @since 2016/07/10
	 */
	DataEndianess getEndianess();
}

