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
import java.util.zip._
import scalax.data._

class InputStreamExtras(s : InputStream) {
	def slurp() = StreamHelp.slurp(s)
	def pumpTo(d : OutputStream) = StreamHelp.pump(s, d)
	def unzipTo(outdir : File) = StreamHelp.unzip(s, outdir)
}

class ReaderExtras(r : Reader) {
	def slurp() = StreamHelp.slurp(r)
	def pumpTo(d : Writer) = StreamHelp.pump(r, d)
	def lines = StreamHelp.lines(r)
	def ensureBuffered = StreamHelp.ensureBuffered(r)
}

object StreamHelp
{
	/** Slurps the entire input stream into a byte array. */
	def slurp(in : InputStream) : Array[Byte] = {
		val out = new ByteArrayOutputStream
		pump(in, out)
		out.toByteArray()
	}

	/** Slurps the entire input stream into a string. */
	def slurp(in : Reader) : String = {
		val out = new StringWriter
		pump(in, out)
		out.toString()
	}

	/** Pumps all data from the input stream through to the output stream.
	 * Returns the number of bytes transferred. */
	def pump(in : InputStream, out : OutputStream) : Int = {
		val buf = new Array[Byte](65536)
		var len = in.read(buf)
		var count = 0
		while(len > -1) {
			out.write(buf, 0, len)
			count = count + len
			len = in.read(buf)
		}
		count
	}

	/** Pumps all data from the reader through to the writer. Returns the
	 * number of characters transferred. */
	def pump(in : Reader, out : Writer) : Int = {
		val buf = new Array[Char](32768)
		var len = in.read(buf)
		var count = 0
		while(len > -1) {
			out.write(buf, 0, len)
			count = count + len
			len = in.read(buf)
		}
		count
	}
	
	/** 
	 * Iterates over the lines of the reader.
	 * Keeps reader open even after reaching EOF, reader must be closed explicitly.
	 */
	def lines(br : BufferedReader) = new Iterator[String] {
		var fetched = false
		var nextLine : String = _
		
		def hasNext = {
			if (!fetched) {
				nextLine = br.readLine()
				fetched = true
			}
			nextLine ne null
		}
		
		def next() = {
			if (!hasNext) throw new NoSuchElementException("EOF")
			fetched = false
			nextLine
		}
		
	}

	/** Iterates over the lines of the reader. */
	def lines(in : Reader) : Iterator[String] = {
		val br = ensureBuffered(in)
		lines(br)
	}

	/** Wrap this Reader into a BufferedReader if it isn't one already. */
	def ensureBuffered(r : Reader) : BufferedReader =
		r match {
			case b : BufferedReader => b
			case _ => new BufferedReader(r)
		}

	/** Unzips the contents of the supplied stream into the specified directory. */
	def unzip(zip : InputStream, outdir : File) : Unit = {
		val zis = new ZipInputStream(new BufferedInputStream(zip))
		val buf = new Array[Byte](65536)
		for(entry <- IteratorHelp.nonNull { zis.getNextEntry() }) {
			val f = new File(outdir, entry.getName())
			if(entry.isDirectory()) {
				if(!f.mkdir())
					throw new IOException("Couldn't create directory")
			}
			else {
				val fos = new FileOutputStream(f)
				val dest = new BufferedOutputStream(fos, 65536)
				for(cnt <- IteratorHelp.nonNegative{ zis.read(buf) }) {
					dest.write(buf, 0, cnt)
				}
				dest.flush()
				dest.close()
			}
		}
		zis.close()
	}
}

// vim: set ts=4 sw=4 noet:
