#!/bin/sh
# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# For more information see license.txt.
# ---------------------------------------------------------------------------
# Brew installation script

# Where is this script?
__exeDir="$(dirname -- "$0")"

# Output directory
__binDir="$(readlink -f -- "$1")"
__binBinTarget="$(readlink -f -- "$1")/squirreljme-standalone"
__binJarTarget="$(readlink -f -- "$1")/squirreljme-standalone.jar"

# Jar directory
__jarDir="$(readlink -f -- "$2")"

# Install only the first highest versioned Jar
mkdir -p "$__binDir"
find "$__jarDir" -type f | grep '\.jar$' | grep 'squirreljme-standalone' |
	sort -r | head -n 1 | while read __jar
do
	# Determine the base name of the JAR
	__baseName="$(basename -- "$__jar")"
	
	# Copy the JAR to the target
	cp -vf "$__jar" "$__binJarTarget"
	
	# Add script to launch it
	cat << EOF > "$__binBinTarget"
#!/bin/sh
java -jar "\$(dirname -- "\$0")/squirreljme-standalone.jar" "\$@"
exit \$?
EOF
	
	# Make it executable
	chmod +x "$__binBinTarget"
done
