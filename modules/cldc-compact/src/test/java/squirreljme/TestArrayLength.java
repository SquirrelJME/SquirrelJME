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
		Object z = new boolean[1],
			b = new byte[2],
			s = new short[3],
			c = new char[4],
			i = new int[5],
			j = new long[6],
			f = new float[7],
			d = new double[8],
			l = new Object[9],
			o = new Object();
		
		// As pointers
		this.secondary("zp",
			Assembly.arrayLength(Assembly.objectToPointer(z)));
		this.secondary("bp",
			Assembly.arrayLength(Assembly.objectToPointer(b)));
		this.secondary("sp",
			Assembly.arrayLength(Assembly.objectToPointer(s)));
		this.secondary("cp",
			Assembly.arrayLength(Assembly.objectToPointer(c)));
		this.secondary("ip",
			Assembly.arrayLength(Assembly.objectToPointer(i)));
		this.secondary("jp",
			Assembly.arrayLength(Assembly.objectToPointer(j)));
		this.secondary("fp",
			Assembly.arrayLength(Assembly.objectToPointer(f)));
		this.secondary("dp",
			Assembly.arrayLength(Assembly.objectToPointer(d)));
		this.secondary("lp",
			Assembly.arrayLength(Assembly.objectToPointer(l)));
		this.secondary("op",
			Assembly.arrayLength(Assembly.objectToPointer(o)));
		
		// As objects
		this.secondary("z", Assembly.arrayLength(z));
		this.secondary("b", Assembly.arrayLength(b));
		this.secondary("s", Assembly.arrayLength(s));
		this.secondary("c", Assembly.arrayLength(c));
		this.secondary("i", Assembly.arrayLength(i));
		this.secondary("j", Assembly.arrayLength(j));
		this.secondary("f", Assembly.arrayLength(f));
		this.secondary("d", Assembly.arrayLength(d));
		this.secondary("l", Assembly.arrayLength(l));
		this.secondary("o", Assembly.arrayLength(o));
	}
}
