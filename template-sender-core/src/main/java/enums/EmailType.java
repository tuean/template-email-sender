package enums;

public enum EmailType {

    E_163("163"),
    E_GMAIL("gmail"),

    ;

    private String type;

    EmailType(String tyoe) {
        this.type = tyoe;
    }


}
