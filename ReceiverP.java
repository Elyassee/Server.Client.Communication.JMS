import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class ReceiverP implements MessageListener {

    private static QueueConnection conn;
    private static QueueSession session;
    private static QueueSender sender;
    private City city1 = new City("Frankfurt");
    private City city2 = new City("Berlin");


    public static void main(String argv[]) throws Exception {

        Hashtable<String, String> properties = new Hashtable<>();
        properties.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

        Context context = new InitialContext(properties);

        QueueConnectionFactory connFactory =
                (QueueConnectionFactory) context.lookup("ConnectionFactory");

        conn = connFactory.createQueueConnection();
        session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue q = (Queue) context.lookup("dynamicQueues/queue1");
        Queue q2 = (Queue) context.lookup("dynamicQueues/queue2");

        QueueReceiver receiver = session.createReceiver(q);
        sender = session.createSender(q2);

        ReceiverP listener = new ReceiverP();
        receiver.setMessageListener(listener);
        conn.start();

        Scanner input = new Scanner(System.in);
        if(input.hasNext()) {
            System.out.println("Connection closed!");
            session.close();
            input.close();
            conn.close();
        }

    }

    private void parseMessage(Message m) throws JMSException {
        ObjectMessage object = (ObjectMessage) m;
        TextMessage msg = session.createTextMessage();
        InvokeMessage inhabitant = (InvokeMessage) object.getObject();
        if (inhabitant.getChoice() == 1) {
            if (inhabitant.getChoiceObject() == 1) {
                city1.addInhabitant(inhabitant.getName(), inhabitant.getDateOfBirth(), inhabitant.getMaritalStatus());
                msg.setText("The Inhabitant added successfully");
                sender.send(msg);

            } else if (inhabitant.getChoiceObject() == 2) {
                city2.addInhabitant(inhabitant.getName(), inhabitant.getDateOfBirth(), inhabitant.getMaritalStatus());
                msg.setText("The Inhabitant added successfully");
                sender.send(msg);
            }


        } else if (inhabitant.getChoice() == 2) {
            if (inhabitant.getChoiceObject() == 1) {
                String result = "Inhabitant";
                List<Inhabitant> in = city1.getInhabitants();
                int i = 1;
                for (Inhabitant it : in) {
                    result = i + ": " + it.getName();
                    i++;
                    msg.setText(result);
                    sender.send(msg);
                }

            } else if (inhabitant.getChoiceObject() == 2) {
                String result = "Inhabitant";
                List<Inhabitant> in = city2.getInhabitants();
                int i = 1;
                for (Inhabitant it : in) {
                    result = i + ": " + it.getName();
                    i++;
                    msg.setText(result);
                    sender.send(msg);
                }

            }
        }
        else if (inhabitant.getChoice() == 0){
            System.out.println("Connection closed!");
            session.close();
            conn.close();
            System.exit(0);
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            parseMessage(message);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
