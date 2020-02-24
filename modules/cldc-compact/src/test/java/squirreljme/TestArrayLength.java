package squirreljme;

import cc.squirreljme.jvm.Assembly;
import net.multiphasicapps.tac.TestRunnable;

public class TestArrayLength
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2020/02/23
	 */
	@Override
	public final void test()
	{
		this.secondary("not", Assembly.arrayLength(new Object()));
		this.secondary("Z", Assembly.arrayLength(new boolean[1]));
		this.secondary("B", Assembly.arrayLength(new boolean[2]));
		this.secondary("S", Assembly.arrayLength(new boolean[3]));
		this.secondary("C", Assembly.arrayLength(new boolean[4]));
		this.secondary("I", Assembly.arrayLength(new boolean[5]));
		this.secondary("J", Assembly.arrayLength(new boolean[6]));
		this.secondary("F", Assembly.arrayLength(new boolean[7]));
		this.secondary("D", Assembly.arrayLength(new boolean[8]));
		this.secondary("L", Assembly.arrayLength(new boolean[9]));
	}
}
