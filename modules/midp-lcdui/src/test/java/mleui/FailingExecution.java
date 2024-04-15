// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui;

/**
 * Indicates that the display test is failing.
 *
 * @since 2020/10/10
 */
public class FailingExecution
	extends RuntimeException
{
	/**
	 * Initializes the exception.
	 * 
	 * @param __s The message.
	 * @since 2020/10/10
	 */
	public FailingExecution(String __s)
	{
		super(__s);
	}
}
