// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;

public abstract class MIDlet
{
	/** Lock to prevent multiple midlets from running. */
	private static final Object _ACTIVE_LOCK =
		new Object();
	
	/** Only a single midlet may run at a time. */
	private static volatile MIDlet _ACTIVE_MIDLET;
	
	/** The cached manifest for obtaining properties. */
	private volatile Reference<JavaManifest> _manifest;
	
	/** Is there no manifest? */
	private volatile boolean _nomanifest;
	
	/**
	 * Initialize the MIDlet.
	 *
	 * @since 2017/02/08
	 */
	protected MIDlet()
	{
		// Prevent multiple MIDlet launches
		synchronized (_ACTIVE_LOCK)
		{
			// {@squirreljme.error AD01 Only a single MIDlet may be active at
			// a time.}
			MIDlet active = _ACTIVE_MIDLET;
			if (active != null)
				throw new IllegalStateException("AD01");
			
			throw new Error("TODO");
		}
	}
	
	protected abstract void destroyApp(boolean __uc)
		throws MIDletStateChangeException;
	
	protected abstract void startApp()
		throws MIDletStateChangeException;
	
	@Deprecated
	public final int checkPermission(String __p)
		throws IllegalStateException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Obtains the value of a property for the current application. Properties
	 * are defined in the application descriptor along with the manifest file
	 * of the JAR.
	 *
	 * @param __p The property to obtain.
	 * @return The value of the given property or {@code null} if it is not
	 * defined.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/07
	 */
	public final String getAppProperty(String __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// If there is not manifest, ignore this step
		if (!this._nomanifest)
		{
			// Lookup JAR manifest
			Reference<JavaManifest> ref = this._manifest;
			JavaManifest manifest;
		
			// Cache it?
			if (ref == null || (null == (manifest = ref.get())))
			{
				// Some application properties are inside of the manifest so
				// check that
				InputStream is = this.getClass().getResourceAsStream(
					"META-INF/MANIFEST.MF");
				try
				{
					// Not found, force failure
					if (is == null)
						throw new IOException();
					
					// Load it
					manifest = new JavaManifest(is);
					
					// Store it
					this._manifest = new WeakReference<>(manifest);
				}
				
				// Does not exist or failed to read
				catch (IOException e)
				{
					this._nomanifest = true;
					manifest = null;
				}
			}
			
			// Try to get key value
			if (manifest != null)
			{
				String rv = manifest.getMainAttributes().get(__p);
				if (rv != null)
					return rv;
			}
		}
		
		// Key not found or no manifest
		return null;
	}
	
	public final void notifyDestroyed()
	{
		throw new Error("TODO");
	}
	
	/**
	 *
	 *
	 * @since 2017/02/08
	 */
	@Deprecated
	public final void notifyPaused()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Pauise
	 *
	 * @since 2017/02/08
	 */
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
	
	/**
	 * Creates an instance of the application MIDlet and then launches it.
	 *
	 * @since 2017/01/16
	 */
	private static void __launchMidlet()
	{
		throw new Error("TODO");
	}
}

