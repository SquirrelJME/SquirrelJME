// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui.forms;

/**
 * Used to indicate a specific exception in form tests.
 *
 * @since 2020/07/19
 */
public final class FormTestException
	extends RuntimeException
{
	/**
	 * Initializes the exception.
	 * 
	 * @param __c The cause.
	 * @since 2020/07/19
	 */
	public FormTestException(Throwable __c)
	{
		super(__c);
	}
}
