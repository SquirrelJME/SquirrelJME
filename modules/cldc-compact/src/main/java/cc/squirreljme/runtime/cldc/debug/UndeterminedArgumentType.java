// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.debug;

/**
 * This is used when an argument is not truly known.
 *
 * @since 2022/02/27
 */
public final class UndeterminedArgumentType
	extends Error
{
	/**
	 * Cannot be initialized.
	 * 
	 * @since 2022/02/27
	 */
	private UndeterminedArgumentType()
	{
	}
}
