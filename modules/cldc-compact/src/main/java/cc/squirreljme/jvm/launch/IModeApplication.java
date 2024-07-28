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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
		
		if (!__adfProps.containsKey(IModeProperty._APP_NAME) ||
			!__adfProps.containsKey(IModeProperty._APP_CLASS))
			throw new InvalidSuiteException();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/06/13
	 */
	@Override
	public String displayName()
	{
		String appName = this._adfProps.get(IModeProperty._APP_NAME);
		String appClass = this._adfProps.get(IModeProperty._APP_CLASS);
		
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
			adfProps.get(IModeProperty._APP_CLASS),
			adfProps.get(IModeProperty._APP_ICON),
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
			adfProps.get(IModeProperty._CONFIGURATION_VER),
			adfProps.get(IModeProperty._KVM_VER));
		String profile = adfProps.get(IModeProperty._PROFILE_VER);
		String scratchPad = adfProps.get(IModeProperty._SP_SIZE);
		
		// Used as heuristic for versioning
		String drawArea = adfProps.get(IModeProperty._DRAW_AREA);
		
		// Try to guess a reasonable version to use
		if (config == null || config.isEmpty())
			config = "CLDC-1.1";
		if (profile == null || profile.isEmpty())
		{
			// The AppType property essentially specifies that this is a Star
			// application, otherwise it will be a DoJa application
			if (adfProps.get(IModeProperty._APP_TYPE) != null)
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
		String appArgs = this._adfProps.get(IModeProperty._APP_PARAMS);
		
		// Set initial base property set
		List<String> args = new ArrayList<>();
		for (Map.Entry<String, String> e : this.__properties().entrySet())
			args.add(String.format("-Xadf:%s=%s",
				e.getKey(), e.getValue()));
		
		// Application entry point and arguments as its main
		args.add(entry.entryPoint());
		if (appArgs != null)
			args.add(appArgs);
		
		// Give it
		return args.toArray(new String[args.size()]);
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
			return IModeProperty._STAR_BOOT_CLASS;
		return IModeProperty._DOJA_BOOT_CLASS;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/01
	 */
	@Override
	public Map<String, String> loaderSystemProperties()
	{
		return this.__properties();
	}
	
	private Map<String, String> __properties()
	{
		Map<String, String> adfProps = this._adfProps;
		Map<String, String> rv = new LinkedHashMap<>();
		
		// Any base system properties to be added
		Map<String, String> extraSysProps = this._extraSysProps;
		if (extraSysProps != null && !extraSysProps.isEmpty())
			rv.putAll(extraSysProps);
		
		// Application name and vendor, needed for RMS
		String nameProp = Objects.toString(
			adfProps.get(IModeProperty._APP_NAME),
			adfProps.get(IModeProperty._APP_CLASS));
		rv.put(IModeProperty.NAME_PROPERTY, nameProp);
		rv.put(IModeProperty.VENDOR_PROPERTY, "SquirrelJME-i-Mode");
		
		// Encoding and locale override
		rv.put(Application.OVERRIDE_ENCODING, "shift-jis");
		rv.put(Application.OVERRIDE_LOCALE, "ja-JP");
		
		// Scratch pad sizes
		String spSize = adfProps.get(IModeProperty._SP_SIZE);
		if (spSize != null && !spSize.isEmpty())
			rv.put(IModeProperty.SCRATCH_PAD_PROPERTY, spSize);
		
		// If a specific phone model is used, set the platform property
		// explicitly
		if (RuntimeShelf.phoneModel() == PhoneModelType.GENERIC)
			rv.put("microedition.platform",
				SquirrelJME.platform(PhoneModelType.NTT_DOCOMO_D503I));
		
		// Copy all ADF properties to system properties, it can be used in
		// the future to access specific properties accordingly
		for (Map.Entry<String, String> property : adfProps.entrySet())
			rv.put(IModeProperty.ADF_PROPERTY_PREFIX + "." +
				property.getKey(), property.getValue());
		
		return rv;
	}
}
