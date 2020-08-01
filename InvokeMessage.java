import java.io.Serializable;

public class InvokeMessage implements Serializable {

    private String name;
    private String dateOfBirth;
    private String maritalStatus;
    private int choice;
    private int choiceObject;



    public String getName() {
        return name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    int getChoice() {
        return choice;
    }

    InvokeMessage(int choice) {
        this.choice = choice;
    }

    InvokeMessage(int choice, int choiceObject){
        this.choice = choice;
        this.choiceObject=choiceObject;
    }


    InvokeMessage(String name, String dateOfBirth, String maritalStatus, int choice, int choiceObject){
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.maritalStatus = maritalStatus;
    this.choice = choice;
    this.choiceObject=choiceObject;

    }

    int getChoiceObject(){
        return choiceObject;
    }

    }
