import java.util.ArrayList;
import java.util.List;

public class City{

    private String name;
    private List<Inhabitant> inhabitants = new ArrayList<Inhabitant>();


    public City(String name){
        super();
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public List<Inhabitant> getInhabitants() {
        return inhabitants;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean addInhabitant(String name, String dateOfBirth, String maritalStatus){
        Inhabitant inhabitant= new Inhabitant(name, dateOfBirth, maritalStatus);
        inhabitants.add(inhabitant);
        return true;
    }



    public String getNameOfCity() {
        return this.name;
    }



}