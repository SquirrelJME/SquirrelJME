// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import dev.shadowtail.classfile.mini.MinimizedClassFile;
import dev.shadowtail.classfile.mini.MinimizedField;
import cc.squirreljme.runtime.cldc.vki.FixedClassIDs;
import cc.squirreljme.runtime.cldc.vki.Kernel;
import cc.squirreljme.vm.VirtualMachine;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMException;
import cc.squirreljme.vm.VMFactory;
import cc.squirreljme.vm.VMSuiteManager;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.profiler.ProfilerSnapshot;

/**
 * This is the factory which is capable of creating instances of the
 * SummerCoat virtual machine.
 *
 * @since 2018/12/29
 */
public class SummerCoatFactory
	extends VMFactory
{
	/** The base address for suites. */
	public static final int SUITE_BASE_ADDR =
		0x80000000;
	
	/**
	 * Initializes the factory.
	 *
	 * @since 2018/12/29
	 */
	public SummerCoatFactory()
	{
		super("summercoat");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/29
	 */
	@Override
	protected VirtualMachine createVM(ProfilerSnapshot __ps,
		VMSuiteManager __sm, VMClassLibrary[] __cp, String __maincl,
		boolean __ismid, int __gd, Map<String, String> __sprops,
		String[] __args)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		// Setup virtual memory
		SuitesMemory sm;
		VirtualMemory vmem = new VirtualMemory(
			(sm = new SuitesMemory(SUITE_BASE_ADDR, __sm)), 0);
		
		// Initialize the suite space and load the boot address
		sm.__init();
		int kernaddr = sm._kernelmcaddr,
			bootaddr = sm._kernelbootaddr;
		
		// The base address of the kernel object
		int kobjbase = vmem.RAM_START_ADDRESS;
		
		// Write fixed class ID into object base
		if (true)
			throw new todo.TODO();// FixedClassIDs
		
		// Read the information on the boot class into memory, we need to
		// initialize and setup a bunch of fields for it
		try (InputStream kin = new ReadableMemoryInputStream(vmem,
			kernaddr, 1048576))
		{
			// Decode the class file so we can access the fields
			MinimizedClassFile minikern = MinimizedClassFile.decode(kin);
			
			// Base for fields, note that the object includes its class ID
			// and the reference count!
			int kfldbase = kobjbase + 4;
			
			// Go through instance fields and set their data fields
			for (MinimizedField mf : minikern.fields(false))
				switch (mf.name.toString())
				{
						// ROM address
					case "romaddr":
						throw new todo.TODO();
						
						// Kernel object base
					case "kobjbase":
						throw new todo.TODO();
						
						// Starting memory address
					case "memaddr":
						throw new todo.TODO();
					
						// Size of memory
					case "memsize":
						throw new todo.TODO();
						
						// Is this a MIDlet?
					case "ismidlet":
						throw new todo.TODO();
						
						// The guest depth
					case "guestdepth":
						throw new todo.TODO();
						
						// The classpath
					case "classpath":
						throw new todo.TODO();
						
						// System properties
					case "sysprops":
						throw new todo.TODO();
						
						// Main class
					case "mainclass":
						throw new todo.TODO();
						
						// Main arguments
					case "mainargs":
						throw new todo.TODO();
					
					default:
						throw new todo.OOPS(mf.name.toString());
				}
		}
		
		// {@squirreljme.error AE0y Could not read the kernel class for
		// initialization}.
		catch (IOException e)
		{
			throw new RuntimeException("AE0y", e);
		}
		
		// Setup virtual CPU to execute
		NativeCPU cpu = new NativeCPU(vmem);
		cpu.enterFrame(bootaddr);
		
		// Execute the CPU to boot the machine
		cpu.run();
		
		if (true)
			throw new todo.TODO();
		
		// Setup root machine which has our base suite manager
		RootMachine rm = new RootMachine(__sm, __ps, __gd);
		
		// Need to map to cached VMs
		CachingSuiteManager suites = rm.suites;
		int n = __cp.length;
		CachingClassLibrary[] libs = new CachingClassLibrary[n];
		for (int i = 0; i < n; i++)
			libs[i] = suites.loadLibrary(__cp[i]);
		
		// Now create the starting main task
		return new ExitAwaiter(rm.statuses, rm.createTask(libs, __maincl,
			__ismid, __sprops, __args).status, __ps);
	}
}

