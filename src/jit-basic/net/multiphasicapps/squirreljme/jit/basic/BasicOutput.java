// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITOutput;
import net.multiphasicapps.squirreljme.nativecode.base.NativeTarget;
import net.multiphasicapps.squirreljme.nativecode.NativeABI;
import net.multiphasicapps.squirreljme.nativecode.NativeABIProvider;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriter;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterFactory;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterOptions;
import net.multiphasicapps.squirreljme.nativecode.
	NativeCodeWriterOptionsBuilder;

/**
 * This is the output that is used to write to the JIT.
 *
 * @since 2016/09/10
 */
public class BasicOutput
	implements JITOutput
{
	/** The JIT configuration. */
	protected final JITConfig config;
	
	/** The ABI to compile for. */
	private volatile Reference<NativeABI> _abi;
	
	/** The factory for native code writers. */
	private volatile Reference<NativeCodeWriterFactory> _factory;
	
	/** Options for the native code generator. */
	private volatile Reference<NativeCodeWriterOptions> _options;
	
	/**
	 * Initializes the basic output.
	 *
	 * @param __conf The configuration used.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	public BasicOutput(JITConfig __conf)
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public JITNamespaceWriter beginNamespace(String __ns, OutputStream __os)
		throws JITException, NullPointerException
	{
		// Check
		if (__ns == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new BasicNamespaceWriter(this.config, __ns, __os, this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public JITConfig config()
	{
		return this.config;
	}
	
	/**
	 * Creates a native code writer for generating code for the given target
	 * machine.
	 *
	 * @param __os The output stream to write to.
	 * @return The native code writer.
	 * @since 2016/09/15
	 */
	public NativeCodeWriter createCodeWriter(OutputStream __os)
	{
		return nativeCodeWriterFactory().create(nativeCodeWriterOptions(),
			__os);
	}
	
	/**
	 * Returns the native ABI to target.
	 *
	 * @return The native ABI.
	 * @since 2016/09/15
	 */
	public NativeABI nativeABI()
	{
		// Get
		Reference<NativeABI> ref = this._abi;
		NativeABI rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// {@squirreljme.error BV0c No native ABI provider has been
			// specified. (The configuration)}
			JITConfig config = this.config;
			JITTriplet triplet = config.triplet();
			NativeABIProvider abiprov = config.<NativeABIProvider>
				getAsClass(BasicOutputFactory.NATIVE_ABI_FACTORY_PROPERTY,
				NativeABIProvider.class);
			if (abiprov == null)
				throw new JITException(String.format("BV0c %s", config));
			
			// {@squirreljme.error BV0d The native ABI factory did not return
			// any ABI. (The configuration)}
			NativeTarget nativetarget = triplet.nativeTarget();
			rv = abiprov.byName(config.getProperty(
				BasicOutputFactory.NATIVE_ABI_PROPERTY), nativetarget.bits(),
				nativetarget.floatingPoint());
			if (rv == null)
				throw new JITException(String.format("BV0d %s", config));
			
			// Cache
			this._abi = new WeakReference<>(rv);
		}
		
		// Return
		return rv;
	}
	
	/**
	 * Returns the options which are used to configure the native writer.
	 *
	 * @return The options for the native code writer.
	 * @since 2016/09/15
	 */
	public NativeCodeWriterOptions nativeCodeWriterOptions()
	{
		// Get
		Reference<NativeCodeWriterOptions> ref = this._options;
		NativeCodeWriterOptions rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Create builder
			NativeCodeWriterOptionsBuilder b =
				new NativeCodeWriterOptionsBuilder();
			
			// Set the ABI
			b.setABI(nativeABI());
			
			// Store
			this._options = new WeakReference<>((rv = b.build()));
		}
		
		// Return
		return rv;
	}
	
	/**
	 * Returns the native code writer factory.
	 *
	 * @return The native code writer factory.
	 * @since 2016/09/15
	 */
	public NativeCodeWriterFactory nativeCodeWriterFactory()
	{
		// Get
		Reference<NativeCodeWriterFactory> ref = this._factory;
		NativeCodeWriterFactory rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// {@squirreljme.error BV01 No native code factory was set in the
			// configuration. (The configuration)}
			JITConfig config = this.config;
			rv = config.<NativeCodeWriterFactory>
				getAsClass(BasicOutputFactory.NATIVE_CODE_WRITER_PROPERTY,
				NativeCodeWriterFactory.class);
			if (rv == null)
				throw new JITException(String.format("BV01 %s", config));
			
			// Cache
			this._factory = new WeakReference<>(rv);
		}
		
		// Return
		return rv;
	}
}

