/* Generated By:JJTree: Do not edit this line. ASTMinusTermNode.java */

package org.vcell.model.bngl;

public class ASTMinusTermNode extends SimpleNode {
  public ASTMinusTermNode(int id) {
    super(id);
  }

  public ASTMinusTermNode(BNGLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(BNGLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }

@Override
public String toBNGL() {
	return "-"+jjtGetChild(0).toBNGL();
}
}
