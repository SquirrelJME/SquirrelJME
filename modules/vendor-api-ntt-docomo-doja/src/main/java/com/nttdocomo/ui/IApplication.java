// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.constants.TaskStatusType;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.midlet.DoJaRuntime;
import cc.squirreljme.runtime.nttdocomo.io.SquirrelJMEWebRootConnectionFactory;
import org.intellij.lang.annotations.Language;

@Api
public abstract class IApplication
{
	@Api
	public static final int	LAUNCH_AS_LAUNCHER =
		4;
	
	@Api
	public static final int	LAUNCH_BROWSER =
		1;
	
	@Api
	public static final int	LAUNCH_BROWSER_SUSPEND =
		13;
	
	@Api
	public static final int	LAUNCH_DTV =
		12;
	
	@Api
	public static final int	LAUNCH_IAPPLI =
		3;
	
	@Api
	public static final int	LAUNCH_MAIL_LAST_INCOMING =
		10;
	
	@Api
	public static final int	LAUNCH_MAIL_RECEIVED =
		7;
	
	@Api
	public static final int	LAUNCH_MAIL_SENT =
		8;
	
	@Api
	public static final int	LAUNCH_MAIL_UNSENT =
		9;
	
	@Api
	public static final int	LAUNCH_MAILMENU =
		5;
	
	@Api
	public static final int	LAUNCH_SCHEDULER =
		6;
	
	@Api
	public static final int	LAUNCH_VERSIONUP =
		2;
	
	@Api
	public static final int	LAUNCHED_AFTER_DOWNLOAD =
		1;
	
	@Api
	public static final int	LAUNCHED_AS_CONCIERGE =
		3;
	
	@Api
	public static final int	LAUNCHED_AS_ILET =
		9;
	
	@Api
	public static final int	LAUNCHED_FROM_BML =
		21;
	
	@Api
	public static final int	LAUNCHED_FROM_BROWSER =
		5;
	
	@Api
	public static final int	LAUNCHED_FROM_DTV =
		17;
	
	@Api
	public static final int	LAUNCHED_FROM_EXT =
		4;
	
	@Api
	public static final int	LAUNCHED_FROM_FELICA_ADHOC =
		19;
	
	@Api
	public static final int	LAUNCHED_FROM_IAPPLI =
		7;
	
	@Api
	public static final int	LAUNCHED_FROM_LAUNCHER =
		8;
	
	@Api
	public static final int	LAUNCHED_FROM_LOCATION_IMAGE =
		14;
	
	@Api
	public static final int	LAUNCHED_FROM_LOCATION_INFO =
		13;
	
	@Api
	public static final int	LAUNCHED_FROM_MAILER =
		6;
	
	@Api
	public static final int	LAUNCHED_FROM_MENU =
		0;
	
	@Api
	public static final int	LAUNCHED_FROM_MENU_FOR_DELETION =
		20;
	
	@Api
	public static final int	LAUNCHED_FROM_PHONEBOOK =
		15;
	
	@Api
	public static final int	LAUNCHED_FROM_TIMER =
		2;
	
	@Api
	public static final int	LAUNCHED_FROM_TORUCA =
		18;
	
	@Api
	public static final int	LAUNCHED_MSG_RECEIVED =
		10;
	
	@Api
	public static final int	LAUNCHED_MSG_SENT =
		11;
	
	@Api
	public static final int	LAUNCHED_MSG_UNSENT =
		12;
	
	@Api
	public static final int	SUSPEND_BY_IAPP =
		2;
	
	@Api
	public static final int	SUSPEND_BY_NATIVE =
		1;
	
	@Api
	public static final int	SUSPEND_CALL_IN =
		1024;
	
	@Api
	public static final int	SUSPEND_CALL_OUT =
		512;
	
	@Api
	public static final int	SUSPEND_MAIL_RECEIVE =
		4096;
	
	@Api
	public static final int	SUSPEND_MAIL_SEND =
		2048;
	
	@Api
	public static final int	SUSPEND_MESSAGE_RECEIVE =
		8192;
	
	@Api
	public static final int	SUSPEND_MULTITASK_APPLICATION =
		32768;
	
	@Api
	public static final int	SUSPEND_PACKETIN =
		256;
	
	@Api
	public static final int	SUSPEND_SCHEDULE_NOTIFY =
		16384;

	/** The last application created. */
	@SquirrelJMEVendorApi
	static volatile IApplication _lastApp;
	
	/** Application args, these are injected within. */
	@SquirrelJMEVendorApi
	static volatile String[] _appArgs;
	
	{
		synchronized (IApplication.class)
		{
			IApplication._lastApp = this;
		}
	}
	
	@Api
	public abstract void start();
	
	@Api
	public String[] getArgs()
	{
		synchronized (IApplication.class)
		{
			return IApplication._appArgs.clone();
		}
	}
	
	/**
	 * Returns the means by which this application was launched.
	 *
	 * @return The launch type of this application.
	 * @since 2025/06/15
	 */
	@Api
	public final int getLaunchType()
	{
		// Was the launch type changed?
		String value = DoJaRuntime.getProperty(DoJaRuntime.LAUNCH_TYPE);
		if (value != null)
			try
			{
				return Integer.parseInt(value, 10);
			}
			catch (NumberFormatException ignored)
			{
			}
		
		// Defaults to normal launch from a "menu" otherwise
		return IApplication.LAUNCHED_FROM_MENU;
	}
	
	/**
	 * Returns the value of a parameter that was used to launch the
	 * application either from a web browser or another application.
	 * 
	 * @param __name The parameter name.
	 * @return The value of the parameter or {@code null}.
	 * @since 2022/10/07
	 */
	@Api
	public String getParameter(String __name)
	{
		Debugging.todoNote("getParameter(%s)", __name);
		
		return null;
	}
	
	/**
	 * Returns the location of where the application was downloaded from, this
	 * may be used to access additional resources.
	 * 
	 * If the application was downloaded from
	 * {@code https://squirreljme.cc/apps/example.jar} the return value will
	 * be {@code https://squirreljme.cc/apps/}.
	 * 
	 * @return The URL where the application was downloaded.
	 * @since 2022/10/07
	 */
	@Api
	@Language("http-url-reference")
	@SuppressWarnings("MethodNamesDifferingOnlyByCase")
	public final String getSourceUrl()
	{
		// Our webroot is always non-networked, so we handle and potentially
		// proxy all the various HTTP calls accordingly.
		return SquirrelJMEWebRootConnectionFactory.URI_SCHEME +
			"://";
	}
	
	/**
	 * Alias for {@link #getSourceUrl()}.
	 *
	 * @return The value of {@link #getSourceUrl()}.
	 * @since 2023/07/21
	 */
	@Api
	@Language("http-url-reference")
	@SuppressWarnings("MethodNamesDifferingOnlyByCase")
	public final String getSourceURL()
	{
		return this.getSourceUrl();
	}
	
	/**
	 * Returns the reason which resulted in this application being suspended.
	 *
	 * @return The reason for suspension.
	 * @since 2025/06/15
	 */
	@Api
	public int getSuspendInfo()
	{
		// Suspension is not supported before DoJa 3
		if (DoJaRuntime.versionBefore(3, 0))
			return 0;
		
		// We detected ourselves as not being foregrounded
		boolean detected = TaskShelf.status(
			TaskShelf.current()) != TaskStatusType.ALIVE;
		
		// If we detected suspension, this will always return that
		if (detected)
			return IApplication.SUSPEND_BY_NATIVE;
		return 0;
	}
	
	@Api
	public void launch(int __target, String[] __args)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void resume()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final void terminate()
	{
		// Do nothing here as the application handler will handle our exit
		// status accordingly.
	}
	
	@Api
	public static IApplication getCurrentApp()
	{
		synchronized (IApplication.class)
		{
			return IApplication._lastApp;
		}
	}
}
