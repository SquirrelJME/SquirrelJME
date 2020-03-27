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
import java.io.IOException;
import java.io.InputStream;
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
	
	/**
	 * Signals that the MIDlet is in the destruction state.
	 *
	 * @param __uc Is is unconditional?
	 * @throws MIDletStateChangeException If the destruction might stop.
	 * @since 2020/02/29
	 */
	@SuppressWarnings("deprecation")
	protected abstract void destroyApp(boolean __uc)
		throws MIDletStateChangeException;
	
	/**
	 * Signals that the application is started and any start logic should be
	 * performed.
	 *
	 * @throws MIDletStateChangeException If the start might stop.
	 * @since 2020/03/26
	 */
	@SuppressWarnings("deprecation")
	protected abstract void startApp()
		throws MIDletStateChangeException;
	
	/**
	 * Checks if the given permission is valid.
	 *
	 * Do not use this to check permissions.
	 *
	 * @param __p The permission to check.
	 * @return {@code 0} if permission is denied, {@code 1} if permitted,
	 * and {@code -1} if unknown.
	 * @throws IllegalStateException If this is a MIDP 3.0 application.
	 * @deprecated Via standard library.
	 * @since 2019/05/05
	 */
	@Deprecated
	public final int checkPermission(String __p)
		throws IllegalStateException
	{
		// Ignore if null
		if (__p == null)
			return 1;
		
		// Not permitted on MIDP 3 or MEEP
		String profile = this.getAppProperty("microedition-profile");
		if (profile != null)
		{
			// Makes it easier to use
			profile = profile.toLowerCase();
			
			// {@squirreljme.error AD03 Cannot use check permission on
			// MIDP 3.0 or MEEP suite profiles.}
			if (profile.contains("midp-3") || profile.contains("meep"))
				throw new IllegalStateException("AD03");
		}
		
		// Do security check
		try
		{
			// If there is no security manager, just assume everything is
			// okay
			SecurityManager sm = System.getSecurityManager();
			if (sm == null)
				return 1;
			
			// Check it now
			sm.checkPermission(new RuntimePermission(__p));
			return 1;
		}
		catch (SecurityException e)
		{
			return 0;
		}
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
		String val = System.getProperty(MIDlet._APP_PROPERTY_OVERRIDE + key);
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
	
	/**
	 * Used by the application to notify the MIDlet that it has entered the
	 * destroyed state and resources should be cleaned up and such. When this
	 * is called the program will be terminated.
	 *
	 * @since 2019/04/15
	 */
	public final void notifyDestroyed()
	{
		todo.DEBUG.note("Notification of destruction");
		
		// Kill the program
		System.exit(0);
	}
	
	/**
	 * Notifies that the application should be paused now.
	 *
	 * This does nothing on SquirrelJME.
	 *
	 * @deprecated Via standard library.
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
	 * @deprecated Via standard library.
	 * @since 2017/02/08
	 */
	@Deprecated
	public void pauseApp()
	{
	}
	
	@SuppressWarnings("RedundantThrows")
	public final boolean platformRequest(String __url)
		throws Exception
	{
		// Games from Konami require this to return true even though that means
		// the application should terminate after this point. This is handled
		// and does not thrown an exception on bad requests.
		// Returning false here will cause the games to not work.
		if ("hjoja".equals(__url))
			return true;
		
		// Debug
		todo.DEBUG.note("%s", __url);
		
		throw new todo.TODO();
	}
	
	@Deprecated
	public final void resumeRequest()
	{
		throw new todo.TODO();
	}
	
	@SuppressWarnings("unused")
	public static String getAppProperty(String __name, String __vend,
		String __attrname, String __attrdelim)
		throws NullPointerException
	{
		if (__attrname == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

