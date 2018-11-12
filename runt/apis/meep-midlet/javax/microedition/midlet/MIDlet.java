// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

import cc.squirreljme.runtime.midlet.ActiveMidlet;
import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

public abstract class MIDlet
{
	/** This is the prefix used to override settings. */
	private static final String _APP_PROPERTY_OVERRIDE =
		"cc.squirreljme.runtime.midlet.override.";
	
	/** The cached manifest for obtaining properties. */
	private Reference<JavaManifest> _manifest;
	
	/** Is there no manifest? */
	private boolean _nomanifest;
	
	/**
	 * Initialize the MIDlet.
	 *
	 * @since 2017/02/08
	 */
	protected MIDlet()
	{
		// Set the active midlet to this one
		ActiveMidlet.set(this);
	}
	
	protected abstract void destroyApp(boolean __uc)
		throws MIDletStateChangeException;
	
	protected abstract void startApp()
		throws MIDletStateChangeException;
	
	@Deprecated
	public final int checkPermission(String __p)
		throws IllegalStateException
	{
		throw new todo.TODO();
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
		
		// Overridden property?
		JavaManifestKey key = new JavaManifestKey(__p);
		String val = System.getProperty(_APP_PROPERTY_OVERRIDE + key);
		if (val != null)
			return val.trim();
		
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
				String rv = manifest.getMainAttributes().get(
					new JavaManifestKey(__p));
				if (rv != null)
					return rv.trim();
			}
		}
		
		// Key not found or no manifest
		return null;
	}
	
	public final void notifyDestroyed()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Notifies that the application should be paused now.
	 *
	 * This does nothing on SquirrelJME.
	 *
	 * @since 2017/02/08
	 */
	@Deprecated
	public final void notifyPaused()
	{
	}
	
	/**
	 * This is code which should be called before the application is paused.
	 * 
	 * This does nothing on SquirrelJME.
	 *
	 * @since 2017/02/08
	 */
	@Deprecated
	public void pauseApp()
	{
	}
	
	public final boolean platformRequest(String __url)
		throws Exception
	{
		throw new todo.TODO();
	}
	
	@Deprecated
	public final void resumeRequest()
	{
		throw new todo.TODO();
	}
	
	public static String getAppProperty(String __name, String __vend,
		String __attrname, String __attrdelim)
		throws NullPointerException
	{
		if (__attrname == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

