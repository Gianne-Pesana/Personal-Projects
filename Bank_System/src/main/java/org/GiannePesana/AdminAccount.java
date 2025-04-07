package org.GiannePesana;

public class AdminAccount extends Account{
    private String passcode;

    public AdminAccount() {
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }
}
