package com.fastcampus.ch3.diCopy1;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

class Car{}
class SportsCar extends Car{}
class Truck extends Car{}
class Engine{}
public class Main1 {

    public static void main(String[] args) throws Exception {
        Car car =(Car)getObject("car");
        Engine engine =(Engine) getObject("engine");

        System.out.println("engine : " + engine);



    }
    static  Car getCar() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Properties p =new Properties();
        p.load(new FileReader("config.txt"));

        Class clazz = Class.forName(p.getProperty("car"));

        return (Car)clazz.newInstance();
    }

    static  Object getObject(String key) throws Exception{
        Properties p =new Properties();
        p.load(new FileReader("config.txt"));

        Class clazz = Class.forName(p.getProperty(key));

        return clazz.newInstance();
    }
}
