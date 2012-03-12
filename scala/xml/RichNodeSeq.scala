package xml

import scala.xml._


object RichNodeSeq {
    val MatchNodeByAttributeValueRegExp = """^(.*)\[@(.*)==(.*)\]$""".r

    def apply(nodeSeq: NodeSeq): RichNodeSeq = {
        println("iii" + nodeSeq)
        new RichNodeSeq(nodeSeq)
    }

  def main(args: Array[String]) {
   val xml =
      <a>
          <b/>
          <b>text of b2</b>
          <b attribute="value_b3"/>
          <b attribute="value_b4">text of b4</b>
          <b attribute="value b5">text of b5</b>

          <c property="value_c1">
              <d/>
              <d>text of d2</d>
              <d attribute="value_d3"/>
              <d attribute="value_d4">text of d4</d>
              <d attribute="value d5">text of d5</d>
          </c>
      </a>
   val richXml = new RichNodeSeq(xml)
   val b2 =  richXml \ "b[@attribute=='value_b4']"
    println(b2)
  }
}

class RichNodeSeq(nodeSeq: NodeSeq) extends NodeSeq {
    def theSeq = nodeSeq.theSeq

    import RichNodeSeq._

    override def \(that: String): RichNodeSeq = {
        def filterChildNodes(cond: (Node) => Boolean) = {
          println("i am here !")
          RichNodeSeq(NodeSeq fromSeq (this flatMap (_.child) filter cond))

        }

        that match {
            case MatchNodeByAttributeValueRegExp(element, attribute, value)
                   => filterChildNodes(isElementWithAttributeValue(_, element.trim, attribute.trim, value.trim))

            case _ => RichNodeSeq(super.\(that))
        }
    }

    override def \\(that: String): RichNodeSeq = {
        def filterChildNodes(cond: (Node) => Boolean) = RichNodeSeq(NodeSeq fromSeq (this flatMap (_.descendant_or_self) filter cond))

        that match {
            case MatchNodeByAttributeValueRegExp(element, attribute, value)
                   => filterChildNodes(isElementWithAttributeValue(_, element.trim, attribute.trim, value.trim))

            case _ => RichNodeSeq(super.\\(that))
        }
    }

    private def isElementWithAttributeValue(node: Node, elementName: String, attributeName: String, attributeValue: String): Boolean = {
        (node.label == elementName)
        .&&(
            node.attribute(attributeName) match {
                case Some(attributes) => attributes(0) == attributeValue
                case None => false
            }
        )
    }
}