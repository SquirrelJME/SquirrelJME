// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.callbacks;

import cc.squirreljme.jvm.mle.callbacks.AudioStreamRenderer;
import cc.squirreljme.jvm.mle.callbacks.NativeImageLoadCallback;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.SpringCallbackAdapter;
import cc.squirreljme.vm.springcoat.SpringMachine;
import cc.squirreljme.vm.springcoat.SpringObject;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Callback for the loading of native images.
 *
 * @since 2022/06/28
 */
public class AudioStreamRendererAdapter
	extends SpringCallbackAdapter
	implements AudioStreamRenderer
{
	/** The class used to call back. */
	private static final ClassName CALLBACK_CLASS =
		new ClassName(AudioStreamRenderer.class.getName()
			.replace('.', '/'));
	
	/**
	 * Initializes the image loading callback.
	 * 
	 * @param __machine The machine used.
	 * @param __target The target object.
	 * @since 2022/06/28
	 */
	public AudioStreamRendererAdapter(SpringMachine __machine,
		SpringObject __target)
	{
		super(AudioStreamRendererAdapter.CALLBACK_CLASS, __machine,
			__target);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/07
	 */
	@Override
	public void render(int __format,
		@Range(from = 0, to = Integer.MAX_VALUE) int __rate,
		@Range(from = 0, to = Integer.MAX_VALUE) int __channels,
		@NotNull Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("render", null,
				"I", "I", "I", "Ljava/lang/Object;", "I", "I"),
			__format, __rate, __channels, __buf, __off, __len);
	}
}
