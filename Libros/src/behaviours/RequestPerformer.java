package behaviours;

import agents.BookBuyerAgent;
import gui.BookBuyerGui;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

//////
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
////////7

public class RequestPerformer extends Behaviour {
  public String mejorPrecio; //agrego
  private AID bestSeller;
  private int bestPrice;
  private int repliesCount = 0;
  private MessageTemplate mt;
  private int step = 0;
  private BookBuyerAgent bBuyerAgent;
  private String bookTitle;
  private BookBuyerGui buyer;
  
  
  public RequestPerformer(BookBuyerAgent a) {
    bBuyerAgent = a;
    bookTitle = a.getBookTitle();
  }
  
  public void action() {
    switch(step) {
    case 0:
      ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
      for(int i = 0; i < bBuyerAgent.getSellerAgents().length; i++) {
        cfp.addReceiver(bBuyerAgent.getSellerAgents()[i]);
      }
      
      cfp.setContent(bookTitle);
      cfp.setConversationId("book-trade");
      cfp.setReplyWith("cfp" + System.currentTimeMillis());
      myAgent.send(cfp);
      
      mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
          MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
      step = 1;
    break;
    
    case 1:
      ACLMessage reply = bBuyerAgent.receive(mt);
      if(reply != null) {
        if(reply.getPerformative() == ACLMessage.PROPOSE) {
          int price = Integer.parseInt(reply.getContent());
          if(bestSeller == null || price < bestPrice) {
            bestPrice = price;
            
            bestSeller = reply.getSender();
          }
        }
        repliesCount++;
        if(repliesCount >= bBuyerAgent.getSellerAgents().length) {
          step = 2;
        }
      } else {
        block();
      }
    break;
    
    case 2:
      ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
      order.addReceiver(bestSeller);
      order.setContent(bookTitle);
      order.setConversationId("book-trade");
      order.setReplyWith("order" + System.currentTimeMillis());
      bBuyerAgent.send(order);
      
      mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
          MessageTemplate.MatchInReplyTo(order.getReplyWith()));
      
      step = 3;
      
    break;
    
    case 3:      
      reply = myAgent.receive(mt);
      if (reply != null) {
         if (reply.getPerformative() == ACLMessage.INFORM) {
            System.out.println(bookTitle+" successfully purchased from agent "+reply.getSender().getName());
            System.out.println("Price = "+bestPrice);
            mejorPrecio = String.valueOf(bestPrice); 
            buyer = new BookBuyerGui(bBuyerAgent, mejorPrecio);
            buyer.gui(mejorPrecio);
            myAgent.doDelete();
         }
         else {
            System.out.println("Attempt failed: requested book already sold.");
         }

         step = 4;
      }
      else {
         block();
      }
      break;
    }
  }
  
  public boolean done() {
    if (step == 2 && bestSeller == null) {
       System.out.println("Attempt failed: "+bookTitle+" not available for sale");
    }
    return ((step == 2 && bestSeller == null) || step == 4);
 }
  
}
