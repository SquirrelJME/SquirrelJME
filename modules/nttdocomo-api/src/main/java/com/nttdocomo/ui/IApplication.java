// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public abstract class IApplication
{
	public abstract void start();
	
	public String[] getArgs()
	{
		throw Debugging.todo();
	}
	
	public final String getSourceUrl()
	{
		throw Debugging.todo();
	}
	
	public void resume()
	{
		throw Debugging.todo();
	}
	
	public final void terminate()
	{
		throw Debugging.todo();
	}
	
	public static IApplication getCurrentApp()
	{
		throw Debugging.todo();
	}
}
