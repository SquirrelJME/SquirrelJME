package cc.squirreljme.plugin.tasks;

import java.nio.file.Path;

/**
 * The output for a task.
 *
 * @since 2020/02/28
 */
final class __Output__
{
	/** The input file. */
	public final __Input__ input;
	
	/** The output path. */
	public final Path output;
	
	/**
	 * Initializes the output path.
	 *
	 * @param __input The input path.
	 * @param __output The output path.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/28
	 */
	public __Output__(__Input__ __input, Path __output)
		throws NullPointerException
	{
		if (__input == null || __output == null)
			throw new NullPointerException();
		
		this.input = __input;
		this.output = __output;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/28
	 */
	@Override
	public final String toString()
	{
		return String.format("{input=%s, output=%s}",
			this.input, this.output);
	}
}
