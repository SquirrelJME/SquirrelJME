// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

public abstract class MIDlet
{
	protected MIDlet()
	{
		throw new Error("TODO");
	}
	
	@Deprecated
	public final int checkPermission(String __p)
		throws IllegalStateException
	{
		throw new Error("TODO");
	}
	
	protected abstract void destroyApp(boolean __uc)
		throws MIDletStateChangeException;
	
	protected abstract void startApp()
		throws MIDletStateChangeException;
	
	public final String getAppProperty(String __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	public final void notifyDestroyed()
	{
		throw new Error("TODO");
	}
	
	@Deprecated
	public final void notifyPaused()
	{
		throw new Error("TODO");
	}
	
	@Deprecated
	public void pauseApp()
	{
		throw new Error("TODO");
	}
	
	public final boolean platformRequest(String __url)
		throws Exception
	{
		throw new Error("TODO");
	}
	
	@Deprecated
	public final void resumeRequest()
	{
		throw new Error("TODO");
	}
	
	public static String getAppProperty(String __name, String __vend,
		String __attrname, String __attrdelim)
		throws NullPointerException
	{
		if (__attrname == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

