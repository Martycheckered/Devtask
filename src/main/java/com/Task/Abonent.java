package com.Task;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Abonent implements Comparable<Abonent> {

    private String telephoneNumber = null;
    private BigDecimal balance = new BigDecimal(0);

    public Abonent () {}

    public Abonent( String telephoneNumber, BigDecimal balance) {

        this.telephoneNumber = telephoneNumber;
        this.balance = balance;
    }



    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    // реализация метода интерфейса для сортировки по номерам
    public int compareTo(Abonent compareAbonentByNumber) {
        //
        BigInteger compareNumber = new BigInteger(compareAbonentByNumber.getTelephoneNumber());

        //ascending order
        BigInteger temp = new BigInteger(this.telephoneNumber);

        return temp.compareTo(compareNumber);  //сравнивает два числа. Возвращает -1, если текущий объект меньше числа
                                               // compareNumber, 1 - если текущий объект больше и 0 - если числа равны

        //descending order
        //return compareQuantity - this.quantity;

    }

    public String toString (){
       return  (this.getTelephoneNumber()+"  "+this.getBalance());
    }
}
