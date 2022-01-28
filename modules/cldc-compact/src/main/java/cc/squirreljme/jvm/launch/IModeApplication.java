// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.suite.Configuration;
import cc.squirreljme.jvm.suite.DependencyInfo;
import cc.squirreljme.jvm.suite.EntryPoint;
import cc.squirreljme.jvm.suite.InvalidSuiteException;
import cc.squirreljme.jvm.suite.Profile;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Describes an i-mode/i-appli application and how to launch it.
 *
 * @since 2021/06/13
 */
public class IModeApplication
	extends Application
{
	/** Property for the scratch pad sizes. */
	public static final String SCRATCH_PAD_PROPERTY =
		"cc.squirreljme.imode.scratchpads";
	
	/** Property for the application name. */
	public static final String NAME_PROPERTY =
		"cc.squirreljme.imode.name";
	
	/** Property for the application vendor. */
	public static final String VENDOR_PROPERTY =
		"cc.squirreljme.imode.vendor";
	
	/** Boot class. */
	private static final String _BOOT_CLASS =
		"com.nttdocomo.ui.__AppLaunch__";
	
	/** The application name. */
	private static final String _APP_NAME =
		"AppName";
	
	/** The application launch class. */
	private static final String _APP_CLASS =
		"AppClass";
	
	/** Application parameters. */
	private static final String _APP_PARAMS =
		"AppParam";
	
	/** The configuration to use. */
	private static final String _CONFIGURATION_VER =
		"Configurationver";
	
	/** KVM Version, same as {@link #_CONFIGURATION_VER}. */
	private static final String _KVM_VER =
		"KvmVer";
	
	/** Profile version (DoJa 2.0+). */
	private static final String _PROFILE_VER =
		"ProfileVer";
	
	/** Application icon. */
	private static final String _APP_ICON =
		"AppIcon";
	
	/** Scratch pad sizes. */
	private static final String _SP_SIZE =
		"SPsize";
	
	/** ADF Properties. */
	private final Map<String, String> _adfProps;
	
	/**
	 * The application to load.
	 *
	 * @param __jar The JAR used.
	 * @param __libs The libraries to map.
	 * @param __adfProps Properties for the ADF/JAM.
	 * @throws InvalidSuiteException If this suite is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/06/13
	 */
	IModeApplication(JarPackageBracket __jar, __Libraries__ __libs,
		Map<String, String> __adfProps)
		throws InvalidSuiteException, NullPointerException
	{
		super(__jar, __libs);
		
		this._adfProps = __adfProps;
		
		if (!__adfProps.containsKey(IModeApplication._APP_NAME) ||
			!__adfProps.containsKey(IModeApplication._APP_CLASS))
			throw new InvalidSuiteException();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public String displayName()
	{
		String appName = this._adfProps.get(IModeApplication._APP_NAME);
		String appClass = this._adfProps.get(IModeApplication._APP_CLASS);
		
		if (appName != null)
			return appName;
		return (appClass != null ? appClass : "Invalid i-mode Application");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public EntryPoint entryPoint()
	{
		Map<String, String> adfProps = this._adfProps;
		return new EntryPoint(this.displayName(),
			adfProps.get(IModeApplication._APP_CLASS),
			adfProps.get(IModeApplication._APP_ICON),
			false); 
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public DependencyInfo loaderDependencies()
	{
		// This determines which library set to load
		Map<String, String> adfProps = this._adfProps;
		String config = Objects.toString(
			adfProps.get(IModeApplication._CONFIGURATION_VER),
			adfProps.get(IModeApplication._KVM_VER));
		String profile = adfProps.get(IModeApplication._PROFILE_VER);
		
		// Default to old stuff
		if (config == null || config.isEmpty())
			config = "CLDC-1.0";
		if (profile == null || profile.isEmpty())
			profile = "DoJa-1.0";
		
		return new DependencyInfo(new Configuration(config),
			new Profile(profile));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public String[] loaderEntryArgs()
	{
		EntryPoint entry = this.entryPoint();
		String args = this._adfProps.get(IModeApplication._APP_PARAMS);
		
		if (args == null)
			return new String[]{entry.entryPoint()};
		return new String[]{entry.entryPoint(), args};
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/01
	 */
	@Override
	public Map<String, String> loaderSystemProperties()
	{
		Map<String, String> adfProps = this._adfProps;
		Map<String, String> rv = new LinkedHashMap<>();
		
		// Application name and vendor, needed for RMS
		String nameProp = Objects.toString(
			adfProps.get(IModeApplication._APP_NAME),
			adfProps.get(IModeApplication._APP_CLASS));
		rv.put(IModeApplication.NAME_PROPERTY, nameProp);
		rv.put(IModeApplication.VENDOR_PROPERTY, "SquirrelJME-i-Mode");
		
		// Scratch pad sizes
		String spSize = adfProps.get(IModeApplication._SP_SIZE);
		if (spSize != null && !spSize.isEmpty())
			rv.put(IModeApplication.SCRATCH_PAD_PROPERTY, spSize);
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public String loaderEntryClass()
	{
		// Always use the application helper
		return IModeApplication._BOOT_CLASS;
	}
}
