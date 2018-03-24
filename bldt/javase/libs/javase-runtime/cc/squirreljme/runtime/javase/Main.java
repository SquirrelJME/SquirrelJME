// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase;

import cc.squirreljme.kernel.Kernel;
import cc.squirreljme.kernel.PrimitiveKernel;
import cc.squirreljme.kernel.suiteinfo.EntryPoint;
import cc.squirreljme.kernel.suiteinfo.EntryPoints;
import cc.squirreljme.runtime.cldc.io.StandardOutput;
import cc.squirreljme.runtime.cldc.system.api.Call;
import cc.squirreljme.runtime.cldc.system.EasyCall;
import cc.squirreljme.runtime.cldc.system.SystemCall;
import cc.squirreljme.runtime.cldc.system.SystemCallDispatch;
import cc.squirreljme.runtime.cldc.system.SystemFunction;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.Objects;
import javax.microedition.midlet.MIDlet;
import net.multiphasicapps.tool.manifest.JavaManifest;

/**
 * This initializes the SquirrelJME CLDC run-time interfaces and provides a
 * bridge so that the run-time functions properly on non-SquirrelJME Java VMs.
 *
 * @since 2017/12/07
 */
public class Main
{
	/** Property which specifies the client main entry point. */
	public static final String CLIENT_MAIN =
		"cc.squirreljme.runtime.javase.clientmain";
	
	/** Property specifying the main entry point for server entries. */
	public static final String SERVER_MAIN =
		"cc.squirreljme.runtime.javase.servermain";
	
	/**
	 * {@squirreljme.property cc.squirreljme.runtime.javase.program=(id)
	 * The program to run when launching.}
	 */
	public static final String PROGRAM =
		"cc.squirreljme.runtime.javase.program";
	
	/**
	 * Wrapped main entry point.
	 *
	 * @param __args Program arguments, these are forwarded.
	 * @throws Throwable On any kind of throwable.
	 * @since 2017/12/07
	 */
	public static void main(String... __args)
		throws Throwable
	{
		// These are launch parameters which are used by the actual Java SE
		// wrappers to spawn new tasks
		String servermain = System.getProperty(SERVER_MAIN),
			clientmain = System.getProperty(CLIENT_MAIN);
		boolean isclient = (servermain != null ? false : clientmain != null);
		
		// Initialize the run-time which sets up the SquirrelJME specific
		// APIs
		__initializeRunTime(isclient);
		
		// Search through programs to find the entry point desired
		String mainclassname;
		int pdx = Integer.getInteger(Main.PROGRAM, -1);
		if (pdx >= 0)
		{
			// Need to go through all the URLs and find the midlet containing
			// entry points to be parsed. However since there is no way to
			// know which manifest in the classpath refers to the midlet we
			// kinda just have to guess
			List<URL> urls = Collections.<URL>list(Main.class.getClassLoader().
				getResources("META-INF/MANIFEST.MF"));
			EntryPoints entries = null;
			for (int i = 0, n = urls.size(); i < n; i++)
			{
				// Read in the manifest
				JavaManifest man;
				try (InputStream in = urls.get(i).openStream())
				{
					man = new JavaManifest(in);
				}
				
				// If this refers to a midlet then use it
				if (man.getMainAttributes().definesValue("midlet-name"))
				{
					entries = new EntryPoints(man);
					break;
				}
			}
			
			// {@squirreljme.error AF08 No entry points were found.}
			if (entries == null)
				throw new IllegalArgumentException("AF08");
			
			// Print them out for debug
			System.err.println("Entry points:");
			int n = entries.size();
			for (int i = 0; i < n; i++)
				System.err.printf("    %d: %s%n", i, entries.get(i));
			
			// If the entry is out of bounds just use the first one
			if (pdx < 0 || pdx >= n)
				pdx = 0;
			
			// Use that entry point
			mainclassname = entries.get(pdx).entryPoint();
		}
		
		// Using the old standard method of launching
		else
		{
			// The client just uses the specified main class
			if (isclient)
				mainclassname = clientmain;
			
			// Determines the class name via manifest
			else
				if (servermain != null)
					mainclassname = servermain;
				else
					mainclassname = __mainClassByManifest();
		}
		
		// Exceptions generated as of the result of the method call are
		// wrapped so they must be unwrapped
		try
		{
			Class<?> mainclass = Class.forName(mainclassname);
			
			// Try initializing with a main method
			try
			{
				// {@squirreljme.error AF02 The main method is not static.}
				Method mainmethod = mainclass.getMethod("main",
					String[].class);
				if ((mainmethod.getModifiers() & Modifier.STATIC) == 0)
					throw new RuntimeException("AF02");
				
				// Initialization complete
				SystemCall.EASY.initialized();
				
				// Call it
				mainmethod.invoke(null, new Object[]{__args});
				
				// All okay!
				return;
			}
			
			// Does not exist
			catch (NoSuchMethodException e)
			{
			}
			
			// Construct new object but only say start is valid once it
			// has been fully constructed
			MIDlet mid = (MIDlet)mainclass.newInstance();
			
			// startApp is protected so it has to be made callable
			Method startmethod = MIDlet.class.getDeclaredMethod(
				"startApp");
			startmethod.setAccessible(true);
			
			// Initialization complete
			SystemCall.EASY.initialized();
			
			// Invoke the start method
			startmethod.invoke(mid);
		}
		
		// Completely hide call exceptions
		catch (InvocationTargetException e)
		{
			Throwable c = e.getCause();
			if (c != null)
				throw c;
			else
				throw e;
		}
	}
	
	/**
	 * Initializes the run-time.
	 *
	 * @param __client If {@code true} then it is initialized for the client.
	 * @throws Throwable On any throwable.
	 * @since 2017/12/07
	 */
	private static void __initializeRunTime(boolean __client)
		throws Throwable
	{
		// System calls may be performed on single objects or multiple objects
		SystemFunction[] functions = SystemFunction.values();
		int numf = functions.length;
		Call[] impls = new Call[numf];
		
		// Clients for Java SE use a bi-directional stream attached to the
		// input and output streams so that there is portable RPC
		if (__client)
		{
			System.err.println("SquirrelJME Client Launch!");
			
			// The client uses the process's normal input and output stream
			// to communicate with the kernel, so they need to be remapped
			InputStream win = System.in;
			OutputStream wout = System.out;
			
			// Wipe the input stream so that it does not accidentally get used
			// by anything
			Field fin = System.class.getDeclaredField("in");
			
			// It is final, so it must be cleared
			Field modifiersfield = Field.class.getDeclaredField("modifiers");
			modifiersfield.setAccessible(true);
			
			// Remember the old modifiers and clear the final field
			int oldmods = modifiersfield.getInt(fin);
			modifiersfield.setInt(fin, fin.getModifiers() & ~Modifier.FINAL);
			
			// Change field to an empty byte array so it ends right away
			fin.setAccessible(true);
			fin.set(null, new ByteArrayInputStream(new byte[0]));
			
			// Revert state
			modifiersfield.setInt(fin, oldmods);
			modifiersfield.setAccessible(false);
			fin.setAccessible(false);
			
			// Redirect standard output to use the system caller interface
			// instead
			System.setOut(new PrintStream(new StandardOutput(), true));
			
			// Use the original streams instead
			throw new todo.TODO();
			/*syscaller = new JavaClientCaller(
				Objects.<Integer>requireNonNull(null),
				new DatagramInputStream(win),
				new DatagramOutputStream(wout));*/
		}
		
		// The server uses the actual kernel
		else
		{
			// Initialize the kernel
			JavaPrimitiveKernel jpk = new JavaPrimitiveKernel();
			Kernel kernel = new Kernel(jpk);
			
			// Set calls to be implemented by the kernel
			Call dispatch = kernel.systemDispatch();
			for (int i = 0; i < numf; i++)
				impls[i] = dispatch;
		}
		
		// Initialize the same user side calls
		UserSideCalls usc = new UserSideCalls();
		impls[SystemFunction.CLASS_ENUM_CONSTANTS.ordinal()] = usc;
		impls[SystemFunction.CLASS_ENUM_COUNT.ordinal()] = usc;
		impls[SystemFunction.CLASS_ENUM_INDEXOF.ordinal()] = usc;
		impls[SystemFunction.CLASS_ENUM_VALUEOF.ordinal()] = usc;
		impls[SystemFunction.SET_DAEMON_THREAD.ordinal()] = usc;
		impls[SystemFunction.THROWABLE_GET_STACK.ordinal()] = usc;
		impls[SystemFunction.THROWABLE_SET_STACK.ordinal()] = usc;
		
		// Need to obtain the interface field so that it is initialized
		Field callerfield = SystemCall.class.getDeclaredField(
			"_CALLS");
		
		// There is an internal modifiers field which needs to be cleared so
		// that the data can be accessed as such
		Field modifiersfield = Field.class.getDeclaredField("modifiers");
		modifiersfield.setAccessible(true);
		
		// Remember the old modifiers and clear the final field
		int oldmods = modifiersfield.getInt(callerfield);
		modifiersfield.setInt(callerfield,
			callerfield.getModifiers() & ~Modifier.FINAL);
		
		// It is final so it must be settable
		callerfield.setAccessible(true);
		
		// Set the interface used to interact with the kernel
		callerfield.set(null, impls);
		
		// Protect everything again
		modifiersfield.setInt(callerfield, oldmods);
		modifiersfield.setAccessible(false);
		callerfield.setAccessible(false);
	}
	
	/**
	 * Returns the main class obtained by the manifest.
	 *
	 * @return The main manifest class.
	 * @throws Throwable On any throwable
	 * @since 2017/12/07
	 */
	private static String __mainClassByManifest()
		throws Throwable
	{
		// Determine the main class to actually call using the copied
		// manifest
		try (InputStream is = Main.class.getResourceAsStream(
			"/SQUIRRELJME-BOOTSTRAP.MF"))
		{
			// {@squirreljme.error AF03 No manifest is available?}
			if (is == null)
				throw new RuntimeException("AF03");
		
			// {@squirreljme.error AF04 No main class is available?}
			String mainclassname = new Manifest(is).getMainAttributes().
				getValue("Main-Class");
			if (mainclassname == null || mainclassname.isEmpty())
				throw new RuntimeException("AF04");
			return mainclassname;
		}
	}
}

