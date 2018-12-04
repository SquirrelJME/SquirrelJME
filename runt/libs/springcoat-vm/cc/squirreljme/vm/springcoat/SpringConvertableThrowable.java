// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
	public abstract String getMessage();
	
	/**
	 * Returns the class this throwable converts to.
	 *
	 * @return The target class this converts to.
	 * @since 2018/12/04
	 */
	public abstract String targetClass();
}

