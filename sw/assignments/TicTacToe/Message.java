package sw.assignments.TicTacToe;

/**
 * Represents a message (has a sender and a text)
 */
public class Message {

  private final String sender;
  private final String text;

  /**
   * Initialises the message
   * @param sender The sender
   * @param text The text
   */
  Message(String sender, String text) {
    this.sender = sender;
    this.text = text;
  }

  /**
   * Gets the sender of the message
   * @return The sender
   */
  public String getSender() {
    return sender;
  }

  /**
   * Gets the text of the message
   * @return The text
   */
  public String getText() {
    return text;
  }

  @Override
  /**
   * Returns a string of form "From <sender>: <text"
   */
  public String toString() {
    return "From " + sender + ": " + text;
  }
}
