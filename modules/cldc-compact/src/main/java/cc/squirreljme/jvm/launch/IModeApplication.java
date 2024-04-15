// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.constants.PhoneModelType;
import cc.squirreljme.jvm.suite.APIName;
import cc.squirreljme.jvm.suite.Configuration;
import cc.squirreljme.jvm.suite.DependencyInfo;
import cc.squirreljme.jvm.suite.EntryPoint;
import cc.squirreljme.jvm.suite.InvalidSuiteException;
import cc.squirreljme.jvm.suite.MarkedDependency;
import cc.squirreljme.jvm.suite.Profile;
import cc.squirreljme.jvm.suite.SuiteUtils;
import cc.squirreljme.runtime.cldc.SquirrelJME;
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
	/** The prefix for ADF properties. */
	public static final String ADF_PROPERTY_PREFIX =
		"cc.squirreljme.imode.adf";
	
	/** Property for the application name. */
	public static final String NAME_PROPERTY =
		"cc.squirreljme.imode.name";
	
	/** Property for the scratch pad sizes. */
	public static final String SCRATCH_PAD_PROPERTY =
		"cc.squirreljme.imode.scratchpads";
	
	/** Initial seed for the scratch pad. */
	public static final String SEED_SCRATCHPAD_PREFIX =
		"cc.squirreljme.imode.seedscratchpad";
	
	/** Property for the application vendor. */
	public static final String VENDOR_PROPERTY =
		"cc.squirreljme.imode.vendor";
	
	/** Boot class for DoJa. */
	private static final String _DOJA_BOOT_CLASS =
		"com.nttdocomo.ui.__AppLaunch__";
	
	/** Boot class for Star. */
	private static final String _STAR_BOOT_CLASS =
		"com.docomostar.__StarAppLaunch__";
	
	/** The application launch class. */
	static final String _APP_CLASS =
		"AppClass";
	
	/** Application icon. */
	static final String _APP_ICON =
		"AppIcon";
	
	/** The application name. */
	static final String _APP_NAME =
		"AppName";
	
	/** Application parameters. */
	static final String _APP_PARAMS =
		"AppParam";
	
	/** Application size of the Jar. */
	static final String _APP_SIZE =
		"AppSize";
	
	/** Application tracing enabled? */
	static final String _APP_TRACE =
		"AppTrace";
	
	/** Application type (Star). */
	static final String _APP_TYPE =
		"AppType";
	
	/** Application version. */
	static final String _APP_VERSION =
		"AppVer";
	
	/** The configuration to use. */
	static final String _CONFIGURATION_VER =
		"Configurationver";
	
	/** Draw area. */
	static final String _DRAW_AREA =
		"DrawArea";
	
	/** KVM Version, same as {@link #_CONFIGURATION_VER}. */
	static final String _KVM_VER =
		"KvmVer";
	
	/** Last modified time. */
	static final String _LAST_MODIFIED =
		"LastModified";
	
	/** Launch at given time. */
	static final String _LAUNCH_AT =
		"LaunchAt";
	
	/** Package URL. */
	static final String _PACKAGE_URL =
		"PackageURL";
	
	/** Profile version (DoJa 2.0+). */
	static final String _PROFILE_VER =
		"ProfileVer";
	
	/** Scratch pad sizes. */
	static final String _SP_SIZE =
		"SPsize";
	
	/** The Jar path. */
	protected final String jarPath;
	
	/** ADF Properties. */
	private final Map<String, String> _adfProps;
	
	/** Extra system properties. */
	private final Map<String, String> _extraSysProps;
	
	/**
	 * The application to load.
	 *
	 * @param __jar The JAR used.
	 * @param __libs The libraries to map.
	 * @param __adfProps Properties for the ADF/JAM.
	 * @param __jarPath The Jar path.
	 * @throws InvalidSuiteException If this suite is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/06/13
	 */
	IModeApplication(JarPackageBracket __jar, __Libraries__ __libs,
		Map<String, String> __adfProps, String __jarPath)
		throws InvalidSuiteException, NullPointerException
	{
		this(__jar, __libs, __adfProps, __jarPath, null);
	}
	
	/**
	 * The application to load.
	 *
	 * @param __jar The JAR used.
	 * @param __libs The libraries to map.
	 * @param __adfProps Properties for the ADF/JAM.
	 * @param __jarPath The Jar path.
	 * @param __sysProps Extra system properties.
	 * @throws InvalidSuiteException If this suite is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/04/13
	 */
	IModeApplication(JarPackageBracket __jar, __Libraries__ __libs,
		Map<String, String> __adfProps, String __jarPath, 
		Map<String, String> __sysProps)
		throws InvalidSuiteException, NullPointerException
	{
		super(__jar, __libs);
		
		this._adfProps = __adfProps;
		this._extraSysProps = __sysProps;
		this.jarPath = __jarPath;
		
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
		{
			// If this contains any non-ISO-8859-1 characters, then append the
			// Jar name
			boolean nonIso = false;
			for (int i = 0, n = appName.length(); i < n; i++)
				if (appName.charAt(i) > 0xFF)
				{
					nonIso = true;
					break;
				}
			
			// If the application name contains an invalid character then
			// it is an unsupported character we do not know about
			if (nonIso || appName.indexOf(0xFFFD) >= 0)
			{
				String jarPath = this.jarPath;
				if (jarPath != null)
					return appName + " (" + SuiteUtils.baseName(jarPath) + ")";
			}
			
			return appName;
		}
		
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
	 * Returns whether this is a Star application.
	 * 
	 * @return If this is a Star application.
	 * @since 2022/02/28
	 */
	public boolean isStarApplication()
	{
		// Check if any dependency implements the Star APIs
		APIName starApiName = new APIName("Star");
		for (MarkedDependency dependency : this.loaderDependencies())
			if (dependency instanceof Profile)
				if (starApiName.equals(((Profile)dependency).apiName()))
					return true;
		
		// Not one
		return false;
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
		String scratchPad = adfProps.get(IModeApplication._SP_SIZE);
		
		// Used as heuristic for versioning
		String drawArea = adfProps.get(IModeApplication._DRAW_AREA);
		
		// Try to guess a reasonable version to use
		if (config == null || config.isEmpty())
			config = "CLDC-1.1";
		if (profile == null || profile.isEmpty())
		{
			// The AppType property essentially specifies that this is a Star
			// application, otherwise it will be a DoJa application
			if (adfProps.get(IModeApplication._APP_TYPE) != null)
				profile = "Star-1.0";
			
			// Based on which properties exist, try to guess the specific
			// version of DoJa used...
			else if (scratchPad != null && scratchPad.indexOf(',') > 0)
				profile = "DoJa-3.0";
			else if (drawArea != null)
				profile = "DoJa-2.0";
			else
				profile = "DoJa-1.0";
		}
		
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
	 * @since 2021/06/13
	 */
	@Override
	public String loaderEntryClass()
	{
		// Always use the application helper
		if (this.isStarApplication())
			return IModeApplication._STAR_BOOT_CLASS;
		return IModeApplication._DOJA_BOOT_CLASS;
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
		
		// Any base system properties to be added
		Map<String, String> extraSysProps = this._extraSysProps;
		if (extraSysProps != null && !extraSysProps.isEmpty())
			rv.putAll(extraSysProps);
		
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
		
		// If a specific phone model is used, set the platform property
		// explicitly
		if (RuntimeShelf.phoneModel() == PhoneModelType.GENERIC)
			rv.put("microedition.platform",
				SquirrelJME.platform(PhoneModelType.NTT_DOCOMO_D503I));
		
		// Copy all ADF properties to system properties, it can be used in
		// the future to access specific properties accordingly
		for (Map.Entry<String, String> property : adfProps.entrySet())
			rv.put(IModeApplication.ADF_PROPERTY_PREFIX + "." +
				property.getKey(), property.getValue());
		
		return rv;
	}
}
