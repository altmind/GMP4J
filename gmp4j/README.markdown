GMP4J
=====

High-speed API-compatible portable implementation of BigInteger using native GMP.
---------------------------------------------------------------------------------

Motivation
----------

Most operations with arbitrary precision integers in Java are performed using java.math.BigInteger. j.m.BI is implemented in plain java and is mature and ubiqious. However j.m.BI

* uses ineffective algorithms(for instance, multiplication is performed using schoolgrade method) 
* completely lacks some useful methods(for instance, sqrt).
* could accomodate short-cut methods which implement some often used operations effectively(lcm,factorials,binomials)

GMP4J is not only faster, but is much more feature rich. 

Implementation
--------------

GMP4J uses Java Native Interface to communicate to GMP - library for manipulation with arbitrary precision numbers. GMP4J links dynamically to GMP under unices and links statically to MPIR(GMP clone with better windows support) under windows. GMP4J do support both 32bit and 64bit platforms.

Compatibility
-------------
GMP4J intended to be drop-in replacement of j.m.BI: you need to simply change import "java.math.BigInteger" to "us.altio.gmp4j.BigInteger" - GMP4J is (mostly) API complitant with j.m.BI. You can face problems with methods operating with byte[], however - those methods expose internals of class too much. Those problems could be solved in future.

GMP4J provides many new convinience methods, in addition to those, defined in j.m.BI.

GMP4J objects are immutable, just like j.m.BI objects. Each operation returns new object, this implies copy for every operation in GMP4J. However, those expenses are negligible, comparing to benefits of advanced algorithms. 

Performance
-----------
GMP4J benefits from operations on big input values. Operations on small inputs may be simply not algorithm-bounded. Gains are especially visible in multiplication, powering, toString. Some GMP4J operations are not faster than those, in j.m.BI: for instnce random number generation(GMP internally uses Mersenne Twister for PRNG, whereas java SecureRandom uses linear generator and hash, which is generally faster). To give you orders of performance increase, here is results of

Fibonacci/Takashi:
> n	GMP4j	JMB
> 5000	0	5
> 10000	0	20
> 20000	0	40
> 40000	0	44
> 80000	3	89
> 160000	3	283
> 320000	7	973
> 640000	21	3972
> 1280000	70	15276

PIDigits:
> length GMP4j	JUB
> 1000	67		347
> 2000	126		384
> 3000	240		515
> 4000	368		715
> 5000	427		980
> 6000	557		1392
> 7000	630		1781
> 8000	995		2228
> 9000	1122	2861
> 10000	1241	3513
> 11000	1524	4189
> 12000	1956	4916
> 13000	2042	5799
> 14000	2047	6717
> 15000	2201	7798

Installation
------------
GMP4J was designed to run on both unices and windows as first-class platforms.
Build on unices are done using gcc, builds on windows are done using MS VC.
Tested on: 
* Debian GNU/Linux squeeze(20110420), gcc i486-linux-gnu 4.4.4-1
* Windows 7, 32bit, Microsoft Visual Studio 2010		

Unix
----
Ensure that you installed JDK, sane C compiler, GMP library and GMP library headers(usually in gmp-dev package of your distro). Ensure that $JAVA_HOME points to root directory of jdk.
Navigate to gmp4j directory
	cd gmp4j
Create "bin" direcrtory, if missing
	mkdir bin
Run make
	make
If make was successful, copy bin/libgmp4j.so to somewhere in your library.path, for instance in bin directory of your jre/jdk.
	cp bin/libgmp4j.so $(dirname `which java`)
And add gmp4j.jar to your classpath.

Windows
-----
Ensure that %JAVA_HOME% points to root directory of jdk. Open "Visual studio command prompt"(look in "Start" menu)
Navigate to gmp4j directory
	cd gmp4j
Create "bin" direcrtory, if missing
	mkdir bin
Run nmake -f Makefile.win
	nmake -f Makefile.win
If make was successful, copy bin/gmp4j.dll to somewhere in your library.path, for instance in bin directory of your jre/jdk.
And add gmp4j.jar to your classpath.

_N.B. for your convinience, prebuilt binaries of MPIR are included in package, you dont need to download and build MPIR separately, however, you may._

Quirks
------
* GMP4J uses native unmanaged memory for storing GMP BI objects. Each of this objects is associated with GMP4J BigInteger class. When GMP4J java BI object is garbage collected, native object is GC'd as well. However, unmanaged JNI memory is not tracked by JVM(JVM only tracks heap) - when there are actually a lot of native objects which can be GC'd, JVM see that there is a lot of free space in heap left, and don't run GC. It may be a good idea to run System.gc() when you suppose that there may be GMP4J objects for collection.
* When building under cygwin, gcc may complain about code in jni_md.h. In that case replace line 

	typedef __int64 jlong;

with lines:
	#ifdef __GNUC__
	typedef long long jlong;
	#else
	typedef __int64 jlong;
	#endif

Where to find more information
------------------------------
GMP4J comes with test suite. You may consult testsuite for some clues on usage.


License information
-------------------
This is free software licensed under GNU Lesser General Public License Version 3 Available at http://www.gnu.org/licenses/lgpl.html
