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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * Performs the action of building the virtual machine.
 *
 * @since 2020/08/07
 */
public class VMLibraryTaskAction
	implements Action<Task>
{
	/** The classifier used. */
	public final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the task action.
	 * 
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public VMLibraryTaskAction(SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public void execute(Task __task)
	{
		VMLibraryTaskAction.execute((VMBaseTask)__task, this.classifier,
			((__t, __isTest, __in, __out) -> this.classifier.getVmType()
				.processLibrary((VMBaseTask)__t, __isTest, __in, __out)));
	}
	
	/**
	 * Performs a library like action.
	 * 
	 * @param __task The task calling from.
	 * @param __classifier The classifier used.
	 * @param __func The function to use.
	 * @since 2021/05/16
	 */
	public static void execute(VMBaseTask __task,
		SourceTargetClassifier __classifier, VMLibraryExecuteFunction __func)
	{
		// Open the input library for processing
		Path tempFile = null;
		try (InputStream in = Files.newInputStream(__task.getInputs()
			.getFiles().getSingleFile().toPath(), StandardOpenOption.READ))
		{
			// Where shall this go?
			tempFile = Files.createTempFile(
				__classifier.getVmType().vmName(VMNameFormat.LOWERCASE),
				String.format("%s_%s", __classifier.getSourceSet(),
					__classifier.getBangletVariant().properNoun));
			
			// Setup output file for writing
			try (OutputStream out = Files.newOutputStream(tempFile,
				StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.CREATE))
			{
				__func.function(__task,
					__classifier.isTestSourceSet(),
					in, out);
			}
			
			// Move the file over
			Files.move(tempFile,
				__task.getOutputs().getFiles().getSingleFile().toPath(),
				StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e)
		{
			throw new RuntimeException("I/O Error processing input for " +
				__task.getProject().getName(), e);
		}
		
		// Ensure the temporary file is cleaned up on failure
		finally
		{
			if (tempFile != null)
				try
				{
					Files.deleteIfExists(tempFile);
				}
				catch (IOException ignored)
				{
				}
		}
	}
}
