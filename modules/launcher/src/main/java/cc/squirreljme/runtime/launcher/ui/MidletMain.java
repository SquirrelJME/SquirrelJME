// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.swm.EntryPoint;
import cc.squirreljme.runtime.swm.EntryPoints;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import net.multiphasicapps.tool.manifest.JavaManifest;

/**
 * This is the main midlet for the LCDUI based launcher interface.
 *
 * @since 2016/10/11
 */
public class MidletMain
	extends MIDlet
{
	/**
	 * {@squirreljme.property cc.squirreljme.autolaunch=program This specifies
	 * the program that should be auto-launched once the program list has been
	 * processed.}
	 */
	public static final String AUTOLAUNCH_PROPERTY =
		"cc.squirreljme.autolaunch";
	
	/** Command used to exit the launcher and terminate. */
	public static final Command EXIT_COMMAND =
		new Command("Exit", Command.EXIT, 1);
	
	/** The about command. */
	public static final Command ABOUT_COMMAND =
		new Command("About", Command.HELP, 2);
	
	/** Timer used to reschedule things. */
	static final Timer _TIMER =
		new Timer("LauncherRecoverThread");
	
	/** The display that is being used. */
	static volatile Display _MAIN_DISPLAY;
	
	/** The list which contains all of the programs we can run. */
	protected final List programList =
		new List("SquirrelJME Launcher", Choice.IMPLICIT);
	
	/** The active task. */
	private final __ActiveTask__ _activeTask =
		new __ActiveTask__();
	
	/** The current end-time for the splash screen. */
	private volatile long _endTime;
	
	/** The programs which are mapped to the list. */
	private volatile __Program__[] _programs;
	
	/** Automatic launch program. */
	private volatile String _autoLaunch;
	
	{
		// Do not crash if we cannot read properties
		String al = null;
		try
		{
			al = System.getProperty(MidletMain.AUTOLAUNCH_PROPERTY);
		}
		catch (SecurityException e)
		{
		}
		
		this._autoLaunch = al;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/11
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
		// This is not used at all
	}
	
	/**
	 * Refreshes the list.
	 *
	 * @since 2018/11/16
	 */
	public void refresh()
	{
		// When a refresh is happening, change the title so that is
		// indicated
		List programList = this.programList;
		programList.setTitle("Loading (Querying Suites)...");
		
		// Used for checking and such
		__ActiveTask__ activeTask = this._activeTask;
		
		// The list was previously not used
		if (programList.size() <= 0)
		{
			// Add a message saying what is going on
			programList.append("Loading available programs...", null);
			programList.append("This might take awhile!", null);
			programList.append("UP/DOWN -- Adjust focused item", null);
			programList.append("FIRE    -- Select item", null);
			programList.append("The SPACEBAR key is the FIRE key", null);
			
			// Re-flip on this display
			if (MidletMain._MAIN_DISPLAY.getCurrent() == programList)
				MidletMain._MAIN_DISPLAY.setCurrent(programList);
		}
		
		// Go through all of the available application suites and build the
		// program list
		boolean queried = false;
		int foundcount = 0;
		
		// Locate programs via the library path
		ArrayList<__Program__> programs = new ArrayList<>();
		JarPackageBracket[] jars = JarPackageShelf.libraries();
		for (JarPackageBracket jar : jars)
		{
			// Debug
			Debugging.debugNote("Checking %s...",
				JarPackageShelf.libraryPath(jar));
			
			// Try to read the manifest from the given JAR
			JavaManifest man;
			try (InputStream rc = JarPackageShelf.openResource(jar,
				"META-INF/MANIFEST.MF"))
			{
				// If no manifest exists, might not be a JAR
				if (rc == null)
					continue;
				
				man = new JavaManifest(rc);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				
				continue;
			}
			
			// Hide this on the SquirrelJME launcher?
			if (Boolean.parseBoolean(man.getMainAttributes().getValue(
				"X-SquirrelJME-NoLauncher")))
				continue;
			
			// Query is done, scan suites
			if (!queried)
			{
				programList.setTitle("Loading (Scanning Suites)...");
				queried = true;
			}
			
			// Handle each entry point and create an entry in the list for it
			for (EntryPoint entry : new EntryPoints(man))
			{
				// Build program
				programs.add(new __Program__(jar, man, activeTask, entry));
				
				// Say it was found via the title
				programList.setTitle(String.format(
					"Loading (%d Found)...", ++foundcount));
			}
		}
		
		// Indicate that the program list is being built
		programList.setTitle(String.format(
			"Building List (%d Found)...", foundcount));
		
		// Build program array
		__Program__[] arrprogs = programs.<__Program__>toArray(
			new __Program__[programs.size()]);
		
		// Clear the program list
		programList.deleteAll();
		
		// Build the list in the program order
		for (__Program__ p : arrprogs)
			programList.append(p.displayName(), p.displayImage());
		
		// Use this list
		this._programs = arrprogs;
		
		// All done so, return the title back
		programList.setTitle("SquirrelJME Launcher");
		
		// If the program list started too quickly then wait until the splash
		// time has expired so it is always shown
		long endTime = this._endTime;
		for (long nowTime = System.nanoTime(); nowTime < endTime;
			nowTime = System.nanoTime())
			try
			{
				Debugging.debugNote("Stalling...");
				Thread.sleep((endTime - nowTime) / 1_000_000L);
			}
			catch (InterruptedException ignored)
			{
			}
		
		// Make sure the program list is showing
		Displayable current = MidletMain._MAIN_DISPLAY.getCurrent();
		if (current == null || (current instanceof SplashScreen))
			MidletMain._MAIN_DISPLAY.setCurrent(programList);
		
		// Automatically launch a program?
		String autoLaunch = this._autoLaunch;
		if (autoLaunch != null)
		{
			this._autoLaunch = null;
			
			// Launch it
			System.err.println("Auto-launching " + autoLaunch + "...");
			this.__launch(autoLaunch);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/16
	 */
	@Override
	protected void startApp()
	{
		// We will need to access our own display to build the list of
		// MIDlets that could actually be ran
		Display disp = Display.getDisplay(this);
		MidletMain._MAIN_DISPLAY = disp;
		
		// Add commands to the list so things can be done with them
		List programList = this.programList;
		programList.addCommand(MidletMain.EXIT_COMMAND);
		programList.addCommand(MidletMain.ABOUT_COMMAND);
		
		// Need to handle commands and such
		__CommandHandler__ ch = new __CommandHandler__();
		programList.setCommandListener(ch);
		
		// Used to ensure the splash screen is visible for at least a second
		this._endTime = System.nanoTime() + 1_000_000_000L;
		
		// Refresh the list in another thread
		Thread refresher = new __Refresher__(this);
		refresher.start();
		
		// Instead of showing the program list early, just show a splash screen
		// with a handsome Lex and the version information
		SplashScreen spl = new SplashScreen(disp.getWidth(), disp.getHeight());
		if (disp.getCurrent() == null)
			disp.setCurrent(spl);
	}
	
	/**
	 * Launches the specified program.
	 *
	 * @param __p The program to launch.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	private void __launch(__Program__ __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Indication that something is happening
		this.programList.setTitle("Launching " + __p.displayName + "...");
		
		// Launch this program
		__p.__launch();
		
		// All done so, return the title back
		this.programList.setTitle("SquirrelJME Launcher");
	}
	
	/**
	 * Launches the specified program.
	 *
	 * @param __p The program to launch.
	 * @since 2019/04/14
	 */
	private void __launch(int __p)
	{
		__Program__[] programs = this._programs;
		
		// Do nothing if out of bounds
		if (programs == null || __p < 0 || __p >= programs.length)
			return;
		
		// Launch
		this.__launch(programs[__p]);
	}
	
	/**
	 * Launches the specified program.
	 *
	 * @param __p The program to launch.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	private void __launch(String __p)
		throws NullPointerException
	{
		throw Debugging.todo();
		/*
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// This will use multiple matches, with less priority following
		__Program__ bysj = null,
			bydn = null,
			bysn = null,
			bymc = null;
		
		// Find all the possible matches for a program
		for (__Program__ p : this._programs)
			if (bysj == null && __p.equalsIgnoreCase(p.squirreljmeName))
				bysj = p;
			else if (bydn == null && __p.equalsIgnoreCase(p.displayName))
				bydn = p;
			else if (bysn == null && __p.equalsIgnoreCase(p.suiteName))
				bysn = p;
			else if (bymc == null && __p.equalsIgnoreCase(p.main))
				bymc = p;
		
		// Use a priority based order
		__Program__ p = (bysj != null ? bysj :
			(bydn != null ? bydn :
			(bysn != null ? bysn :
			(bymc != null ? bymc : null))));
		if (p != null)
		{
			this.__launch(p);
			return;
		}
		
		// If everything fails, just assume it is an index to a program on the
		// program list
		try
		{
			this.__launch(Integer.parseInt(__p));
		}
		catch (NumberFormatException e)
		{
		}
		
		 */
	}
	
	/**
	 * This is the handler for commands.
	 *
	 * @since 2018/11/16
	 */
	private final class __CommandHandler__
		implements CommandListener
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/11/16
		 */
		@Override
		public final void commandAction(Command __c, Displayable __d)
		{
			// Launching a program?
			if (__c == List.SELECT_COMMAND)
			{
				// If the list is empty then do nothing because it will NPE
				// or out of bounds if the selection is off
				List list = (List)__d;
				int seldx = list.getSelectedIndex();
				if (list.size() <= 0 || seldx < 0)
					return;
				
				// Call other launcher
				MidletMain.this.__launch(seldx);
			}
			
			// Exiting the VM?
			else if (__c == MidletMain.EXIT_COMMAND)
			{
				// Indication that something is happening
				MidletMain.this.programList.setTitle("Exiting...");
				
				System.exit(0);
			}
			
			// About SquirrelJME
			else if (__c == MidletMain.ABOUT_COMMAND)
			{
			}
		}
	}
}

