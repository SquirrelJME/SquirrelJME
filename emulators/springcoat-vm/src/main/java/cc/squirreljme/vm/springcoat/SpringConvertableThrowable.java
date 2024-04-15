// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * This interface describes any throwable which may be converted.
 *
 * @since 2018/12/04
 */
public interface SpringConvertableThrowable
{
	/**
	 * Returns the message.
	 *
	 * @return The message.
	 * @since 2018/12/04
	 */
	String getMessage();
	
	/**
	 * Returns the class this throwable converts to.
	 *
	 * @return The target class this converts to.
	 * @since 2018/12/04
	 */
	String targetClass();
}

