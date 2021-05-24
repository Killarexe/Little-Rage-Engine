package net.killarexe.littlerage.engine.util;

import net.killarexe.littlerage.engine.Window;

public class Logger {

    String className;

    public Logger(Class class1){
        this.className = class1.getSimpleName();
    }
    public Logger(Logger logger){
        this.className = logger.className;
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
    public void error(String msg){System.err.println("[ERROR](" + this.className + "): " + msg);}
    public void fatal(String msg){System.err.println("[FATAL](" + this.className + "): " + msg);Window.getInstance().stop();}
    public void debug(int msg){
        System.out.println("[DEGUG](" + this.className + "): " + msg);
    }
    public void info(int msg){
        System.out.println("[INFO](" + this.className + "): " + msg);
    }
    public void warn(int msg){
        System.out.println("[WARN](" + this.className + "): " + msg);
    }
    public void error(int msg){System.err.println("[ERROR](" + this.className + "): " + msg);}
    public void fatal(int msg){System.err.println("[FATAL](" + this.className + "): " + msg);Window.getInstance().stop();}
    public void debug(float msg){
        System.out.println("[DEGUG](" + this.className + "): " + msg);
    }
    public void info(float msg){
        System.out.println("[INFO](" + this.className + "): " + msg);
    }
    public void warn(float msg){
        System.out.println("[WARN](" + this.className + "): " + msg);
    }
    public void error(float msg){System.err.println("[ERROR](" + this.className + "): " + msg);}
    public void fatal(float msg){System.err.println("[FATAL](" + this.className + "): " + msg);Window.getInstance().stop();}
    public void debug(char[] msg){
        System.out.println("[DEGUG](" + this.className + "): " + msg);
    }
    public void info(char[] msg){
        System.out.println("[INFO](" + this.className + "): " + msg);
    }
    public void warn(char[] msg){
        System.out.println("[WARN](" + this.className + "): " + msg);
    }
    public void error(char[] msg){System.err.println("[ERROR](" + this.className + "): " + msg);}
    public void fatal(char[] msg){System.err.println("[FATAL](" + this.className + "): " + msg);Window.getInstance().stop();}
    public void debug(Object msg){
        System.out.println("[DEGUG](" + this.className + "): " + msg);
    }
    public void info(Object msg){
        System.out.println("[INFO](" + this.className + "): " + msg);
    }
    public void warn(Object msg){
        System.out.println("[WARN](" + this.className + "): " + msg);
    }
    public void error(Object msg){System.err.println("[ERROR](" + this.className + "): " + msg);}
    public void fatal(Object msg){System.err.println("[FATAL](" + this.className + "): " + msg);Window.getInstance().stop();}
}
