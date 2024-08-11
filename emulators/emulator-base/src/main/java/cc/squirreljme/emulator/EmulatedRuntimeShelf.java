// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.mle.constants.VMStatisticType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Emulated shelf for {@link RuntimeShelf}.
 *
 * @since 2021/08/08
 */
public class EmulatedRuntimeShelf
{
	/**
	 * Same as {@link RuntimeShelf#systemEnv(String)}.
	 * 
	 * @param __key The environment variable key.
	 * @return The value of the variable if it is set, otherwise {@code null}.
	 * @throws MLECallError If key is {@code null}.
	 * @since 2023/02/02
	 */
	@SuppressWarnings("CallToSystemGetenv")
	public static String systemEnv(String __key)
		throws MLECallError
	{
		if (__key == null)
			throw new MLECallError("No key specified.");
		
		return System.getenv(__key);
	}
	
	/**
	 * Same as {@link RuntimeShelf#vmDescription(int)}.
	 *
	 * @param __type One of {@link VMDescriptionType}.
	 * @return The string for the given description or {@code null} if not
	 * set.
	 * @throws MLECallError If {@code __type} is not valid.
	 * @since 2020/06/17
	 */
	public static String vmDescription(int __type)
		throws MLECallError
	{
		switch (__type)
		{
			case VMDescriptionType.UNSPECIFIED:
				return null;
			
			case VMDescriptionType.VM_VERSION:
				return System.getProperty("java.vm.version");
			
			case VMDescriptionType.VM_NAME:
				return System.getProperty("java.vm.name");
			
			case VMDescriptionType.VM_VENDOR:
				return System.getProperty("java.vm.vendor");
			
			case VMDescriptionType.VM_EMAIL:
				return System.getProperty("java.vm.vendor.email");
			
			case VMDescriptionType.VM_URL:
				return System.getProperty("java.vm.vendor.url");
			
			case VMDescriptionType.EXECUTABLE_PATH:
				return null;
			
			case VMDescriptionType.OS_NAME:
				return System.getProperty("os.name");
			
			case VMDescriptionType.OS_VERSION:
				return System.getProperty("os.version");
			
			case VMDescriptionType.OS_ARCH:
				return System.getProperty("os.arch");
			
			case VMDescriptionType.VM_SECURITY_POLICY:
				return null;
				
			case VMDescriptionType.PATH_SEPARATOR:
				return System.getProperty("file.separator");
				
			default:
				throw new MLECallError("Invalid " + __type);
		}
	}
	
	/**
	 * Returns a statistic of the virtual machine.
	 *
	 * @param __type The {@link VMStatisticType}.
	 * @return The value of the statistic, or {@code 0L} if not set.
	 * @throws MLECallError If {@code __type} is not valid.
	 * @since 2022/10/03
	 */
	public static long vmStatistic(int __type)
		throws MLECallError
	{
		if (__type < 0 || __type >= VMStatisticType.NUM_STATISTICS)
			throw new MLECallError("Bad statistic: " + __type);
		
		switch (__type)
		{
			case VMStatisticType.CPU_THREAD_COUNT:
				return Runtime.getRuntime().availableProcessors();
			
			case VMStatisticType.MEM_FREE:
				return Runtime.getRuntime().freeMemory();
			
			case VMStatisticType.MEM_MAX:
				return Runtime.getRuntime().maxMemory();
			
			case VMStatisticType.MEM_USED:
				return Runtime.getRuntime().totalMemory();
		}
		
		return 0L;
	}
}
