package org.apache.pivot.scala.log
import scala.reflect.BeanProperty
import io.Source
import org.apache.pivot.wtk.content.ListViewItemRenderer
import java.lang.String
import org.apache.pivot.wtkx.{WTKX, WTKXSerializer}
 
import org.apache.pivot.wtk.{ Application => PivotApplication, _}
import org.apache.pivot.collections.{ArrayList, Map}

 

 
class LogRecord ( @BeanProperty val threadName : String,
                  @BeanProperty val date : String,
                  @BeanProperty val time : String,
                  @BeanProperty val module : String,
                  @BeanProperty val level : String,
                  @BeanProperty val content : String){

	 
  override def toString()  = {
    threadName +" "+date +" "+ time +" "+module +" "+level+" "+content
  }

}

 
object LogRecord {
  def apply( line : String  ) : LogRecord = {
 
    val logRegex = """([A-Za-z0-9]+) +([0-9/]*) +([0-9:]*) +([A-Z]*) +: *([A-Z_]+).*""".r

    line match {
     
      case logRegex(threadName,date,time,module,level) =>
          val logRecord: LogRecord = new LogRecord( threadName, date, time, module, level,line)
            logRecord
		 
      case _ =>
          val logRecord: LogRecord = new LogRecord("N/A","N/A","N/A","N/A","N/A","N/A")
          	logRecord
    }

  }
}

 
class LogListViewItemRenderer extends ListViewItemRenderer {

  imageView.setVisible(false)

  override def render(item: AnyRef, index: Int, listView: ListView, selected: Boolean, checked: Boolean, 

highlighted: Boolean, disabled: Boolean) = {
      if ( item != null && item.isInstanceOf[LogRecord])
        {
          val log = item.asInstanceOf[LogRecord]
          label.setText(log.content)

     
        }
  }


  
}
 
class MainWindow extends PivotApplication {
  var window : Window   = null
  @WTKX var textInputFilePath : TextInput  = null
  @WTKX var browsePushButton : PushButton  = null
  @WTKX var loadPushButton : PushButton   = null
  @WTKX var textInputThreadName :TextInput   = null
  @WTKX var textInputModule : TextInput    = null
  @WTKX var textInputLevel : TextInput    = null
  @WTKX var textInputContent : TextInput   = null
  @WTKX var logListView : ListView = null


  def resume = {}

  def suspend = {}

  def shutdown(optional: Boolean) = {
    if ( window != null)
      {
        window.close
        true
      }
    false
  }

  def startup(display: Display, properties: Map[String, String]) = {
    val wtkxSerializer = new WTKXSerializer()
    var matchString : String = null

	 
    window = wtkxSerializer.readObject(this,"MainWindow.xml").asInstanceOf[Window]

    wtkxSerializer.bind(this)
		if ( properties containsKey "logfile")
		{
				textInputFilePath setText ( properties get "logfile")
		}
		
 
    browsePushButton.getButtonPressListeners.add( function2Listener (browseButtonPressed ) )
    loadPushButton.getButtonPressListeners.add( function2Listener(loadButtonPressed ))
    
    
    window.open(display)

  }

 
  def browseButtonPressed( button : Button ) : Unit = {
     val dialog : FileBrowserSheet = new   FileBrowserSheet(FileBrowserSheet.Mode.OPEN)

    dialog.open( window, new SheetCloseListener() {
      def sheetClosed(sheet: Sheet) = {
        if ( sheet.getResult)
          {
             val fileBrowseSheet = sheet.asInstanceOf[FileBrowserSheet]
             textInputFilePath.setText( fileBrowseSheet.getSelectedFile.getPath.toString)
          }
      }
    })
  }
  
 
  def loadButtonPressed( button : Button ) : Unit = {
     val logFile = Source.fromFile(textInputFilePath.getText)
     val list = new ArrayList[LogRecord]
    for ( line <- logFile.getLines ; logRecord = LogRecord(line.trim);
          if ( textInputThreadName.getText == "" || textInputThreadName.getText.contains

(logRecord.threadName) );
          if ( textInputModule.getText == "" || textInputModule.getText.contains(logRecord.module));
          if ( textInputLevel.getText == "" || textInputLevel.getText.contains(logRecord.level))){
      
      list add logRecord
    }

    logListView.setListData( list)
    
  }
 
   def function2Listener( fun : Button => Unit ) :ButtonPressListener =  {
    val listener = new ButtonPressListener()
    {
      def buttonPressed(button: Button) = {
        fun(button)
      }
    }

    listener
  }
}
 
object LogAnalyse {
  def main ( args : Array[String]) : Unit = {

  	
    DesktopApplicationContext.main( classOf[MainWindow], args)
   

  }
}