// -----------------------------------------------------------------------------
//
//  Scalax - The Scala Community Library
//  Copyright (c) 2005-8 The Scalax Project. All rights reserved.
//
//  The primary distribution site is http://scalax.scalaforge.org/
//
//  This software is released under the terms of the Revised BSD License.
//  There is NO WARRANTY.  See the file LICENSE for the full text.
//
// -----------------------------------------------------------------------------

package scalax.io
import java.io._
import java.nio.channels._
import java.nio.charset._
import java.util.regex._
import scalax.control._
import scalax.data._
import scala.collection.mutable._

/** Adds extra methods to File. */
class FileExtras(file : File) {
	/** Deletes the file or directory recursively. Returns false if it failed. */
	def deleteRecursively() = FileHelp.deleteRecursively(file)

	/** Returns a FileTree for this file. */
	def tree = new FileTree(file);

	/** Obtains a Reader using the system default charset. */
	def reader = inputStream.reader

	/** Obtains a BufferedReader using the supplied charset. */
	def reader(charset : String) = inputStream.reader(charset)

	/** Obtains a Writer using the system default charset. */
	def writer = outputStream.writer

	/** Obtains a Writer using the supplied charset. */
	def writer(charset : String) = outputStream.writer(charset)
	
	def printWriter = writer.buffered.printWriter

	/** Obtains an InputStream. */
	def inputStream = InputStreamResource.file(file)
	
	/** Obtains a OutputStream. */
	def outputStream = OutputStreamResource.file(file)
	
	def appendOutputStream = OutputStreamResource.fileAppend(file)
	
	/** Obtains an input FileChannel. */
	def inChannel =
		new ManagedResource[FileChannel] {
			type Handle = FileInputStream
			def unsafeOpen() =
				new FileInputStream(file)
			def unsafeClose(s : Handle) =
				s.close()
			def translate(s : Handle) = s.getChannel
		}
	
	/** Obtains an output FileChannel. */
	def outChannel =
		new ManagedResource[FileChannel] {
			type Handle = FileOutputStream
			def unsafeOpen() =
				new FileOutputStream(file)
			def unsafeClose(s : Handle) =
				s.close()
			def translate(s : Handle) = s.getChannel
		}

	/** Attempts to return the file extension. */
	def extension = FileHelp.extension(file)

	/** Slurps the entire input file into a string, using the system default
	 * character set. */
	def slurp() = for(r <- reader) yield StreamHelp.slurp(r)

	/** Slurps the entire input file into a string, using the supplied
	 * character set. */
	def slurp(charset : String) = for(r <- reader(charset)) yield StreamHelp.slurp(r)

	/** Views the file as a sequence of lines. */
	def lines = reader.lines

	/** Views the file as a sequence of lines. */
	def lines(charset : String) = inputStream.lines(charset)
	
	def readLines() = reader.readLines()
	
	def readLine() = reader.readLine()

	/** Writes the supplied string to the file, replacing any existing content,
	 * using the system default character set. */
	def write(s : String) = writeString(s)

	/** Writes the supplied string to the file, replacing any existing content,
	 * using the supplied character set. */
	def write(s : String, charset : String) = writer(charset).writeString(s)
	
	def writeString(s : String) = writer.writeString(s)
	
	def writeLine(line : String) = writer.writeLine(line)
	
	def writeLines(lines : Seq[String]) = writer.writeLines(lines)

	/** Copies the file. */
	def copyTo(dest : File) = FileHelp.copy(file, dest)

	/** Moves the file, by rename if possible, otherwise by copy-and-delete. */
	def moveTo(dest : File) = FileHelp.move(file, dest)

	/** Unzips the file into the specified directory. */
	def unzipTo(outdir : File) = FileHelp.unzip(file, outdir)

	def /(child : String) = new File(file, child)
}

/** Some shortcuts for file operations. */
object FileHelp {
	/** Deletes a file or directory recursively. Returns false if it failed. */
	def deleteRecursively(file : File) : Boolean = {
		if(file.isDirectory()) {
			for(i <- file.listFiles())
				deleteRecursively(i)
		}
		file.delete()
	}

	/** Unzips a file into the specified directory. */
	def unzip(zipfile : File, outdir : File) : Unit =
		StreamHelp.unzip(new FileInputStream(zipfile), outdir)

	private val extensionPattern = Pattern.compile(".*\\.([A-Za-z0-9_+-]+)$")
	/** Attempts to return the file extension. */
	def extension(file : File) : Option[String] = {
		val n = file.getName()
		val m = extensionPattern.matcher(n)
		if(m.matches()) Some(m.group(1)) else None
	}

	/** Copies a file. */
	def copy(src : File, dest : File) : Unit =
		for {
			in <- new FileExtras(src).inChannel
			out <- new FileExtras(dest).outChannel
		} in.transferTo(0, in.size, out)

	/** Moves a file, by rename if possible, otherwise by copy-and-delete. */
	def move(src : File, dest : File) : Unit = {
		dest.delete()
		if(!src.renameTo(dest)) {
			copy(src, dest)
			if(!src.delete()) throw new IOException("Delete after copy failed: "+src)
		}
	}
	
	def tmpDir = new File(System.getProperty("java.io.tmpdir"))
	
	val lineSeparator = System.getProperty("line.separator", "\n")
	
	// XXX: add implicit def toFileExtras(file : File) = new FileExtras(file)
}

/** Represents the preorder traversal of the directory tree rooted at the given file. */
class FileTree(val root : File) extends IteratorSeq[File] {
	def elements = new Walk

	class Walk private[FileTree] extends Iterator[File] {
		private val files = new Queue[File]();
		private val dirs = new Queue[File]();
		private var failed = false;
		dirs.enqueue(root)

		private def enqueue(dir: File) = {
			val list = dir.listFiles()
			if(list == null) {
				failed = true;
			} else {
				for (file <- dir.listFiles()) {
					if (file.isDirectory()) dirs.enqueue(file)
					else files.enqueue(file)
				}
			}
			dir
		}

		def hasNext = files.length != 0 || dirs.length != 0

		def next() = {
			failed = false;
			if (!hasNext) throw new NoSuchElementException("No more results")
			if (files.length != 0) files.dequeue else enqueue(dirs.dequeue)
		}

		def wasUnreadable = failed;
	}
}
