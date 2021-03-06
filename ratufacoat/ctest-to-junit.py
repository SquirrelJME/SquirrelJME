import sys
import os
import xml.etree.ElementTree as xml
import datetime

# Convert banner
print("Converting CTest from stdin...")

# Globals
squirreljmeGlobalTestTime = None

# Resultant Variables for runs
squirreljmeTestName = None
squirreljmeTestResult = None
squirreljmeNumSkipped = None
squirreljmeNumFailed = None
squirreljmeExecTime = None
squirreljmeWantValue = None
squirreljmeStdErr = None

# Parse the XML Tree
for event, tree in xml.iterparse(sys.stdin, events=("start", "end")):
    tag = tree.tag
    text = tree.text
    attrib = tree.attrib

    # Debugging
    # print('DEBUG: %s %s `%s` %s' % (event, tag, text, attrib))

    # Start of a new tag
    if event == "start":
        # Test time (UNIX Time -> 2021-03-05T18:23:27.788)
        if tag == "StartTestTime":
            squirreljmeGlobalTestTime = datetime.datetime \
                .utcfromtimestamp(int(text)).strftime("%Y-%m-%dT%H:%M:%S")

        # Defining new test
        elif tag == "Test" and "Status" in attrib:
            # Failed test?
            status = attrib["Status"]
            if status == "failed":
                squirreljmeNumSkipped = 0
                squirreljmeNumFailed = 1

            # Skipped test
            elif status == "notrun":
                squirreljmeNumSkipped = 1
                squirreljmeNumFailed = 0

            # Passed test
            elif status == "passed":
                squirreljmeNumSkipped = 0
                squirreljmeNumFailed = 0

        # Name of test
        elif tag == "Name":
            squirreljmeTestName = text

        # Time test took to execute (CTest ?? -> JUnit ms)
        elif (tag == "NamedMeasurement" and "name" in attrib and
              attrib["name"] == "Execution Time"):
            squirreljmeWantValue = "execTime"

        # Test output
        elif tag == "Measurement" and len(attrib) == 0:
            squirreljmeWantValue = "stdErr"

        # Reading a value
        elif tag == "Value":
            # Execution time?
            if "execTime" == squirreljmeWantValue:
                squirreljmeExecTime = max(float(1), float(text) * 1000)

            # Standard error?
            elif "stdErr" == squirreljmeWantValue:
                squirreljmeStdErr = text

            # Always clear out what we want, so it is not used
            squirreljmeWantValue = None

    # End of a test tag, dump the XML, or end of entire file
    elif event == "end" and tag == 'Test' and "Status" in attrib:
        # Force values to be set?
        if squirreljmeExecTime is None:
            squirreljmeExecTime = 0

        # Create build directory if missing
        if not os.path.isdir("build"):
            os.mkdir("build")
        if not os.path.isdir("build/junit"):
            os.mkdir("build/junit")

        # Open output file
        with open("build/junit/TEST-" + squirreljmeTestName + ".xml",
                  "wt") as file:
            # XML Header
            file.write('<?xml version="1.0" encoding="UTF-8"?>')
            file.write('\n')

            # Test Suite Information
            file.write('<testsuite name="%s" tests="1" skipped="%d" '
                       'failures="%d" errors="%d" timestamp="%s" '
                       'hostname="CTest" time="%g">' %
                       (squirreljmeTestName, squirreljmeNumSkipped,
                        squirreljmeNumFailed, squirreljmeNumFailed,
                        squirreljmeGlobalTestTime, squirreljmeExecTime))
            file.write('\n')

            # Test Case (mostly the same)
            file.write('<testcase name="%s" '
                       'classname="%s" time="%g">' %
                       (squirreljmeTestName, squirreljmeTestName,
                        squirreljmeExecTime))
            file.write('\n')

            # Standard output
            file.write('<system-out><![CDATA[]]></system-out>')
            file.write('\n')

            # Standard error
            file.write(
                '<system-err><![CDATA[%s]]></system-err>' % squirreljmeStdErr)
            file.write('\n')

            # End tags
            file.write('</testcase>')
            file.write('</testsuite>')
            file.write('\n')

            # <testsuite name="io.TestReadLineEOF" tests="1" skipped="0" failures="0" errors="0" timestamp="2021-03-05T18:23:27.788" hostname="Hosted" time="916.623">
            # <properties>
            # <property name="squirreljme.test.result" value="XERSQUIRRELJMEXER:result:PASS:" />
            # <property name="squirreljme.test.nanoseconds" value="XERSQUIRRELJMEXER:nanoseconds:916622600:" />
            # </properties>
            # <testcase name="io.TestReadLineEOF" classname="io.TestReadLineEOF" time="916.623">
            # <system-out><![CDATA[]]></system-out>
            # <system-err><![CDATA[Java Version: 1.8.0_252Java Over-Layer: Loading C:\Users\xer\Projects\squirreljme\emulators\emulator-base\build\lib\main\release\emulator-base.dll...
            # ...
            # ]]></system-err>
            # </testcase>
            # </testsuite>

        # Reset all test state
        squirreljmeTestName = None
        squirreljmeTestResult = None
        squirreljmeNumSkipped = None
        squirreljmeNumFailed = None
        squirreljmeExecTime = None
        squirreljmeWantValue = None
        squirreljmeStdErr = None
