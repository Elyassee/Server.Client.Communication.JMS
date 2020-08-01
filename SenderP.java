import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import java.util.Scanner;

public class SenderP implements MessageListener {
   private QueueConnection conn;
   private QueueSession session;
   private QueueSender sender;
   private QueueReceiver receiver;
   private Scanner scanner = new Scanner(System.in);

   public static void main(String argv[]) throws Exception {
      SenderP sender1=new SenderP();
      sender1.sendMessage();
   }
   public SenderP() throws NamingException, JMSException {

      Hashtable<String, String> properties = new Hashtable<String, String>();
      properties.put(Context.INITIAL_CONTEXT_FACTORY,
              "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
      properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

      Context context = new InitialContext(properties);
      QueueConnectionFactory connFactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");

      conn = connFactory.createQueueConnection();
      session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

      Queue q = (Queue) context.lookup("dynamicQueues/queue1");
      Queue q2 = (Queue) context.lookup("dynamicQueues/queue2");

      sender = session.createSender(q);
      receiver=session.createReceiver(q2);
      conn.start();
   }
   public void sendMessage() throws JMSException, InterruptedException {
      ObjectMessage msg = session.createObjectMessage();
      while(true) {
         System.out.println("****Press '1' to add an Inhabitant****\n" + "****Press '2' to get the Names of all Inhabitants of this City\n"
                 + "Press '0' to exit");
         int choice = scanner.nextInt();

         if(choice == 0){
            InvokeMessage message = new InvokeMessage(choice);
            msg.setObject(message);
            sender.send(msg);
            session.close();
            conn.close();
            System.exit(0);
         }

         System.out.println("****Press '1' to choose Frankfurt****\n "+ "****Press '2' to choose Berlin****\n" );
         int objectChosen=scanner.nextInt();
         if (choice == 1){
            System.out.println("Enter Name: ");
            String name = scanner.next();
            System.out.println("Enter Date of Birth: ");
            String dateOfBirth = scanner.next();
            System.out.println("Enter marital Status: ");
            String maritalStatus = scanner.next();

            InvokeMessage message = new InvokeMessage(name, dateOfBirth, maritalStatus, choice,objectChosen);
            msg.setObject(message);
            sender.send(msg);

         } else if (choice == 2) {
            InvokeMessage message = new InvokeMessage(choice,objectChosen);
            msg.setObject(message);
            sender.send(msg);

         }

         receiver.setMessageListener(this);
         Thread.sleep(1000);
      }

   }

   @Override
   public void onMessage(Message message) {
      TextMessage msg=(TextMessage) message;
      try {
         String ms=msg.getText();
         System.out.println(ms);
      } catch (JMSException e) {
         e.printStackTrace();
      }

   }
}
