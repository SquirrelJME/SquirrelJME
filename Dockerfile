# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# We need the entire JDK here for this to work!
FROM openjdk:8-jdk AS build

# emulator-base uses JNI to provide Assembly methods, we need a C++ compiler
RUN apt-get update
RUN apt-get install -y build-essential g++

# Copy repository for building and use it for building
COPY . /tmp/src
WORKDIR /tmp/src

# Build entire JAR distribution (we do not need a daemon here)
RUN ./gradlew --no-daemon jar

# We do not need a big complex environment to run SquirrelJME now, so we
# can use a more compact image here
FROM openjdk:8-jre

# Description
LABEL cc.squirreljme.vm="springcoat"
LABEL cc.squirreljme.version="0.3.0"
LABEL version="0.3.0"
LABEL description="SquirrelJME is a Java ME 8 Virtual Machine for embedded and Internet of Things devices. It has the ultimate goal of being 99.9% compatible with the Java ME standard."
LABEL maintainer="Stephanie Gawroriski <xerthesquirrel@gmail.com>"

# All of the SquirrelJME data is here
RUN mkdir /squirreljme

# SquirrelJME system JARs
RUN mkdir /squirreljme/system
COPY --from=build /tmp/src/modules/*/build/libs/*.jar /squirreljme/system/

# SquirrelJME emulator support
RUN mkdir /squirreljme/emulator
COPY --from=build /tmp/src/emulators/*/build/libs/*.jar /squirreljme/emulator/

# Where the user classpath exists (to run extra programs)
RUN mkdir /squirreljme/jars
VOLUME /squirreljme/jars

# Expose TelNet (LUI) and VNC (LCDUI)
EXPOSE 23/tcp
EXPOSE 5900/tcp

# Run the VM and go to the launcher
ENTRYPOINT java \
	-classpath "/squirreljme/emulator/*:/squirreljme/system/*" \
	cc.squirreljme.emulator.vm.VMFactory \
	-Xemulator:springcoat \
	-Xlibraries:"/squirreljme/system/*:/squirreljme/jars/*" \
	-classpath "/squirreljme/system/*" \
	javax.microedition.midlet.__MainHandler__ \
	cc.squirreljme.runtime.launcher.Main
