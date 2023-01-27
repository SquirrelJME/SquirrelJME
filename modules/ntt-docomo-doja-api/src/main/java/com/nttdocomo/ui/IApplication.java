// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

public abstract class IApplication
{
	/** The last application created. */
	static volatile IApplication _lastApp;
	
	/** Application args, these are injected within. */
	static volatile String[] _appArgs;
	
	{
		synchronized (IApplication.class)
		{
			IApplication._lastApp = this;
		}
	}
	
	@Api
	public abstract void start();
	
	@Api
	public String[] getArgs()
	{
		synchronized (IApplication.class)
		{
			return IApplication._appArgs.clone();
		}
	}
	
	@Api
	public final String getSourceUrl()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void resume()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final void terminate()
	{
		// Do nothing here as the application handler will handle our exit
		// status accordingly.
	}
	
	@Api
	public static IApplication getCurrentApp()
	{
		synchronized (IApplication.class)
		{
			return IApplication._lastApp;
		}
	}
}
