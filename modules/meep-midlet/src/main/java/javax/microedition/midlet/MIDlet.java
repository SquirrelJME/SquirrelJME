// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

import cc.squirreljme.jvm.manifest.JavaManifest;
import cc.squirreljme.jvm.manifest.JavaManifestKey;
import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import cc.squirreljme.runtime.midlet.CleanupHandler;
import cc.squirreljme.runtime.midlet.ManifestSource;
import cc.squirreljme.runtime.midlet.ManifestSourceType;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Api
public abstract class MIDlet
{
	/** This is the prefix used to override settings. */
	private static final String _APP_PROPERTY_OVERRIDE =
		"cc.squirreljme.runtime.midlet.override.";
	
	/** The source manifests that are available. */
	private final ManifestSource[] _manifestSources =
		new ManifestSource[ManifestSourceType.COUNT];
	
	/**
	 * Initialize the MIDlet.
	 *
	 * @since 2017/02/08
	 */
	@Api
	protected MIDlet()
	{
		// Set the active midlet to this one
		ActiveMidlet.set(this);
	}
	
	/**
	 * Signals that the MIDlet is in the destruction state.
	 *
	 * @param __uc Is this unconditional?
	 * @throws MIDletStateChangeException If the destruction might stop.
	 * @since 2020/02/29
	 */
	@Api
	protected abstract void destroyApp(boolean __uc)
		throws MIDletStateChangeException;
	
	/**
	 * This is the starting point for all MIDlets.
	 * 
	 * @throws MIDletStateChangeException If the MIDlet should not start.
	 * @since 2020/10/03
	 */
	@Api
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
	 * @since 2019/05/05
	 */
	@Api
	@ApiDefinedDeprecated
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
			
			/* {@squirreljme.error AD03 Cannot use check permission on
			MIDP 3.0 or MEEP suite profiles.} */
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
	@Api
	public final String getAppProperty(String __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Debug
		Debugging.debugNote("getAppProperty(%s)", __p);
		
		// Overridden property?
		JavaManifestKey key = new JavaManifestKey(__p);
		String val = System.getProperty(MIDlet._APP_PROPERTY_OVERRIDE + key);
		if (val != null)
			return val.trim();
		
		// Determine what our JAR is
		JarPackageBracket[] classPath =
			JarPackageShelf.classPath();
		JarPackageBracket ourJar = classPath[classPath.length - 1];
		
		// Go through each type and try to determine what we can do
		ManifestSource[] sources = this._manifestSources;
		for (ManifestSourceType type : ManifestSourceType.VALUES)
		{
			// Get pre-existing source, if it exists
			ManifestSource source = sources[type.ordinal()];
			if (source == null)
				sources[type.ordinal()] = (source = new ManifestSource());
			
			// Ignore manifests we know to be missing
			if (source.isMissing)
				continue;
			
			// Was a manifest already cached?
			JavaManifest manifest = source.manifest;
			if (manifest == null)
				try (InputStream in = type.manifestStream(ourJar))
				{
					/* {@squirreljme.error ZZ4j No manifest available for
					this current MIDlet of the given type. (The type)} */
					if (in == null)
						throw new IOException("ZZ4j " + type);
					
					// Try loading it
					manifest = new JavaManifest(
						new InputStreamReader(in, type.encoding()));
				}
				catch (IOException e)
				{
					// It failed, so maybe give a reason why?
					e.printStackTrace();
					
					source.isMissing = true;
				}
			
			// Try reading a property from the manifest
			if (manifest != null)
			{
				String rv = manifest.getMainAttributes()
					.getValue(new JavaManifestKey(__p));
				if (rv != null)
				{
					// Debugging
					Debugging.debugNote("getAppProperty(%s) @ %s -> %s",
						__p, type, rv);
					
					// Use it
					return rv;
				}
			}
		}
		
		// None found, so return nothing
		return null;
	}
	
	@Api
	public final MIDletIdentity getMIDletIdentity()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final long getSplashScreenTime()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final boolean isSelectedScreenSaver()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Used by the application to notify the MIDlet that it has entered the
	 * destroyed state and resources should be cleaned up and such. When this
	 * is called the program will be terminated.
	 *
	 * @since 2019/04/15
	 */
	@Api
	public final void notifyDestroyed()
	{
		Debugging.debugNote("Notification of destruction");
		
		// Run all cleanup handlers
		CleanupHandler.runAll();
		
		// Kill the program
		System.exit(0);
	}
	
	/**
	 * Notifies that the application should be paused now.
	 *
	 * This does nothing on SquirrelJME.
	 *
	 * @since 2017/02/08
	 */
	@Api
	@ApiDefinedDeprecated
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
	@Api
	@ApiDefinedDeprecated
	public void pauseApp()
	{
	}
	
	/**
	 * Requests that the system perform the given request.
	 * 
	 * @param __url The URL to request on the native system.
	 * @return This wil return {@code true} if the MIDlet must exit first
	 * before the request can be handled.
	 * @throws Exception Will throw
	 * {@code javax.microedition.io.ConnectionNotFoundException} if this
	 * connection type is not supported.
	 * @since 2020/07/02
	 */
	@Api
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
		Debugging.debugNote("Platform request: %s", __url);
		
		throw Debugging.todo();
	}
	
	/**
	 * This is called when an application was resumed.
	 * 
	 * On SquirrelJME this has no effect.
	 * 
	 * @since 2020/07/03
	 */
	@Api
	@ApiDefinedDeprecated
	public final void resumeRequest()
	{
	}
	
	@Api
	public static String getAppProperty(String __name, String __vend,
		String __attrname, String __attrdelim)
		throws NullPointerException
	{
		if (__attrname == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}

