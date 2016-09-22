// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.linux;

import java.io.IOException;
import net.multiphasicapps.squirreljme.builder.BuildConfig;
import net.multiphasicapps.squirreljme.builder.BuildInstance;
import net.multiphasicapps.squirreljme.builder.TargetNotSupportedException;
import net.multiphasicapps.squirreljme.emulator.EmulatorConfig;
import net.multiphasicapps.squirreljme.emulator.os.linux.LinuxDevFSFileSource;
import net.multiphasicapps.squirreljme.emulator.os.linux.LinuxProcFSFileSource;
import net.multiphasicapps.squirreljme.emulator.os.linux.LinuxSysFSFileSource;
import net.multiphasicapps.squirreljme.fs.NativeFileSystem;
import net.multiphasicapps.squirreljme.fs.virtual.VirtualFileSystem;
import net.multiphasicapps.squirreljme.fs.virtual.VirtualMounts;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.basic.BasicOutputFactory;
import net.multiphasicapps.squirreljme.jit.JITClassNameRewrite;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigBuilder;
import net.multiphasicapps.squirreljme.jit.JITOutputFactory;
import net.multiphasicapps.squirreljme.nativecode.base.NativeEndianess;
import net.multiphasicapps.squirreljme.nativecode.base.NativeTarget;
import net.multiphasicapps.squirreljme.paths.posix.PosixPaths;

/**
 * This is the base build instance for all Linux based targets.
 *
 * @since 2016/09/02
 */
public abstract class LinuxBuildInstance
	extends BuildInstance
{
	/**
	 * Initializes the build instance.
	 *
	 * @param __conf The build configuration.
	 * @param __arch The expected architecture.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public LinuxBuildInstance(BuildConfig __conf, String __arch)
	{
		super(__conf);
		
		// Check
		if (__arch == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BU04 The specified architecture is not
		// supported by this build instance.}
		JITTriplet triplet = __conf.triplet();
		NativeTarget nativetarget = triplet.nativeTarget();
		if (!nativetarget.architecture().equals(__arch))
			throw new TargetNotSupportedException(String.format("BU04 %s",
				__arch));
		
		// {@squirreljme.error BU03 Only Linux is supported by this build
		// instance.}
		if (!triplet.operatingSystem().equals("linux"))
			throw new TargetNotSupportedException("BU03");
		
		// {@squirreljme.error BU06 Only 32-bit and 64-bit Linux targets
		// are supported.}
		int bits = nativetarget.bits();
		if (bits != 32 && bits != 64)
			throw new TargetNotSupportedException("BU06");
		
		// {@squirreljme.error BU07 Only big endian or little endian Linux
		// targets are supported.}
		NativeEndianess end = nativetarget.endianess();
		if (end != NativeEndianess.BIG && end != NativeEndianess.LITTLE)
			throw new TargetNotSupportedException("BU07");
	}
	
	/**
	 * Configures the Linux emulator.
	 *
	 * @param __conf The configuration to modify.
	 * @throws IOException If it could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	protected abstract void configureLinuxEmulator(EmulatorConfig __conf)
		throws IOException, NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	protected final void configureEmulator(EmulatorConfig __conf)
		throws IOException, NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Setup a base file system that all architectures use, such as the
		// standard Linux directories
		PosixPaths pp = PosixPaths.instance();
		VirtualFileSystem vfs = new VirtualFileSystem(pp);
		VirtualMounts vm = vfs.virtualMounts();
		
		// Mount three sets of required Linux filesystems
		vm.mount(pp.get("/dev"), new LinuxDevFSFileSource());
		vm.mount(pp.get("/proc"), new LinuxProcFSFileSource());
		vm.mount(pp.get("/sys"), new LinuxSysFSFileSource());
		
		// Register the filesystem the emulator will use
		__conf.<NativeFileSystem>registerObject(NativeFileSystem.class, vfs);
		
		if (true)
			throw new Error("TODO");
		
		// Setup base Linux details
		if (true)
			throw new Error("TODO");
		
		// Setup architecture specified details
		configureLinuxEmulator(__conf);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/02
	 */
	@Override
	protected void modifyOutputConfig(JITConfigBuilder __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Use the basic JIT always
		__conf.setOutputFactory(BasicOutputFactory.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/02
	 */
	@Override
	protected String[] packageGroup()
	{
		// Always include Linux packages and any architecture specific
		// Linux one also.
		return new String[]{"os-generic", "os-linux",
			"os-linux-" + this.triplet.nativeTarget().architecture()};
	}
}

