// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Properties for DoJa/Star Applications.
 *
 * @since 2024/07/28
 */
@SquirrelJMEVendorApi
public interface IModeProperty
{
	/** The prefix for ADF properties. */
	@SquirrelJMEVendorApi
	String ADF_PROPERTY_PREFIX =
		"cc.squirreljme.imode.adf";
	
	/** The current DoJa profile. */
	@SquirrelJMEVendorApi
	String DOJA_PROFILE_PROPERTY = 
		"cc.squirreljme.imode.profile";
	
	/** Property for the application name. */
	@SquirrelJMEVendorApi
	String NAME_PROPERTY =
		IModeProperty._APP_NAME;
	
	/** Property for the scratch pad sizes. */
	@SquirrelJMEVendorApi
	String SCRATCH_PAD_PROPERTY =
		IModeProperty._SP_SIZE;
	
	/** Initial seed for the scratch pad. */
	@SquirrelJMEVendorApi
	String SEED_SCRATCHPAD_PREFIX =
		"cc.squirreljme.imode.seedscratchpad";
	
	/** Property for the application vendor. */
	@SquirrelJMEVendorApi
	String VENDOR_PROPERTY =
		"cc.squirreljme.imode.vendor";
	
	/** The application launch class. */
	@SquirrelJMEVendorApi
	String _APP_CLASS =
		"AppClass";
	
	/** Application icon. */
	@SquirrelJMEVendorApi
	String _APP_ICON =
		"AppIcon";
	
	/** The application name. */
	@SquirrelJMEVendorApi
	String _APP_NAME =
		"AppName";
	
	/** Application parameters. */
	@SquirrelJMEVendorApi
	String _APP_PARAMS =
		"AppParam";
	
	/** Application size of the Jar. */
	@SquirrelJMEVendorApi
	String _APP_SIZE =
		"AppSize";
	
	/** Application tracing enabled? */
	@SquirrelJMEVendorApi
	String _APP_TRACE =
		"AppTrace";
	
	/** Application type (Star). */
	@SquirrelJMEVendorApi
	String _APP_TYPE =
		"AppType";
	
	/** Application version. */
	@SquirrelJMEVendorApi
	String _APP_VERSION =
		"AppVer";
	
	/** The configuration to use. */
	@SquirrelJMEVendorApi
	String _CONFIGURATION_VER =
		"ConfigurationVer";
	
	/** Boot class for DoJa. */
	@SquirrelJMEVendorApi
	String _DOJA_BOOT_CLASS =
		"com.nttdocomo.ui.__AppLaunch__";
	
	/** Draw area. */
	@SquirrelJMEVendorApi
	String _DRAW_AREA =
		"DrawArea";
	
	/** KVM Version, same as {@link #_CONFIGURATION_VER}. */
	@SquirrelJMEVendorApi
	String _KVM_VER =
		"KvmVer";
	
	/** Last modified time. */
	@SquirrelJMEVendorApi
	String _LAST_MODIFIED =
		"LastModified";
	
	/** Application can launch other applications. */
	@SquirrelJMEVendorApi
	String _LAUNCH_APP = 
		"LaunchApp";
	
	/** Launch at given time. */
	@SquirrelJMEVendorApi
	String _LAUNCH_AT =
		"LaunchAt";
	
	/** Package URL. */
	@SquirrelJMEVendorApi
	String _PACKAGE_URL =
		"PackageURL";
	
	/** Profile version (DoJa 2.0+). */
	@SquirrelJMEVendorApi
	String _PROFILE_VER =
		"ProfileVer";
	
	/** Use browser (DoJa 2.0+). */
	@SquirrelJMEVendorApi
	String _USE_BROWSER =
		"UseBrowser";
	
	/** Scratch pad sizes. */
	@SquirrelJMEVendorApi
	String _SP_SIZE =
		"SPsize";
	
	/** The target device. */
	@SquirrelJMEVendorApi
	String _TARGET_DEVICE =
		"TargetDevice";
	
	/** Boot class for Star. */
	@SquirrelJMEVendorApi
	String _STAR_BOOT_CLASS =
		"com.docomostar.__StarAppLaunch__";
}
