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

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;

public abstract class MIDlet
{
	/** The cached manifest for obtaining properties. */
	private volatile Reference<JavaManifest> _manifest;
	
	/** Is there no manifest? */
	private volatile boolean _nomanifest;
	
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

