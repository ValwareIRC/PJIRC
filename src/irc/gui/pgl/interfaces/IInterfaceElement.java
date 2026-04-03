package irc.gui.pgl.interfaces;

/**
 * IInterfaceElement
 */
public interface IInterfaceElement extends IItem
{
  /**
   * Get the component.
   * @return the component.
   */
  public IItem getComponent();
  
  /**
   * Get the constraint. Only applies if component is an instance of IVisibleItem.
   * @return component constraint.
   */
  public ILayoutConstraint getConstraint();
}
