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
import cc.squirreljme.kernel.KernelTask;
import cc.squirreljme.runtime.cldc.StandardOutput;
import cc.squirreljme.runtime.cldc.system.MnemonicCall;
import cc.squirreljme.runtime.cldc.system.SystemCall;
import cc.squirreljme.runtime.cldc.system.SystemCallImplementation;
import cc.squirreljme.runtime.cldc.system.SystemFunction;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.Objects;
import javax.microedition.midlet.MIDlet;

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
		
		// The client just uses the specified main class
		String mainclassname;
		if (isclient)
			mainclassname = clientmain;
		
		// Determines the class name via manifest
		else
			if (servermain != null)
				mainclassname = servermain;
			else
				mainclassname = __mainClassByManifest();
		
		// Exceptions generated as of the result of the method call are
		// wrapped so they must be unwrapped
		try
		{
			Class<?> mainclass = Class.forName(mainclassname);
			
			// Is an instance of MIDlet
			if (MIDlet.class.isAssignableFrom(mainclass))
			{
				// Construct new object but only say start is valid once it
				// has been fully constructed
				MIDlet mid = (MIDlet)mainclass.newInstance();
				
				// startApp is protected so it has to be made callable
				Method startmethod = MIDlet.class.getDeclaredMethod(
					"startApp");
				startmethod.setAccessible(true);
				
				// Initialization complete
				MnemonicCall.MNEMONIC.clientInitializationComplete();
				
				// Invoke the start method
				startmethod.invoke(mid);
			}
			
			// Use a main method instead
			else
			{
				// {@squirreljme.error AF02 The main method is not static.}
				Method mainmethod = mainclass.getMethod("main",
					String[].class);
				if ((mainmethod.getModifiers() & Modifier.STATIC) == 0)
					throw new RuntimeException("AF02");
				
				// Initialization complete
				MnemonicCall.MNEMONIC.clientInitializationComplete();
				
				// Call it
				mainmethod.invoke(null, new Object[]{__args});
			}
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
		SystemCallImplementation[] impls = new SystemCallImplementation[numf];
		
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
			JavaKernel kernel = new JavaKernel();
			
			// Set all calls to be implemented to this system task
			KernelTask ktask = kernel.systemTask();
			for (int i = 0; i < numf; i++)
				impls[i] = ktask;
		}
		
		// Need to obtain the interface field so that it is initialized
		Field callerfield = SystemCall.class.getDeclaredField(
			"_IMPLEMENTATIONS");
		
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

