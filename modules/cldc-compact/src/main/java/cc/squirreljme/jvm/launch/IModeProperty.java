// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

/**
 * Properties for DoJa/Star Applications.
 *
 * @since 2024/07/28
 */
public interface IModeProperty
{
	/** The prefix for ADF properties. */
	String ADF_PROPERTY_PREFIX =
		"cc.squirreljme.imode.adf";
	
	/** Property for the application name. */
	String NAME_PROPERTY =
		IModeProperty._APP_NAME;
	
	/** Property for the scratch pad sizes. */
	String SCRATCH_PAD_PROPERTY =
		IModeProperty._SP_SIZE;
	
	/** Initial seed for the scratch pad. */
	String SEED_SCRATCHPAD_PREFIX =
		"cc.squirreljme.imode.seedscratchpad";
	
	/** Property for the application vendor. */
	String VENDOR_PROPERTY =
		"cc.squirreljme.imode.vendor";
	
	/** The application launch class. */
	String _APP_CLASS =
		"AppClass";
	
	/** Application icon. */
	String _APP_ICON =
		"AppIcon";
	
	/** The application name. */
	String _APP_NAME =
		"AppName";
	
	/** Application parameters. */
	String _APP_PARAMS =
		"AppParam";
	
	/** Application size of the Jar. */
	String _APP_SIZE =
		"AppSize";
	
	/** Application tracing enabled? */
	String _APP_TRACE =
		"AppTrace";
	
	/** Application type (Star). */
	String _APP_TYPE =
		"AppType";
	
	/** Application version. */
	String _APP_VERSION =
		"AppVer";
	
	/** The configuration to use. */
	String _CONFIGURATION_VER =
		"Configurationver";
	
	/** Boot class for DoJa. */
	String _DOJA_BOOT_CLASS =
		"com.nttdocomo.ui.__AppLaunch__";
	
	/** Draw area. */
	String _DRAW_AREA =
		"DrawArea";
	
	/** KVM Version, same as {@link #_CONFIGURATION_VER}. */
	String _KVM_VER =
		"KvmVer";
	
	/** Last modified time. */
	String _LAST_MODIFIED =
		"LastModified";
	
	/** Launch at given time. */
	String _LAUNCH_AT =
		"LaunchAt";
	
	/** Package URL. */
	String _PACKAGE_URL =
		"PackageURL";
	
	/** Profile version (DoJa 2.0+). */
	String _PROFILE_VER =
		"ProfileVer";
	
	/** Scratch pad sizes. */
	String _SP_SIZE =
		"SPsize";
	
	/** Boot class for Star. */
	String _STAR_BOOT_CLASS =
		"com.docomostar.__StarAppLaunch__";
}
