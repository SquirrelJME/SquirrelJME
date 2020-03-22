#parse("File Header.java")

#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end 

import net.multiphasicapps.tac.TestRunnable;

/**
 * Not Described.
 *
 * @since ${YEAR}/${MONTH}/${DAY}
 */
public class ${NAME}
	extends TestRunnable
{
    /**
     * {@inheritDoc}
     * @since ${YEAR}/${MONTH}/${DAY}
     */
    @Override
    public void test()
    {
        this.secondary("key", "value");
    }
}
