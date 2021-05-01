package net.killarexe.littlerage.engine.util;

public class Logger {

    String className;

    public Logger(Class class1){
        this.className = class1.getSimpleName();
    }
    public void debug(String msg){
        System.out.println("[DEGUG](" + this.className + "): " + msg);
    }
    public void info(String msg){
        System.out.println("[INFO](" + this.className + "): " + msg);
    }
    public void warn(String msg){
        System.out.println("[WARN](" + this.className + "): " + msg);
    }
    public void error(String msg){
        System.out.println("[ERROR](" + this.className + "): " + msg);
    }
    public void fatal(String msg){
        System.err.println("[FATAL](" + this.className + "): " + msg);
    }
}
