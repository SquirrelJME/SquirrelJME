// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchChoiceInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchChoiceBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Dynamic library interface over choices.
 *
 * @since 2024/07/16
 */
public class DylibChoiceInterface
	extends DylibBaseInterface
	implements ScritchChoiceInterface
{
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/16
	 */
	public DylibChoiceInterface(Reference<DylibScritchInterface> __selfApi,
		NativeScritchDylib __dyLib)
	{
		super(__selfApi, __dyLib);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/25
	 */
	@Override
	public void choiceDelete(ScritchChoiceBracket __choice,
		int __atIndex)
		throws MLECallError
	{
		if (__choice == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__choiceRemove(this.dyLib._stateP,
			((DylibChoiceObject)__choice).objectPointer(), __atIndex);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/25
	 */
	@Override
	public void choiceDeleteAll(ScritchChoiceBracket __choice)
		throws MLECallError
	{
		if (__choice == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__choiceRemoveAll(this.dyLib._stateP,
			((DylibChoiceObject)__choice).objectPointer());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/28
	 */
	@Override
	public int choiceGetSelectedIndex(ScritchChoiceBracket __choice)
		throws MLECallError
	{
		if (__choice == null)
			throw new MLECallError("Null arguments.");
		
		return NativeScritchDylib.__choiceGetSelectedIndex(this.dyLib._stateP,
			((DylibChoiceObject)__choice).objectPointer());
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return
	 * @since 2024/07/25
	 */
	@Override
	public int choiceInsert(ScritchChoiceBracket __choice,
		int __atIndex)
		throws MLECallError
	{
		if (__choice == null)
			throw new MLECallError("Null arguments.");
		
		return NativeScritchDylib.__choiceInsert(this.dyLib._stateP,
			((DylibChoiceObject)__choice).objectPointer(), __atIndex);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/28
	 */
	@Override
	public int choiceLength(ScritchChoiceBracket __choice)
		throws MLECallError
	{
		if (__choice == null)
			throw new MLECallError("Null arguments.");
		
		return NativeScritchDylib.__choiceLength(this.dyLib._stateP,
			((DylibChoiceObject)__choice).objectPointer());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/25
	 */
	@Override
	public void choiceSetEnabled(ScritchChoiceBracket __choice,
		int __atIndex,
		boolean __enabled)
		throws MLECallError
	{
		if (__choice == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__choiceSetEnabled(this.dyLib._stateP,
			((DylibChoiceObject)__choice).objectPointer(), __atIndex,
			__enabled);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/25
	 */
	@Override
	public void choiceSetImage(ScritchChoiceBracket __choice,
		int __atIndex,
		int[] __data,
		int __off,
		int __scanLen,
		int __width,
		int __height)
		throws MLECallError
	{
		if (__choice == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__choiceSetImage(this.dyLib._stateP,
			((DylibChoiceObject)__choice).objectPointer(), __atIndex,
			__data, __off, __scanLen, __width, __height);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/25
	 */
	@Override
	public void choiceSetSelected(ScritchChoiceBracket __choice,
		int __atIndex,
		boolean __selected)
		throws MLECallError
	{
		if (__choice == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__choiceSetSelected(this.dyLib._stateP,
			((DylibChoiceObject)__choice).objectPointer(), __atIndex,
			__selected);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/25
	 */
	@Override
	public void choiceSetString(ScritchChoiceBracket __choice,
		int __atIndex,
		String __string)
		throws MLECallError
	{
		if (__choice == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__choiceSetString(this.dyLib._stateP,
			((DylibChoiceObject)__choice).objectPointer(), __atIndex,
			__string);
	}
}
