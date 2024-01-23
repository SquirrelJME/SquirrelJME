// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.emulator.profiler.ProfilerSnapshot;
import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.emulator.vm.VMFactory;
import cc.squirreljme.emulator.vm.VMSuiteManager;
import cc.squirreljme.emulator.vm.VMThreadModel;
import cc.squirreljme.emulator.vm.VirtualMachine;
import cc.squirreljme.jdwp.host.JDWPHostFactory;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.IntegerArrayList;
import cc.squirreljme.vm.VMClassLibrary;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Instantiates and initializes the NanoCoat VM.
 *
 * @since 2023/12/03
 */
public class NanoCoatFactory
	extends VMFactory
{
	/**
	 * Initializes the factory.
	 *
	 * @since 2023/12/03
	 */
	public NanoCoatFactory()
	{
		super("nanocoat");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/03
	 */
	@Override
	protected VirtualMachine createVM(ProfilerSnapshot __profiler,
		JDWPHostFactory __jdwp, VMThreadModel __threadModel,
		VMSuiteManager __suiteManager, VMClassLibrary[] __classpath,
		String __mainClass, Map<String, String> __sysProps, String[] __args)
		throws IllegalArgumentException, NullPointerException, VMException
	{
		// Make sure the native library is loaded
		__Native__.__loadLibrary();
		
		// We need an allocation pool to work with everything
		AllocPool mainPool = new AllocPool(32 * 1048576);
		
		// Allocate the reserved pool, so we can throw everything in there
		AllocLink reservedLink = mainPool.alloc(
			AllocSizeOf.RESERVED_POOL.size());
		AllocPool reservedPool = new AllocPool(reservedLink.pointerAddress(), 
			reservedLink.size());
		
		// Setup virtual suite manager
		VirtualSuite suite = new VirtualSuite(reservedPool, __suiteManager);
		
		// Map the class path to IDs so NanoCoat knows which ones to use
		int numClassPath = __classpath.length;
		int[] classPathIds = new int[numClassPath];
		for (int i = 0; i < numClassPath; i++)
			classPathIds[i] = suite.manager.libraryId(__classpath[i]);
		
		// Debug
		Debugging.debugNote("Main ClassPath: %s",
			IntegerArrayList.asList(classPathIds));
		
		// Initialize boot parameters
		NvmBootParam param = new NvmBootParam(reservedPool);
		
		// Configure the virtual machine
		param.setSuite(suite);
		param.setMainClassPathIds(
			FlatList.fromArray(reservedPool, classPathIds));
		param.setMainClass(reservedPool.strDup(__mainClass));
		param.setMainArgs(FlatList.flatten(reservedPool, __args));
		
		// Determine all system properties to copy
		List<String> rawSysProps = new ArrayList<>(
			__sysProps.size() * 2);
		for (Map.Entry<String, String> entry : __sysProps.entrySet())
		{
			rawSysProps.add(Objects.toString(entry.getKey(), ""));
			rawSysProps.add(Objects.toString(entry.getValue(), ""));
		}
		
		// Set all system properties
		param.setSysProps(FlatList.flatten(reservedPool, rawSysProps));
		
		// Setup main virtual machine
		NvmState state = new NvmState(mainPool, reservedPool, param);
		
		throw Debugging.todo();
	}
}
