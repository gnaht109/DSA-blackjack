public class Card {
    String value;
    String type;

    Card(String value,String type){
        this.value = value;
        this.type = type;
    }

    public String toString(){
        return value + "-" + type;
    }

    public boolean isAce(){
        return value == "A";
    }

    public int getValue(){
        if("AJQK".contains(value)){
            if(value == "A"){
                return 11;
            }
            return 10;
        }
        return Integer.parseInt(value);
    }

    public String getImagePath(){
        return "./cards/" + toString() +".png";
    }
}
