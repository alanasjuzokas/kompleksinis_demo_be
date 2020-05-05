package bakalauras.demo.web.domain;

public  class Subject {
    String givenName;
    String serialNumber;
    String surname;

    public Subject(String givenName, String serialNumber, String surname) {
        this.givenName = givenName;
        this.serialNumber = serialNumber;
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getSurname() {
        return surname;
    }
}
