// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Boot parameters for the virtual machine.
 *
 * @since 2023/12/12
 */
public final class NvmBootParam
	implements Pointer
{
	/** The pointer where the parameters are stored. */
	private final AllocLink _link;
	
	static
	{
		__Native__.__loadLibrary();
	}
	
	/**
	 * Initializes the base boot parameters.
	 *
	 * @param __pool The pool to allocate within.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/12
	 */
	public NvmBootParam(AllocPool __pool)
		throws NullPointerException
	{
		if (__pool == null)
			throw new NullPointerException("NARG");
		
		this._link = __pool.alloc(AllocSizeOf.NVM_BOOT_PARAM);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/12
	 */
	@Override
	public long pointerAddress()
	{
		return this._link.pointerAddress();
	}
	
	/**
	 * Sets the main arguments.
	 *
	 * @param __args The arguments to use.
	 * @since 2023/12/15
	 */
	public void setMainArgs(FlatList<CharStarPointer> __args)
	{
		NvmBootParam.__setMainArgs(this._link.pointerAddress(),
			(__args == null ? 0 : __args.pointerAddress()));
	}
	
	/**
	 * Sets the main class.
	 *
	 * @param __mainClass The main class to use.
	 * @since 2023/12/15
	 */
	public void setMainClass(CharStarPointer __mainClass)
		throws VMException
	{
		NvmBootParam.__setMainClass(this._link.pointerAddress(),
			(__mainClass == null ? 0 : __mainClass.pointerAddress()));
	}
	
	/**
	 * Sets the main class path IDs.
	 *
	 * @param __ids The IDS to set.
	 * @throws VMException If it could not be set.
	 * @since 2023/12/20
	 */
	public void setMainClassPathIds(FlatList<Integer> __ids)
		throws VMException
	{
		NvmBootParam.__setMainClassPathIds(this._link.pointerAddress(),
			(__ids == null ? 0 : __ids.pointerAddress()));
	}
	
	/**
	 * Sets the suite handler.
	 *
	 * @param __suite The suites to use.
	 * @throws VMException If it could not be set.
	 * @since 2023/12/13
	 */
	public void setSuite(VirtualSuite __suite)
		throws VMException
	{
		NvmBootParam.__setSuite(this._link.pointerAddress(),
			(__suite == null ? 0 : __suite.pointerAddress()));
	}
	
	/**
	 * Sets the system properties.
	 *
	 * @param __args The arguments to use.
	 * @since 2023/12/16
	 */
	public void setSysProps(FlatList<CharStarPointer> __args)
	{
		NvmBootParam.__setSysProps(this._link.pointerAddress(),
			(__args == null ? 0 : __args.pointerAddress()));
	}
	
	/**
	 * Sets the main arguments.
	 *
	 * @param __thisPtr The structure pointer.
	 * @param __listPtr The main arguments string pointers.
	 * @throws VMException If it could not be set.
	 * @since 2023/12/16
	 */
	private static native void __setMainArgs(long __thisPtr, long __listPtr)
		throws VMException;
	
	/**
	 * Sets the main class.
	 *
	 * @param __thisPtr The structure pointer.
	 * @param __strPtr The main class string pointer.
	 * @throws VMException If it could not be set.
	 * @since 2023/12/16
	 */
	private static native void __setMainClass(long __thisPtr, long __strPtr)
		throws VMException;
	
	/**
	 * Sets the main class path IDs.
	 *
	 * @param __thisPtr Pointer to self.
	 * @param __idsPtr Pointers to the ID list.
	 * @throws VMException If it could not be set.
	 * @since 2023/12/20
	 */
	private static native void __setMainClassPathIds(long __thisPtr,
		long __idsPtr)
		throws VMException;
	
	/**
	 * Sets the suite handler.
	 *
	 * @param __thisPtr The structure pointer.
	 * @param __suitePtr The suite functions pointer.
	 * @throws VMException If it could not be set.
	 * @since 2023/12/13
	 */
	private static native void __setSuite(long __thisPtr, long __suitePtr)
		throws VMException;
	
	/**
	 * Sets the system properties.
	 *
	 * @param __thisPtr The structure pointer.
	 * @param __listPtr The system properties as string pointers.
	 * @throws VMException If it could not be set.
	 * @since 2023/12/16
	 */
	private static native void __setSysProps(long __thisPtr, long __listPtr)
		throws VMException;
}
