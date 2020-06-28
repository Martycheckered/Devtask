package com.Task;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MFAbonentBaseTest {

    @Test
    public void createAbonentList_GENERATED_ABONENTS() {
        int number = 3;
        List<Abonent> one = MFAbonentBase.createAbonentList(number);
        List<Abonent> two = new ArrayList<>();
        two.add(new Abonent());
        two.add (new Abonent());
        two.add (new Abonent());

        Assert.assertEquals(one.size(),two.size());
    }
}