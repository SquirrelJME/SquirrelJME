// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import cc.squirreljme.plugin.util.JavaExecSpecFiller;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;

/**
 * Provider interface for the various virtual machines that are available.
 * 
 * Note that source sets refer to {@link SourceSet#MAIN_SOURCE_SET_NAME}
 * and {@link SourceSet#TEST_SOURCE_SET_NAME}.
 *
 * @since 2020/08/06
 */
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
public interface VMSpecifier
{
	/**
	 * Is only debugging permitted for this target, that is when being ran
	 * it cannot use a release variant.
	 * 
	 * @return If only debug is permitted for this target.
	 * @since 2023/02/10
	 */
	boolean allowOnlyDebug();
	
	/**
	 * Returns the supported target banglets.
	 * 
	 * @return The supported banglets for this target.
	 * @since 2022/09/30
	 */
	Set<BangletVariant> banglets();
	
	/**
	 * Dumps the library.
	 * 
	 * @param __task The task running this, may be used to launch a VM.
	 * @param __isTest Is this a test run?
	 * @param __in The input data, this may be a JAR or otherwise.
	 * @param __out The destination output file.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/16
	 */
	void dumpLibrary(VMBaseTask __task, boolean __isTest, InputStream __in,
		OutputStream __out)
		throws IOException, NullPointerException;
	
	/**
	 * Can this be dumped?
	 * 
	 * @return If this can be dumped.
	 * @since 2021/05/16
	 */
	boolean hasDumping();
	
	/**
	 * Does this have an emulator available
	 * 
	 * @return If an emulator is available.
	 * @since 2023/05/28
	 */
	boolean hasEmulator();
	
	/**
	 * Is the emulator for this JIT capable, as in there does not need to be
	 * a library or ROM compilation before running?
	 * 
	 * @return If this has a JIT.
	 * @since 2022/12/23
	 */
	boolean hasEmulatorJit();
	
	/**
	 * Does this have native port support?
	 * 
	 * @return The native port support.
	 * @since 2023/05/31
	 */
	NativePortSupport[] hasNativePortSupport();
	
	/**
	 * Is there a ROM task for the VM?
	 * 
	 * @param __variant The variant used.
	 * @return If a ROM is available.
	 * @since 2020/08/23
	 */
	boolean hasRom(BangletVariant __variant);
	
	/**
	 * If the ROM contains only members of its own source set, then this
	 * will be {@code true}. This means that there will be {@code main},
	 * {@code testFixtures}, and {@code test} ROMs.
	 *
	 * @param __variant The banglet variant.
	 * @return If this is a single source set or not.
	 * @since 2023/07/25
	 */
	boolean isSingleSourceSetRom(BangletVariant __variant);
	
	/**
	 * Returns the name of the project that is used to run this using the
	 * emulator.
	 * 
	 * @param __variant The variant used.
	 * @return The project used for running the emulator.
	 * @since 2020/08/16
	 */
	List<String> emulatorProjects(BangletVariant __variant);
	
	/**
	 * Determines the name of the library that the provider uses for what is
	 * executed by the virtual machine and output by the virtual machine.
	 * 
	 * @param __project The project to get from.
	 * @param __sourceSet The source set to determine the path for.
	 * @return The path to the source set's library.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/06
	 */
	String outputLibraryName(Project __project, String __sourceSet)
		throws NullPointerException;
	
	/**
	 * Determines the output ROM name.
	 * 
	 * @param __sourceSet The source set used.
	 * @param __variant
	 * @return The name the ROM should be.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/27
	 */
	String outputRomName(String __sourceSet, BangletVariant __variant)
		throws NullPointerException;
	
	/**
	 * Processes the library.
	 * 
	 * @param __task The task running this, may be used to launch a VM.
	 * @param __isTest Is this a test run?
	 * @param __in The input data, this may be a JAR or otherwise.
	 * @param __out The destination output file.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	void processLibrary(VMBaseTask __task, boolean __isTest, InputStream __in,
		OutputStream __out)
		throws IOException, NullPointerException;
	
	/**
	 * Returns the optional library dependencies needed to perform library
	 * processing, if any.
	 * 
	 * @param __task The owning task.
	 * @param __variant
	 * @return Iterable of dependent tasks.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/21
	 */
	Iterable<Task> processLibraryDependencies(VMBaseTask __task,
		BangletVariant __variant)
		throws NullPointerException;
	
	/**
	 * Processes the ROM file for linking.
	 * 
	 * @param __task The task running under.
	 * @param __variant The variant used.
	 * @param __out The output of the given path.
	 * @param __build Build parameters for the ROM.
	 * @param __libs The libraries to link in.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/27
	 */
	void processRom(VMBaseTask __task, BangletVariant __variant,
		OutputStream __out,
		RomBuildParameters __build, List<Path> __libs)
		throws IOException, NullPointerException;
	
	/**
	 * Fills the execution spec with the arguments used to create the
	 * virtual machine.
	 *
	 * @param __anyProject Any project.
	 * @param __classifier The classifier to use.
	 * @param __debugEligible Is this eligible for debug?
	 * @param __execSpec The execution spec to fill.
	 * @param __mainClass The main class to execute.
	 * @param __commonName The common name for the program.
	 * @param __sysProps The system properties to define.
	 * @param __libPath The library path to use for the virtual machine.
	 * @param __classPath The class path of the execution target.
	 * @param __args Arguments to the started program.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	void spawnJvmArguments(Project __anyProject, 
		SourceTargetClassifier __classifier, boolean __debugEligible,
		JavaExecSpecFiller __execSpec, String __mainClass,
		String __commonName, Map<String, String> __sysProps, Path[] __libPath,
		Path[] __classPath, String... __args)
		throws NullPointerException;  
	
	/**
	 * Returns the virtual machine name using the given name format.
	 * 
	 * @param __format The format of the name to use.
	 * @return The formatted name.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/06
	 */
	String vmName(VMNameFormat __format)
		throws NullPointerException;
	
	/**
	 * Is this a golden standard for testing?
	 * 
	 * @return If this is a golden standard for testing.
	 * @since 2022/10/01
	 */
	default boolean isGoldTest()
	{
		return false;
	}
}
