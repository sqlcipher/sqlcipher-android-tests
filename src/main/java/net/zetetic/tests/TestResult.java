package net.zetetic.tests;

public class TestResult {

    private String name;
    private boolean success;
    private String message;

    public TestResult(String name, boolean success){
        this.name = name;
        this.success = success;
    }

    public void setResult(boolean success){
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return isSuccess() ? "OK" : "FAILED";
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }
}
